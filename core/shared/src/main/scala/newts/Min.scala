package newts

import cats.{Semigroup, Show}
import cats.kernel.Order
import cats.syntax.order._

final case class Min[A](getMin: A) extends AnyVal

object Min {
  implicit def instances[A: Order]: Order[Min[A]] with Semigroup[Min[A]] = new Order[Min[A]] with Semigroup[Min[A]] {
    def combine(x: Min[A], y: Min[A]): Min[A] = Min(x.getMin min y.getMin)

    def compare(x: Min[A], y: Min[A]): Int = x.getMin compare y.getMin
  }

  implicit def showInstance[A](implicit ev: Show[A]): Show[Min[A]] = new Show[Min[A]] {
    override def show(f: Min[A]): String = s"Min(${ev.show(f.getMin)})"
  }
}
