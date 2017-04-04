package newts

import cats.{Applicative, Eq, Eval, Monad, Semigroup, SemigroupK, Show, Traverse}
import cats.syntax.functor._

import scala.annotation.tailrec

final case class First[A](getFirst: A) extends AnyVal

object First extends FirstInstances0 {
  implicit def newtypeInstance[A]: Newtype.Aux[First[A], A] = Newtype.from[First[A], A](First.apply)(_.getFirst)

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

trait FirstInstances0 {
  implicit val traverseInstance: Traverse[First] = new Traverse[First] {
    def traverse[G[_], A, B](fa: First[A])(f: A => G[B])(implicit ev: Applicative[G]): G[First[B]] =
      f(fa.getFirst).map(First(_))

    def foldLeft[A, B](fa: First[A], b: B)(f: (B, A) => B): B =
      f(b, fa.getFirst)

    def foldRight[A, B](fa: First[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      f(fa.getFirst, lb)
  }
}