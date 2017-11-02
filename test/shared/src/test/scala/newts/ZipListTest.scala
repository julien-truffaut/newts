package newts

import cats.Show
import cats.kernel.laws.discipline.EqTests
import cats.laws.discipline.ApplyTests
import cats.syntax.apply._

class ZipListTest extends NewtsSuite {

  checkAll("ZipList[Int]", ApplyTests[ZipList].apply[Int, Int, Int])
  checkAll("ZipList[Int]", EqTests[ZipList[Int]].eqv)

  test("ap"){
    (List[Int => Int](_ + 1, _ * 2, _ + 2).asZipList ap List(1,2,3).asZipList) shouldEqual ZipList(List(2,4,5))
  }

  case class ShowTest(value: String)
  implicit val showTest = new Show[ShowTest]() {
    override def show(f: ShowTest): String = s"test show method ${f.value}"
  }

  test("show") {
    ZipList(List(2,4,5)).show shouldEqual "ZipList(List(2, 4, 5))"
    ZipList(List.empty[String]).show shouldEqual "ZipList(List())"
    ZipList(List(ShowTest("a"), ShowTest("b"))).show shouldEqual "ZipList(List(test show method a, test show method b))"
  }
}
