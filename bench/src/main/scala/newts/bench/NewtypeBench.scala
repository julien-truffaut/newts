package newts.bench

import java.util.concurrent.TimeUnit

import cats.kernel.Monoid
import newts.Conjunction
import org.openjdk.jmh.annotations._

@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
class NewtypeBench {
  import scalaz.{ConjunctionZ, conjunctionZMonoid}
  import shapeless.{ConjunctionS, conjunctionSMonoid}

  def and(b1: Boolean, b2: Boolean): Boolean = b1 && b2

  @Benchmark def booleanCombine  : Boolean      = and(true, false)
  @Benchmark def anyvalCombine   : Conjunction  = Monoid[Conjunction].combine(Conjunction(true) , Conjunction(false))
  @Benchmark def scalazCombine   : ConjunctionZ = Monoid[ConjunctionZ].combine(scalaz.True, scalaz.False)
  @Benchmark def shapelessCombine: ConjunctionS = Monoid[ConjunctionS].combine(shapeless.True, shapeless.False)

}
