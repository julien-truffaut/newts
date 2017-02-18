package newts

import cats.{Eq, Semigroup, SemigroupK, Show}

final case class First[A](getFirst: A) extends AnyVal

object First {
  implicit val semigroupKInstance: SemigroupK[First] = new SemigroupK[First] {
    def combineK[A](x: First[A], y: First[A]) = x
  }

  implicit def semigroupInstance[A]: Semigroup[First[A]] = SemigroupK[First].algebra

  implicit def eqInstance[A: Eq]: Eq[First[A]] = Eq.by(_.getFirst)

  implicit def showIntance[A](implicit ev: Show[A]): Show[First[A]] = (f: First[A]) => s"First(${ev.show(f.getFirst)})"
}
