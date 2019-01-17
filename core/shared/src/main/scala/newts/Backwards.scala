package newts

import cats._
import cats.syntax.functor._
import cats.syntax.apply._
import cats.kernel.CommutativeMonoid

/**
  * The same functor, but with an [[Applicative]] instance that performs actions in the reverse order.
  *
  * Based on Haskell's [[http://hackage.haskell.org/package/transformers-0.5.5.0/docs/Control-Applicative-Backwards.html Control.Applicative.Backwards]]
  */
final case class Backwards[F[_], A](forwards: F[A])

object Backwards extends BackwardsInstances0

trait BackwardsInstances0 extends BackwardsInstances1 {

  implicit def newtypeInstance[F[_], A]: Newtype.Aux[Backwards[F, A], F[A]] =
    Newtype.from[Backwards[F, A], F[A]](Backwards.apply)(_.forwards)

  implicit def showInstance[F[_], A](implicit ev: Show[F[A]]): Show[Backwards[F, A]] =
    Show.show(bw => s"Backwards(${ev.show(bw.forwards)})")


  implicit def orderInstance[F[_], A](implicit ev: Order[F[A]]): Order[Backwards[F, A]] =
    Order.by(_.forwards)

  implicit def nonEmptyTraverseInstance[F[_]: NonEmptyTraverse]: NonEmptyTraverse[Backwards[F, ?]] =
    new NonEmptyTraverse[Backwards[F, ?]] with BackwardsTraverse[F] with BackwardsReducible[F] {
      val F: NonEmptyTraverse[F] = NonEmptyTraverse[F]

      def nonEmptyTraverse[G[_]: Apply, A, B](fa: Backwards[F, A])(f: A => G[B]): G[Backwards[F, B]] =
       F.nonEmptyTraverse(fa.forwards)(f).map(Backwards.apply)
    }

  implicit def traverseFilterInstance[F[_]: TraverseFilter]: TraverseFilter[Backwards[F, ?]] =
    new TraverseFilter[Backwards[F, ?]] {
      val traverse: Traverse[Backwards[F, ?]] = traverseInstance(TraverseFilter[F].traverse)

      def traverseFilter[G[_]: Applicative, A, B](fa: Backwards[F, A])(f: A => G[Option[B]]): G[Backwards[F, B]] =
        TraverseFilter[F].traverseFilter(fa.forwards)(f).map(Backwards.apply)
    }
}

trait BackwardsInstances1 extends BackwardsInstances2 {

  implicit def eqInstance[F[_], A](implicit ev: Eq[F[A]]): Eq[Backwards[F, A]] =
    Eq.by(_.forwards)

  implicit def traverseInstance[F[_]: Traverse]: Traverse[Backwards[F, ?]] =
    new BackwardsTraverse[F] {
      val F: Traverse[F] = Traverse[F]
    }

  implicit def functorFilterInstance[F[_]: Applicative : FunctorFilter]: FunctorFilter[Backwards[F, ?]] =
    new FunctorFilter[Backwards[F, ?]] {
      def functor: Functor[Backwards[F, ?]] = applicativeInstance

      def mapFilter[A, B](fa: Backwards[F, A])(f: A => Option[B]): Backwards[F, B] =
        Backwards(FunctorFilter[F].mapFilter(fa.forwards)(f))
    }
}

trait BackwardsInstances2 extends BackwardsInstances3 {
  implicit def distributiveInstance[F[_]: Distributive]: Distributive[Backwards[F, ?]] =
    new Distributive[Backwards[F, ?]] {
      def distribute[G[_]: Functor, A, B](ga: G[A])(f: A => Backwards[F, B]): Backwards[F, G[B]] =
        Backwards(Distributive[F].distribute(ga)(a => f(a).forwards))

      def map[A, B](fa: Backwards[F, A])(f: A => B): Backwards[F, B] =
        Backwards(Distributive[F].map(fa.forwards)(f))
    }
}

trait BackwardsInstances3 extends BackwardsInstances4 {
  implicit def unorderedTraverseInstance[F[_]: UnorderedTraverse]: UnorderedTraverse[Backwards[F, ?]] =
    new BackwardsUnorderedTraverse[F] {
      val F: UnorderedTraverse[F] = UnorderedTraverse[F]
    }
}

trait BackwardsInstances4 extends BackwardsInstances5 {
  implicit def reducibleInstance[F[_]: Reducible]: Reducible[Backwards[F, ?]] =
    new BackwardsReducible[F] {
      val F: Reducible[F] = Reducible[F]
    }
}

trait BackwardsInstances5 extends BackwardsInstances6 {
  implicit def foldableInstance[F[_]: Foldable]: Foldable[Backwards[F, ?]] =
    new BackwardsFoldable[F] {
      val F: Foldable[F] = Foldable[F]
    }
}

trait BackwardsInstances6 extends BackwardsInstances7 {
  implicit def unorderedFoldableInstance[F[_]: UnorderedFoldable]: UnorderedFoldable[Backwards[F, ?]] =
    new BackwardsUnorderedFoldable[F] {
      val F: UnorderedFoldable[F] = UnorderedFoldable[F]
    }
}

trait BackwardsInstances7 extends BackwardsInstances8 {
  implicit def alternativeInstance[F[_]: Alternative]: Alternative[Backwards[F, ?]] =
    new Alternative[Backwards[F, ?]] with BackwardsApplicative[F] with BackwardsMonoidK[F] {
      val F: Alternative[F] = Alternative[F]
    }
}

