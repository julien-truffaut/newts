package newts.internal

import cats.kernel.Eq
import cats.kernel.laws.OrderLaws
import cats.kernel.laws.discipline.OrderTests
import cats.laws._
import cats.laws.discipline._
import cats.syntax.order._
import org.scalacheck.{Arbitrary, Cogen}
import org.scalacheck.Prop.forAll

object laws {

  trait MaxBoundedLaws[A] extends OrderLaws[A] {

    override implicit def E: MaxBounded[A]

    def maxValue(a: A): IsEq[A] =
      (E.maxValue max a) <-> E.maxValue

  }
  object MaxBoundedLaws {
    def apply[A](implicit ev: MaxBounded[A]): MaxBoundedLaws[A] =
      new MaxBoundedLaws[A] { def E: MaxBounded[A] = ev }
  }


  trait MinBoundedLaws[A] extends OrderLaws[A] {

    override implicit def E: MinBounded[A]

    def maxValue(a: A): IsEq[A] =
      (E.minValue min a) <-> E.minValue

  }
  object MinBoundedLaws {
    def apply[A](implicit ev: MinBounded[A]): MinBoundedLaws[A] =
      new MinBoundedLaws[A] { def E: MinBounded[A] = ev }
  }

  object discipline {

    trait MaxBoundedTests[A] extends OrderTests[A] {

      override def laws: MaxBoundedLaws[A]

      def maxBounded(implicit arbA: Arbitrary[A], arbF: Arbitrary[A => A], eqOA: Eq[Option[A]], eqA: Eq[A]): RuleSet =
        new DefaultRuleSet(
          "maxBounded",
          Some(order),
          "maxValue" -> forAll(laws.maxValue _)
        )

    }
    object MaxBoundedTests {
      def apply[A: MaxBounded]: MaxBoundedTests[A] =
        new MaxBoundedTests[A] { def laws: MaxBoundedLaws[A] = MaxBoundedLaws[A] }
    }

    trait MinBoundedTests[A] extends OrderTests[A] {

      override def laws: MinBoundedLaws[A]

      def minBounded(implicit arbA: Arbitrary[A], arbF: Arbitrary[A => A], eqOA: Eq[Option[A]], eqA: Eq[A]): RuleSet =
        new DefaultRuleSet(
          "minBounded",
          Some(order),
          "minValue" -> forAll(laws.maxValue _)
        )

    }
    object MinBoundedTests {
      def apply[A: MinBounded]: MinBoundedTests[A] =
        new MinBoundedTests[A] { def laws: MinBoundedLaws[A] = MinBoundedLaws[A] }
    }

  }

}
