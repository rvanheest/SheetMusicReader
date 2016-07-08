package nl.rvanheest.sheetmusicreader.test.monadics

import nl.rvanheest.sheetmusicreader.monadics.StateT
import org.junit.Assert._
import org.junit.Test

import scala.language.postfixOps

class StateTOptionTest {

	private type StringParser[T] = StateT[String, T, Option]

	private def item: StringParser[Char] = {
		StateT(_.toList match {
			case x :: xs => Some((x, xs.mkString))
			case Nil => None
		})
	}

	@Test
	def testFrom() = {
		val parser: StringParser[Char] = StateT.from('1')
		assertEquals(Option('1', "abc"), parser.run("abc"))
	}

	@Test
	def testFromEmpty() = {
		val parser: StringParser[Char] = StateT.from('1')
		assertEquals(Option('1', ""), parser.run(""))
	}

	@Test
	def testFailure() = {
		val parser: StringParser[Char] = StateT.failure
		assertEquals(None, parser.run("abc"))
	}

	@Test
	def testRun() = {
		assertEquals(Option('a', "bc"), item.run("abc"))
	}

	@Test
	def testRunEmpty() = {
		assertEquals(Option.empty, item.run(""))
	}

	@Test
	def testEval() = {
		assertEquals(Option('a'), item.eval("abc"))
	}

	@Test
	def testEvalEmpty() = {
		assertEquals(Option.empty, item.eval(""))
	}

	@Test
	def testExecute() = {
		assertEquals(Option("bc"), item.execute("abc"))
	}

	@Test
	def testExecuteEmpty() = {
		assertEquals(Option.empty, item.execute(""))
	}

	@Test
	def testOrElse() = {
		val parser = item.orElse(StateT.failure)
		assertEquals(Option('a', "bc"), parser.run("abc"))
	}

	@Test
	def testOrElseCovariance() = {
		abstract class A
		case class B() extends A
		case class C() extends A

		val parser1 = item.map(_ => new B)
		val parser2 = item.map(_ => new C)
		val parser: StateT[String, A, Option] = parser1.orElse(parser2)

		val result: Option[A] = parser.eval("abc")

		assertEquals(new B, result.get)
	}

	@Test
	def testOrElseFailure() = {
		val parser = item.orElse(StateT.failure)
		assertEquals(Option.empty, parser.run(""))
	}

	@Test
	def testMap() = {
		val parser = item.map(Char.char2int).map(1 +).map(_.toChar)
		assertEquals(Option('b', "bc"), parser.run("abc"))
	}

	@Test
	def testMapEmpty() = {
		val parser = item.map(Char.char2int).map(1 +).map(_.toChar)
		assertEquals(Option.empty, parser.run(""))
	}

	@Test
	def testDoOnNext() = {
		var toggle = false
		val parser = item.doOnNext(_ => toggle = true)
		assertEquals(Option('a', "bc"), parser.run("abc"))
		assertTrue(toggle)
	}

	@Test
	def testDoOnNextEmpty() = {
		var toggle = false
		val parser = item.doOnNext(_ => toggle = true)
		assertEquals(None, parser.run(""))
		assertFalse(toggle)
	}

	@Test
	def testFlatMap() = {
		val parser = item.flatMap(c1 => item.map(c2 => s"$c1$c2"))
		assertEquals(Some("ab", "c"), parser.run("abc"))
	}

	@Test
	def testFlatMapForComprehension() = {
		val parser = for {
			c1 <- item
			c2 <- item
		} yield s"$c1$c2"
		assertEquals(Some("ab", "c"), parser.run("abc"))
	}

	@Test
	def testFlatMapEmpty() = {
		val parser = item.flatMap(c1 => item.map(c2 => s"$c1$c2"))
		assertEquals(None, parser.run(""))
	}

	@Test
	def testFlatMapForComprehensionEmpty() = {
		val parser = for {
			c1 <- item
			c2 <- item
		} yield s"$c1$c2"
		assertEquals(None, parser.run(""))
	}

	@Test
	def testAndThen() = {
		val parser = item >> item
		assertEquals(Some('b', "c"), parser.run("abc"))
	}

	@Test
	def testAndThenEmpty() = {
		val parser = item >> item
		assertEquals(None, parser.run(""))
	}

	@Test
	def testThenAnd() = {
		val parser = item << item
		assertEquals(Some('a', "c"), parser.run("abc"))
	}

	@Test
	def testThenAndEmpty() = {
		val parser = item << item
		assertEquals(None, parser.run(""))
	}

	@Test
	def testSatisfyTrue() = {
		val parser = item.satisfy('a' ==)
		assertEquals(Some('a', "bc"), parser.run("abc"))
	}

	@Test
	def testSatisfyFalse() = {
		val parser = item.satisfy('b' ==)
		assertEquals(None, parser.run("abc"))
	}

	@Test
	def testSatisfyEmpty() = {
		val parser = item.satisfy('a' ==)
		assertEquals(None, parser.run(""))
	}

