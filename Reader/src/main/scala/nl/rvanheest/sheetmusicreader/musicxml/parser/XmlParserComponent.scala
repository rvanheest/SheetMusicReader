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
package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.monadics.{MonadPlus, StateT}

import scala.xml.{NamespaceBinding, Node}

trait XmlParserComponent extends ParserComponent {

	type ParseResult[+_]

	type XmlParser[A] = StateT[Seq[Node], A, ParseResult]

	protected implicit val mp: MonadPlus[ParseResult]

	protected val xmlParser: XmlParser.type

	object XmlParser {
		private def nodeItem: XmlParser[Node] = {
			StateT(ns => ns
				.headOption
				.map(head => mp.create(head, ns.tail))
				.getOrElse(mp.empty))
		}

		private def withException[T](s: String)(constructor: String => T): XmlParser[T] = {
			try { StateT.from(constructor(s)) }
			catch { case e: Throwable => StateT.failure(e) }
		}

		def nodeWithName(name: String): XmlParser[Node] = {
			nodeItem.satisfy(_.label == name)
		}

		def xmlToString(name: String): XmlParser[String] = {
			nodeWithName(name).map(_.text)
		}

		def node[T](name: String)(constructor: String => T): XmlParser[T] = {
			xmlToString(name)
				.flatMap(withException(_)(constructor))
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
				.map(head => mp.create(head, ns))
				.getOrElse(mp.empty))
		}

		def attribute[T](attr: String)(constructor: String => T): XmlParser[T] = {
			attributeItem
				.map(_ \@ attr)
				.satisfy(_.nonEmpty)
			  .flatMap(withException(_)(constructor))
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
