package newts

import cats.instances.option._
import cats.kernel.Eq
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.{Applicative, Eval, MonadCombine, Monoid, MonoidK, Show, Traverse}

final case class LastOption[A](getLastOption: Option[A]) extends AnyVal

object LastOption extends LastOptionInstances0 {
  implicit val monadCombineInstance: MonadCombine[LastOption] = new MonadCombine[LastOption] {
    override def empty[A]: LastOption[A] = LastOption(None)
    override def combineK[A](x: LastOption[A], y: LastOption[A]): LastOption[A] = LastOption(y.getLastOption.orElse(x.getLastOption))
    override def pure[A](x: A): LastOption[A] = LastOption(Some(x))
    override def flatMap[A, B](fa: LastOption[A])(f: (A) => LastOption[B]): LastOption[B] =
      fa.getLastOption.fold(empty[B])(f)

    override def tailRecM[A, B](a: A)(f: (A) => LastOption[Either[A, B]]): LastOption[B] = {
      f(a).getLastOption match {
        case None => empty
        case Some(Left(a1)) => tailRecM(a1)(f)
        case Some(Right(b)) => pure(b)
      }
    }
  }

  implicit def monoidInstance[A]: Monoid[LastOption[A]] = MonoidK[LastOption].algebra

  implicit def eqInstance[A: Eq]: Eq[LastOption[A]] = Eq.by(_.getLastOption)

  implicit def showInstance[A](implicit ev: Show[Option[A]]): Show[LastOption[A]] = new Show[LastOption[A]] {
    override def show(f: LastOption[A]): String =  s"LastOption(${ev.show(f.getLastOption)})"
  }
}

trait LastOptionInstances0 {
  implicit val traverseInstance: Traverse[LastOption] = new Traverse[LastOption] {
    def traverse[G[_], A, B](fa: LastOption[A])(f: A => G[B])(implicit ev: Applicative[G]): G[LastOption[B]] =
      fa.getLastOption.traverse(f).map(LastOption(_))

    def foldLeft[A, B](fa: LastOption[A], b: B)(f: (B, A) => B): B =
      fa.getLastOption.fold(b)(f(b, _))

    def foldRight[A, B](fa: LastOption[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa.getLastOption.fold(lb)(f(_, lb))
  }
}