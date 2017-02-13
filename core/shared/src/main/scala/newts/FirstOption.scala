package newts

import cats.{MonadCombine, Monoid, MonoidK}
import cats.instances.option._
import cats.kernel.Eq

import scala.annotation.tailrec

final case class FirstOption[A](getFirstOption: Option[A]) extends AnyVal

object FirstOption {
  implicit val monadCombineInstance: MonadCombine[FirstOption] = new MonadCombine[FirstOption] {
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
}