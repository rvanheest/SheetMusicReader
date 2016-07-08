package nl.rvanheest.sheetmusicreader.test.parser.integration

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.{FormattedText, Level}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexNote.Forward
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.{EditorialVoice, Staff}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.ForwardChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.PositiveDivisions

object ForwardMusicDataObjects {
	val forward0 = ForwardChoice(Forward(
		PositiveDivisions(13.12),
		EditorialVoice(
			Option(FormattedText("test")),
			Option(Level("test123")),
			Option("voice123")),
		Option(
			Staff(11))))

	val forward1 = ForwardChoice(Forward(
		PositiveDivisions(12.11),
		staff = Option(Staff(10))))

	val forward2 = ForwardChoice(Forward(PositiveDivisions(11.10)))
}
