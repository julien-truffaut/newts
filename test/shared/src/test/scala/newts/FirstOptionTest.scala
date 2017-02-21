package newts

import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.{MonadCombineTests, TraverseTests}
import fixtures.ShowTestClass

class FirstOptionTest extends NewtsSuite {

  checkAll("FirstOption[Int]", MonadCombineTests[FirstOption].monoidK[Int])
  checkAll("FirstOption[Int]", GroupLaws[FirstOption[Int]].monoid)
  checkAll("FirstOption[Int]", OrderLaws[FirstOption[Int]].eqv)
  checkAll("FirstOption[Int]", TraverseTests[FirstOption].traverse[Int, Int, Int, Int, Option, Option])

  test("combine"){
    1.some.asFirstOption    |+| 2.some.asFirstOption shouldEqual FirstOption(Some(1))
    none[Int].asFirstOption |+| 2.some.asFirstOption shouldEqual FirstOption(Some(2))
  }

  test("show") {
    FirstOption(Some("aString")).show shouldEqual "FirstOption(Some(aString))"
    FirstOption(Some(42)).show shouldEqual "FirstOption(Some(42))"
    FirstOption(Some(new ShowTestClass)).show shouldEqual s"FirstOption(Some(${ShowTestClass.show}))"
    FirstOption[Int](None).show shouldEqual "FirstOption(None)"
  }
}
