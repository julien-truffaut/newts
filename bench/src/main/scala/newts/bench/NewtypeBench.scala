package newts.bench

import java.util.concurrent.TimeUnit

import cats.Cartesian
import cats.instances.list._
import cats.instances.string._
import cats.kernel.{Monoid, Semigroup}
import cats.syntax.foldable._
import newts.bench.scalaz.{ConjunctionZ, ZipListZ, conjunctionZMonoid, zipListZApply}
import newts.bench.shapeless.{ConjunctionS, conjunctionSMonoid}
import newts.{All, Min, ZipList}
import org.openjdk.jmh.annotations._

@Warmup(     iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3, jvmArgsAppend = Array("-XX:+UseG1GC", "-Xms1G", "-Xmx1G"))
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
class NewtypeBench {

  def and(b1: Boolean, b2: Boolean): Boolean = b1 && b2

  @Benchmark def stdAllCombine1      : Boolean      = and(true, false)
  @Benchmark def anyvalAllCombine1   : All          = Monoid[All].combine(All(true) , All(false))
  @Benchmark def scalazAllCombine1   : ConjunctionZ = Monoid[ConjunctionZ].combine(scalaz.True, scalaz.False)
  @Benchmark def shapelessAllCombine1: ConjunctionS = Monoid[ConjunctionS].combine(shapeless.True, shapeless.False)

  val booleans: List[Boolean]      = List.fill(99)(true) :+ false
  val alls    : List[All]          = booleans.map(All(_))
  val allZ    : List[ConjunctionZ] = booleans.map(scalaz.fromBoolean)
  val allS    : List[ConjunctionS] = booleans.map(shapeless.fromBoolean)

  @Benchmark def stdAllCombineAll      : Boolean      = booleans.foldLeft(true)(_ && _)
  @Benchmark def anyvalAllCombineAll   : All          = alls.combineAll
  @Benchmark def scalazAllCombineAll   : ConjunctionZ = allZ.combineAll
  @Benchmark def shapelessAllCombineAll: ConjunctionS = allS.combineAll

//  val intList    : List[Int]      = 1.to(100).toList
//  val intZipList : ZipList[Int]   = ZipList(intList)
//  val intZipListz: ZipListZ[Int]  = scalaz.tag(intList)
//
//  @Benchmark def stdIntProduct   : List[(Int, Int)]     = intList.zip(intList)
//  @Benchmark def anyvalIntProduct: ZipList[(Int, Int)]  = Cartesian[ZipList].product(intZipList, intZipList)
//  @Benchmark def scalazIntProduct: ZipListZ[(Int, Int)] = Cartesian[ZipListZ].product(intZipListz, intZipListz)

  @Benchmark def stdIntMin   : String      = Ordering[String].min("abcdefgh", "abcdefghz")
  @Benchmark def anyvalIntMin: Min[String] = Semigroup[Min[String]].combine(Min("abcdefgh"), Min("abcdefghz"))

}
