package newts.internal

import newts.NewtsSuite

class MinBoundedTest extends NewtsSuite {

  checkAll("Short", BoundedTests[Short].minBounded)
  checkAll("Int", BoundedTests[Int].minBounded)
  checkAll("Long", BoundedTests[Long].minBounded)
  checkAll("String", BoundedTests[String].minBounded)
  checkAll("Option[Int]", BoundedTests[Option[Int]].minBounded)

}
