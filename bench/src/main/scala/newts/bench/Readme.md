# Benchmark

run benchmarks using: `sbt "bench/jmh:run -i 10 -wi 10 -f 2 -t 1"`


## results

```
[info] Benchmark                       Mode  Cnt    Score    Error   Units
[info] NewtypeBench.stdCombine        thrpt   20  386.849 ± 32.014  ops/us
[info] NewtypeBench.anyvalCombine     thrpt   20  321.301 ± 11.252  ops/us
[info] NewtypeBench.scalazCombine     thrpt   20  201.884 ±  8.828  ops/us
[info] NewtypeBench.shapelessCombine  thrpt   20  137.199 ±  2.421  ops/us
[info] NewtypeBench.stdIntProduct     thrpt   20    0.782 ±  0.122  ops/us
[info] NewtypeBench.anyvalIntProduct  thrpt   20    0.852 ±  0.015  ops/us
[info] NewtypeBench.scalazIntProduct  thrpt   20    0.845 ±  0.031  ops/us
[info] NewtypeBench.stdIntMin         thrpt   20   79.680 ± 20.518  ops/us
[info] NewtypeBench.anyvalIntMin      thrpt   20   81.066 ±  1.507  ops/us
```