package newts

import cats.kernel.{CommutativeMonoid, CommutativeSemigroup, Order}
import cats.syntax.functor._
import cats.syntax.order._
import cats.{Applicative, CommutativeMonad, Distributive, Eval, Functor, Show, Traverse}
import newts.internal.MinBounded

import scala.annotation.tailrec

final case class Max[A](getMax: A) extends AnyVal

object Max extends MaxInstances0{
  implicit def newtypeInstance[A]: Newtype.Aux[Max[A], A] = Newtype.from[Max[A], A](Max.apply)(_.getMax)

  implicit val monadInstance: CommutativeMonad[Max] = new CommutativeMonad[Max] {
    def pure[A](x: A): Max[A] = Max(x)
    def flatMap[A, B](fa: Max[A])(f: A => Max[B]): Max[B] = f(fa.getMax)
    @tailrec
    def tailRecM[A, B](a: A)(f: A => Max[Either[A, B]]): Max[B] = f(a) match {
      case Max(Left(a1)) => tailRecM(a1)(f)
      case Max(Right(b)) => Max(b)
    }
  }
  
  implicit def instances[A: Order]: Order[Max[A]] with CommutativeSemigroup[Max[A]] = new Order[Max[A]] with CommutativeSemigroup[Max[A]] {
    def combine(x: Max[A], y: Max[A]): Max[A] = Max(x.getMax max y.getMax)
    def compare(x: Max[A], y: Max[A]): Int = x.getMax compare y.getMax
  }

  implicit def showInstance[A](implicit ev: Show[A]): Show[Max[A]] = Show.show(a =>
    s"Max(${ev.show(a.getMax)})"
  )
}

trait MaxInstances0{
  implicit def maxMonoid[A](implicit A: MinBounded[A]): CommutativeMonoid[Max[A]] = new CommutativeMonoid[Max[A]]{
    def empty: Max[A] = Max(A.minValue)
    def combine(x: Max[A], y: Max[A]): Max[A] = Max(x.getMax max y.getMax)
  }

  implicit val traverseInstance: Traverse[Max] = new Traverse[Max] {
    def traverse[G[_], A, B](fa: Max[A])(f: A => G[B])(implicit ev: Applicative[G]): G[Max[B]] =
      f(fa.getMax).map(Max(_))

    def foldLeft[A, B](fa: Max[A], b: B)(f: (B, A) => B): B =
      f(b, fa.getMax)

    def foldRight[A, B](fa: Max[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      f(fa.getMax, lb)
  }

  implicit val distributiveInstance: Distributive[Max] = new Distributive[Max] {
    def distribute[G[_], A, B](ga: G[A])(f: A => Max[B])(implicit ev: Functor[G]): Max[G[B]] =
      Max(ga.map(f(_).getMax))

    def map[A, B](fa: Max[A])(f: A => B): Max[B] = Max(f(fa.getMax))
  }
}
