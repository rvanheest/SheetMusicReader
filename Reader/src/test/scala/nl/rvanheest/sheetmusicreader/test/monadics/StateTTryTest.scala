package nl.rvanheest.sheetmusicreader.test.monadics

import nl.rvanheest.sheetmusicreader.monadics.StateT
import org.junit.Assert._
import org.junit.Test

import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

class StateTTryTest {

	private type StringParser[T] = StateT[String, T, Try]

	private val errorMessage = "empty string"

	private def item: StringParser[Char] = {
		StateT(_.toList match {
			case x :: xs => Success((x, xs.mkString))
			case Nil => Failure(new NoSuchElementException(errorMessage))
		})
	}

	@Test
	def testFrom() = {
		val parser: StringParser[Char] = StateT.from('1')
		assertEquals(Try('1', "abc"), parser.run("abc"))
	}

	@Test
	def testFromEmpty() = {
		val parser: StringParser[Char] = StateT.from('1')
		assertEquals(Try('1', ""), parser.run(""))
	}

	@Test
	def testFailure() = {
		val parser: StringParser[Char] = StateT.failure
		val result = parser.run("abc")

		assertTrue(result.isFailure)
		assertEquals("empty", result.failed.get.getMessage)
	}

	@Test
	def testRun() = {
		assertEquals(Try('a', "bc"), item.run("abc"))
	}

