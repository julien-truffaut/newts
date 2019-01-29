package newts

import cats.instances.AllInstances
import newts.syntax.AllSyntax
import org.scalacheck.{Arbitrary, Cogen}
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
  def arbNewtype[S, A: Arbitrary](implicit newtype: Newtype.Aux[S, A]): Arbitrary[S] =
    Arbitrary(arbitrary[A].map(newtype.wrap))

  def cogenNewtype[S, A: Cogen](implicit newtype: Newtype.Aux[S, A]): Cogen[S] =
    Cogen[A].contramap(newtype.unwrap)

  implicit val allArbitrary: Arbitrary[All] = arbNewtype[All, Boolean]
  implicit val anyArbitrary: Arbitrary[Any] = arbNewtype[Any, Boolean]
  implicit def multArbitrary[A:Arbitrary]: Arbitrary[Mult[A]] = arbNewtype[Mult[A], A]
  implicit def dualArbitrary[A: Arbitrary]: Arbitrary[Dual[A]] = arbNewtype[Dual[A], A]
  implicit def firstArbitrary[A: Arbitrary]: Arbitrary[First[A]] = arbNewtype[First[A], A]
  implicit def lastArbitrary[A: Arbitrary]: Arbitrary[Last[A]] = arbNewtype[Last[A], A]
  implicit def firstOptionArbitrary[A: Arbitrary]: Arbitrary[FirstOption[A]] = arbNewtype[FirstOption[A], Option[A]]
  implicit def lastOptionArbitrary[A: Arbitrary]: Arbitrary[LastOption[A]]  = arbNewtype[LastOption[A], Option[A]]
  implicit def minArbitrary[A: Arbitrary]: Arbitrary[Min[A]]  = arbNewtype[Min[A], A]
  implicit def maxArbitrary[A: Arbitrary]: Arbitrary[Max[A]]  = arbNewtype[Max[A], A]
  implicit def zipListArbitrary[A: Arbitrary]: Arbitrary[ZipList[A]] = arbNewtype[ZipList[A], List[A]]
  implicit def backwardsArbitrary[F[_], A](implicit ev: Arbitrary[F[A]]): Arbitrary[Backwards[F, A]] = arbNewtype[Backwards[F, A], F[A]]
  implicit def reverseArbitrary[F[_], A](implicit ev: Arbitrary[F[A]]): Arbitrary[Reverse[F, A]] = arbNewtype[Reverse[F, A], F[A]]

  implicit val allCogen: Cogen[All] = cogenNewtype[All, Boolean]
  implicit val anyCogen: Cogen[Any] = cogenNewtype[Any, Boolean]
  implicit def multCogen[A: Cogen]: Cogen[Mult[A]] = cogenNewtype[Mult[A], A]
  implicit def dualCogen[A: Cogen]: Cogen[Dual[A]] = cogenNewtype[Dual[A], A]
  implicit def firstCogen[A: Cogen]: Cogen[First[A]] = cogenNewtype[First[A], A]
  implicit def lastCogen[A: Cogen]: Cogen[Last[A]] = cogenNewtype[Last[A], A]
  implicit def firstOptionCogen[A: Cogen]: Cogen[FirstOption[A]] = cogenNewtype[FirstOption[A], Option[A]]
  implicit def lastOptionCogen[A: Cogen] : Cogen[LastOption[A]]  = cogenNewtype[LastOption[A], Option[A]]
  implicit def minOptionCogen[A: Cogen] : Cogen[Min[A]]  = cogenNewtype[Min[A], A]
  implicit def maxOptionCogen[A: Cogen] : Cogen[Max[A]]  = cogenNewtype[Max[A], A]
  implicit def zipListCogen[A: Cogen]: Cogen[ZipList[A]] = cogenNewtype[ZipList[A], List[A]]
  implicit def backwardsCogen[F[_], A](implicit ev: Cogen[F[A]]): Cogen[Backwards[F, A]] = cogenNewtype[Backwards[F, A], F[A]]
  implicit def reverseCogen[F[_], A](implicit ev: Cogen[F[A]]): Cogen[Reverse[F, A]] = cogenNewtype[Reverse[F, A], F[A]]
}