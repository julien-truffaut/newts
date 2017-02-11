package newts

import cats.Monoid
import cats.kernel.Eq

final case class Conjunction(value: Boolean) extends AnyVal

object Conjunction {
  implicit val instances: Monoid[Conjunction] with Eq[Conjunction] = new Monoid[Conjunction] with Eq[Conjunction]{
    def eqv(x: Conjunction, y: Conjunction): Boolean = x == y
    val empty: Conjunction = Conjunction(true)
    def combine(x: Conjunction, y: Conjunction): Conjunction = Conjunction(x.value && y.value)
  }
}