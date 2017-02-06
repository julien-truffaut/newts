package newts

import cats.{Monoid, MonoidK}
import cats.instances.option._
import cats.kernel.Eq

final case class Last[A](value: Option[A]) extends AnyVal

object Last {
  implicit val monoidKInstance: MonoidK[Last] = new MonoidK[Last] {
    def empty[A]: Last[A] = Last(None)
    def combineK[A](x: Last[A], y: Last[A]): Last[A] = Last(y.value.orElse(x.value))
  }

  implicit def monoidInstance[A]: Monoid[Last[A]] = MonoidK[Last].algebra

  implicit def eqInstance[A: Eq]: Eq[Last[A]] = Eq.by(_.value)
}