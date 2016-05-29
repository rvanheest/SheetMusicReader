package nl.rvanheest.sheetmusicreader.test.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexDirection.{Feature, Grouping}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.GroupingChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.{SSS_Single, SSS_Start}

object GroupingMusicDataObjects {

	val grouping0 = GroupingChoice(Grouping(
		feature = List(
			Feature("feature 1", Option("type1")),
			Feature("feature 2")),
		groupingType = SSS_Single))

	val grouping1 = GroupingChoice(Grouping(
		groupingType = SSS_Start,
		number = "2",
		memberOf = Option("myMemberOf")))
}
