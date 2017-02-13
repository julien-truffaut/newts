package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.MonoidKTests

class LastOptionTest extends NewtsSuite {

  checkAll("LastOption[Int]", MonoidKTests[LastOption].monoidK[Int])
  checkAll("LastOption[Int]", GroupLaws[LastOption[Int]].monoid)
  checkAll("LastOption[Int]", OrderLaws[LastOption[Int]].eqv)

  test("combine"){
    1.some.asLastOption |+| 2.some.asLastOption shouldEqual LastOption(Some(2))
    1.some.asLastOption |+| none.asLastOption   shouldEqual LastOption(Some(1))
  }

}
