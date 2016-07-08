package nl.rvanheest.sheetmusicreader.test.parser.integration

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsCommon.DocumentAttributes
import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsScore.MeasureAttributes
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.MusicData
import nl.rvanheest.sheetmusicreader.musicxml.model.Root.{MeasureScorePartwise, PartScorePartwise, ScorePartwise}

object ScoreObjects {

	import HeaderObjects._
	import Measure1Objects._

	val documentAttributes = DocumentAttributes("3.0")
	val measure1_2 = MeasureScorePartwise(MusicData(), MeasureAttributes("1_2"))
	val part1 = PartScorePartwise(List(measure1_1, measure1_2), "P1")
	val parts = List(part1)
	val scorePartwise = ScorePartwise(header, parts, documentAttributes)
}
