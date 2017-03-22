package newts

/**
  * `S` is a newtype means `S` is a wrapper around an `A`
  */
trait Newtype[S, A] {
  def wrap(value: A): S
  def unwrap(value: S): A
}

object Newtype {
  def apply[S, A](implicit ev: Newtype[S, A]): Newtype[S, A] = ev

  def from[S, A](f: A => S)(g: S => A): Newtype[S, A] = new Newtype[S, A] {
    def wrap(value: A): S   = f(value)
    def unwrap(value: S): A = g(value)
  }
}
