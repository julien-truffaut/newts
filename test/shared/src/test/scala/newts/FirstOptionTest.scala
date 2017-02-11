package newts

import cats.laws.discipline.MonoidKTests

class FirstOptionTest extends NewtsSuite {

  checkAll("First[Int]", MonoidKTests[FirstOption].monoidK[Int])

  test("combine"){
    FirstOption(Some(1))           |+| FirstOption(Some(2)) shouldEqual FirstOption(Some(1))
    FirstOption(Option.empty[Int]) |+| FirstOption(Some(2)) shouldEqual FirstOption(Some(2))
  }

}
