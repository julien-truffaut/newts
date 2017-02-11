package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.MonoidKTests

class LastOptionTest extends NewtsSuite {

  checkAll("LastOption[Int]", MonoidKTests[LastOption].monoidK[Int])
  checkAll("LastOption[Int]", GroupLaws[LastOption[Int]].monoid)
  checkAll("LastOption[Int]", OrderLaws[LastOption[Int]].eqv)

  test("combine"){
    LastOption(Some(1)) |+| LastOption(Some(2)) shouldEqual LastOption(Some(2))
    LastOption(Some(1)) |+| LastOption(none)    shouldEqual LastOption(Some(1))
  }

}
