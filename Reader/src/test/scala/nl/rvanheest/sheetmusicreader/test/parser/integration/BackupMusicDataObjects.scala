package nl.rvanheest.sheetmusicreader.test.parser.integration

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsCommon.LevelDisplay
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.{FormattedText, Level}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexNote.Backup
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.Editorial
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.BackupChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.{PositiveDivisions, SymbolSize_Cue, YN_No, YN_Yes}

object BackupMusicDataObjects {
	val backup0 = BackupChoice(Backup(
		PositiveDivisions(15.14),
		Editorial(
			Option(FormattedText("foobar")),
			Option(Level(
				"foo",
				Option(YN_No),
				LevelDisplay(Option(YN_Yes), Option(YN_No), Option(SymbolSize_Cue)))))))

	val backup1 = BackupChoice(Backup(PositiveDivisions(14.13)))
}
