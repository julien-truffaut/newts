package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}

class AllTest extends NewtsSuite {

  checkAll("All", GroupLaws[All].monoid)
  checkAll("All", OrderLaws[All].eqv)

  test("combine"){
    true.all   |+| true.all   shouldEqual true.all
    All(true)  |+| All(false) shouldEqual All(false)
    All(false) |+| All(true)  shouldEqual All(false)
    All(false) |+| All(false) shouldEqual All(false)
  }

}
