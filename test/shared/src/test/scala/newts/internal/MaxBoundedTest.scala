package newts.internal

import newts.NewtsSuite

class MaxBoundedTest extends NewtsSuite {

  checkAll("Short", BoundedTests[Short].maxBounded)
  checkAll("Int", BoundedTests[Int].maxBounded)
  checkAll("Long", BoundedTests[Long].maxBounded)

}
