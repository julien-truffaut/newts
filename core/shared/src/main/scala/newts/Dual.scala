package newts

import cats.kernel.Eq
import cats.syntax.functor._
import cats.{Applicative, Eval, Monad, Monoid, Semigroup, Show, Traverse}

import scala.annotation.tailrec

final case class Dual[A](getDual: A) extends AnyVal

object Dual extends DualInstances0 {
  implicit val monadInstance: Monad[Dual] = new Monad[Dual] {
    def pure[A](x: A): Dual[A] = Dual(x)
    def flatMap[A, B](fa: Dual[A])(f: A => Dual[B]): Dual[B] = f(fa.getDual)
    @tailrec
    def tailRecM[A, B](a: A)(f: A => Dual[Either[A, B]]): Dual[B] = f(a) match {
      case Dual(Left(a1)) => tailRecM(a1)(f)
      case Dual(Right(b)) => Dual(b)
    }
  }
  
  implicit def semigroupInstance[A](implicit A: Semigroup[A]): Semigroup[Dual[A]] = new Semigroup[Dual[A]]{
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(A.combine(y.getDual, x.getDual))
  }

  implicit def showInstance[A : Show]: Show[Dual[A]] = new Show[Dual[A]] {
    override def show(f: Dual[A]): String = s"Dual(${Show[A].show(f.getDual)})"
  }

  implicit def dualEq[A: Eq]: Eq[Dual[A]] = Eq.by(_.getDual)
}

trait DualInstances0 {
  implicit def monoidInstances[A](implicit A: Monoid[A]): Monoid[Dual[A]] = new Monoid[Dual[A]]{
    def empty: Dual[A] = Dual(A.empty)
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(A.combine(y.getDual, x.getDual))
  }

  implicit val traverseInstance: Traverse[Dual] = new Traverse[Dual] {
    def traverse[G[_], A, B](fa: Dual[A])(f: A => G[B])(implicit ev: Applicative[G]): G[Dual[B]] =
      f(fa.getDual).map(Dual(_))

    def foldLeft[A, B](fa: Dual[A], b: B)(f: (B, A) => B): B =
      f(b, fa.getDual)

    def foldRight[A, B](fa: Dual[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      f(fa.getDual, lb)
  }
}