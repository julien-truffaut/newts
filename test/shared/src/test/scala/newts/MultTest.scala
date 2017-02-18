package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}

class MultTest extends NewtsSuite {

  checkAll("Mult[Int]", GroupLaws[Mult[Int]].monoid)
  checkAll("Mult[Int]", OrderLaws[Mult[Int]].eqv)
  checkAll("Mult[Long]", GroupLaws[Mult[Long]].monoid)
  checkAll("Mult[Long]", OrderLaws[Mult[Long]].eqv)

  test("combine") {
    1L.asMult |+| 1L.asMult shouldEqual 1L.asMult
    1.asMult |+| 1.asMult shouldEqual 1.asMult
    0.asMult |+| 1.asMult shouldEqual 0.asMult
  }

}
