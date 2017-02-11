package newts

import cats.{Monoid, Semigroup}
import cats.kernel.Eq

final case class Dual[A](value: A) extends AnyVal

object Dual extends DualInstances0 {
  implicit def semigroupInstances[A](implicit A: Semigroup[A]): Semigroup[Dual[A]] = new Semigroup[Dual[A]]{
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(A.combine(y.value, x.value))
  }

  implicit def dualEq[A: Eq]: Eq[Dual[A]] = Eq.by(_.value)
}

trait DualInstances0 {
  implicit def monoidInstances[A](implicit A: Monoid[A]): Monoid[Dual[A]] = new Monoid[Dual[A]]{
    def empty: Dual[A] = Dual(A.empty)
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(A.combine(y.value, x.value))
  }
}