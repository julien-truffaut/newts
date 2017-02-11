package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}

class AllTest extends NewtsSuite {

  checkAll("All", GroupLaws[All].monoid)
  checkAll("All", OrderLaws[All].eqv)

  test("combine"){
    All(true)  |+| All(true)  shouldEqual All(true)
    All(true)  |+| All(false) shouldEqual All(false)
    All(false) |+| All(true)  shouldEqual All(false)
    All(false) |+| All(false) shouldEqual All(false)
  }

}
