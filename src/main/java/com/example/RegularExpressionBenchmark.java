package com.example;

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

import com.networknt.schema.regex.GraalJSRegularExpressionFactory;
import com.networknt.schema.regex.JDKRegularExpressionFactory;
import com.networknt.schema.regex.JoniRegularExpressionFactory;
import com.networknt.schema.regex.RegularExpression;

/**
 * Regular expression benchmark.
 * <p>
 * This benchmarks the different regular expression implementations.
 */
public class RegularExpressionBenchmark {
	static final String REGEX = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";

	@State(Scope.Benchmark)
	public static class GraalJSBenchmarkState {
		private RegularExpression regularExpression;

		public GraalJSBenchmarkState() {
			this.regularExpression = GraalJSRegularExpressionFactory.getInstance()
					.getRegularExpression(RegularExpressionBenchmark.REGEX);
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public boolean graaljs(GraalJSBenchmarkState state) {
		return state.regularExpression.matches("2001:db8:3333:4444:5555:6666:7777:8888");
	}

	@State(Scope.Benchmark)
	public static class JoniBenchmarkState {
		private RegularExpression regularExpression;

		public JoniBenchmarkState() {
			this.regularExpression = JoniRegularExpressionFactory.getInstance()
					.getRegularExpression(RegularExpressionBenchmark.REGEX);
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public boolean joni(JoniBenchmarkState state) {
		return state.regularExpression.matches("2001:db8:3333:4444:5555:6666:7777:8888");
	}

	@State(Scope.Benchmark)
	public static class JDKBenchmarkState {
		private RegularExpression regularExpression;

		public JDKBenchmarkState() {
			this.regularExpression = JDKRegularExpressionFactory.getInstance()
					.getRegularExpression(RegularExpressionBenchmark.REGEX);
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public boolean jdk(JDKBenchmarkState state) {
		return state.regularExpression.matches("2001:db8:3333:4444:5555:6666:7777:8888");
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(RegularExpressionBenchmark.class.getSimpleName())
				.addProfiler(GCProfiler.class).build();
		new Runner(opt).run();
	}
}
