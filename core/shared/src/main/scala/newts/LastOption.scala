package newts

import cats.{Monoid, MonoidK}
import cats.instances.option._
import cats.kernel.Eq

final case class LastOption[A](value: Option[A]) extends AnyVal

object LastOption {
  implicit val monoidKInstance: MonoidK[LastOption] = new MonoidK[LastOption] {
    def empty[A]: LastOption[A] = LastOption(None)
    def combineK[A](x: LastOption[A], y: LastOption[A]): LastOption[A] = LastOption(y.value.orElse(x.value))
  }

  implicit def monoidInstance[A]: Monoid[LastOption[A]] = MonoidK[LastOption].algebra

  implicit def eqInstance[A: Eq]: Eq[LastOption[A]] = Eq.by(_.value)
}