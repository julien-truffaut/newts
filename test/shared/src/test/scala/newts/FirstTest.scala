package newts

import cats.Id
import cats.kernel.laws.discipline.{EqTests, SemigroupTests}
import cats.laws.discipline.{DistributiveTests, MonadTests, SemigroupKTests, TraverseTests}
import fixtures.ShowTestClass

class FirstTest extends NewtsSuite {

  checkAll("First[Int]", SemigroupKTests[First].semigroupK[Int])
  checkAll("First[Int]", SemigroupTests[First[Int]].semigroup)
  checkAll("First[Int]", EqTests[First[Int]].eqv)
  checkAll("First[Int]", MonadTests[First].monad[Int, Int, Int])
  checkAll("First[Int]", TraverseTests[First].traverse[Int, Int, Int, Int, Option, Option])
  checkAll("First[Int]", DistributiveTests[First].distributive[Int, Int, Int, Option, Id])

  test("combine"){
    1.asFirst |+| 2.asFirst shouldEqual First(1)
  }


  test("show") {
    First("aString").show shouldEqual "First(aString)"
    First(42).show shouldEqual "First(42)"
    First(new ShowTestClass).show shouldEqual s"First(${ShowTestClass.show})"
  }
}
