---
layout: home
title:  "Home"
section: "home"
position: 1
---

[![Build Status](https://travis-ci.org/julien-truffaut/newts.svg?branch=master)](https://travis-ci.org/julien-truffaut/newts)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.julien-truffaut/newts_2.12.svg)](http://search.maven.org/#search|ga|1|com.github.julien-truffaut.newts)


Newts defines newtypes compatible with [cats](https://github.com/typelevel/cats) typeclasses. 

```scala
libraryDependencies ++= Seq(
  "com.github.julien-truffaut" %%  "newts-core"  % "0.3.2"
)
```

## Example

Using `cats` you can squash values using `Monoid` or `Semigroup` instance (based on if your data structure can be empty):

```tut:silent
import cats.data.NonEmptyList
import cats.instances.all._
import cats.syntax.all._

val ints    = 1.to(100).toList
val intNel  = NonEmptyList(1, 2.to(100).toList)
val strings = List("abc", "cde", "fgh")
```

```tut
ints.combineAll
intNel.reduce
strings.combineAll
```

However, some types have more than one valid instance of a typeclass, e.g. `(+, 0)` and `(*, 1)` are two
completely valid `Monoid`. A solution to this problem is to chose one instance somehow arbitrarily and create a wrapper 
type for each other implementation. This pattern is called newtype and it comes from the haskell language.

```tut:silent
import cats.Monoid

implicit val longMonoid: Monoid[Long] = new Monoid[Long] {
  def empty: Long = 0
  def combine(x: Long, y: Long): Long = x + y
}

case class Mult(value: Long)

implicit val longMult: Monoid[Mult] = new Monoid[Mult] {
  def empty: Mult = Mult(1)
  def combine(x: Mult, y: Mult): Mult = Mult(x.value * y.value)
}
```

Here are a few examples of newtypes define in `newts`:

```tut:silent
import newts._
```

```tut
ints.foldMap(i => LastOption(Some(i)))
intNel.reduceMap(First(_))
intNel.reduceMap(Min(_))
intNel.reduceMap(Max(_))
strings.foldMap(Dual(_))
```

Instead of using newtype constructors, one can use `newts.syntax`:

```tut:silent
import newts.syntax.all._
import cats.syntax.all._
```

```tut
strings.foldMap(_.asDual)
ints.foldMap(_.some.asLastOption)
```

## Maintainers and contributors

The current maintainers (people who can merge pull requests) are:

* Julien Truffaut - [@julien-truffaut](https://github.com/julien-truffaut)

and the [contributors](https://github.com/julien-truffaut/newts/graphs/contributors) (people who committed to newts).

## Copyright and license

Newts is licensed under the **[Apache License, Version 2.0][http://www.apache.org/licenses/LICENSE-2.0]** (the
"License"); you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.