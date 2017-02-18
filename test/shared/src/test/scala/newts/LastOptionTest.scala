package newts

import cats.Show
import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.MonadCombineTests

class LastOptionTest extends NewtsSuite {

  checkAll("LastOption[Int]", MonadCombineTests[LastOption].monoidK[Int])
  checkAll("LastOption[Int]", GroupLaws[LastOption[Int]].monoid)
  checkAll("LastOption[Int]", OrderLaws[LastOption[Int]].eqv)

  test("combine"){
    1.some.asLastOption |+| 2.some.asLastOption shouldEqual LastOption(Some(2))
    1.some.asLastOption |+| none.asLastOption   shouldEqual LastOption(Some(1))
  }

  class ShowTest
  implicit val showTest = new Show[ShowTest]() {
    override def show(f: ShowTest): String = "test show method"
  }

  test("show") {
    LastOption(Some(1)).show shouldEqual "LastOption(Some(1))"
    LastOption(Some("Hello")).show shouldEqual "LastOption(Some(Hello))"
    LastOption(Option.empty[String]).show shouldEqual "LastOption(None)"
    new ShowTest().some.asLastOption.show shouldEqual "LastOption(Some(test show method))"
  }
}
