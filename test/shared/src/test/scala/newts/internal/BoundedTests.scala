package newts.internal

import cats.kernel.Eq
import cats.kernel.laws.OrderLaws
import cats.laws._
import cats.laws.discipline._
import cats.syntax.order._
import org.scalacheck.{Arbitrary, Cogen}
import org.scalacheck.Prop.forAll

trait BoundedTests[A] extends OrderLaws[A] {
  def minBounded(implicit minBounded: MaxBounded[A]): RuleSet = new OrderProperties(
    name = "MaxBounded",
    parent = Some(order),
    "maxValue is the maximum" -> forAll((a: A) => (MaxBounded[A].maxValue max a) <-> MaxBounded[A].maxValue)
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