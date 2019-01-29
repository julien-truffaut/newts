package newts

import cats._
import cats.data.{NonEmptyList, Writer}
import cats.implicits._
import cats.kernel.laws.discipline.{EqTests, OrderTests}
import cats.laws.discipline.{AlternativeTests, ApplicativeErrorTests, CommutativeApplicativeTests, DistributiveTests, FunctorFilterTests, NonEmptyTraverseTests}
import cats.laws.discipline.arbitrary._
import fixtures.ShowTestClass

class BackwardsTest extends NewtsSuite {

  checkAll("Backwards[Option, Int]", EqTests[Backwards[Option, Int]].eqv)
  checkAll("Backwards[Option, Int]", OrderTests[Backwards[Option, Int]].order)
  checkAll("Backwards[Option, Int]", AlternativeTests[Backwards[Option, ?]].alternative[Int, Int, Int])
  checkAll("Backwards[Option, Int]", CommutativeApplicativeTests[Backwards[Option, ?]].commutativeApplicative[Int, Int, Int])
  checkAll("Backwards[Last, Int]]", DistributiveTests[Backwards[Last, ?]].distributive[Int, Int, Int, Option, Function0])
  checkAll("Backwards[List, Int]", FunctorFilterTests[Backwards[List, ?]].functorFilter[Int, Int, Int])
  checkAll("Backwards[NonEmptyList, Int]", NonEmptyTraverseTests[Backwards[NonEmptyList, ?]].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option])
  checkAll("Backwards[Either[String, ?], Int]", ApplicativeErrorTests[Backwards[Either[String, ?], ?], String].applicativeError[Int, Int, Int])

  test("applies actions in reverse order") {
    val f1 = Writer.tell(List(1)) *> Writer.value(1)
    val f2 = Writer.tell(List(2)) *> Writer.value(2)
    val f3 = Writer.tell(List(3)) *> Writer.value(3)

    val composed = (Backwards(f1), Backwards(f2), Backwards(f3)).mapN(Tuple3.apply).forwards

    composed.value shouldEqual ((1, 2, 3))
    composed.written shouldEqual List(3, 2, 1)
  }

  test("show") {
    Backwards(Option("aString")).show shouldEqual "Backwards(Some(aString))"
    Backwards(List(42, 24)).show shouldEqual "Backwards(List(42, 24))"
    Backwards(Writer("log", new ShowTestClass)).show shouldEqual s"Backwards((log,${ShowTestClass.show}))"
  }
}
