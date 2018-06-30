package newts

import cats.{Applicative, Distributive, Eq, Eval, Functor, Monad, Semigroup, SemigroupK, Show, Traverse}
import cats.syntax.functor._

import scala.annotation.tailrec

final case class Last[A](getLast: A) extends AnyVal

object Last extends LastInstances0 {
  implicit def newtypeInstance[A]: Newtype.Aux[Last[A], A] = Newtype.from[Last[A], A](Last.apply)(_.getLast)

  implicit val monadInstance: Monad[Last] = new Monad[Last] {
    def pure[A](x: A): Last[A] = Last(x)
    def flatMap[A, B](fa: Last[A])(f: A => Last[B]): Last[B] = f(fa.getLast)
    @tailrec
    def tailRecM[A, B](a: A)(f: A => Last[Either[A, B]]): Last[B] = f(a) match {
      case Last(Left(a1)) => tailRecM(a1)(f)
      case Last(Right(b)) => Last(b)
    }
  }
  
  implicit val semigroupKInstance: SemigroupK[Last] = new SemigroupK[Last] {
    def combineK[A](x: Last[A], y: Last[A]) = y
  }

  implicit def semigroupInstance[A]: Semigroup[Last[A]] = SemigroupK[Last].algebra

  implicit def eqInstance[A: Eq]: Eq[Last[A]] = Eq.by(_.getLast)

  implicit def showInstance[A](implicit ev: Show[A]): Show[Last[A]] = Show.show(a =>
    s"Last(${ev.show(a.getLast)})"
  )
}

trait LastInstances0 {
  implicit val traverseInstance: Traverse[Last] = new Traverse[Last] {
    def traverse[G[_], A, B](fa: Last[A])(f: A => G[B])(implicit ev: Applicative[G]): G[Last[B]] =
      f(fa.getLast).map(Last(_))

    def foldLeft[A, B](fa: Last[A], b: B)(f: (B, A) => B): B =
      f(b, fa.getLast)

    def foldRight[A, B](fa: Last[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      f(fa.getLast, lb)
  }

  implicit val distributiveInstance: Distributive[Last] = new Distributive[Last] {
    def distribute[G[_], A, B](ga: G[A])(f: A => Last[B])(implicit ev: Functor[G]): Last[G[B]] =
      Last(ga.map(f(_).getLast))

    def map[A, B](fa: Last[A])(f: A => B): Last[B] = Last(f(fa.getLast))
  }
}
