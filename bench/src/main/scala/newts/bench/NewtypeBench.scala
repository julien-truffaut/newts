package newts.bench

import java.util.concurrent.TimeUnit

import cats.instances.list._
import cats.syntax.foldable._
import newts.Conjunction
import org.openjdk.jmh.annotations._

@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
class NewtypeBench {
  val booleans    : List[Boolean]     = List.fill(99)(true) :+ false
  val conjunctions: List[Conjunction] = booleans.map(Conjunction(_))

  @Benchmark def booleanFoldLeft: Boolean = booleans.foldLeft(true)(_ && _)
  @Benchmark def conjunctionsCombineAll: Conjunction = conjunctions.combineAll
  @Benchmark def conjunctionsFoldLeft: Conjunction = conjunctions.foldLeft(Conjunction.instances.empty)(Conjunction.instances.combine)

}
