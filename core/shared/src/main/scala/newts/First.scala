package newts

import cats.{Eq, Monad, Semigroup, SemigroupK, Show}

import scala.annotation.tailrec

final case class First[A](getFirst: A) extends AnyVal

object First {
  implicit val monadInstance: Monad[First] = new Monad[First] {
    def pure[A](x: A): First[A] = First(x)
    def flatMap[A, B](fa: First[A])(f: A => First[B]): First[B] = f(fa.getFirst)
    @tailrec
    def tailRecM[A, B](a: A)(f: A => First[Either[A, B]]): First[B] = f(a) match {
      case First(Left(a1)) => tailRecM(a1)(f)
      case First(Right(b)) => First(b)
    }
  }
  
  implicit val semigroupKInstance: SemigroupK[First] = new SemigroupK[First] {
    def combineK[A](x: First[A], y: First[A]) = x
  }

  implicit def semigroupInstance[A]: Semigroup[First[A]] = SemigroupK[First].algebra

  implicit def eqInstance[A: Eq]: Eq[First[A]] = Eq.by(_.getFirst)

  implicit def showIntance[A : Show]: Show[First[A]] = new Show[First[A]] {
    override def show(f: First[A]): String = s"First(${Show[A].show(f.getFirst)})"
  }
}
