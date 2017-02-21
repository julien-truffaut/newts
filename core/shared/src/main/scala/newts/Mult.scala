package newts

import cats.kernel.Eq
import cats.syntax.functor._
import cats.{Applicative, Eval, Monad, Monoid, Traverse}

import scala.annotation.tailrec

final case class Mult[A](getMult: A) extends AnyVal

object Mult extends MultInstances0 {
  implicit val monadInstance: Monad[Mult] = new Monad[Mult] {
    def pure[A](x: A): Mult[A] = Mult(x)
    def flatMap[A, B](fa: Mult[A])(f: A => Mult[B]): Mult[B] = f(fa.getMult)
    @tailrec
    def tailRecM[A, B](a: A)(f: A => Mult[Either[A, B]]): Mult[B] = f(a) match {
      case Mult(Left(a1)) => tailRecM(a1)(f)
      case Mult(Right(b)) => Mult(b)
    }
  }

  implicit def monoidInstance[A](implicit num: Numeric[A]): Monoid[Mult[A]] = new Monoid[Mult[A]] {
    val empty: Mult[A] = Mult(num.one)
    def combine(x: Mult[A], y: Mult[A]): Mult[A] = Mult(num.times(x.getMult, y.getMult))
  }

  implicit def eqInstance[A: Eq]: Eq[Mult[A]] = Eq.by(_.getMult)
}

trait MultInstances0 {
  implicit val traverseInstance: Traverse[Mult] = new Traverse[Mult] {
    def traverse[G[_], A, B](fa: Mult[A])(f: A => G[B])(implicit ev: Applicative[G]): G[Mult[B]] =
      f(fa.getMult).map(Mult(_))

    def foldLeft[A, B](fa: Mult[A], b: B)(f: (B, A) => B): B =
      f(b, fa.getMult)

    def foldRight[A, B](fa: Mult[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      f(fa.getMult, lb)
  }
}