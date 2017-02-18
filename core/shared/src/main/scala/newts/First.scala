package newts

import cats.{Eq, Semigroup, SemigroupK, Show}

final case class First[A](getFirst: A) extends AnyVal

object First {
  implicit val semigroupKInstance: SemigroupK[First] = new SemigroupK[First] {
    def combineK[A](x: First[A], y: First[A]) = x
  }

  implicit def semigroupInstance[A]: Semigroup[First[A]] = SemigroupK[First].algebra

  implicit def eqInstance[A: Eq]: Eq[First[A]] = Eq.by(_.getFirst)

  implicit def showIntance[A : Show]: Show[First[A]] = new Show[First[A]] {
    override def show(f: First[A]): String = s"First(${Show[A].show(f.getFirst)})"
  }
}
