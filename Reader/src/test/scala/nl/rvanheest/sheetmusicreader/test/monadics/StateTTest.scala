/**
 * Copyright (C) 2016 Richard van Heest (richard.v.heest@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.rvanheest.sheetmusicreader.test.monadics

import nl.rvanheest.sheetmusicreader.monadics.{MonadPlus, OptionIsMonadPlus, StateT, TryIsMonadPlus}
import org.junit.Assert._
import org.junit.Test

import scala.language.postfixOps
import scala.util.Try

abstract class StateTTest[M[+_]] {

	private type StringParser[T] = StateT[String, T, M]

	implicit val mp: MonadPlus[M]

	def isEmpty[T](m: M[T]): Boolean

	private def item: StringParser[Char] = {
		StateT(_.toList match {
			case x :: xs => mp.create(x, xs.mkString)
			case Nil => mp.empty
		})
	}

	@Test
	def testFrom() = {
		val parser: StringParser[Char] = StateT.from('1')
		assertEquals(mp.create('1', "abc"), parser.run("abc"))
	}

	@Test
	def testFromEmpty() = {
		val parser: StringParser[Char] = StateT.from('1')
		assertEquals(mp.create('1', ""), parser.run(""))
	}

	@Test
	def testFailure() = {
		val parser: StringParser[Char] = StateT.failure
		assertTrue(isEmpty(parser.run("abc")))
	}

	@Test
	def testRun() = {
		assertEquals(mp.create('a', "bc"), item.run("abc"))
	}

	@Test
	def testRunEmpty() = {
		assertTrue(isEmpty(item.run("")))
	}

	@Test
	def testEval() = {
		assertEquals(mp.create('a'), item.eval("abc"))
	}

	@Test
	def testEvalEmpty() = {
		assertTrue(isEmpty(item.eval("")))
	}

	@Test
	def testExecute() = {
		assertEquals(mp.create("bc"), item.execute("abc"))
	}

	@Test
	def testExecuteEmpty() = {
		assertTrue(isEmpty(item.execute("")))
	}

	@Test
	def testOrElse() = {
		val parser = item.orElse(StateT.failure)
		assertEquals(mp.create('a', "bc"), parser.run("abc"))
	}

	@Test
	def testOrElseCovariance() = {
		abstract class A
		case class B() extends A
		case class C() extends A

		val parser1 = item.map(_ => new B)
		val parser2 = item.map(_ => new C)
		val parser: StateT[String, A, M] = parser1.orElse(parser2)

		val result: M[A] = parser.eval("abc")

		assertEquals(mp.create(new B), result)
	}

	@Test
	def testOrElseFailure() = {
		val parser = item.orElse(StateT.failure)
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testMap() = {
		val parser = item.map(Char.char2int).map(1 +).map(_.toChar)
		assertEquals(mp.create('b', "bc"), parser.run("abc"))
	}

	@Test
	def testMapEmpty() = {
		val parser = item.map(Char.char2int).map(1 +).map(_.toChar)
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testDoOnNext() = {
		var toggle = false
		val parser = item.doOnNext(_ => toggle = true)
		assertEquals(mp.create('a', "bc"), parser.run("abc"))
		assertTrue(toggle)
	}

	@Test
	def testDoOnNextEmpty() = {
		var toggle = false
		val parser = item.doOnNext(_ => toggle = true)
		assertTrue(isEmpty(parser.run("")))
		assertFalse(toggle)
	}

	@Test
	def testFlatMap() = {
		val parser = item.flatMap(c1 => item.map(c2 => s"$c1$c2"))
		assertEquals(mp.create("ab", "c"), parser.run("abc"))
	}

	@Test
	def testFlatMapForComprehension() = {
		val parser = for {
			c1 <- item
			c2 <- item
		} yield s"$c1$c2"
		assertEquals(mp.create("ab", "c"), parser.run("abc"))
	}

	@Test
	def testFlatMapEmpty() = {
		val parser = item.flatMap(c1 => item.map(c2 => s"$c1$c2"))
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testFlatMapForComprehensionEmpty() = {
		val parser = for {
			c1 <- item
			c2 <- item
		} yield s"$c1$c2"
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testAndThen() = {
		val parser = item >> item
		assertEquals(mp.create('b', "c"), parser.run("abc"))
	}

	@Test
	def testAndThenEmpty() = {
		val parser = item >> item
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testThenAnd() = {
		val parser = item << item
		assertEquals(mp.create('a', "c"), parser.run("abc"))
	}

	@Test
	def testThenAndEmpty() = {
		val parser = item << item
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testSatisfyTrue() = {
		val parser = item.satisfy('a' ==)
		assertEquals(mp.create('a', "bc"), parser.run("abc"))
	}

	@Test
	def testSatisfyFalse() = {
		val parser = item.satisfy('b' ==)
		assertTrue(isEmpty(parser.run("abc")))
	}

	@Test
	def testSatisfyEmpty() = {
		val parser = item.satisfy('a' ==)
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testSatisfyForComprehensionTrue() = {
		val parser = for {
			c1 <- item
			if c1 == 'a'
			c2 <- item
		} yield s"$c1$c2"
		assertEquals(mp.create("ab", "c"), parser.run("abc"))
	}

	@Test
	def testSatisfyForComprehensionFalse() = {
		val parser = for {
			c1 <- item
			if c1 == 'b'
			c2 <- item
		} yield s"$c1$c2"
		assertTrue(isEmpty(parser.run("abc")))
	}

	@Test
	def testSatisfyForComprehensionEmpty() = {
		val parser = for {
			c1 <- item
			if c1 == 'a'
			c2 <- item
		} yield s"$c1$c2"
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testMaybeSuccess() = {
		val parser = item.satisfy('a' ==).maybe
		assertEquals(mp.create(Some('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testMaybeFailure() = {
		val parser = item.satisfy('b' ==).maybe
		assertEquals(mp.create(None, "abc"), parser.run("abc"))
	}

	@Test
	def testMaybeEmpty() = {
		val parser = item.maybe
		assertEquals(mp.create(None, ""), parser.run(""))
	}

	@Test
	def testMany() = {
		val parser = item.many
		assertEquals(mp.create(List('a', 'b', 'c'), ""), parser.run("abc"))
	}

	@Test
	def testManySatisfy() = {
		val parser = item.satisfy('a' ==).many
		assertEquals(mp.create(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testManySatisfyEmptyList() = {
		val parser = item.satisfy('b' ==).many
		assertEquals(mp.create(Nil, "abc"), parser.run("abc"))
	}

	@Test
	def testManyEmpty() = {
		val parser = item.many
		assertEquals(mp.create(Nil, ""), parser.run(""))
	}

	@Test
	def testAtLeastOnce() = {
		val parser = item.atLeastOnce
		assertEquals(mp.create(List('a', 'b', 'c'), ""), parser.run("abc"))
	}

	@Test
	def testAtLeastOnceSatisfy() = {
		val parser = item.satisfy('a' ==).atLeastOnce
		assertEquals(mp.create(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testAtLeastOnceSatisfyEmptyList() = {
		val parser = item.satisfy('b' ==).atLeastOnce
		assertTrue(isEmpty(parser.run("abc")))
	}

	@Test
	def testAtLeastOnceEmpty() = {
		val parser = item.atLeastOnce
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testTakeUntilOne() = {
		val parser = item.takeUntil('a' !=)
		assertEquals(mp.create(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testTakeUntilMany() = {
		val parser = item.takeUntil('a' !=)
		assertEquals(mp.create(List('a', 'a', 'a'), "bc"), parser.run("aaabc"))
	}

	@Test
	def testTakeUntilFalse() = {
		val parser = item.takeUntil('b' !=)
		assertEquals(mp.create(Nil, "abc"), parser.run("abc"))
	}

	@Test
	def testTakeUntilEmpty() = {
		val parser = item.takeUntil('a' !=)
		assertEquals(mp.create(Nil, ""), parser.run(""))
	}

	@Test
	def testTakeWhileOne() = {
		val parser = item.takeWhile('a' ==)
		assertEquals(mp.create(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testTakeWhileMany() = {
		val parser = item.takeWhile('a' ==)
		assertEquals(mp.create(List('a', 'a', 'a'), "bc"), parser.run("aaabc"))
	}

	@Test
	def testTakeWhileFalse() = {
		val parser = item.takeWhile('b' ==)
		assertEquals(mp.create(Nil, "abc"), parser.run("abc"))
	}

	@Test
	def testTakeWhileEmpty() = {
		val parser = item.takeWhile('a' ==)
		assertEquals(mp.create(Nil, ""), parser.run(""))
	}

	@Test
	def testSeparatedBy() = {
		val parser = item.separatedBy(item.satisfy('-' ==))
		assertEquals(mp.create(List('a', 'b', 'c'), ""), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedByUnsatisfy() = {
		val parser = item.separatedBy(item.satisfy('#' ==))
		assertEquals(mp.create(List('a'), "-b-c"), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedByEmpty() = {
		val parser = item.separatedBy(item.satisfy('-' ==))
		assertEquals(mp.create(Nil, ""), parser.run(""))
	}

	@Test
	def testSeparated1By() = {
		val parser = item.separatedBy1(item.satisfy('-' ==))
		assertEquals(mp.create(List('a', 'b', 'c'), ""), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedBy1Unsatisfy() = {
		val parser = item.separatedBy1(item.satisfy('#' ==))
		assertEquals(mp.create(List('a'), "-b-c"), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedBy1Empty() = {
		val parser = item.separatedBy1(item.satisfy('-' ==))
		assertTrue(isEmpty(parser.run("")))
	}

	@Test
	def testSkipMany() = {
		val parser = item.skipMany
		assertEquals(mp.create((), ""), parser.run("abc"))
	}

	@Test
	def testSkipManyEmpty() = {
		val parser = item.skipMany
		assertEquals(mp.create((), ""), parser.run(""))
	}
}

class StateTOptionTest extends StateTTest[Option] {
	implicit val mp: MonadPlus[Option] = OptionIsMonadPlus

	def isEmpty[T](m: Option[T]): Boolean = m.isEmpty
}

class StateTTryTest extends StateTTest[Try] {
	implicit val mp: MonadPlus[Try] = TryIsMonadPlus

	def isEmpty[T](m: Try[T]): Boolean = m.isFailure
}
