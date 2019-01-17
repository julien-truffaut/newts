package newts

import cats._
import cats.data.{NonEmptyList, Writer}
import cats.implicits._
import cats.kernel.laws.discipline.{EqTests, OrderTests}
import cats.laws.discipline.arbitrary._
import cats.laws.discipline.{AlternativeTests, BimonadTests, CommutativeMonadTests, DistributiveTests, FunctorFilterTests, MonadErrorTests, NonEmptyTraverseTests}
import fixtures.ShowTestClass

class ReverseTest extends NewtsSuite {

  checkAll("Reverse[Option, Int]", EqTests[Reverse[Option, Int]].eqv)
  checkAll("Reverse[Option, Int]", OrderTests[Reverse[Option, Int]].order)
  checkAll("Reverse[Option, Int]", AlternativeTests[Reverse[Option, ?]].alternative[Int, Int, Int])
  checkAll("Reverse[Option, Int]", CommutativeMonadTests[Reverse[Option, ?]].commutativeMonad[Int, Int, Int])
  checkAll("Reverse[Last, Int]]", DistributiveTests[Reverse[Last, ?]].distributive[Int, Int, Int, Option, Function0])
  checkAll("Reverse[List, Int]", FunctorFilterTests[Reverse[List, ?]].functorFilter[Int, Int, Int])
  checkAll("Reverse[NonEmptyList, Int]", BimonadTests[Reverse[NonEmptyList, ?]].bimonad[Int, Int, Int])
  checkAll("Reverse[NonEmptyList, Int]", NonEmptyTraverseTests[Reverse[NonEmptyList, ?]].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option])
  checkAll("Reverse[Either[String, ?], Int]", MonadErrorTests[Reverse[Either[String, ?], ?], String].monadError[Int, Int, Int])

  type RevList[A] = Reverse[List, A]

  test("is folded in reverse order") {
    Reverse(List("a", "b", "c")).foldLeft("")(_ ++ _) shouldBe "cba"
    Reverse(List("a", "b", "c")).foldRight(Eval.now(""))((x, y) => Eval.now(x ++ y.value)).value shouldBe "cba"
  }

  test("is traversed in reverse order") {
    def write(n: Int): Writer[List[Int], Int] = Writer.tell(List(n)) *> Writer.value(n)

    val result: Writer[List[Int], List[Int]] = Reverse(List(1,2,3)).traverse(write).map(_.getReverse)

    result.value shouldEqual List(1,2,3)
    result.written shouldEqual List(3,2,1)
  }

  test("show") {
    Reverse(Option("aString")).show shouldEqual "Reverse(Some(aString))"
    Reverse(List(42, 24)).show shouldEqual "Reverse(List(42, 24))"
    Reverse(Writer("log", new ShowTestClass)).show shouldEqual s"Reverse((log,${ShowTestClass.show}))"
  }
}
