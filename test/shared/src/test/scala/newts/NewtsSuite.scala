package newts

import cats.instances.AllInstances
import newts.syntax.AllSyntax
import org.scalacheck.{Arbitrary, Cogen}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FunSuite, Matchers}
import org.typelevel.discipline.scalatest.Discipline
import monocle.Iso

trait NewtsSuite extends FunSuite
  with Matchers
  with GeneratorDrivenPropertyChecks
  with Discipline
  with AllSyntax
  with AllInstances
  with cats.syntax.AllSyntax
  with ArbitraryInstances

trait ArbitraryInstances {
  val allIso: Iso[Boolean, All] = Iso(All(_))(_.getAll)
  def multIso[A]: Iso[A, Mult[A]] = Iso[A, Mult[A]](Mult(_))(_.getMult)
  def dualIso[A]: Iso[A, Dual[A]] = Iso[A, Dual[A]](Dual(_))(_.getDual)
  def firstIso[A]: Iso[A, First[A]] = Iso[A, First[A]](First(_))(_.getFirst)
  def lastIso[A]: Iso[A, Last[A]] = Iso[A, Last[A]](Last(_))(_.getLast)
  def firstOptionIso[A]: Iso[Option[A], FirstOption[A]] = Iso[Option[A], FirstOption[A]](FirstOption(_))(_.getFirstOption)
  def lastOptionIso[A]: Iso[Option[A], LastOption[A]] = Iso[Option[A], LastOption[A]](LastOption(_))(_.getLastOption)
  def minIso[A]: Iso[A, Min[A]] = Iso[A, Min[A]](Min(_))(_.getMin)
  def maxIso[A]: Iso[A, Max[A]] = Iso[A, Max[A]](Max(_))(_.getMax)
  def zipListIso[A]: Iso[List[A], ZipList[A]] = Iso[List[A], ZipList[A]](ZipList(_))(_.getZipList)

  def arbFromIso[A: Arbitrary, B](iso: Iso[A, B]): Arbitrary[B] = Arbitrary(arbitrary[A].map(iso.get))
  def cogenFromIso[A: Cogen, B](iso: Iso[A, B]): Cogen[B] = Cogen[A].contramap(iso.reverseGet)

  implicit val allArbitrary: Arbitrary[All] = arbFromIso(allIso)
  implicit def multArbitrary[A:Arbitrary]: Arbitrary[Mult[A]] = arbFromIso(multIso)
  implicit def dualArbitrary[A: Arbitrary]: Arbitrary[Dual[A]] = arbFromIso(dualIso)
  implicit def firstArbitrary[A: Arbitrary]: Arbitrary[First[A]] = arbFromIso(firstIso)
  implicit def lastArbitrary[A: Arbitrary]: Arbitrary[Last[A]] = arbFromIso(lastIso)
  implicit def firstOptionArbitrary[A: Arbitrary]: Arbitrary[FirstOption[A]] = arbFromIso(firstOptionIso)
  implicit def lastOptionArbitrary[A: Arbitrary]: Arbitrary[LastOption[A]]  = arbFromIso(lastOptionIso)
  implicit def minArbitrary[A: Arbitrary]: Arbitrary[Min[A]]  = arbFromIso(minIso)
  implicit def maxArbitrary[A: Arbitrary]: Arbitrary[Max[A]]  = arbFromIso(maxIso)
  implicit def zipListArbitrary[A: Arbitrary]: Arbitrary[ZipList[A]] = arbFromIso(zipListIso)

  implicit val allCogen: Cogen[All] = cogenFromIso(allIso)
  implicit def multCogen[A: Cogen]: Cogen[Mult[A]] = cogenFromIso(multIso)
  implicit def dualCogen[A: Cogen]: Cogen[Dual[A]] = cogenFromIso(dualIso)
  implicit def firstCogen[A: Cogen]: Cogen[First[A]] = cogenFromIso(firstIso)
  implicit def lastCogen[A: Cogen]: Cogen[Last[A]] = cogenFromIso(lastIso)
  implicit def firstOptionCogen[A: Cogen]: Cogen[FirstOption[A]] = cogenFromIso(firstOptionIso)
  implicit def lastOptionCogen[A: Cogen] : Cogen[LastOption[A]]  = cogenFromIso(lastOptionIso)
  implicit def minOptionCogen[A: Cogen] : Cogen[Min[A]]  = cogenFromIso(minIso)
  implicit def maxOptionCogen[A: Cogen] : Cogen[Max[A]]  = cogenFromIso(maxIso)
  implicit def zipListCogen[A: Cogen]: Cogen[ZipList[A]] = cogenFromIso(zipListIso)
}