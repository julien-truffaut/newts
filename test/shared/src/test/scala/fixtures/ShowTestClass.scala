package fixtures

import cats.Show

class ShowTestClass

object ShowTestClass {
  val show = "test show"

  implicit val _: Show[ShowTestClass] = new Show[ShowTestClass] {
    def show(f: ShowTestClass) = ShowTestClass.show
  }
}
