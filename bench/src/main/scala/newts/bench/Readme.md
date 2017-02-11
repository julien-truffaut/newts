# Benchmark

run benchmarks using: `sbt "bench/jmh:run -i 10 -wi 10 -f 2 -t 1"`


## results

```
[info] Benchmark                       Mode  Cnt    Score    Error   Units
[info] NewtypeBench.stdCombine        thrpt   20  409.686 ± 31.672  ops/us
[info] NewtypeBench.anyvalCombine     thrpt   20  199.697 ±  7.098  ops/us
[info] NewtypeBench.scalazCombine     thrpt   20  209.569 ±  2.159  ops/us
[info] NewtypeBench.shapelessCombine  thrpt   20  136.395 ±  1.511  ops/us
[info] NewtypeBench.stdIntProduct     thrpt   20    0.891 ±  0.043  ops/us
[info] NewtypeBench.anyvalIntProduct  thrpt   20    0.846 ±  0.014  ops/us
[info] NewtypeBench.scalazIntProduct  thrpt   20    0.862 ±  0.015  ops/us
```