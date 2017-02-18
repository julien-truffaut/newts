package newts

import cats.Show
import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.SemigroupKTests
import fixtures.ShowTestClass


class FirstTest extends NewtsSuite {

  checkAll("First[Int]", SemigroupKTests[First].semigroupK[Int])
  checkAll("First[Int]", GroupLaws[First[Int]].semigroup)
  checkAll("First[Int]", OrderLaws[First[Int]].eqv)

  test("combine"){
    1.asFirst |+| 2.asFirst shouldEqual First(1)
  }


  test("show") {
    First("aString").show shouldEqual "First(aString)"
    First(42).show shouldEqual "First(42)"
    First(new ShowTestClass).show shouldEqual s"First(${ShowTestClass.show})"
  }
}
