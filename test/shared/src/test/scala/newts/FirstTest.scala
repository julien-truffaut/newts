package newts

import cats.laws.discipline.SemigroupKTests

class FirstTest extends NewtsSuite {

  checkAll("First[Int]", SemigroupKTests[First].semigroupK[Int])

  test("combine"){
    1.first |+| 2.first shouldEqual First(1)
  }

}
