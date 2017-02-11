package newts

class MinTest extends NewtsSuite {

  test("combine"){
    Min(5) |+| Min(1) shouldEqual Min(1)
    Min(1) |+| Min(5) shouldEqual Min(1)
  }

}
