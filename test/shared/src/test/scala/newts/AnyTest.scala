package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}

class AnyTest extends NewtsSuite {

  checkAll("Any", GroupLaws[Any].monoid)
  checkAll("Any", OrderLaws[Any].eqv)

  test("combine"){
    true.asAny   |+| true.asAny shouldEqual true.asAny
    Any(true)    |+| Any(false) shouldEqual Any(true)
    Any(false)   |+| Any(true)  shouldEqual Any(true)
    Any(false)   |+| Any(false) shouldEqual Any(false)
  }

  test("show") {
    Any(true).show  shouldEqual "Any(true)"
    Any(false).show shouldEqual "Any(false)"
  }
}
