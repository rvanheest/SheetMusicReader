package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.Root.{PartScoreTimewise, _}

trait RootGroupParserComponent {
	this: GroupParserComponent
		with AttributeGroupsParserComponent
		with XmlParserComponent =>

	protected val rootParser: RootParser

	class RootParser {

		import attributeGroupsCommonParser._
		import attributeGroupsScoreParser._
		import groupScoreParser._
		import xmlParser._

		def xmlToScorePartwise: XmlParser[ScorePartwise] = {
			for {
				attr <- xmlToDocumentAttributes
				scorePartwise <- branchNode("score-partwise") {
					for {
						header <- xmlToScoreHeader
						parts <- xmlToPartScorePartwise("part").many
					} yield ScorePartwise(header, parts, attr)
				}
			} yield scorePartwise
		}

		def xmlToPartScorePartwise(name: String): XmlParser[PartScorePartwise] = {
			for {
				attr <- xmlToPartAttributes
				measures <- branchNode(name)(xmlToMeasureScorePartwise("measure").many)
			} yield PartScorePartwise(measures, attr)
		}

		def xmlToMeasureScorePartwise(name: String): XmlParser[MeasureScorePartwise] = {
			for {
				attr <- xmlToMeasureAttributes
				data <- branchNode(name)(xmlToMusicData)
			} yield MeasureScorePartwise(data, attr)
		}

		def xmlToScoreTimewise: XmlParser[ScoreTimewise] = {
			for {
				attr <- xmlToDocumentAttributes
				scoreTimewise <- branchNode("score-timewise") {
					for {
						header <- xmlToScoreHeader
						measures <- xmlToMeasureScoreTimewise("measure").many
					} yield ScoreTimewise(header, measures, attr)
				}
			} yield scoreTimewise
		}

		def xmlToMeasureScoreTimewise(name: String): XmlParser[MeasureScoreTimewise] = {
			for {
				attr <- xmlToMeasureAttributes
				parts <- branchNode(name)(xmlToPartScoreTimewise("part").many)
			} yield MeasureScoreTimewise(parts, attr)
		}

		def xmlToPartScoreTimewise(name: String): XmlParser[PartScoreTimewise] = {
			for {
				attr <- xmlToPartAttributes
				data <- branchNode(name)(xmlToMusicData)
			} yield PartScoreTimewise(data, attr)
		}
	}

	def partwise: XmlParser[ScorePartwise] = {
		rootParser.xmlToScorePartwise
	}

	def timewise: XmlParser[ScoreTimewise] = {
		rootParser.xmlToScoreTimewise
	}
}
