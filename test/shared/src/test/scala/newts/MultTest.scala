package newts

import cats.kernel.laws.discipline.{CommutativeMonoidTests, EqTests}
import cats.laws.discipline.{MonadTests, TraverseTests}

class MultTest extends NewtsSuite {

  checkAll("Mult[Int]", CommutativeMonoidTests[Mult[Int]].commutativeMonoid)
  checkAll("Mult[Int]", EqTests[Mult[Int]].eqv)
  checkAll("Mult[Int]", MonadTests[Mult].monad[Int, Int, Int])
  checkAll("Mult[Int]", TraverseTests[Mult].traverse[Int, Int, Int, Int, Option, Option])

  test("combine") {
    1L.asMult |+| 1L.asMult shouldEqual 1L.asMult
    1.asMult |+| 1.asMult shouldEqual 1.asMult
    0.asMult |+| 1.asMult shouldEqual 0.asMult
  }

}
