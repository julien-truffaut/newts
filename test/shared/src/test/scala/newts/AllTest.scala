package newts

class AllTest extends NewtsSuite {

  test("combine"){
    All(true)  |+| All(true)  shouldEqual All(true)
    All(true)  |+| All(false) shouldEqual All(false)
    All(false) |+| All(true)  shouldEqual All(false)
    All(false) |+| All(false) shouldEqual All(false)
  }

}
