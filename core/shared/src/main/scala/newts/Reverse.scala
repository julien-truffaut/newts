package newts

import cats._
import cats.syntax.functor._
import cats.kernel.CommutativeMonoid

/**
  * The same functor, but with [[Foldable]] and [[Traverse]] instances that process the elements in the reverse order.
  *
  * Based on Haskell's [[http://hackage.haskell.org/package/transformers-0.5.5.0/docs/Data-Functor-Reverse.html Data.Functor.Reverse]]
  *
  * @note`foldLeft` is implemented in terms of a strict `foldRight`.
  */
final case class Reverse[F[_], A](getReverse: F[A])

object Reverse extends ReverseInstances0

trait ReverseInstances0 extends ReverseInstances1 {

  implicit def newtypeInstance[F[_], A]: Newtype.Aux[Reverse[F, A], F[A]] =
    Newtype.from[Reverse[F, A], F[A]](Reverse.apply)(_.getReverse)

  implicit def showInstance[F[_], A](implicit ev: Show[F[A]]): Show[Reverse[F, A]] =
    Show.show(bw => s"Reverse(${ev.show(bw.getReverse)})")


  implicit def orderInstance[F[_], A](implicit ev: Order[F[A]]): Order[Reverse[F, A]] =
    Order.by(_.getReverse)

  implicit def nonEmptyTraverseInstance[F[_]: NonEmptyTraverse]: NonEmptyTraverse[Reverse[F, ?]] =
    new NonEmptyTraverse[Reverse[F, ?]] with ReverseTraverse[F] with ReverseReducible[F] {
      val F: NonEmptyTraverse[F] = NonEmptyTraverse[F]

      def nonEmptyTraverse[G[_]: Apply, A, B](fa: Reverse[F, A])(f: A => G[B]): G[Reverse[F, B]] =
       F.nonEmptyTraverse(fa.getReverse)(f).map(Reverse.apply)
    }

  implicit def traverseFilterInstance[F[_]: TraverseFilter]: TraverseFilter[Reverse[F, ?]] =
    new TraverseFilter[Reverse[F, ?]] {
      val traverse: Traverse[Reverse[F, ?]] = traverseInstance(TraverseFilter[F].traverse)

      def traverseFilter[G[_]: Applicative, A, B](fa: Reverse[F, A])(f: A => G[Option[B]]): G[Reverse[F, B]] =
        TraverseFilter[F].traverseFilter(fa.getReverse)(f).map(Reverse.apply)
    }
}

trait ReverseInstances1 extends ReverseInstances2 {
  implicit def eqInstance[F[_], A](implicit ev: Eq[F[A]]): Eq[Reverse[F, A]] =
    Eq.by(_.getReverse)

  implicit def bimonadInstance[F[_]: Bimonad]: Bimonad[Reverse[F, ?]] =
    new Bimonad[Reverse[F, ?]] with ReverseComonad[F] with ReverseMonad[F] {
      val F: Bimonad[F] = Bimonad[F]
    }
}

trait ReverseInstances2 extends ReverseInstances3 {
  implicit def commutativeMonadInstance[F[_]: CommutativeMonad]: CommutativeMonad[Reverse[F, ?]] =
    new CommutativeMonad[Reverse[F, ?]] with ReverseMonad[F] {
      val F: CommutativeMonad[F] = CommutativeMonad[F]
    }
}

trait ReverseInstances3 extends ReverseInstances4 {
  implicit def monadErrorInstance[F[_]: MonadError[?[_], E], E]: MonadError[Reverse[F, ?], E] =
    new ReverseApplicativeError[F, E] with ReverseMonad[F] with MonadError[Reverse[F, ?], E] {
      val F: MonadError[F, E] = MonadError[F, E]
    }
}

trait ReverseInstances4 extends ReverseInstances5 {
  implicit def comonadInstance[F[_]: Comonad]: Comonad[Reverse[F, ?]] =
    new ReverseComonad[F] {
      val F: Comonad[F] = Comonad[F]
    }
}

trait ReverseInstances5 extends ReverseInstances6 {
  implicit def monadInstance[F[_]: Monad]: Monad[Reverse[F, ?]] =
    new ReverseMonad[F] {
      val F: Monad[F] = Monad[F]
    }
}

trait ReverseInstances6 extends ReverseInstances7 {
  implicit def traverseInstance[F[_]: Traverse]: Traverse[Reverse[F, ?]] =
    new ReverseTraverse[F] {
      val F: Traverse[F] = Traverse[F]
    }

  implicit def functorFilterInstance[F[_]: Applicative : FunctorFilter]: FunctorFilter[Reverse[F, ?]] =
    new FunctorFilter[Reverse[F, ?]] {
      def functor: Functor[Reverse[F, ?]] = applicativeInstance

      def mapFilter[A, B](fa: Reverse[F, A])(f: A => Option[B]): Reverse[F, B] =
        Reverse(FunctorFilter[F].mapFilter(fa.getReverse)(f))
    }
}

