package newts.internal

import cats.Order
import cats.kernel.instances.all._

trait MinBounded[A] extends Order[A]{
  def minValue: A
}

object MinBounded{
  def apply[A](implicit ev: MinBounded[A]): MinBounded[A] = ev

  implicit val shortMinBounded : MinBounded[Short]  = fromOrder(Short.MinValue)(catsKernelStdOrderForShort)
  implicit val intMinBounded   : MinBounded[Int]    = fromOrder(  Int.MinValue)(catsKernelStdOrderForInt)
  implicit val longMinBounded  : MinBounded[Long]   = fromOrder( Long.MinValue)(catsKernelStdOrderForLong)
  implicit val stringMinBounded: MinBounded[String] = fromOrder("")(catsKernelStdOrderForString)
  implicit def optionMinBounded[A: Order]: MinBounded[Option[A]] = fromOrder(Option.empty[A])(catsKernelStdOrderForOption)

  def fromOrder[A](value: A)(A: Order[A]): MinBounded[A] = new MinBounded[A] {
    def minValue: A = value
    def compare(x: A, y: A): Int = A.compare(x, y)
  }
}