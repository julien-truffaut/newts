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
  val allIso: Iso[Boolean, All] = Iso(All(_))(_.value)
  def dualIso[A]: Iso[A, Dual[A]] = Iso[A, Dual[A]](Dual(_))(_.value)
  def firstIso[A]: Iso[A, First[A]] = Iso[A, First[A]](First(_))(_.value)
  def firstOptionIso[A]: Iso[Option[A], FirstOption[A]] = Iso[Option[A], FirstOption[A]](FirstOption(_))(_.value)
  def lastOptionIso[A]: Iso[Option[A], LastOption[A]] = Iso[Option[A], LastOption[A]](LastOption(_))(_.value)
  def minIso[A]: Iso[A, Min[A]] = Iso[A, Min[A]](Min(_))(_.value)
  def zipListIso[A]: Iso[List[A], ZipList[A]] = Iso[List[A], ZipList[A]](ZipList(_))(_.value)

  def arbFromIso[A: Arbitrary, B](iso: Iso[A, B]): Arbitrary[B] = Arbitrary(arbitrary[A].map(iso.get))
  def cogenFromIso[A: Cogen, B](iso: Iso[A, B]): Cogen[B] = Cogen[A].contramap(iso.reverseGet)

  implicit val allArbitrary: Arbitrary[All] = arbFromIso(allIso)
  implicit def dualArbitrary[A: Arbitrary]: Arbitrary[Dual[A]] = arbFromIso(dualIso)
  implicit def firstArbitrary[A: Arbitrary]: Arbitrary[First[A]] = arbFromIso(firstIso)
  implicit def firstOptionArbitrary[A: Arbitrary]: Arbitrary[FirstOption[A]] = arbFromIso(firstOptionIso)
  implicit def lastOptionArbitrary[A: Arbitrary]: Arbitrary[LastOption[A]]  = arbFromIso(lastOptionIso)
  implicit def minArbitrary[A: Arbitrary]: Arbitrary[Min[A]]  = arbFromIso(minIso)
  implicit def zipListArbitrary[A: Arbitrary]: Arbitrary[ZipList[A]] = arbFromIso(zipListIso)

  implicit val allCogen: Cogen[All] = cogenFromIso(allIso)
  implicit def dualCogen[A: Cogen]: Cogen[Dual[A]] = cogenFromIso(dualIso)
  implicit def firstCogen[A: Cogen]: Cogen[First[A]] = cogenFromIso(firstIso)
  implicit def firstOptionCogen[A: Cogen]: Cogen[FirstOption[A]] = cogenFromIso(firstOptionIso)
  implicit def lastOptionCogen[A: Cogen] : Cogen[LastOption[A]]  = cogenFromIso(lastOptionIso)
  implicit def minOptionCogen[A: Cogen] : Cogen[Min[A]]  = cogenFromIso(minIso)
  implicit def zipListCogen[A: Cogen]: Cogen[ZipList[A]] = cogenFromIso(zipListIso)
}