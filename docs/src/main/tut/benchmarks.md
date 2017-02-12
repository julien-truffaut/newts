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
Benchmark                Mode  Cnt    Score    Error   Units
stdAllCombine1          thrpt   20  411.346 ±  6.424  ops/us
anyvalAllCombine1       thrpt   20  318.740 ± 23.665  ops/us
scalazAllCombine1       thrpt   20  209.799 ±  1.647  ops/us
shapelessAllCombine1    thrpt   20  136.814 ±  0.873  ops/us
stdAllCombineAll        thrpt   20    4.046 ±  0.046  ops/us
anyvalAllCombineAll     thrpt   20    2.748 ±  0.029  ops/us
scalazAllCombineAll     thrpt   20    3.648 ±  0.034  ops/us
shapelessAllCombineAll  thrpt   20    2.429 ±  0.018  ops/us
```


`Min == (_ min _)`

```
Benchmark                Mode  Cnt    Score    Error   Units
stdIntMin               thrpt   20  101.116 ±  0.804  ops/us
anyvalIntMin            thrpt   20   82.383 ±  1.288  ops/us
```