	@Test
	def testSatisfyForComprehensionTrue() = {
		val parser = for {
			c1 <- item
			if c1 == 'a'
			c2 <- item
		} yield s"$c1$c2"
		assertEquals(Some("ab", "c"), parser.run("abc"))
	}

	@Test
	def testSatisfyForComprehensionFalse() = {
		val parser = for {
			c1 <- item
			if c1 == 'b'
			c2 <- item
		} yield s"$c1$c2"
		assertEquals(None, parser.run("abc"))
	}

	@Test
	def testSatisfyForComprehensionEmpty() = {
		val parser = for {
			c1 <- item
			if c1 == 'a'
			c2 <- item
		} yield s"$c1$c2"
		assertEquals(None, parser.run(""))
	}

	@Test
	def testMaybeSuccess() = {
		val parser = item.satisfy('a' ==).maybe
		assertEquals(Option(Some('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testMaybeFailure() = {
		val parser = item.satisfy('b' ==).maybe
		assertEquals(Option(None, "abc"), parser.run("abc"))
	}

	@Test
	def testMaybeEmpty() = {
		val parser = item.maybe
		assertEquals(Option(None, ""), parser.run(""))
	}

	@Test
	def testMany() = {
		val parser = item.many
		assertEquals(Option(List('a', 'b', 'c'), ""), parser.run("abc"))
	}

	@Test
	def testManySatisfy() = {
		val parser = item.satisfy('a' ==).many
		assertEquals(Option(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testManySatisfyEmptyList() = {
		val parser = item.satisfy('b' ==).many
		assertEquals(Option(Nil, "abc"), parser.run("abc"))
	}

	@Test
	def testManyEmpty() = {
		val parser = item.many
		assertEquals(Option(Nil, ""), parser.run(""))
	}

	@Test
	def testAtLeastOnce() = {
		val parser = item.atLeastOnce
		assertEquals(Option(List('a', 'b', 'c'), ""), parser.run("abc"))
	}

	@Test
	def testAtLeastOnceSatisfy() = {
		val parser = item.satisfy('a' ==).atLeastOnce
		assertEquals(Option(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testAtLeastOnceSatisfyEmptyList() = {
		val parser = item.satisfy('b' ==).atLeastOnce
		assertEquals(None, parser.run("abc"))
	}

	@Test
	def testAtLeastOnceEmpty() = {
		val parser = item.atLeastOnce
		assertEquals(None, parser.run(""))
	}

	@Test
	def testTakeUntilOne() = {
		val parser = item.takeUntil('a' !=)
		assertEquals(Option(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testTakeUntilMany() = {
		val parser = item.takeUntil('a' !=)
		assertEquals(Option(List('a', 'a', 'a'), "bc"), parser.run("aaabc"))
	}

	@Test
	def testTakeUntilFalse() = {
		val parser = item.takeUntil('b' !=)
		assertEquals(Option(Nil, "abc"), parser.run("abc"))
	}

	@Test
	def testTakeUntilEmpty() = {
		val parser = item.takeUntil('a' !=)
		assertEquals(Option(Nil, ""), parser.run(""))
	}

	@Test
	def testTakeWhileOne() = {
		val parser = item.takeWhile('a' ==)
		assertEquals(Option(List('a'), "bc"), parser.run("abc"))
	}

	@Test
	def testTakeWhileMany() = {
		val parser = item.takeWhile('a' ==)
		assertEquals(Option(List('a', 'a', 'a'), "bc"), parser.run("aaabc"))
	}

	@Test
	def testTakeWhileFalse() = {
		val parser = item.takeWhile('b' ==)
		assertEquals(Option(Nil, "abc"), parser.run("abc"))
	}

	@Test
	def testTakeWhileEmpty() = {
		val parser = item.takeWhile('a' ==)
		assertEquals(Option(Nil, ""), parser.run(""))
	}

	@Test
	def testSeparatedBy() = {
		val parser = item.separatedBy(item.satisfy('-' ==))
		assertEquals(Option(List('a', 'b', 'c'), ""), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedByUnsatisfy() = {
		val parser = item.separatedBy(item.satisfy('#' ==))
		assertEquals(Option(List('a'), "-b-c"), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedByEmpty() = {
		val parser = item.separatedBy(item.satisfy('-' ==))
		assertEquals(Option(Nil, ""), parser.run(""))
	}

	@Test
	def testSeparated1By() = {
		val parser = item.separatedBy1(item.satisfy('-' ==))
		assertEquals(Option(List('a', 'b', 'c'), ""), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedBy1Unsatisfy() = {
		val parser = item.separatedBy1(item.satisfy('#' ==))
		assertEquals(Option(List('a'), "-b-c"), parser.run("a-b-c"))
	}

	@Test
	def testSeparatedBy1Empty() = {
		val parser = item.separatedBy1(item.satisfy('-' ==))
		assertEquals(None, parser.run(""))
	}

	@Test
	def testSkipMany() = {
		val parser = item.skipMany
		assertEquals(Option((), ""), parser.run("abc"))
	}

	@Test
	def testSkipManyEmpty() = {
		val parser = item.skipMany
		assertEquals(Option((), ""), parser.run(""))
	}
}
