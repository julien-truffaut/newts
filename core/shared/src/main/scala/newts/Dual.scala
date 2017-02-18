package newts

import cats.{Monoid, Semigroup, Show}
import cats.kernel.Eq

final case class Dual[A](getDual: A) extends AnyVal

object Dual extends DualInstances0 {
  implicit def semigroupInstance[A](implicit A: Semigroup[A]): Semigroup[Dual[A]] = new Semigroup[Dual[A]]{
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(A.combine(y.getDual, x.getDual))
  }

  implicit def showInstance[A : Show]: Show[Dual[A]] = new Show[Dual[A]] {
    override def show(f: Dual[A]): String = s"Dual(${Show[A].show(f.getDual)})"
  }

  implicit def dualEq[A: Eq]: Eq[Dual[A]] = Eq.by(_.getDual)
}

trait DualInstances0 {
  implicit def monoidInstances[A](implicit A: Monoid[A]): Monoid[Dual[A]] = new Monoid[Dual[A]]{
    def empty: Dual[A] = Dual(A.empty)
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(A.combine(y.getDual, x.getDual))
  }
}