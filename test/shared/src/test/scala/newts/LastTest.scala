package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.{MonadTests, SemigroupKTests, TraverseTests}
import fixtures.ShowTestClass

class LastTest extends NewtsSuite {

  checkAll("Last[Int]", SemigroupKTests[Last].semigroupK[Int])
  checkAll("Last[Int]", GroupLaws[Last[Int]].semigroup)
  checkAll("Last[Int]", OrderLaws[Last[Int]].eqv)
  checkAll("Last[Int]", MonadTests[Last].monad[Int, Int, Int])
  checkAll("Last[Int]", TraverseTests[Last].traverse[Int, Int, Int, Int, Option, Option])

  test("combine"){
    1.asLast |+| 2.asLast shouldEqual Last(2)
  }


  test("show") {
    Last("aString").show shouldEqual "Last(aString)"
    Last(42).show shouldEqual "Last(42)"
    Last(new ShowTestClass).show shouldEqual s"Last(${ShowTestClass.show})"
  }
}
