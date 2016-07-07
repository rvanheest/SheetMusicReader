package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.monadics.MonadPlus

class MusicXmlParser[M[+_]](implicit monadPlus: MonadPlus[M]) extends RootGroupParser[M]
																																	 with GroupParser[M]
																																	 with ComplexParser[M]
																																	 with AttributeGroupsParser[M]
																																	 with PrimativesParser[M]
																																	 with XmlParser[M] {
	protected implicit val mp: MonadPlus[M] = monadPlus
}

object MusicXmlParser {
	def parser[M[+_]](implicit monadPlus: MonadPlus[M]): MusicXmlParser[M] = {
		new MusicXmlParser[M]
	}
}
