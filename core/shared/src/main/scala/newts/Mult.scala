package newts

import cats.kernel.{CommutativeMonoid, Eq}
import cats.syntax.functor._
import cats.{Applicative, CommutativeMonad, Distributive, Eval, Functor, Show, Traverse}

import scala.annotation.tailrec

final case class Mult[A](getMult: A) extends AnyVal

object Mult extends MultInstances0 {
  implicit def newtypeInstance[A]: Newtype.Aux[Mult[A], A] = Newtype.from[Mult[A], A](Mult.apply)(_.getMult)

  implicit val monadInstance: CommutativeMonad[Mult] = new CommutativeMonad[Mult] {
    def pure[A](x: A): Mult[A] = Mult(x)
    def flatMap[A, B](fa: Mult[A])(f: A => Mult[B]): Mult[B] = f(fa.getMult)
    @tailrec
    def tailRecM[A, B](a: A)(f: A => Mult[Either[A, B]]): Mult[B] = f(a) match {
      case Mult(Left(a1)) => tailRecM(a1)(f)
      case Mult(Right(b)) => Mult(b)
    }
  }

  implicit def monoidInstance[A](implicit num: Numeric[A]): CommutativeMonoid[Mult[A]] = new CommutativeMonoid[Mult[A]] {
    val empty: Mult[A] = Mult(num.one)
    def combine(x: Mult[A], y: Mult[A]): Mult[A] = Mult(num.times(x.getMult, y.getMult))
  }

  implicit def eqInstance[A: Eq]: Eq[Mult[A]] = Eq.by(_.getMult)

  implicit def showInstance[A](implicit ev: Show[A]): Show[Mult[A]] = Show.show(a =>
    s"Mult(${ev.show(a.getMult)})"
  )
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

  implicit val distributiveInstance: Distributive[Mult] = new Distributive[Mult] {
    def distribute[G[_], A, B](ga: G[A])(f: A => Mult[B])(implicit ev: Functor[G]): Mult[G[B]] =
      Mult(ga.map(f(_).getMult))

    def map[A, B](fa: Mult[A])(f: A => B): Mult[B] = Mult(f(fa.getMult))
  }
}
