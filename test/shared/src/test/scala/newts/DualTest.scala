package newts

import cats.data.NonEmptyList

class DualTest extends NewtsSuite {

  test("combine"){
    val xs = NonEmptyList.of(1,2)
    val ys = NonEmptyList.of(3,4)

    xs.dual |+| ys.dual shouldEqual (ys |+| xs).dual
  }

}
