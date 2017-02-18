package newts.internal

import cats.Order
import cats.kernel.instances.all._

trait MaxBounded[A] extends Order[A]{
  def maxValue: A
}

object MaxBounded{
  def apply[A](implicit ev: MaxBounded[A]): MaxBounded[A] = ev

  implicit val shortMaxBounded: MaxBounded[Short] = fromOrder(Short.MaxValue)(catsKernelStdOrderForShort)
  implicit val intMaxBounded  : MaxBounded[Int]   = fromOrder(  Int.MaxValue)(catsKernelStdOrderForInt)
  implicit val longMaxBounded : MaxBounded[Long]  = fromOrder( Long.MaxValue)(catsKernelStdOrderForLong)

  def fromOrder[A](value: A)(A: Order[A]): MaxBounded[A] = new MaxBounded[A] {
    def maxValue: A = value
    def compare(x: A, y: A): Int = A.compare(x, y)
  }
}