package newts

import cats.Id
import cats.kernel.laws.discipline.{CommutativeMonoidTests, EqTests}
import cats.laws.discipline.{CommutativeMonadTests, DistributiveTests, TraverseTests}

class MultTest extends NewtsSuite {

  checkAll("Mult[Int]", CommutativeMonoidTests[Mult[Int]].commutativeMonoid)
  checkAll("Mult[Int]", EqTests[Mult[Int]].eqv)
  checkAll("Mult[Int]", CommutativeMonadTests[Mult].commutativeMonad[Int, Int, Int])
  checkAll("Mult[Int]", TraverseTests[Mult].traverse[Int, Int, Int, Int, Option, Option])
  checkAll("Mult[Int]", DistributiveTests[Mult].distributive[Int, Int, Int, Option, Id])

  test("combine") {
    1L.asMult |+| 1L.asMult shouldEqual 1L.asMult
    1.asMult |+| 1.asMult shouldEqual 1.asMult
    0.asMult |+| 1.asMult shouldEqual 0.asMult
  }

}
