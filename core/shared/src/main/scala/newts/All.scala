package newts

import cats.Monoid
import cats.kernel.Eq

final case class All(getAll: Boolean) extends AnyVal

object All {
  implicit val instances: Monoid[All] with Eq[All] = new Monoid[All] with Eq[All]{
    def eqv(x: All, y: All): Boolean = x == y
    val empty: All = All(true)
    def combine(x: All, y: All): All = All(x.getAll && y.getAll)
  }
}