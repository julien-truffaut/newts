package newts

import cats.laws.discipline.SemigroupKTests

class FirstTest extends NewtsSuite {

  checkAll("First[Int]", SemigroupKTests[First].semigroupK[Int])

  test("combine"){
    First(1) |+| First(2) shouldEqual First(1)
  }

}
