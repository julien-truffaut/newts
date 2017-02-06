package newts

import cats.laws.discipline.MonoidKTests

class LastTest extends NewtsSuite {

  checkAll("Last[Int]", MonoidKTests[Last].monoidK[Int])

  test("combine"){
    Last(Some(1)) |+| Last(Some(2))           shouldEqual Last(Some(2))
    Last(Some(1)) |+| Last(Option.empty[Int]) shouldEqual Last(Some(1))
  }

}
