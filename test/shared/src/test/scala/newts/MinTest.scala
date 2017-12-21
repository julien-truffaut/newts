package newts

import cats.kernel.laws.discipline.{CommutativeSemigroupTests, CommutativeMonoidTests, OrderTests}
import cats.laws.discipline.{CommutativeMonadTests, TraverseTests}

class MinTest extends NewtsSuite {

  checkAll("Min[Int]", CommutativeSemigroupTests[Min[Int]].commutativeSemigroup)
  checkAll("Min[Int]", CommutativeMonoidTests[Min[Int]].commutativeMonoid)
  checkAll("Min[Int]", OrderTests[Min[Int]].order)
  checkAll("Min[Int]", CommutativeMonadTests[Min].commutativeMonad[Int, Int, Int])
  checkAll("Min[Int]", TraverseTests[Min].traverse[Int, Int, Int, Int, Option, Option])

  test("combine"){
    5.asMin |+| 1.asMin shouldEqual Min(1)
    1.asMin |+| 5.asMin shouldEqual Min(1)
  }

  test("show") {
    Min(5).show shouldEqual "Min(5)"
    Min("1").show shouldEqual "Min(1)"
  }
}
