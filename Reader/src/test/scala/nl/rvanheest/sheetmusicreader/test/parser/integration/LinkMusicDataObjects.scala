package nl.rvanheest.sheetmusicreader.test.parser.integration

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsLink.{ElementPosition, LinkAttributes}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLink.Link
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.LinkChoice

object LinkMusicDataObjects {

	val link = LinkChoice(Link(
		LinkAttributes("http://www.google.com/"),
		Option("link name"),
		ElementPosition(Option("abc"), Option(123))))
}
