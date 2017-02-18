package newts

import cats.Monoid
import cats.kernel.Eq

final case class Mult[A](getMult: A) extends AnyVal

object Mult {
  implicit def instances[A](implicit num: Numeric[A]): Monoid[Mult[A]] with Eq[Mult[A]] = new Monoid[Mult[A]] with Eq[Mult[A]] {
    def eqv(x: Mult[A], y: Mult[A]): Boolean = x == y

    val empty: Mult[A] = Mult(num.one)

    def combine(x: Mult[A], y: Mult[A]): Mult[A] = Mult(num.times(x.getMult, y.getMult))
  }
}
