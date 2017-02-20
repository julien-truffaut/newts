package newts.internal

import cats.kernel.Eq
import cats.kernel.laws.OrderLaws
import cats.laws._
import cats.laws.discipline._
import cats.syntax.order._
import org.scalacheck.{Arbitrary, Cogen}
import org.scalacheck.Prop.forAll

trait BoundedTests[A] extends OrderLaws[A] {
  def maxBounded(implicit maxBounded: MaxBounded[A]): RuleSet = new OrderProperties(
    name = "MaxBounded",
    parent = Some(order),
    "maxValue is the maximum" -> forAll((a: A) => (maxBounded.maxValue max a) <-> maxBounded.maxValue)
  )

  def minBounded(implicit minBounded: MinBounded[A]): RuleSet = new OrderProperties(
    name = "MaxBounded",
    parent = Some(order),
    "minValue is the minimum" -> forAll((a: A) => (minBounded.minValue min a) <-> minBounded.minValue)
  )
}

object BoundedTests {
  def apply[A: Eq: Arbitrary: Cogen]: BoundedTests[A] =
    new BoundedTests[A] {
      def Equ = Eq[A]
      def Arb = implicitly[Arbitrary[A]]
      def Cog = implicitly[Cogen[A]]
    }
}