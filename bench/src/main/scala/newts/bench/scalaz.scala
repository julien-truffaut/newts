package newts.bench

import cats.Monoid

object scalaz {

  type @@[T, U] = { type Self = T; type Tag = U }

  def tag[U] = new Tagger[U]

  def untag[T, U](tu: T @@ U): T = tu.asInstanceOf[T]

  class Tagger[U] {
    def apply[T](t: T): T @@ U = t.asInstanceOf[T @@ U]
  }

  sealed trait Conjunction

  type ConjunctionZ = Boolean @@ Conjunction

  implicit val conjunctionZMonoid: Monoid[ConjunctionZ] = new Monoid[ConjunctionZ]{
    def empty: ConjunctionZ = tag[Conjunction](true)
    def combine(x: ConjunctionZ, y: ConjunctionZ): ConjunctionZ =
      tag[Conjunction](untag(x) && untag(y))
  }

  val True : ConjunctionZ = tag[Conjunction](true)
  val False: ConjunctionZ = tag[Conjunction](false)

}
