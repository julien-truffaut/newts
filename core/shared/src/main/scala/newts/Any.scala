package newts

import cats.kernel.Eq
import cats.{Monoid, Show}

final case class Any(getAny: Boolean) extends AnyVal

object Any {
  implicit val newtypeInstance: Newtype.Aux[Any, Boolean] = Newtype.from(Any.apply)(_.getAny)

  implicit val monoidInstance: Monoid[Any] with Eq[Any] = new Monoid[Any] with Eq[Any]{
    def eqv(x: Any, y: Any): Boolean = x == y
    val empty: Any = Any(false)
    def combine(x: Any, y: Any): Any = Any(x.getAny || y.getAny)
  }

  implicit def showInstance: Show[Any] = Show.fromToString
}