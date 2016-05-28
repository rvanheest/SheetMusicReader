package nl.rvanheest.sheetmusicreader.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.Root.{ScorePartwise, ScoreTimewise}

import scala.xml.Node

trait MusicXmlParser extends GroupParser {

	class RootParser extends GroupScoreParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.Root._

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
		val parser = new RootParser
		parser.xmlToScorePartwise
	}

	def timewise(xml: Node): XmlParser[ScoreTimewise] = {
		val parser = new RootParser
		parser.xmlToScoreTimewise
	}
}
