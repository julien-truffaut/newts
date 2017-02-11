package newts.syntax

import newts.{Dual, First}

object all extends AllSyntax

trait AllSyntax {
  implicit final def valueOps[A](self: A): ValueOps[A] = new ValueOps(self)
}

final class ValueOps[A](val self: A) extends AnyVal {
  def dual: Dual[A] = Dual(self)
  def first: First[A] = First(self)
}