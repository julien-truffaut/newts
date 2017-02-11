package newts

import cats.instances.AllInstances
import cats.syntax.AllSyntax
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline

trait NewtsSuite extends FunSuite
  with Matchers
  with GeneratorDrivenPropertyChecks
  with Discipline
  with AllInstances
  with AllSyntax
  with ArbitraryInstances

trait ArbitraryInstances {
  implicit def firstArbitrary[A: Arbitrary]: Arbitrary[FirstOption[A]] = Arbitrary(arbitrary[Option[A]].map(FirstOption(_)))
  implicit def lastArbitrary[A: Arbitrary] : Arbitrary[LastOption[A]]  = Arbitrary(arbitrary[Option[A]].map(LastOption(_)))
  implicit def zipListArbitrary[A: Arbitrary]: Arbitrary[ZipList[A]] = Arbitrary(arbitrary[List[A]].map(ZipList(_)))
}