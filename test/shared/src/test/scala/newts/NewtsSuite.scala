package newts

import cats.instances.AllInstances
import newts.syntax.AllSyntax
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline

trait NewtsSuite extends FunSuite
  with Matchers
  with GeneratorDrivenPropertyChecks
  with Discipline
  with AllSyntax
  with AllInstances
  with cats.syntax.AllSyntax
  with ArbitraryInstances

trait ArbitraryInstances {
  implicit def firstArbitrary[A: Arbitrary]: Arbitrary[First[A]] = Arbitrary(arbitrary[A].map(First(_)))
  implicit def firstOptionArbitrary[A: Arbitrary]: Arbitrary[FirstOption[A]] = Arbitrary(arbitrary[Option[A]].map(FirstOption(_)))
  implicit def lastOptionArbitrary[A: Arbitrary] : Arbitrary[LastOption[A]]  = Arbitrary(arbitrary[Option[A]].map(LastOption(_)))
  implicit def zipListArbitrary[A: Arbitrary]: Arbitrary[ZipList[A]] = Arbitrary(arbitrary[List[A]].map(ZipList(_)))
}