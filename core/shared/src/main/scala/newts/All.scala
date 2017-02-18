package newts

import cats.{Monoid, Show}
import cats.kernel.Eq
import cats.implicits._

final case class All(getAll: Boolean) extends AnyVal

object All {
  implicit val instances: Monoid[All] with Eq[All] = new Monoid[All] with Eq[All]{
    def eqv(x: All, y: All): Boolean = x == y
    val empty: All = All(true)
    def combine(x: All, y: All): All = All(x.getAll && y.getAll)
  }

  implicit def showInstance: Show[All] = new Show[All] {
    override def show(all: All): String = s"All(${implicitly[Show[Boolean]].show(all.getAll)})"
  }
}