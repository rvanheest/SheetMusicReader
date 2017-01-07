package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.monadics.MonadPlus
import nl.rvanheest.sheetmusicreader.musicxml.model.Root.{ScorePartwise, ScoreTimewise}

trait MusicXmlParserComponent {

	def partwiseParser[M[+_]](implicit mp: MonadPlus[M]): MusicXmlParser[M]#XmlParser[ScorePartwise] = new MusicXmlParser[M].partwise
	def timewiseParser[M[+_]](implicit mp: MonadPlus[M]): MusicXmlParser[M]#XmlParser[ScoreTimewise] = new MusicXmlParser[M].timewise

	class MusicXmlParser[M[+_]](implicit ev: MonadPlus[M]) extends RootGroupParserComponent[M]
																																 with GroupParserComponent[M]
																																 with ComplexParserComponent[M]
																																 with AttributeGroupsParserComponent[M]
																																 with PrimativesParserComponent[M]
																																 with XmlParserComponent[M] {

		override protected implicit val mp: MonadPlus[M] = ev

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
