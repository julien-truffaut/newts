package newts

import cats.{Monad, Monoid}
import cats.kernel.Eq

import scala.annotation.tailrec

final case class Mult[A](getMult: A) extends AnyVal

object Mult {
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
