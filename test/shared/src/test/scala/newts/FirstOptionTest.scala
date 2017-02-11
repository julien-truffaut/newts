package newts

import cats.laws.discipline.MonoidKTests

class FirstOptionTest extends NewtsSuite {

  checkAll("FirstOption[Int]", MonoidKTests[FirstOption].monoidK[Int])

  test("combine"){
    FirstOption(Some(1))   |+| FirstOption(Some(2)) shouldEqual FirstOption(Some(1))
    FirstOption(none[Int]) |+| FirstOption(Some(2)) shouldEqual FirstOption(Some(2))
  }

}
