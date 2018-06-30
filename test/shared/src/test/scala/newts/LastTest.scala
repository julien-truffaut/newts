package newts

import cats.Id
import cats.kernel.laws.discipline.{EqTests, SemigroupTests}
import cats.laws.discipline.{DistributiveTests, MonadTests, SemigroupKTests, TraverseTests}
import fixtures.ShowTestClass

class LastTest extends NewtsSuite {

  checkAll("Last[Int]", SemigroupKTests[Last].semigroupK[Int])
  checkAll("Last[Int]", SemigroupTests[Last[Int]].semigroup)
  checkAll("Last[Int]", EqTests[Last[Int]].eqv)
  checkAll("Last[Int]", MonadTests[Last].monad[Int, Int, Int])
  checkAll("Last[Int]", TraverseTests[Last].traverse[Int, Int, Int, Int, Option, Option])
  checkAll("Last[Int]", DistributiveTests[Last].distributive[Int, Int, Int, Option, Id])

  test("combine"){
    1.asLast |+| 2.asLast shouldEqual Last(2)
  }


  test("show") {
    Last("aString").show shouldEqual "Last(aString)"
    Last(42).show shouldEqual "Last(42)"
    Last(new ShowTestClass).show shouldEqual s"Last(${ShowTestClass.show})"
  }
}
