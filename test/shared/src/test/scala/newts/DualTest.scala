package newts

import cats.data.NonEmptyList
import cats.kernel.laws.{GroupLaws, OrderLaws}
import cats.laws.discipline.arbitrary._

class DualTest extends NewtsSuite {

  checkAll("Dual[NonEmptyList[Int]]", GroupLaws[Dual[NonEmptyList[Int]]].semigroup)
  checkAll("Dual[List[Int]]"        , GroupLaws[Dual[List[Int]]].monoid)
  checkAll("Dual[Int]"              , OrderLaws[Dual[Int]].eqv)

  test("combine"){
    val xs = NonEmptyList.of(1,2)
    val ys = NonEmptyList.of(3,4)

    xs.dual |+| ys.dual shouldEqual (ys |+| xs).dual
  }

  test("dual of first is last"){
    val xs = NonEmptyList(1, List(2,3,4,5))

    xs.reduceMap(i => Dual(First(i))).value.value shouldEqual 5
  }

}
