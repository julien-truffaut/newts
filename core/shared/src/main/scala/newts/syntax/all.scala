package newts.syntax

import newts._

object all extends AllSyntax

trait AllSyntax {
  implicit final def booleanOps(self: Boolean): BooleanOps = new BooleanOps(self)
  implicit final def listOps[A](self: List[A]): ListOps[A] = new ListOps(self)
  implicit final def optionOps[A](self: Option[A]): OptionOps[A] = new OptionOps(self)
  implicit final def valueOps[A](self: A): ValueOps[A] = new ValueOps(self)
}

final class BooleanOps(val self: Boolean) extends AnyVal {
  def asAll: All = All(self)
}

final class ListOps[A](val self: List[A]) extends AnyVal {
  def asZipList: ZipList[A] = ZipList(self)
}

final class OptionOps[A](val self: Option[A]) extends AnyVal {
  def asFirstOption: FirstOption[A] = FirstOption(self)
  def asLastOption : LastOption[A]  = LastOption(self)
}


final class ValueOps[A](val self: A) extends AnyVal {
  def asDual: Dual[A] = Dual(self)
  def asFirst: First[A] = First(self)
  def asLast: Last[A] = Last(self)
  def asMin: Min[A] = Min(self)
  def asMax: Max[A] = Max(self)
  def asMult: Mult[A] = Mult(self)
}