trait ReverseInstances7 extends ReverseInstances8 {
  implicit def distributiveInstance[F[_]: Distributive]: Distributive[Reverse[F, ?]] =
    new Distributive[Reverse[F, ?]] with ReverseFunctor[F] {
      val F: Distributive[F] = Distributive[F]

      def distribute[G[_]: Functor, A, B](ga: G[A])(f: A => Reverse[F, B]): Reverse[F, G[B]] =
        Reverse(F.distribute(ga)(a => f(a).getReverse))
    }
}

trait ReverseInstances8 extends ReverseInstances9 {
  implicit def unorderedTraverseInstance[F[_]: UnorderedTraverse]: UnorderedTraverse[Reverse[F, ?]] =
    new ReverseUnorderedTraverse[F] {
      val F: UnorderedTraverse[F] = UnorderedTraverse[F]
    }
}

trait ReverseInstances9 extends ReverseInstances10 {
  implicit def reducibleInstance[F[_]: Reducible]: Reducible[Reverse[F, ?]] =
    new ReverseReducible[F] {
      val F: Reducible[F] = Reducible[F]
    }
}

trait ReverseInstances10 extends ReverseInstances11 {
  implicit def foldableInstance[F[_]: Foldable]: Foldable[Reverse[F, ?]] =
    new ReverseFoldable[F] {
      val F: Foldable[F] = Foldable[F]
    }
}

trait ReverseInstances11 extends ReverseInstances12 {
  implicit def unorderedFoldableInstance[F[_]: UnorderedFoldable]: UnorderedFoldable[Reverse[F, ?]] =
    new ReverseUnorderedFoldable[F] {
      val F: UnorderedFoldable[F] = UnorderedFoldable[F]
    }
}

trait ReverseInstances12 extends ReverseInstances13 {
  implicit def alternativeInstance[F[_]: Alternative]: Alternative[Reverse[F, ?]] =
    new Alternative[Reverse[F, ?]] with ReverseApplicative[F] with ReverseMonoidK[F] {
      val F: Alternative[F] = Alternative[F]
    }
}

trait ReverseInstances13 extends ReverseInstances14 {
  implicit def applicativeErrorInstance[F[_]: ApplicativeError[?[_], E], E]: ApplicativeError[Reverse[F, ?], E] =
    new ReverseApplicativeError[F, E] {
      val F: ApplicativeError[F, E] = ApplicativeError[F, E]
    }
}

trait ReverseInstances14 extends ReverseInstances15 {
  implicit def commutativeApplicativeInstance[F[_]: CommutativeApplicative]: CommutativeApplicative[Reverse[F, ?]] =
    new CommutativeApplicative[Reverse[F, ?]] with ReverseApplicative[F] {
      val F: CommutativeApplicative[F] = CommutativeApplicative[F]
    }

  implicit def monoidKInstance[F[_]: MonoidK]: MonoidK[Reverse[F, ?]] =
    new ReverseMonoidK[F] {
      val F: MonoidK[F] = MonoidK[F]
    }
}

trait ReverseInstances15 extends ReverseInstances16 {
  implicit def semigroupKInstance[F[_]: SemigroupK]: SemigroupK[Reverse[F, ?]] =
    new ReverseSemigroupK[F] {
      val F: SemigroupK[F] = SemigroupK[F]
    }
}

trait ReverseInstances16 {
  implicit def applicativeInstance[F[_]: Applicative]: Applicative[Reverse[F, ?]] =
    new ReverseApplicative[F] {
      val F: Applicative[F] = Applicative[F]
    }
}


private [newts] trait ReverseFunctor[F[_]] extends Functor[Reverse[F, ?]] {
  val F: Functor[F]

  def map[A, B](fa: Reverse[F, A])(f: A => B): Reverse[F, B] =
    Reverse(F.map(fa.getReverse)(f))
}

private [newts] trait ReverseApplicative[F[_]] extends ReverseFunctor[F] with Applicative[Reverse[F, ?]] {
  val F: Applicative[F]

  def pure[A](x: A): Reverse[F, A] =
    Reverse(F.pure(x))

  def ap[A, B](ff: Reverse[F, A => B])(fa: Reverse[F, A]): Reverse[F, B] =
    Reverse(F.ap(ff.getReverse)(fa.getReverse))
}

private [newts] trait ReverseApplicativeError[F[_], E] extends ReverseApplicative[F] with ApplicativeError[Reverse[F, ?], E] {
  val F: ApplicativeError[F, E]

  def raiseError[A](e: E): Reverse[F, A] = Reverse(F.raiseError(e))

  def handleErrorWith[A](fa: Reverse[F, A])(f: E => Reverse[F, A]): Reverse[F, A] = Reverse(F.handleErrorWith(fa.getReverse)(e => f(e).getReverse))
}

