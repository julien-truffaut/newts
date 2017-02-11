package newts

import cats.Apply
import cats.instances.list._
import cats.kernel.Eq

final case class ZipList[A](value: List[A])

object ZipList {

  implicit val instances: Apply[ZipList] = new Apply[ZipList] {
    def map[A, B](fa: ZipList[A])(f: A => B): ZipList[B] =
      ZipList(fa.value.map(f))

    def ap[A, B](ff: ZipList[A => B])(fa: ZipList[A]): ZipList[B] =
      map(product(ff, fa)){case (f, a) => f(a)}

    override def product[A, B](fa: ZipList[A], fb: ZipList[B]): ZipList[(A, B)] =
      ZipList(fa.value.zip(fb.value))
  }

  implicit def eqInstances[A: Eq]: Eq[ZipList[A]] = Eq.by(_.value)
}