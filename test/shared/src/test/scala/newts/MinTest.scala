package newts

import cats.kernel.laws.discipline.{SemigroupTests, MonoidTests, OrderTests}
import cats.laws.discipline.{MonadTests, TraverseTests}

class MinTest extends NewtsSuite {

  checkAll("Min[Int]", SemigroupTests[Min[Int]].semigroup)
  checkAll("Min[Int]", MonoidTests[Min[Int]].monoid)
  checkAll("Min[Int]", OrderTests[Min[Int]].order)
  checkAll("Min[Int]", MonadTests[Dual].monad[Int, Int, Int])
  checkAll("Min[Int]", TraverseTests[Min].traverse[Int, Int, Int, Int, Option, Option])

  test("combine"){
    5.asMin |+| 1.asMin shouldEqual Min(1)
    1.asMin |+| 5.asMin shouldEqual Min(1)
  }

  test("show") {
    Min(5).show shouldEqual "Min(5)"
    Min("1").show shouldEqual "Min(1)"
  }
}
