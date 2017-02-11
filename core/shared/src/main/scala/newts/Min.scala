package newts

import cats.Semigroup
import cats.kernel.Order
import cats.syntax.order._

final case class Min[A](value: A)

object Min {
  implicit def instances[A: Order]: Order[Min[A]] with Semigroup[Min[A]]  = new Order[Min[A]] with Semigroup[Min[A]] {
    def combine(x: Min[A], y: Min[A]): Min[A] = Min(x.value min y.value)
    def compare(x: Min[A], y: Min[A]): Int = x.value compare y.value
  }
}