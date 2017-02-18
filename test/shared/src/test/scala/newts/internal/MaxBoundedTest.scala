package newts.internal

import newts.NewtsSuite

class MaxBoundedTest extends NewtsSuite {

  checkAll("Short", BoundedTests[Int].minBounded)
  checkAll("Int", BoundedTests[Int].minBounded)
  checkAll("Long", BoundedTests[Int].minBounded)

}
