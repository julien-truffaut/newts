package newts

import cats.laws.discipline.MonoidKTests

class FirstTest extends NewtsSuite {

  checkAll("First[Int]", MonoidKTests[First].monoidK[Int])

  test("combine"){
    First(Some(1))           |+| First(Some(2)) shouldEqual First(Some(1))
    First(Option.empty[Int]) |+| First(Some(2)) shouldEqual First(Some(2))
  }

}
