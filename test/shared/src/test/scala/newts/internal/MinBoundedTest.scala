package newts.internal

import newts.NewtsSuite
import newts.internal.laws.discipline.MinBoundedTests

class MinBoundedTest extends NewtsSuite {

  checkAll("Short", MinBoundedTests[Short].minBounded)
  checkAll("Int", MinBoundedTests[Int].minBounded)
  checkAll("Long", MinBoundedTests[Long].minBounded)
  checkAll("String", MinBoundedTests[String].minBounded)
  checkAll("Option[Int]", MinBoundedTests[Option[Int]].minBounded)

}
