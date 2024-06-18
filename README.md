# JSON Schema Validator Benchmark

Benchmarks the relative performance impact of setting certain configuration options when running validation.

## Building

```shell
mvn clean verify
```

## Running

### Running all benchmarks

```shell
java -jar target/benchmarks.jar
```

## Results

### Regular Expressions

```shell
java -jar target/benchmarks.jar com.example.RegularExpressionBenchmark -prof gc
```

```
Benchmark                                               Mode  Cnt        Score       Error   Units
RegularExpressionBenchmark.graaljs                     thrpt    6   362696.226 ± 15811.099   ops/s
RegularExpressionBenchmark.graaljs:gc.alloc.rate       thrpt    6     2584.386 ±   112.708  MB/sec
RegularExpressionBenchmark.graaljs:gc.alloc.rate.norm  thrpt    6     7472.003 ±     0.001    B/op
RegularExpressionBenchmark.graaljs:gc.count            thrpt    6      130.000              counts
RegularExpressionBenchmark.graaljs:gc.time             thrpt    6      144.000                  ms
RegularExpressionBenchmark.jdk                         thrpt    6  2776184.321 ± 41838.479   ops/s
RegularExpressionBenchmark.jdk:gc.alloc.rate           thrpt    6     1482.565 ±    22.343  MB/sec
RegularExpressionBenchmark.jdk:gc.alloc.rate.norm      thrpt    6      560.000 ±     0.001    B/op
RegularExpressionBenchmark.jdk:gc.count                thrpt    6       74.000              counts
RegularExpressionBenchmark.jdk:gc.time                 thrpt    6       78.000                  ms
RegularExpressionBenchmark.joni                        thrpt    6  1810229.581 ± 35230.798   ops/s
RegularExpressionBenchmark.joni:gc.alloc.rate          thrpt    6     1463.887 ±    28.483  MB/sec
RegularExpressionBenchmark.joni:gc.alloc.rate.norm     thrpt    6      848.003 ±     0.001    B/op
RegularExpressionBenchmark.joni:gc.count               thrpt    6       73.000              counts
RegularExpressionBenchmark.joni:gc.time                thrpt    6       77.000                  ms
```

### JSON Schema Ref Caching

```shell
java -jar target/benchmarks.jar com.example.SchemaLargeFanOutBenchmark -prof gc
```

```
Benchmark                                                                           Mode  Cnt       Score      Error   Units
SchemaLargeFanOutBenchmark.cacheRefsFalse                                          thrpt    6   66770.301 ±  866.916   ops/s
SchemaLargeFanOutBenchmark.cacheRefsFalse:gc.alloc.rate                            thrpt    6    2200.576 ±   28.572  MB/sec
SchemaLargeFanOutBenchmark.cacheRefsFalse:gc.alloc.rate.norm                       thrpt    6   34560.018 ±    0.001    B/op
SchemaLargeFanOutBenchmark.cacheRefsFalse:gc.count                                 thrpt    6     110.000             counts
SchemaLargeFanOutBenchmark.cacheRefsFalse:gc.time                                  thrpt    6     124.000
  ms
SchemaLargeFanOutBenchmark.preloadJsonSchemaFalse                                  thrpt    6  221078.181 ±  895.167   ops/s
SchemaLargeFanOutBenchmark.preloadJsonSchemaFalse:gc.alloc.rate                    thrpt    6    2401.715 ±    9.730  MB/sec
SchemaLargeFanOutBenchmark.preloadJsonSchemaFalse:gc.alloc.rate.norm               thrpt    6   11392.005 ±    0.001    B/op
SchemaLargeFanOutBenchmark.preloadJsonSchemaFalse:gc.count                         thrpt    6     121.000             counts
SchemaLargeFanOutBenchmark.preloadJsonSchemaFalse:gc.time                          thrpt    6     135.000
  ms
SchemaLargeFanOutBenchmark.preloadJsonSchemaRefMaxNestingDepth                     thrpt    6  216739.368 ± 4709.816   ops/s
SchemaLargeFanOutBenchmark.preloadJsonSchemaRefMaxNestingDepth:gc.alloc.rate       thrpt    6    2354.570 ±   51.138  MB/sec
SchemaLargeFanOutBenchmark.preloadJsonSchemaRefMaxNestingDepth:gc.alloc.rate.norm  thrpt    6   11392.005 ±    0.001    B/op
SchemaLargeFanOutBenchmark.preloadJsonSchemaRefMaxNestingDepth:gc.count            thrpt    6      58.000             counts
SchemaLargeFanOutBenchmark.preloadJsonSchemaRefMaxNestingDepth:gc.time             thrpt    6      66.000
  ms
```