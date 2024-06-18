package com.example;

import java.io.IOException;
import java.io.InputStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.InputFormat;
import com.networknt.schema.serialization.JsonNodeReader;

/**
 * Benchmark location aware nodes.
 */
public class JsonNodeReaderBenchmark {
	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public JsonNode typicalDataLocationAware() throws IOException {
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("data/typical-schema-data.json")) {
			return JsonNodeReader.builder().locationAware().build().readTree(inputStream, InputFormat.JSON);
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public JsonNode typicalDataNormal() throws IOException {
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("data/typical-schema-data.json")) {
			return JsonNodeReader.builder().build().readTree(inputStream, InputFormat.JSON);
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public JsonNode typicalSchemaLocationAware() throws IOException {
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("schema/typical-schema.json")) {
			return JsonNodeReader.builder().locationAware().build().readTree(inputStream, InputFormat.JSON);
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public JsonNode typicalSchemaNormal() throws IOException {
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("schema/typical-schema.json")) {
			return JsonNodeReader.builder().build().readTree(inputStream, InputFormat.JSON);
		}
	}
	
	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(JsonNodeReaderBenchmark.class.getSimpleName())
				.addProfiler(GCProfiler.class).build();

		new Runner(opt).run();
	}

}
