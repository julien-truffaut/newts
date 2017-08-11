package newts.syntax

import newts._

object all extends AllSyntax

trait AllSyntax {
  implicit final def booleanOps(self: Boolean): BooleanOps = new BooleanOps(self)
  implicit final def listOps[A](self: List[A]): ListOps[A] = new ListOps(self)
  implicit final def optionOps[A](self: Option[A]): OptionOps[A] = new OptionOps(self)
  implicit final def valueOps[A](self: A): ValueOps[A] = new ValueOps(self)
  implicit final def wrapOps[A](self: A): WrapOps[A] = new WrapOps(self)
  implicit final def unwrapOps[S, A](self: S)(implicit ev: Newtype.Aux[S, A]): UnWrapOps[S, A] = new UnWrapOps(self)
}

final class BooleanOps(val self: Boolean) extends AnyVal {
  def asAll: All = All(self)
  def asAny: Any = Any(self)
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

final class WrapOps[A](val self: A) {
  def wrap[S](implicit S: Newtype.Aux[S, A]): S = S.wrap(self)
}

final class UnWrapOps[S, A](val self: S)(implicit val ev: Newtype.Aux[S, A]) {
  def unwrap: A = ev.unwrap(self)
}
