package newts

import cats.{Eq, Semigroup, SemigroupK}

final case class First[A](value: A) extends AnyVal

object First {
  implicit val semigroupKInstance: SemigroupK[First] = new SemigroupK[First] {
    def combineK[A](x: First[A], y: First[A]) = x
  }

  implicit def semigroupInstance[A]: Semigroup[First[A]] = SemigroupK[First].algebra

  implicit def eqInstance[A: Eq]: Eq[First[A]] = Eq.by(_.value)
}