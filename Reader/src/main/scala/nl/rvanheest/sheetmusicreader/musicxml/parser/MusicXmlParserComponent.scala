package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.monadics.MonadPlus
import nl.rvanheest.sheetmusicreader.musicxml.model.Root.{ScorePartwise, ScoreTimewise}

trait MusicXmlParserComponent {

	def partwiseParser[M[+_]: MonadPlus]: MusicXmlParser[M]#XmlParser[ScorePartwise] = new MusicXmlParser[M].partwise
	def timewiseParser[M[+_]: MonadPlus]: MusicXmlParser[M]#XmlParser[ScoreTimewise] = new MusicXmlParser[M].timewise

	class MusicXmlParser[M[+_]](implicit ev: MonadPlus[M]) extends RootGroupParserComponent
																																 with GroupParserComponent
																																 with ComplexParserComponent
																																 with AttributeGroupsParserComponent
																																 with PrimativesParserComponent
																																 with XmlParserComponent {

		override type ParseResult[+A] = M[A]

		override protected implicit val mp: MonadPlus[ParseResult] = ev

		override val xmlParser: XmlParser.type = XmlParser

		override val primativeAttributesParser = new PrimativeAttributesParser
		override val primativeBarlineParser = new PrimativeBarlineParser
		override val primativeCommonParser = new PrimativeCommonParser
		override val primativeDirectionParser = new PrimativeDirectionParser
		override val primativeLayoutParser = new PrimativeLayoutParser
		override val primativeNoteParser = new PrimativeNoteParser
		override val primativeScoreParser = new PrimativeScoreParser

		override val attributeGroupsCommonParser = new AttributeGroupsCommonParser
		override val attributeGroupsDirectionParser = new AttributeGroupsDirectionParser
		override val attributeGroupsLinkParser = new AttributeGroupsLinkParser
		override val attributeGroupsScoreParser = new AttributeGroupsScoreParser

		override val complexAttributesParser = new ComplexAttributesParser
		override val complexBarlineParser = new ComplexBarlineParser
		override val complexCommonParser = new ComplexCommonParser
		override val complexDirectionParser = new ComplexDirectionParser
		override val complexIdentityParser = new ComplexIdentityParser
		override val complexLayoutParser = new ComplexLayoutParser
		override val complexLinkParser = new ComplexLinkParser
		override val complexNoteParser = new ComplexNoteParser
		override val complexScoreParser = new ComplexScoreParser

		override val groupAttributesParser = new GroupAttributesParser
		override val groupCommonParser = new GroupCommonParser
		override val groupDirectionParser = new GroupDirectionParser
		override val groupLayoutParser = new GroupLayoutParser
		override val groupNoteParser = new GroupNoteParser
		override val groupScoreParser = new GroupScoreParser

		override val rootParser = new RootParser
	}
}
