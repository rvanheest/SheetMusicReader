package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.monadics.{MonadPlus, StateT}

import scala.xml.{NamespaceBinding, Node}

trait XmlParser[M[+_]] extends Parser {

	type XmlParser[A] = StateT[Seq[Node], A, M]

	protected implicit val mp: MonadPlus[M]

	protected val xmlParser = XmlParser

	object XmlParser {
		private def nodeItem: XmlParser[Node] = {
			StateT(ns => ns
				.headOption
				.map((_, ns.tail))
				.map(x => mp.create(x))
				.getOrElse(mp.empty))
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
			StateT(input => {
				mp.flatMap(nodeWithName(name).map(_.child).run(input)) {
					case (childNodes, rest) =>
						mp.map(subParser.run(childNodes)) {
							case (result, rest2) => (result, rest2 ++ rest)
						}
				}
			})
		}

		private def attributeItem: XmlParser[Node] = {
			StateT(ns => ns
				.headOption
				.map((_, ns))
				.map(x => mp.create(x))
				.getOrElse(mp.empty))
		}

		def attribute[T](attr: String)(constructor: String => T): XmlParser[T] = {
			attributeItem.map(_ \@ attr).satisfy(_.nonEmpty).flatMap(x => {
				try { StateT.from(constructor(x)) }
				catch { case e: Throwable => StateT.failure }
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
			StateT(xs => sys.error(s"you hit a debug statement at $pos: $xs"))
		}

		def debugAndContinue(pos: String = ""): XmlParser[Unit] = {
			StateT(xs => {
				println(s"you hit a debug statement at $pos: $xs")
				mp create ((), xs)
			})
		}
	}
}
