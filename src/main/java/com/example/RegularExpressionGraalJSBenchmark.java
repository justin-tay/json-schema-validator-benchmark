package com.example;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Value;
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
import com.networknt.schema.regex.RegularExpression;

/**
 * Regular expression GraalJS benchmark.
 * <p>
 * This benchmarks the different strategies for using GraalJS regular expression.
 */
public class RegularExpressionGraalJSBenchmark {
	static final String REGEX = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";

	@State(Scope.Benchmark)
	public static class GraalJSDefaultBenchmarkState {
		private RegularExpression regularExpression;

		public GraalJSDefaultBenchmarkState() {
			this.regularExpression = GraalJSRegularExpressionFactory.getInstance()
					.getRegularExpression(RegularExpressionGraalJSBenchmark.REGEX);
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public boolean graaljsDefault(GraalJSDefaultBenchmarkState state) {
		return state.regularExpression.matches("2001:db8:3333:4444:5555:6666:7777:8888");
	}
	
	public static class GraalJSAlwaysCreateContextRegularExpression implements RegularExpression {
	    private static final String SOURCE = "pattern => {\n"
	            + "    const regex = new RegExp(pattern, 'u');\n"
	            + "    return text => text.match(regex)\n"
	            + "};";
		private final String regex;

		public GraalJSAlwaysCreateContextRegularExpression(String regex) {
			this.regex = regex;
		}

		@Override
		public boolean matches(String value) {
			try (Context context = Context.newBuilder("js").option("engine.WarnInterpreterOnly", "false").build()) {
				Value regExpBuilder = context.eval("js", SOURCE);
				Value function = regExpBuilder.execute(regex);
				return !function.execute(value).isNull();
			}
		}
	}

	@State(Scope.Benchmark)
	public static class GraalJSAlwaysCreateContextBenchmarkState {
		private RegularExpression regularExpression;

		public GraalJSAlwaysCreateContextBenchmarkState() {
			this.regularExpression = new GraalJSAlwaysCreateContextRegularExpression(
					RegularExpressionGraalJSBenchmark.REGEX);
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public boolean graaljsAlwaysCreateContext(GraalJSAlwaysCreateContextBenchmarkState state) {
		return state.regularExpression.matches("2001:db8:3333:4444:5555:6666:7777:8888");
	}
	
	
//

	public static class GraalJSAlwaysCreateContextSharedEngineRegularExpression implements RegularExpression {
	    private static final String SOURCE = "pattern => {\n"
	            + "    const regex = new RegExp(pattern, 'u');\n"
	            + "    return text => text.match(regex)\n"
	            + "};";
		private static final Engine ENGINE = Engine.newBuilder().option("engine.WarnInterpreterOnly", "false").build();
		private final String regex;

		public GraalJSAlwaysCreateContextSharedEngineRegularExpression(String regex) {
			this.regex = regex;
		}

		@Override
		public boolean matches(String value) {
			try (Context context = Context.newBuilder("js").engine(ENGINE).build()) {
				Value regExpBuilder = context.eval("js", SOURCE);
				Value function = regExpBuilder.execute(regex);
				return !function.execute(value).isNull();
			}
		}
	}

	@State(Scope.Benchmark)
	public static class GraalJSAlwaysCreateContextSharedEngineBenchmarkState {
		private RegularExpression regularExpression;

		public GraalJSAlwaysCreateContextSharedEngineBenchmarkState() {
			this.regularExpression = new GraalJSAlwaysCreateContextSharedEngineRegularExpression(
					RegularExpressionGraalJSBenchmark.REGEX);
		}
	}

	@BenchmarkMode(Mode.Throughput)
	@Fork(2)
	@Warmup(iterations = 2, time = 5)
	@Measurement(iterations = 3, time = 5)
	@Benchmark
	public boolean graaljsAlwaysCreateContextSharedEngine(GraalJSAlwaysCreateContextSharedEngineBenchmarkState state) {
		return state.regularExpression.matches("2001:db8:3333:4444:5555:6666:7777:8888");
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(RegularExpressionGraalJSBenchmark.class.getSimpleName())
				.addProfiler(GCProfiler.class).build();
		new Runner(opt).run();
	}
}
