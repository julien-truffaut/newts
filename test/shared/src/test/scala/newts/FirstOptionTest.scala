package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.MonadCombineTests

class FirstOptionTest extends NewtsSuite {

  checkAll("FirstOption[Int]", MonadCombineTests[FirstOption].monoidK[Int])
  checkAll("FirstOption[Int]", GroupLaws[FirstOption[Int]].monoid)
  checkAll("FirstOption[Int]", OrderLaws[FirstOption[Int]].eqv)

  test("combine"){
    1.some.asFirstOption    |+| 2.some.asFirstOption shouldEqual FirstOption(Some(1))
    none[Int].asFirstOption |+| 2.some.asFirstOption shouldEqual FirstOption(Some(2))
  }

}
