package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.SemigroupKTests

class FirstTest extends NewtsSuite {

  checkAll("First[Int]", SemigroupKTests[First].semigroupK[Int])
  checkAll("First[Int]", GroupLaws[First[Int]].semigroup)
  checkAll("First[Int]", OrderLaws[First[Int]].eqv)

  test("combine"){
    1.first |+| 2.first shouldEqual First(1)
  }

}