trait BackwardsInstances8 extends BackwardsInstances9 {
  implicit def applicativeErrorInstance[F[_]: ApplicativeError[?[_], E], E]: ApplicativeError[Backwards[F, ?], E] =
    new ApplicativeError[Backwards[F, ?], E] with BackwardsApplicative[F] {
      val F: ApplicativeError[F, E] = ApplicativeError[F, E]

      def raiseError[A](e: E): Backwards[F, A] =
        Backwards(ApplicativeError[F, E].raiseError(e))

      def handleErrorWith[A](fa: Backwards[F, A])(f: E => Backwards[F, A]): Backwards[F, A] =
        Backwards(ApplicativeError[F, E].handleErrorWith(fa.forwards)(f.andThen(_.forwards)))
    }
}

trait BackwardsInstances9 extends BackwardsInstances10 {
  implicit def commutativeApplicativeInstance[F[_]: CommutativeApplicative]: CommutativeApplicative[Backwards[F, ?]] =
    new CommutativeApplicative[Backwards[F, ?]] with BackwardsApplicative[F] {
      val F: CommutativeApplicative[F] = CommutativeApplicative[F]
    }

  implicit def monoidKInstance[F[_]: MonoidK]: MonoidK[Backwards[F, ?]] =
    new BackwardsMonoidK[F] {
      val F: MonoidK[F] = MonoidK[F]
    }
}

trait BackwardsInstances10 extends BackwardsInstances11 {
  implicit def semigroupKInstance[F[_]: SemigroupK]: SemigroupK[Backwards[F, ?]] =
    new BackwardsSemigroupK[F] {
      val F: SemigroupK[F] = SemigroupK[F]
    }
}

trait BackwardsInstances11 {
  implicit def applicativeInstance[F[_]: Applicative]: Applicative[Backwards[F, ?]] =
    new BackwardsApplicative[F] {
      val F: Applicative[F] = Applicative[F]
    }
}

private [newts] trait BackwardsApplicative[F[_]] extends Applicative[Backwards[F, ?]] {
  val F: Applicative[F]

  def pure[A](x: A): Backwards[F, A] =
    Backwards(F.pure(x))

  def ap[A, B](ff: Backwards[F, A => B])(fa: Backwards[F, A]): Backwards[F, B] =
    Backwards((fa.forwards, ff.forwards).mapN({ case (a, f) => f(a) })(F, F))
}

private [newts] trait BackwardsSemigroupK[F[_]] extends SemigroupK[Backwards[F, ?]] {
  val F: SemigroupK[F]
  def combineK[A](x: Backwards[F, A], y: Backwards[F, A]): Backwards[F, A] =
    Backwards(F.combineK(x.forwards, y.forwards))
}

private [newts] trait BackwardsMonoidK[F[_]] extends BackwardsSemigroupK[F] with MonoidK[Backwards[F, ?]] {
  val F: MonoidK[F]
  def empty[A]: Backwards[F, A] = Backwards(F.empty)
}


private [newts] trait BackwardsUnorderedFoldable[F[_]] extends UnorderedFoldable[Backwards[F, ?]] {
  val F: UnorderedFoldable[F]
  def unorderedFoldMap[A, B: CommutativeMonoid](fa: Backwards[F, A])(f: A => B): B =
    F.unorderedFoldMap(fa.forwards)(f)
}

private [newts] trait BackwardsFoldable[F[_]] extends BackwardsUnorderedFoldable[F] with Foldable[Backwards[F, ?]] {
  val F: Foldable[F]

  def foldLeft[A, B](fa: Backwards[F, A], b: B)(f: (B, A) => B): B =
    F.foldLeft(fa.forwards, b)(f)

  def foldRight[A, B](fa: Backwards[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    F.foldRight(fa.forwards, lb)(f)
}

private [newts] trait BackwardsReducible[F[_]] extends BackwardsFoldable[F] with Reducible[Backwards[F, ?]] {
  val F: Reducible[F]

  def reduceLeftTo[A, B](fa: Backwards[F, A])(f: A => B)(g: (B, A) => B): B =
    F.reduceLeftTo(fa.forwards)(f)(g)

  def reduceRightTo[A, B](fa: Backwards[F, A])(f: A => B)(g: (A, Eval[B]) => Eval[B]): Eval[B] =
    F.reduceRightTo(fa.forwards)(f)(g)
}

private [newts] trait BackwardsUnorderedTraverse[F[_]] extends BackwardsUnorderedFoldable[F] with UnorderedTraverse[Backwards[F, ?]] {
  val F: UnorderedTraverse[F]
  def unorderedTraverse[G[_]: CommutativeApplicative, A, B](sa: Backwards[F, A])(f: A => G[B]): G[Backwards[F, B]] =
    F.unorderedTraverse(sa.forwards)(f).map(Backwards.apply)
}

private [newts] trait BackwardsTraverse[F[_]] extends BackwardsFoldable[F] with BackwardsUnorderedTraverse[F] with Traverse[Backwards[F, ?]] {
  val F: Traverse[F]
  override def traverse[G[_]: Applicative, A, B](fa: Backwards[F, A])(f: A => G[B]): G[Backwards[F, B]] =
    F.traverse(fa.forwards)(f).map(Backwards.apply)
}
