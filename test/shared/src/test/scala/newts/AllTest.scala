package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}

class AllTest extends NewtsSuite {

  checkAll("All", GroupLaws[All].monoid)
  checkAll("All", OrderLaws[All].eqv)

  test("combine"){
    true.asAll   |+| true.asAll   shouldEqual true.asAll
    All(true)    |+| All(false) shouldEqual All(false)
    All(false)   |+| All(true)  shouldEqual All(false)
    All(false)   |+| All(false) shouldEqual All(false)
  }

  test("show") {
    All(true).show shouldEqual "All(true)"
    All(false).show shouldEqual "All(false)"
  }
}
