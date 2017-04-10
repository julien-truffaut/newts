package newts

/**
  * `S` is a newtype means `S` is a wrapper around an `A`
  */
trait Newtype[S] {
  type A
  def wrap(value: A): S
  def unwrap(value: S): A
}

object Newtype {
  type Aux[S, A0] = Newtype[S] { type A = A0 }

  def apply[S](implicit ev: Newtype[S]): Aux[S, ev.A] = ev

  def from[S, A0](f: A0 => S)(g: S => A0): Aux[S, A0] = new Newtype[S] {
    type A = A0
    def wrap(value: A): S   = f(value)
    def unwrap(value: S): A = g(value)
  }
}
