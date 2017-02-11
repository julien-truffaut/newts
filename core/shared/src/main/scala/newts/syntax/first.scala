package newts.syntax

import newts.First

object first extends FirstSyntax

trait FirstSyntax {
  implicit final def firstOps[A](self: A): FirstOps[A] = new FirstOps(self)
}

final class FirstOps[A](val self: A)extends AnyVal {
  def first: First[A] = First(self)
}