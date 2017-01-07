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
package nl.rvanheest.sheetmusicreader.test.parser

import nl.rvanheest.sheetmusicreader.monadics.{MonadPlus, OptionIsMonadPlus, TryIsMonadPlus}
import nl.rvanheest.sheetmusicreader.musicxml.parser.XmlParserComponent
import org.junit.Test
import org.junit.Assert._

import scala.collection.mutable.ArrayBuffer
import scala.util.Try
import scala.xml.{NamespaceBinding, TopScope}

abstract class XmlParserTest extends XmlParserComponent {

	import XmlParser._

	def isEmpty[T](m: ParseResult[T]): Boolean

	@Test
	def testNodeItemSimple() = {
		val xml = <foo>1</foo>
		val parser = nodeWithName("foo")

		assertEquals(mp.create(xml, Nil), parser.run(xml))
	}

	@Test
	def testNodeItemSimpleNotExist() = {
		val xml = <foo>1</foo>
		val parser = nodeWithName("bar")

		assertTrue(isEmpty(parser.run(xml)))
	}

	@Test
	def testNodeItemNested() = {
		val xml = <foo><bar1>1</bar1><bar2>2</bar2></foo>
		val parser = nodeWithName("foo")

		assertEquals(mp.create(xml, Nil), parser.run(xml))
	}

	@Test
	def testNodeItemNestedNotExist() = {
		val xml = <foo><bar1>1</bar1><bar2>2</bar2></foo>
		val parser = nodeWithName("bar")

		assertTrue(isEmpty(parser.run(xml)))
	}

	@Test
	def testNodeItemSiblings() = {
		val xml1 = <foo>1</foo>
		val xml2 = <bar>2</bar>
		val parser = nodeWithName("foo")

		assertEquals(mp.create(xml1, List(xml2)), parser.run(ArrayBuffer(xml1, xml2)))
	}

	@Test
	def testNodeItemSiblingsNotFirst() = {
		val xml1 = <foo>1</foo>
		val xml2 = <bar>2</bar>
		val parser = nodeWithName("bar")

		assertTrue(isEmpty(parser.run(ArrayBuffer(xml1, xml2))))
	}

	@Test
	def testNodeItemSiblingsNotExist() = {
		val xml1 = <foo>1</foo>
		val xml2 = <bar>2</bar>
		val parser = nodeWithName("baz")

		assertTrue(isEmpty(parser.run(ArrayBuffer(xml1, xml2))))
	}

	@Test
	def testXmlToStringLeaf() = {
		val xml = <foo>1</foo>
		val parser = xmlToString("foo")

		assertEquals(mp.create("1", Nil), parser.run(xml))
	}

	@Test
	def testXmlToStringNested() = {
		val xml = <foo><bar>1</bar></foo>
		val parser = xmlToString("foo")

		assertEquals(mp.create("1", Nil), parser.run(xml))
	}

	@Test
	def testXmlToStringMultipleNested() = {
		val xml = <foo><bar1>1</bar1><bar2>2</bar2></foo>
		val parser = xmlToString("foo")

		assertEquals(mp.create("12", Nil), parser.run(xml))
	}

	@Test
	def testNode() = {
		val xml = <foo>1</foo>
		val parser = node("foo")(_.toInt)

		assertEquals(mp.create(1, Nil), parser.run(xml))
	}

	@Test
	def testNodeThrowingException() = {
		val xml = <foo bar="abc" baz="def">1</foo>
		val parser = node("foo")(x => sys.error(s"exception in transformation; received $x"))

		assertTrue(isEmpty(parser.run(xml)))
	}

	@Test
	def testBranchNode() = {
		val xml = <foo><bar>1</bar></foo>
		val subParser = node("bar")(_.toInt)
		val parser = branchNode("foo")(subParser)

		assertEquals(mp.create(1, Nil), parser.run(xml))
	}

	@Test
	def testBranchNodeWithRest() = {
		val xml = <foo><bar>1</bar><bar>2</bar></foo>
		val subParser = node("bar")(_.toInt)
		val parser = branchNode("foo")(subParser)

		assertEquals(mp.create(1, List(<bar>2</bar>)), parser.run(xml))
	}

