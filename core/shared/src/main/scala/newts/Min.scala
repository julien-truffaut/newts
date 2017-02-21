package newts

import cats.kernel.Order
import cats.syntax.order._
import cats.{Monad, Monoid, Semigroup, Show}
import newts.internal.MaxBounded

import scala.annotation.tailrec

final case class Min[A](getMin: A) extends AnyVal

object Min extends MinInstances0{
  implicit val monadInstance: Monad[Min] = new Monad[Min] {
    def pure[A](x: A): Min[A] = Min(x)
    def flatMap[A, B](fa: Min[A])(f: A => Min[B]): Min[B] = f(fa.getMin)
    @tailrec
    def tailRecM[A, B](a: A)(f: A => Min[Either[A, B]]): Min[B] = f(a) match {
      case Min(Left(a1)) => tailRecM(a1)(f)
      case Min(Right(b)) => Min(b)
    }
  }
  
  implicit def instances[A: Order]: Order[Min[A]] with Semigroup[Min[A]] = new Order[Min[A]] with Semigroup[Min[A]] {
    def combine(x: Min[A], y: Min[A]): Min[A] = Min(x.getMin min y.getMin)
    def compare(x: Min[A], y: Min[A]): Int = x.getMin compare y.getMin
  }

  implicit def showInstance[A](implicit ev: Show[A]): Show[Min[A]] = new Show[Min[A]] {
    override def show(f: Min[A]): String = s"Min(${ev.show(f.getMin)})"
  }
}

trait MinInstances0{
  implicit def minMonoid[A](implicit A: MaxBounded[A]): Monoid[Min[A]] = new Monoid[Min[A]]{
    def empty: Min[A] = Min(MaxBounded[A].maxValue)
    def combine(x: Min[A], y: Min[A]): Min[A] = Min(x.getMin min y.getMin)
  }
}
