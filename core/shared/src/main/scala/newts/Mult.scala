package newts

import cats.Monoid
import cats.kernel.Eq

final case class Mult[A](getMult: A) extends AnyVal

object Mult {
  implicit def instances[A](implicit num: Numeric[A]): Monoid[Mult[A]] = new Monoid[Mult[A]] {

    val empty: Mult[A] = Mult(num.one)

    def combine(x: Mult[A], y: Mult[A]): Mult[A] = Mult(num.times(x.getMult, y.getMult))
  }

  implicit def eqInstance[A: Eq]: Eq[Mult[A]] = Eq.by(_.getMult)
}
