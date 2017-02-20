package newts

import cats.kernel.Order
import cats.syntax.order._
import cats.{Monoid, Semigroup, Show}
import newts.internal.MinBounded

final case class Max[A](getMax: A) extends AnyVal

object Max extends MaxInstances0{
  implicit def instances[A: Order]: Order[Max[A]] with Semigroup[Max[A]] = new Order[Max[A]] with Semigroup[Max[A]] {
    def combine(x: Max[A], y: Max[A]): Max[A] = Max(x.getMax max y.getMax)
    def compare(x: Max[A], y: Max[A]): Int = x.getMax compare y.getMax
  }

  implicit def showInstance[A](implicit ev: Show[A]): Show[Max[A]] = new Show[Max[A]] {
    override def show(f: Max[A]): String = s"Max(${ev.show(f.getMax)})"
  }
}

trait MaxInstances0{
  implicit def maxMonoid[A](implicit A: MinBounded[A]): Monoid[Max[A]] = new Monoid[Max[A]]{
    def empty: Max[A] = Max(A.minValue)
    def combine(x: Max[A], y: Max[A]): Max[A] = Max(x.getMax max y.getMax)
  }
}
