package newts

import cats.Show
import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.MonadCombineTests

class FirstOptionTest extends NewtsSuite {

  checkAll("FirstOption[Int]", MonadCombineTests[FirstOption].monoidK[Int])
  checkAll("FirstOption[Int]", GroupLaws[FirstOption[Int]].monoid)
  checkAll("FirstOption[Int]", OrderLaws[FirstOption[Int]].eqv)

  test("combine"){
    1.some.asFirstOption    |+| 2.some.asFirstOption shouldEqual FirstOption(Some(1))
    none[Int].asFirstOption |+| 2.some.asFirstOption shouldEqual FirstOption(Some(2))
  }

  test("show") {
    class ShowTestClass
    implicit val _: Show[ShowTestClass] = new Show[ShowTestClass] {
      def show(f: ShowTestClass) = "test show"
    }

    FirstOption(Some("aString")).show shouldEqual "FirstOption(Some(aString))"
    FirstOption(Some(42)).show shouldEqual "FirstOption(Some(42))"
    FirstOption(Some(new ShowTestClass())).show shouldEqual "FirstOption(Some(test show))"
    FirstOption[Int](None).show shouldEqual "FirstOption(None)"
  }
}
