package newts

import cats.{Monoid, MonoidK}
import cats.instances.option._
import cats.kernel.Eq

final case class First[A](value: Option[A]) extends AnyVal

object First {
  implicit val monoidKInstance: MonoidK[First] = new MonoidK[First] {
    def empty[A]: First[A] = First(None)
    def combineK[A](x: First[A], y: First[A]): First[A] = First(x.value.orElse(y.value))
  }

  implicit def monoidInstance[A]: Monoid[First[A]] = MonoidK[First].algebra

  implicit def eqInstance[A: Eq]: Eq[First[A]] = Eq.by(_.value)
}