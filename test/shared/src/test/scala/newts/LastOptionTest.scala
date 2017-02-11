package newts

import cats.laws.discipline.MonoidKTests

class LastOptionTest extends NewtsSuite {

  checkAll("Last[Int]", MonoidKTests[LastOption].monoidK[Int])

  test("combine"){
    LastOption(Some(1)) |+| LastOption(Some(2))           shouldEqual LastOption(Some(2))
    LastOption(Some(1)) |+| LastOption(Option.empty[Int]) shouldEqual LastOption(Some(1))
  }

}
