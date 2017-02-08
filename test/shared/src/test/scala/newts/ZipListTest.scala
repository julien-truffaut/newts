package newts

import cats.laws.discipline.ApplyTests
import cats.syntax.apply._

class ZipListTest extends NewtsSuite {

  checkAll("ZipList[Int]", ApplyTests[ZipList].apply[Int, Int, Int])

  test("ap"){
    (ZipList(List[Int => Int](_ + 1, _ * 2, _ + 2)) ap ZipList(List(1,2,3))) shouldEqual ZipList(List(2,4,5))
  }

}
