package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}

class MinTest extends NewtsSuite {

  checkAll("Min[Int]", GroupLaws[Min[Int]].semigroup)
  checkAll("Min[Int]", OrderLaws[Min[Int]].order)

  test("combine"){
    Min(5) |+| Min(1) shouldEqual Min(1)
    Min(1) |+| Min(5) shouldEqual Min(1)
  }

}
