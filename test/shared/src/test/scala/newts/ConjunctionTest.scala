package newts

class ConjunctionTest extends NewtsSuite {

  test("combine"){
    Conjunction(true)  |+| Conjunction(true)  shouldEqual Conjunction(true)
    Conjunction(true)  |+| Conjunction(false) shouldEqual Conjunction(false)
    Conjunction(false) |+| Conjunction(true)  shouldEqual Conjunction(false)
    Conjunction(false) |+| Conjunction(false) shouldEqual Conjunction(false)
  }

}
