package nl.rvanheest.sheetmusicreader.test.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsLink.ElementPosition
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLink.Bookmark
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.BookmarkChoice

object BookmarkMusicDataObjects {

	val bookmark = BookmarkChoice(Bookmark(
		id = "abc",
		name = Option("myName"),
		elementPosition = ElementPosition(Option("elem"), Option(123))))
}
