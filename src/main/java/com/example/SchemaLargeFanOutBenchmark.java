package com.example;

import java.util.Set;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.serialization.JsonMapperFactory;

/**
 * Schema large fan out benchmark.
 * <p>
 * This benchmarks the impact of setting cacheRefs to false.
 */
public class SchemaLargeFanOutBenchmark {
	static final String DATA = "{\r\n"
			+ "  \"variables\": {\r\n"
			+ "    \"eu7_\": {\r\n"
			+ "      \"name\": \"occaecat in\",\r\n"
			+ "      \"comment\": [\r\n"
			+ "        \"nisi magna minim\",\r\n"
			+ "        \"in Ut\"\r\n"
			+ "      ]\r\n"
			+ "    }\r\n"
			+ "  },\r\n"
			+ "  \"output\": {\r\n"
			+ "    \"progress\": true,\r\n"
			+ "    \"stages\": [\r\n"
			+ "      \"Excepteur in Ut exercitation\",\r\n"
			+ "      \"magna deserunt sit\",\r\n"
			+ "      \"in adipisicing esse commodo\",\r\n"
			+ "      \"velit tempor in\"\r\n"
			+ "    ]\r\n"
			+ "  },\r\n"
			+ "  \"useCache\": true,\r\n"
			+ "  \"comment\": [\r\n"
			+ "    \"nisi sit labore ad\"\r\n"
			+ "  ]\r\n"
			+ "}";

	@State(Scope.Benchmark)
	public static class CacheRefsFalseState {
		private JsonSchema schema;
		private JsonNode data;
		public CacheRefsFalseState() {
			this.schema = JsonSchemaFactory.getInstance(VersionFlag.V202012).getSchema(
					SchemaLocation.of("classpath:schema/large-fan-out-schema.json"),
					SchemaValidatorsConfig.builder().cacheRefs(false).build());
			try {
				this.data = JsonMapperFactory.getInstance().readTree(DATA);
			} catch (JsonMappingException e) {
				throw new IllegalArgumentException(e);
			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public Set<ValidationMessage> cacheRefsFalse(CacheRefsFalseState state) {
		return state.schema.validate(state.data);
	}

	@State(Scope.Benchmark)
	public static class PreloadJsonSchemaRefMaxNestingDepthState {
		private JsonSchema schema;
		private JsonNode data;
		public PreloadJsonSchemaRefMaxNestingDepthState() {
			this.schema = JsonSchemaFactory.getInstance(VersionFlag.V202012).getSchema(
					SchemaLocation.of("classpath:schema/large-fan-out-schema.json"),
					SchemaValidatorsConfig.builder().preloadJsonSchemaRefMaxNestingDepth(20).build());
			try {
				this.data = JsonMapperFactory.getInstance().readTree(DATA);
			} catch (JsonMappingException e) {
				throw new IllegalArgumentException(e);
			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public Set<ValidationMessage> preloadJsonSchemaRefMaxNestingDepth(PreloadJsonSchemaRefMaxNestingDepthState state) {
		return state.schema.validate(state.data);
	}

	@State(Scope.Benchmark)
	public static class PreloadJsonSchemaFalseState {
		private JsonSchema schema;
		private JsonNode data;
		public PreloadJsonSchemaFalseState() {
			this.schema = JsonSchemaFactory.getInstance(VersionFlag.V202012).getSchema(
					SchemaLocation.of("classpath:schema/large-fan-out-schema.json"),
					SchemaValidatorsConfig.builder().preloadJsonSchema(false).build());
			try {
				this.data = JsonMapperFactory.getInstance().readTree(DATA);
			} catch (JsonMappingException e) {
				throw new IllegalArgumentException(e);
			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public Set<ValidationMessage> preloadJsonSchemaFalse(PreloadJsonSchemaFalseState state) {
		return state.schema.validate(state.data);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(SchemaLargeFanOutBenchmark.class.getSimpleName())
				.addProfiler(GCProfiler.class).build();
		new Runner(opt).run();
	}
}
