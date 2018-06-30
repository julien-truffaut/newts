package newts

import cats.Id
import cats.data.NonEmptyList
import cats.kernel.laws.discipline.{EqTests, MonoidTests, SemigroupTests}
import cats.laws.discipline.{DistributiveTests, MonadTests, TraverseTests}
import cats.laws.discipline.arbitrary._
import fixtures.ShowTestClass

class DualTest extends NewtsSuite {

  checkAll("Dual[NonEmptyList[Int]]", SemigroupTests[Dual[NonEmptyList[Int]]].semigroup)
  checkAll("Dual[List[Int]]"        , MonoidTests[Dual[List[Int]]].monoid)
  checkAll("Dual[Int]"              , EqTests[Dual[Int]].eqv)
  checkAll("Dual[Int]"              , MonadTests[Dual].monad[Int, Int, Int])
  checkAll("Dual[Int]"              , TraverseTests[Dual].traverse[Int, Int, Int, Int, Option, Option])
  checkAll("Dual[Int]"              , DistributiveTests[Dual].distributive[Int, Int, Int, Option, Id])

  test("combine"){
    val xs = NonEmptyList.of(1,2)
    val ys = NonEmptyList.of(3,4)

    xs.asDual |+| ys.asDual shouldEqual (ys |+| xs).asDual
  }

  test("show") {
    Dual("aString").show shouldEqual "Dual(aString)"
    Dual(42).show shouldEqual "Dual(42)"
    Dual(new ShowTestClass).show shouldEqual s"Dual(${ShowTestClass.show})"
  }

  test("dual of first is last"){
    val xs = NonEmptyList(1, List(2,3,4,5))

    xs.reduceMap(_.asFirst.asDual).getDual.getFirst shouldEqual 5
  }
}
