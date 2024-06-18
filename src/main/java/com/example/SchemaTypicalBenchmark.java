package com.example;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.serialization.JsonMapperFactory;

/**
 * Schema typical benchmark. 
 */
public class SchemaTypicalBenchmark {

	@State(Scope.Benchmark)
	public static class BenchmarkState {

		private JsonSchema jsonSchema;
		private JsonNode schemas;
		private List<String> schemaNames;

		public BenchmarkState() {
			JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V202012);
			try {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				jsonSchema = factory.getSchema(SchemaLocation.of("classpath:schema/typical-schema.json"));
				jsonSchema.preloadJsonSchema();

				JsonNode root = JsonMapperFactory.getInstance()
						.readTree(classLoader.getResourceAsStream("data/typical-schema-data.json"));
				schemas = root.get("schemas");

				List<String> names = new ArrayList<>();
				schemas.fieldNames().forEachRemaining(names::add);
				schemaNames = names;
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public void validate(BenchmarkState state) {
		for (String name : state.schemaNames) {
			JsonNode json = state.schemas.get(name);
			state.jsonSchema.validate(json);
		}
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(SchemaTypicalBenchmark.class.getSimpleName())
				.addProfiler(GCProfiler.class).build();

		new Runner(opt).run();
	}

}
