package newts

import cats.{Monoid, Semigroup, Show}
import cats.kernel.Eq

final case class Dual[A](getDual: A) extends AnyVal

object Dual extends DualInstances0 {
  implicit def semigroupInstances[A](implicit A: Semigroup[A]): Semigroup[Dual[A]] = new Semigroup[Dual[A]]{
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(A.combine(y.getDual, x.getDual))
  }

  implicit def showInstances[A : Show]: Show[Dual[A]] = new Show[Dual[A]] {
    override def show(dual: Dual[A]): String = s"Dual(${implicitly[Show[A]].show(dual.getDual)})"
  }

  implicit def dualEq[A: Eq]: Eq[Dual[A]] = Eq.by(_.getDual)
}

trait DualInstances0 {
  implicit def monoidInstances[A](implicit A: Monoid[A]): Monoid[Dual[A]] = new Monoid[Dual[A]]{
    def empty: Dual[A] = Dual(A.empty)
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(A.combine(y.getDual, x.getDual))
  }
}