	@Test
	def testRunEmpty() = {
		val result = item.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testEval() = {
		assertEquals(Try('a'), item.eval("abc"))
	}

	@Test
	def testEvalEmpty() = {
		val result = item.eval("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testExecute() = {
		assertEquals(Try("bc"), item.execute("abc"))
	}

	@Test
	def testExecuteEmpty() = {
		val result = item.execute("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testOrElse() = {
		val parser = item.orElse(StateT.failure)
		assertEquals(Try('a', "bc"), parser.run("abc"))
	}

	@Test
	def testOrElseCovariance() = {
		abstract class A
		case class B() extends A
		case class C() extends A

		val parser1 = item.map(_ => new B)
		val parser2 = item.map(_ => new C)
		val parser: StateT[String, A, Try] = parser1.orElse(parser2)

		val result: Try[A] = parser.eval("abc")

		assertEquals(new B, result.get)
	}

	@Test
	def testOrElseFailure() = {
		val parser = item.orElse(StateT.failure)
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals("empty", result.failed.get.getMessage)
	}

	@Test
	def testMap() = {
		val parser = item.map(Char.char2int).map(1 +).map(_.toChar)
		assertEquals(Try('b', "bc"), parser.run("abc"))
	}

	@Test
	def testMapEmpty() = {
		val parser = item.map(Char.char2int).map(1 +).map(_.toChar)
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testDoOnNext() = {
		var toggle = false
		val parser = item.doOnNext(_ => toggle = true)
		assertEquals(Try('a', "bc"), parser.run("abc"))
		assertTrue(toggle)
	}

	@Test
	def testDoOnNextEmpty() = {
		var toggle = false
		val parser = item.doOnNext(_ => toggle = true)
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
		assertFalse(toggle)
	}

	@Test
	def testFlatMap() = {
		val parser = item.flatMap(c1 => item.map(c2 => s"$c1$c2"))
		assertEquals(Try("ab", "c"), parser.run("abc"))
	}

	@Test
	def testFlatMapForComprehension() = {
		val parser = for {
			c1 <- item
			c2 <- item
		} yield s"$c1$c2"
		assertEquals(Try("ab", "c"), parser.run("abc"))
	}

	@Test
	def testFlatMapEmpty() = {
		val parser = item.flatMap(c1 => item.map(c2 => s"$c1$c2"))
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testFlatMapForComprehensionEmpty() = {
		val parser = for {
			c1 <- item
			c2 <- item
		} yield s"$c1$c2"
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testAndThen() = {
		val parser = item >> item
		assertEquals(Try('b', "c"), parser.run("abc"))
	}

	@Test
	def testAndThenEmpty() = {
		val parser = item >> item
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testThenAnd() = {
		val parser = item << item
		assertEquals(Try('a', "c"), parser.run("abc"))
	}

	@Test
	def testThenAndEmpty() = {
		val parser = item << item
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testSatisfyTrue() = {
		val parser = item.satisfy('a' ==)
		assertEquals(Try('a', "bc"), parser.run("abc"))
	}

	@Test
	def testSatisfyFalse() = {
		val parser = item.satisfy('b' ==)
		val result = parser.run("abc")

		assertTrue(result.isFailure)
		assertEquals("empty", result.failed.get.getMessage)
	}

	@Test
	def testSatisfyEmpty() = {
		val parser = item.satisfy('a' ==)
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testSatisfyForComprehensionTrue() = {
		val parser = for {
			c1 <- item
			if c1 == 'a'
			c2 <- item
		} yield s"$c1$c2"
		assertEquals(Try("ab", "c"), parser.run("abc"))
	}

	@Test
	def testSatisfyForComprehensionFalse() = {
		val parser = for {
			c1 <- item
			if c1 == 'b'
			c2 <- item
		} yield s"$c1$c2"
		val result = parser.run("abc")

		assertTrue(result.isFailure)
		assertEquals("empty", result.failed.get.getMessage)
	}

	@Test
	def testSatisfyForComprehensionEmpty() = {
		val parser = for {
			c1 <- item
			if c1 == 'a'
			c2 <- item
		} yield s"$c1$c2"
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testMaybeSuccess() = {
		val parser = item.satisfy('a' ==).maybe
		assertEquals(Try(Some('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testMaybeFailure() = {
		val parser = item.satisfy('b' ==).maybe
		assertEquals(Try(None, "abc"), parser.run("abc"))
	}

	@Test
	def testMaybeEmpty() = {
		val parser = item.maybe
		assertEquals(Try(None, ""), parser.run(""))
	}

	@Test
	def testMany() = {
		val parser = item.many
		assertEquals(Try(List('a', 'b', 'c'), ""), parser.run("abc"))
	}

	@Test
	def testManySatisfy() = {
		val parser = item.satisfy('a' ==).many
		assertEquals(Try(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testManySatisfyEmptyList() = {
		val parser = item.satisfy('b' ==).many
		assertEquals(Try(Nil, "abc"), parser.run("abc"))
	}

	@Test
	def testManyEmpty() = {
		val parser = item.many
		assertEquals(Try(Nil, ""), parser.run(""))
	}

	@Test
	def testAtLeastOnce() = {
		val parser = item.atLeastOnce
		assertEquals(Try(List('a', 'b', 'c'), ""), parser.run("abc"))
	}

	@Test
	def testAtLeastOnceSatisfy() = {
		val parser = item.satisfy('a' ==).atLeastOnce
		assertEquals(Try(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testAtLeastOnceSatisfyEmptyList() = {
		val parser = item.satisfy('b' ==).atLeastOnce
		val result = parser.run("abc")

		assertTrue(result.isFailure)
		assertEquals("empty", result.failed.get.getMessage)
	}

	@Test
	def testAtLeastOnceEmpty() = {
		val parser = item.atLeastOnce
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testTakeUntilOne() = {
		val parser = item.takeUntil('a' !=)
		assertEquals(Try(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testTakeUntilMany() = {
		val parser = item.takeUntil('a' !=)
		assertEquals(Try(List('a', 'a', 'a'), "bc"), parser.run("aaabc"))
	}

	@Test
	def testTakeUntilFalse() = {
		val parser = item.takeUntil('b' !=)
		assertEquals(Try(Nil, "abc"), parser.run("abc"))
	}

	@Test
	def testTakeUntilEmpty() = {
		val parser = item.takeUntil('a' !=)
		assertEquals(Try(Nil, ""), parser.run(""))
	}

	@Test
	def testTakeWhileOne() = {
		val parser = item.takeWhile('a' ==)
		assertEquals(Try(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testTakeWhileMany() = {
		val parser = item.takeWhile('a' ==)
		assertEquals(Try(List('a', 'a', 'a'), "bc"), parser.run("aaabc"))
	}

	@Test
	def testTakeWhileFalse() = {
		val parser = item.takeWhile('b' ==)
		assertEquals(Try(Nil, "abc"), parser.run("abc"))
	}

	@Test
	def testTakeWhileEmpty() = {
		val parser = item.takeWhile('a' ==)
		assertEquals(Try(Nil, ""), parser.run(""))
	}

	@Test
	def testSeparatedBy() = {
		val parser = item.separatedBy(item.satisfy('-' ==))
		assertEquals(Try(List('a', 'b', 'c'), ""), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedByUnsatisfy() = {
		val parser = item.separatedBy(item.satisfy('#' ==))
		assertEquals(Try(List('a'), "-b-c"), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedByEmpty() = {
		val parser = item.separatedBy(item.satisfy('-' ==))
		assertEquals(Try(Nil, ""), parser.run(""))
	}

	@Test
	def testSeparated1By() = {
		val parser = item.separatedBy1(item.satisfy('-' ==))
		assertEquals(Try(List('a', 'b', 'c'), ""), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedBy1Unsatisfy() = {
		val parser = item.separatedBy1(item.satisfy('#' ==))
		assertEquals(Try(List('a'), "-b-c"), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedBy1Empty() = {
		val parser = item.separatedBy1(item.satisfy('-' ==))
		val result = parser.run("")

		assertTrue(result.isFailure)
		assertEquals(errorMessage, result.failed.get.getMessage)
	}

	@Test
	def testSkipMany() = {
		val parser = item.skipMany
		assertEquals(Try((), ""), parser.run("abc"))
	}

	@Test
	def testSkipManyEmpty() = {
		val parser = item.skipMany
		assertEquals(Try((), ""), parser.run(""))
	}
}
