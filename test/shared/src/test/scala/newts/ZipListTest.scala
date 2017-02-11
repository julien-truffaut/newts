package newts

import cats.kernel.laws.OrderLaws
import cats.laws.discipline.ApplyTests
import cats.syntax.apply._

class ZipListTest extends NewtsSuite {

  checkAll("ZipList[Int]", ApplyTests[ZipList].apply[Int, Int, Int])
  checkAll("ZipList[Int]", OrderLaws[ZipList[Int]].eqv)

  test("ap"){
    (List[Int => Int](_ + 1, _ * 2, _ + 2).zipList ap List(1,2,3).zipList) shouldEqual ZipList(List(2,4,5))
  }

}