	@Test
	def testBranchNodeWithRestAndSiblings() = {
		val xml = ArrayBuffer(
			<foo><bar>1</bar><bar>2</bar></foo>,
			<foo><bar>3</bar></foo>
		)
		val subParser = node("bar")(_.toInt)
		val parser = branchNode("foo")(subParser)

		assertEquals(mp.create(1, List(<bar>2</bar>, <foo><bar>3</bar></foo>)), parser.run(xml))
	}

	@Test
	def testAttributeIdSingle() = {
		val xml = <foo bar="abc">1</foo>
		val parser = attributeId("bar")

		assertEquals(mp.create("abc", xml), parser.run(xml))
	}

	@Test
	def testAttributeIdFirst() = {
		val xml = <foo bar="abc" baz="def">1</foo>
		val parser = attributeId("bar")

		assertEquals(mp.create("abc", xml), parser.run(xml))
	}

	@Test
	def testAttributeIdSecond() = {
		val xml = <foo baz="def" bar="abc">1</foo>
		val parser = attributeId("bar")

		assertEquals(mp.create("abc", xml), parser.run(xml))
	}

	@Test
	def testAttributeIdNotExist() = {
		val xml = <foo baz="abc">1</foo>
		val parser = attributeId("bar")

		assertTrue(isEmpty(parser.run(xml)))
	}

	@Test
	def testAttributeSingle() = {
		case class A(s: String)

		val xml = <foo bar="abc">1</foo>
		val parser = attribute("bar")(A)

		assertEquals(mp.create(A("abc"), xml), parser.run(xml))
	}

	@Test
	def testAttributeFirst() = {
		case class A(s: String)

		val xml = <foo bar="abc" baz="def">1</foo>
		val parser = attribute("bar")(A)

		assertEquals(mp.create(A("abc"), xml), parser.run(xml))
	}

	@Test
	def testAttributeSecond() = {
		case class A(s: String)

		val xml = <foo baz="def" bar="abc">1</foo>
		val parser = attribute("bar")(A)

		assertEquals(mp.create(A("abc"), xml), parser.run(xml))
	}

	@Test
	def testAttributeNotExist() = {
		case class A(s: String)

		val xml = <foo baz="abc">1</foo>
		val parser = attributeId("bar")

		assertTrue(isEmpty(parser.run(xml)))
	}

	@Test
	def testAttributeThrowingException() = {
		val xml = <foo bar="abc" baz="def">1</foo>
		val parser = attribute("bar")(x => sys.error(s"exception in transformation; received $x"))

		assertTrue(isEmpty(parser.run(xml)))
	}

	@Test
	def testNamespaceAttribute() = {
		implicit val xlink = "http://www.w3.org/1999/xlink"
		implicit val nb = NamespaceBinding("xlink", xlink, TopScope)

		val xml = <foo xlink:href="bar">abc</foo>
		val parser = namespaceAttribute("href")(xlink, nb)

		assertEquals(mp.create("bar", xml), parser.run(xml))
	}

	@Test
	def testNamespaceAttributeMissingAttribute() = {
		implicit val xlink = "http://www.w3.org/1999/xlink"
		implicit val nb = NamespaceBinding("xlink", xlink, TopScope)

		val xml = <foo bar="def">abc</foo>
		val parser = namespaceAttribute("href")(xlink, nb)

		assertTrue(isEmpty(parser.run(xml)))
	}

	@Test
	def testNamespaceAttributeWithoutNamespace() = {
		implicit val xlink = "http://www.w3.org/1999/xlink"
		implicit val nb = NamespaceBinding("xlink", xlink, TopScope)

		val xml = <foo href="def">abc</foo>
		val parser = namespaceAttribute("href")(xlink, nb)

		assertTrue(isEmpty(parser.run(xml)))
	}
}

class XmlParserOptionTest extends XmlParserTest {

	override type ParseResult[+A] = Option[A]

	protected override implicit val mp: MonadPlus[ParseResult] = OptionIsMonadPlus
	protected override val xmlParser: XmlParser.type = XmlParser

	def isEmpty[T](m: Option[T]): Boolean = m.isEmpty
}

class XmlParserTryTest extends XmlParserTest {

	override type ParseResult[+A] = Try[A]

	protected override implicit val mp: MonadPlus[ParseResult] = TryIsMonadPlus
	protected override val xmlParser: XmlParser.type = XmlParser

	def isEmpty[T](m: Try[T]): Boolean = m.isFailure
}
