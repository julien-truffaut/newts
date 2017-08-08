package newts

import cats.kernel.Eq
import cats.{Monoid, Show}

final case class All(getAll: Boolean) extends AnyVal

object All {
  implicit val newtypeInstance: Newtype.Aux[All, Boolean] = Newtype.from(All.apply)(_.getAll)

  implicit val monoidInstance: Monoid[All] with Eq[All] = new Monoid[All] with Eq[All]{
    def eqv(x: All, y: All): Boolean = x == y
    val empty: All = All(true)
    def combine(x: All, y: All): All = All(x.getAll && y.getAll)
  }

  implicit def showInstance: Show[All] = Show.fromToString
}