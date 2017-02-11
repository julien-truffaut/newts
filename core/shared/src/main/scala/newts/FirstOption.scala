package newts

import cats.{Monoid, MonoidK}
import cats.instances.option._
import cats.kernel.Eq

final case class FirstOption[A](value: Option[A]) extends AnyVal

object FirstOption {
  implicit val monoidKInstance: MonoidK[FirstOption] = new MonoidK[FirstOption] {
    def empty[A]: FirstOption[A] = FirstOption(None)
    def combineK[A](x: FirstOption[A], y: FirstOption[A]): FirstOption[A] = FirstOption(x.value.orElse(y.value))
  }

  implicit def monoidInstance[A]: Monoid[FirstOption[A]] = MonoidK[FirstOption].algebra

  implicit def eqInstance[A: Eq]: Eq[FirstOption[A]] = Eq.by(_.value)
}