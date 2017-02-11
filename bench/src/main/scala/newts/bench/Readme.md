# Benchmark

run benchmarks using: `sbt "bench/jmh:run -i 10 -wi 10 -f 2 -t 1"`


## results

```
[info] Benchmark                       Mode  Cnt    Score    Error   Units
[info] NewtypeBench.stdCombine        thrpt   20  407.352 ± 11.406  ops/us
[info] NewtypeBench.anyvalCombine     thrpt   20  205.131 ±  7.449  ops/us
[info] NewtypeBench.scalazCombine     thrpt   20  207.228 ±  3.434  ops/us
[info] NewtypeBench.shapelessCombine  thrpt   20  136.015 ±  1.402  ops/us
[info] NewtypeBench.stdIntProduct     thrpt   20    0.864 ±  0.022  ops/us
[info] NewtypeBench.anyvalIntProduct  thrpt   20    0.868 ±  0.016  ops/us
[info] NewtypeBench.scalazIntProduct  thrpt   20    0.873 ±  0.013  ops/us
[info] NewtypeBench.stdIntMin         thrpt   20   99.668 ±  2.349  ops/us
[info] NewtypeBench.anyvalIntMin      thrpt   20   70.443 ±  0.583  ops/us
```