package newts

import cats.Id
import cats.kernel.laws.discipline.{CommutativeMonoidTests, CommutativeSemigroupTests, OrderTests}
import cats.laws.discipline.{CommutativeMonadTests, DistributiveTests, TraverseTests}

class MinTest extends NewtsSuite {

  checkAll("Min[Int]", CommutativeSemigroupTests[Min[Int]].commutativeSemigroup)
  checkAll("Min[Int]", CommutativeMonoidTests[Min[Int]].commutativeMonoid)
  checkAll("Min[Int]", OrderTests[Min[Int]].order)
  checkAll("Min[Int]", CommutativeMonadTests[Min].commutativeMonad[Int, Int, Int])
  checkAll("Min[Int]", TraverseTests[Min].traverse[Int, Int, Int, Int, Option, Option])
  checkAll("Min[Int]", DistributiveTests[Min].distributive[Int, Int, Int, Option, Id])

  test("combine"){
    5.asMin |+| 1.asMin shouldEqual Min(1)
    1.asMin |+| 5.asMin shouldEqual Min(1)
  }

  test("show") {
    Min(5).show shouldEqual "Min(5)"
    Min("1").show shouldEqual "Min(1)"
  }
}
