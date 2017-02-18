package newts

import cats.{MonadCombine, Monoid, MonoidK, Show}
import cats.instances.option._
import cats.kernel.Eq

final case class LastOption[A](getLastOption: Option[A]) extends AnyVal

object LastOption {
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
