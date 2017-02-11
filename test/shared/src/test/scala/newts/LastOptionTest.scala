package newts

import cats.laws.discipline.MonoidKTests

class LastOptionTest extends NewtsSuite {

  checkAll("LastOption[Int]", MonoidKTests[LastOption].monoidK[Int])

  test("combine"){
    LastOption(Some(1)) |+| LastOption(Some(2)) shouldEqual LastOption(Some(2))
    LastOption(Some(1)) |+| LastOption(none)    shouldEqual LastOption(Some(1))
  }

}
