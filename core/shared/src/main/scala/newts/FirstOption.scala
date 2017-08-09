package newts

import cats.{Alternative, Applicative, Eval, Monad, Monoid, MonoidK, Show, Traverse}
import cats.instances.option._
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.kernel.Eq

import scala.annotation.tailrec

final case class FirstOption[A](getFirstOption: Option[A]) extends AnyVal

object FirstOption extends FirstOptionInstances0 {
  implicit def newtypeInstance[A]: Newtype.Aux[FirstOption[A], Option[A]] =
    Newtype.from[FirstOption[A], Option[A]](FirstOption.apply)(_.getFirstOption)

  implicit val monadAlternativeInstance: Monad[FirstOption] with Alternative[FirstOption] = new Monad[FirstOption] with Alternative[FirstOption] {
    def empty[A]: FirstOption[A] = FirstOption(None)
    def combineK[A](x: FirstOption[A], y: FirstOption[A]): FirstOption[A] = FirstOption(x.getFirstOption.orElse(y.getFirstOption))
    def pure[A](x: A): FirstOption[A] = FirstOption(Some(x))
    def flatMap[A, B](fa: FirstOption[A])(f: A => FirstOption[B]): FirstOption[B] =
      fa.getFirstOption.fold(empty[B])(f)

    @tailrec
    def tailRecM[A, B](a: A)(f: A => FirstOption[Either[A, B]]): FirstOption[B] =
      f(a).getFirstOption match {
        case None           => empty
        case Some(Left(a1)) => tailRecM(a1)(f)
        case Some(Right(b)) => pure(b)
      }
  }

  implicit def monoidInstance[A]: Monoid[FirstOption[A]] = MonoidK[FirstOption].algebra

  implicit def eqInstance[A: Eq]: Eq[FirstOption[A]] = Eq.by(_.getFirstOption)

  implicit def showInstance[A: Show]: Show[FirstOption[A]] = Show.show(a =>
    s"FirstOption(${Show[Option[A]].show(a.getFirstOption)})"
  )
}

trait FirstOptionInstances0 {
  implicit val traverseInstance: Traverse[FirstOption] = new Traverse[FirstOption] {
    def traverse[G[_], A, B](fa: FirstOption[A])(f: A => G[B])(implicit ev: Applicative[G]): G[FirstOption[B]] =
      fa.getFirstOption.traverse(f).map(FirstOption(_))

    def foldLeft[A, B](fa: FirstOption[A], b: B)(f: (B, A) => B): B =
      fa.getFirstOption.fold(b)(f(b, _))

    def foldRight[A, B](fa: FirstOption[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa.getFirstOption.fold(lb)(f(_, lb))
  }
}
