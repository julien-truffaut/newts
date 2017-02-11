package newts.syntax

import newts._

object all extends AllSyntax

trait AllSyntax {
  implicit final def booleanOps(self: Boolean): BooleanOps = new BooleanOps(self)
  implicit final def listOps[A](self: List[A]): ListOps[A] = new ListOps(self)
  implicit final def valueOps[A](self: A): ValueOps[A] = new ValueOps(self)
}

final class BooleanOps(val self: Boolean) extends AnyVal {
  def all: All = All(self)
}

final class ListOps[A](val self: List[A]) extends AnyVal {
  def zipList: ZipList[A] = ZipList(self)
}

final class ValueOps[A](val self: A) extends AnyVal {
  def dual: Dual[A] = Dual(self)
  def first: First[A] = First(self)
}