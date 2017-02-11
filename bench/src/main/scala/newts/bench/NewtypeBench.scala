package newts.bench

import java.util.concurrent.TimeUnit

import cats.Cartesian
import cats.kernel.Monoid
import newts.{Conjunction, ZipList}
import org.openjdk.jmh.annotations._

@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
class NewtypeBench {
  import scalaz.{ConjunctionZ, conjunctionZMonoid, ZipListZ, zipListZApply}
  import shapeless.{ConjunctionS, conjunctionSMonoid}

  val intList    : List[Int]     = 1.to(100).toList
  val intZipList : ZipList[Int]  = ZipList(intList)
  val intZipListz: ZipListZ[Int] = scalaz.tag(intList)

  def and(b1: Boolean, b2: Boolean): Boolean = b1 && b2

  @Benchmark def stdCombine      : Boolean      = and(true, false)
  @Benchmark def anyvalCombine   : Conjunction  = Monoid[Conjunction].combine(Conjunction(true) , Conjunction(false))
  @Benchmark def scalazCombine   : ConjunctionZ = Monoid[ConjunctionZ].combine(scalaz.True, scalaz.False)
  @Benchmark def shapelessCombine: ConjunctionS = Monoid[ConjunctionS].combine(shapeless.True, shapeless.False)

  @Benchmark def stdIntProduct   : List[(Int, Int)]     = intList.zip(intList)
  @Benchmark def anyvalIntProduct: ZipList[(Int, Int)]  = Cartesian[ZipList].product(intZipList, intZipList)
  @Benchmark def scalazIntProduct: ZipListZ[(Int, Int)] = Cartesian[ZipListZ].product(intZipListz, intZipListz)

}
