package com.example;

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

/**
 * Benchmark startsWith vs charAt.
 */
public class StartsWithBenchmark {
	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public boolean startsWith() {
		String data = "test";
		return data.startsWith("#");
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public boolean charAt() {
		String data = "test";
		return data.length() > 1 && data.charAt(0) == '#';
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(StartsWithBenchmark.class.getSimpleName())
				.addProfiler(GCProfiler.class).build();
		new Runner(opt).run();
	}
}