private [newts] trait ReverseMonad[F[_]] extends ReverseApplicative[F] with Monad[Reverse[F, ?]] {
  val F: Monad[F]

  def flatMap[A, B](fa: Reverse[F, A])(f: A => Reverse[F, B]): Reverse[F, B] =
    Reverse(F.flatMap(fa.getReverse)(a => f(a).getReverse))

  def tailRecM[A, B](a: A)(f: A => Reverse[F, Either[A, B]]): Reverse[F, B] =
    Reverse(F.tailRecM(a)(a => f(a).getReverse))
}

private [newts] trait ReverseComonad[F[_]] extends ReverseFunctor[F] with Comonad[Reverse[F, ?]] {
  val F: Comonad[F]

  def extract[A](x: Reverse[F, A]): A =
    F.extract(x.getReverse)

  def coflatMap[A, B](fa: Reverse[F, A])(f: Reverse[F, A] => B): Reverse[F, B] =
    Reverse(F.coflatMap(fa.getReverse)(x => f(Reverse(x))))
}

private [newts] trait ReverseSemigroupK[F[_]] extends SemigroupK[Reverse[F, ?]] {
  val F: SemigroupK[F]
  def combineK[A](x: Reverse[F, A], y: Reverse[F, A]): Reverse[F, A] =
    Reverse(F.combineK(x.getReverse, y.getReverse))
}

private [newts] trait ReverseMonoidK[F[_]] extends ReverseSemigroupK[F] with MonoidK[Reverse[F, ?]] {
  val F: MonoidK[F]
  def empty[A]: Reverse[F, A] = Reverse(F.empty)
}


private [newts] trait ReverseUnorderedFoldable[F[_]] extends UnorderedFoldable[Reverse[F, ?]] {
  val F: UnorderedFoldable[F]
  def unorderedFoldMap[A, B: CommutativeMonoid](fa: Reverse[F, A])(f: A => B): B =
    F.unorderedFoldMap(fa.getReverse)(f)
}

private [newts] trait ReverseFoldable[F[_]] extends ReverseUnorderedFoldable[F] with Foldable[Reverse[F, ?]] {
  val F: Foldable[F]

  def foldLeft[A, B](fa: Reverse[F, A], b: B)(f: (B, A) => B): B =
    F.foldRight(fa.getReverse, Eval.now(b))((a, evalB) => Eval.now(f(evalB.value, a))).value

  def foldRight[A, B](fa: Reverse[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    F.foldLeft(fa.getReverse, lb)((evalB, a) => f(a, evalB))

  override def forall[A](fa: Reverse[F, A])(p: A => Boolean): Boolean =
    // `forall` needs to be lazy, according to the `UnorderedFoldableLaws`, so, by default,
    // it's implemented using `foldRight` (which can be lazy).
    // But our `foldRight` here is always strict (because it's implemented using the underlying `foldLeft`),
    // and this would break the `UnorderedFoldable` laws.
    // To avoid this, we simply override `forall` and defer the implementation to the underlying foldable.
    F.forall(fa.getReverse)(p)

  override def exists[A](fa: Reverse[F, A])(p: A => Boolean): Boolean =
    // See note above regarding `forall`.
    F.exists(fa.getReverse)(p)
}

private [newts] trait ReverseReducible[F[_]] extends ReverseFoldable[F] with Reducible[Reverse[F, ?]] {
  val F: Reducible[F]

  def reduceLeftTo[A, B](fa: Reverse[F, A])(f: A => B)(g: (B, A) => B): B =
    F.reduceLeftTo(fa.getReverse)(f)(g)

  def reduceRightTo[A, B](fa: Reverse[F, A])(f: A => B)(g: (A, Eval[B]) => Eval[B]): Eval[B] =
    F.reduceRightTo(fa.getReverse)(f)(g)
}

private [newts] trait ReverseUnorderedTraverse[F[_]] extends ReverseUnorderedFoldable[F] with UnorderedTraverse[Reverse[F, ?]] {
  val F: UnorderedTraverse[F]
  def unorderedTraverse[G[_]: CommutativeApplicative, A, B](sa: Reverse[F, A])(f: A => G[B]): G[Reverse[F, B]] =
    F.unorderedTraverse(sa.getReverse)(f).map(Reverse.apply)
}

private [newts] trait ReverseTraverse[F[_]] extends ReverseFoldable[F] with ReverseUnorderedTraverse[F] with Traverse[Reverse[F, ?]] {
  val F: Traverse[F]
  override def traverse[G[_]: Applicative, A, B](fa: Reverse[F, A])(f: A => G[B]): G[Reverse[F, B]] =
    F.traverse(fa.getReverse)(a => Backwards(f(a))).forwards.map(Reverse.apply)
}
