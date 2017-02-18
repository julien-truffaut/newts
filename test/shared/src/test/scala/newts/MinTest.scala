package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}

class MinTest extends NewtsSuite {

  checkAll("Min[Int]", GroupLaws[Min[Int]].semigroup)
  checkAll("Min[Int]", GroupLaws[Min[Int]].monoid)
  checkAll("Min[Int]", OrderLaws[Min[Int]].order)

  test("combine"){
    5.asMin |+| 1.asMin shouldEqual Min(1)
    1.asMin |+| 5.asMin shouldEqual Min(1)
  }

}
