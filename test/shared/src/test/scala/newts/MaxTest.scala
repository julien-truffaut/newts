package newts

import cats.Id
import cats.kernel.laws.discipline.{CommutativeMonoidTests, CommutativeSemigroupTests, OrderTests}
import cats.laws.discipline.{CommutativeMonadTests, DistributiveTests, TraverseTests}

class MaxTest extends NewtsSuite {

  checkAll("Max[Int]", CommutativeSemigroupTests[Max[Int]].commutativeSemigroup)
  checkAll("Max[Int]", CommutativeMonoidTests[Max[Int]].commutativeMonoid)
  checkAll("Max[Int]", OrderTests[Max[Int]].order)
  checkAll("Max[Int]", CommutativeMonadTests[Max].commutativeMonad[Int, Int, Int])
  checkAll("Max[Int]", TraverseTests[Max].traverse[Int, Int, Int, Int, Option, Option])
  checkAll("Max[Int]", DistributiveTests[Max].distributive[Int, Int, Int, Option, Id])

  test("combine"){
    5.asMax |+| 1.asMax shouldEqual Max(5)
    1.asMax |+| 5.asMax shouldEqual Max(5)
  }

  test("show") {
    Max(5).show   shouldEqual "Max(5)"
    Max("1").show shouldEqual "Max(1)"
  }
}
