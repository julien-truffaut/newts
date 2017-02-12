package newts.bench

import cats.{Apply, Monoid}

object scalaz {

  type @@[T, U] = { type Self = T; type Tag = U }

  def tag[U] = new Tagger[U]

  def untag[T, U](tu: T @@ U): T = tu.asInstanceOf[T]

  class Tagger[U] {
    def apply[T](t: T): T @@ U = t.asInstanceOf[T @@ U]
  }

  sealed trait Conjunction

  type ConjunctionZ = Boolean @@ Conjunction

  def fromBoolean(b: Boolean): ConjunctionZ = tag[Conjunction](b)

  implicit val conjunctionZMonoid: Monoid[ConjunctionZ] = new Monoid[ConjunctionZ]{
    def empty: ConjunctionZ = tag[Conjunction](true)
    def combine(x: ConjunctionZ, y: ConjunctionZ): ConjunctionZ =
      tag[Conjunction](untag(x) && untag(y))
  }

  val True : ConjunctionZ = fromBoolean(true)
  val False: ConjunctionZ = fromBoolean(false)

  sealed trait Zip

  type ZipListZ[A] = List[A] @@ Zip

  implicit val zipListZApply: Apply[ZipListZ] = new Apply[ZipListZ]{
    def ap[A, B](ff: ZipListZ[A => B])(fa: ZipListZ[A]): ZipListZ[B] = map(product(ff, fa)){case (f, a) => f(a)}
    def map[A, B](fa: ZipListZ[A])(f: (A) => B): ZipListZ[B] = tag[Zip](untag(fa).map(f))

    override def product[A, B](fa: ZipListZ[A], fb: ZipListZ[B]): ZipListZ[(A, B)] =
      tag[Zip](untag(fa).zip(untag(fb)))
  }

}
