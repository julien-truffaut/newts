package newts

import cats.{Monoid, Semigroup}
import cats.kernel.Order
import cats.syntax.order._
import newts.internal.MaxBounded

final case class Min[A](getMin: A) extends AnyVal

object Min extends MinInstances0{
  implicit def instances[A: Order]: Order[Min[A]] with Semigroup[Min[A]] = new Order[Min[A]] with Semigroup[Min[A]] {
    def combine(x: Min[A], y: Min[A]): Min[A] = Min(x.getMin min y.getMin)
    def compare(x: Min[A], y: Min[A]): Int = x.getMin compare y.getMin
  }
}

trait MinInstances0{
  implicit def minMonoid[A: MaxBounded]: Monoid[Min[A]] = new Monoid[Min[A]]{
    def empty: Min[A] = Min(MaxBounded[A].maxValue)
    def combine(x: Min[A], y: Min[A]): Min[A] = Min(x.getMin min y.getMin)
  }
}