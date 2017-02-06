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
  implicit def firstArbitrary[A: Arbitrary]: Arbitrary[First[A]] = Arbitrary(arbitrary[Option[A]].map(First(_)))
  implicit def lastArbitrary[A: Arbitrary] : Arbitrary[Last[A]]  = Arbitrary(arbitrary[Option[A]].map(Last(_)))
}