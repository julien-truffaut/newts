---
layout: home
title:  "Benchmarks"
section: "main_menu"
position: 2
---

## Benchmarks

run benchmarks using: `sbt "bench/jmh:run -i 10 -wi 10 -f 2 -t 1"`

`All = (true, &&)`

`combine1`: single `Monoid.combine`

`combineAll`: `foldLeft` through a list of 100 elements
 
 
```
[info] Benchmark                             Mode  Cnt    Score   Error   Units
[info] NewtypeBench.stdAllCombine1          thrpt   30  447.216 ± 7.115  ops/us
[info] NewtypeBench.anyvalAllCombine1       thrpt   30  365.722 ± 8.268  ops/us
[info] NewtypeBench.scalazAllCombine1       thrpt   30  223.240 ± 6.023  ops/us
[info] NewtypeBench.shapelessAllCombine1    thrpt   30  147.103 ± 3.319  ops/us
[info] NewtypeBench.stdAllCombineAll        thrpt   30    4.368 ± 0.099  ops/us
[info] NewtypeBench.anyvalAllCombineAll     thrpt   30    2.880 ± 0.034  ops/us
[info] NewtypeBench.scalazAllCombineAll     thrpt   30    3.977 ± 0.061  ops/us
[info] NewtypeBench.shapelessAllCombineAll  thrpt   30    2.629 ± 0.044  ops/us
```


`Min == (_ min _)`

```
[info] Benchmark                             Mode  Cnt    Score   Error   Units
[info] NewtypeBench.anyvalIntMin            thrpt   30   90.782 ± 1.679  ops/us
[info] NewtypeBench.stdIntMin               thrpt   30  107.441 ± 3.404  ops/us
```