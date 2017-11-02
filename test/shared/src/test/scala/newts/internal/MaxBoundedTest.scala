package newts.internal

import newts.NewtsSuite
import newts.internal.laws.discipline.MaxBoundedTests

class MaxBoundedTest extends NewtsSuite {

  checkAll("Short", MaxBoundedTests[Short].maxBounded)
  checkAll("Int", MaxBoundedTests[Int].maxBounded)
  checkAll("Long", MaxBoundedTests[Long].maxBounded)

}
