package nl.rvanheest.sheetmusicreader.musicxml.parser

import scala.xml.{NamespaceBinding, Node}

trait XmlParser extends ParserCombinators {

	type XmlParser[A] = Parser[Seq[Node], A]

	protected val xmlParser = XML

	object XML {
		private def nodeItem: XmlParser[Node] = {
			Parser(ns => ns.headOption.map((_, ns.tail)))
		}

		def nodeWithName(name: String): XmlParser[Node] = {
			nodeItem.satisfy(_.label == name)
		}

		def node[T](name: String)(constructor: String => T): XmlParser[T] = {
			xmlToString(name).map(constructor)
		}

		def xmlToString(name: String): XmlParser[String] = {
			nodeWithName(name).map(_.text)
		}

		def branchNode[A](name: String)(subParser: XmlParser[A]): XmlParser[A] = {
			Parser(input => {
				for {
					(childNodes, rest) <- nodeWithName(name).map(_.child).parse(input)
					(result, rest2) <- subParser.parse(childNodes)
				} yield (result, rest2 ++ rest)
			})
		}

		private def attributeItem: XmlParser[Node] = {
			Parser(ns => ns.headOption.map((_, ns)))
		}

		def attribute[T](attr: String)(constructor: String => T): XmlParser[T] = {
			attributeItem.map(_ \@ attr).satisfy(_.nonEmpty).flatMap(x => {
				try { Parser.from(constructor(x)) }
				catch { case e: Throwable => Parser.failure }
			})
		}

		def attributeId(attr: String): XmlParser[String] = {
			attribute(attr)(identity)
		}

		def namespaceAttribute(attrName: String)(implicit nsURL: String, namespace: NamespaceBinding): XmlParser[String] = {
			// notice that _.attributes(...) can be null!!!
			attributeItem
				.map(_.attributes(nsURL, namespace, attrName))
				.satisfy(xs => xs != null && xs.nonEmpty)
				.map(_.head.text)
		}

		def debugAndFail(pos: String = ""): XmlParser[Nothing] = {
			Parser(xs => sys.error(s"you hit a debug statement at $pos: $xs"))
		}

		def debugAndContinue(pos: String = ""): XmlParser[Unit] = {
			Parser(xs => {
				println(s"you hit a debug statement at $pos: $xs")
				Some(((), xs))
			})
		}
	}
}
