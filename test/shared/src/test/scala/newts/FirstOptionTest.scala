package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.MonoidKTests

class FirstOptionTest extends NewtsSuite {

  checkAll("FirstOption[Int]", MonoidKTests[FirstOption].monoidK[Int])
  checkAll("FirstOption[Int]", GroupLaws[FirstOption[Int]].monoid)
  checkAll("FirstOption[Int]", OrderLaws[FirstOption[Int]].eqv)

  test("combine"){
    FirstOption(Some(1))   |+| FirstOption(Some(2)) shouldEqual FirstOption(Some(1))
    FirstOption(none[Int]) |+| FirstOption(Some(2)) shouldEqual FirstOption(Some(2))
  }

}
