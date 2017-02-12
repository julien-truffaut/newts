package newts.bench

import cats.Monoid

object shapeless {

  type Tagged[U] = { type Tag = U }

  type @@[T, U] = T with Tagged[U]

  def tag[U] = new Tagger[U]

  def untag[T, U](tu: T @@ U): T = tu

  class Tagger[U] {
    def apply[T](t: T): T @@ U = t.asInstanceOf[T @@ U]
  }

  sealed trait Conjunction

  type ConjunctionS = Boolean @@ Conjunction

  def fromBoolean(b: Boolean): ConjunctionS = if(b) True else False

  implicit val conjunctionSMonoid: Monoid[ConjunctionS] = new Monoid[ConjunctionS]{
    def empty: ConjunctionS = True
    def combine(x: ConjunctionS, y: ConjunctionS): ConjunctionS =
      (untag(x) && untag(y)).asInstanceOf[ConjunctionS]
  }

  val True : ConjunctionS = true.asInstanceOf[ConjunctionS]
  val False: ConjunctionS = false.asInstanceOf[ConjunctionS]

}
