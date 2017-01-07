package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupAttributes.{NonTraditionalKey, Slash, TimeSignature, TraditionalKey}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.{Staff, Tuning, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupDirection._
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupLayout.{AllMargins, Layout, LeftRightMargins}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupNote._
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.{ScoreHeader, _}

trait GroupParserComponent[M[+_]] {
	this: ComplexParserComponent[M]
		with AttributeGroupsParserComponent[M]
		with PrimativesParserComponent[M]
		with XmlParserComponent[M] =>

	protected val groupAttributesParser: GroupAttributesParser
	protected val groupCommonParser: GroupCommonParser
	protected val groupDirectionParser: GroupDirectionParser
	protected val groupLayoutParser: GroupLayoutParser
	protected val groupNoteParser: GroupNoteParser
	protected val groupScoreParser: GroupScoreParser

	class GroupAttributesParser {

		import complexAttributesParser._
		import complexCommonParser._
		import xmlParser._
		import primativeAttributesParser._
		import primativeNoteParser._

		def xmlToNonTraditionalKey: XmlParser[NonTraditionalKey] = {
			for {
				step <- step("key-step")(node)
				alter <- semitones("key-alter")(node)
				accidental <- accidentalValue("key-accidental")(node).maybe
			} yield NonTraditionalKey(step, alter, accidental)
		}

		def xmlToSlash: XmlParser[Slash] = {
			for {
				slashType <- noteTypeValue("slash-type")(node)
				slashDot <- xmlToEmpty("slash-dot").many
			} yield Slash(slashType, slashDot)
		}

		def xmlToTimeSignature: XmlParser[TimeSignature] = {
			for {
				beats <- xmlToString("beats")
				beatType <- xmlToString("beat-type")
			} yield TimeSignature(beats, beatType)
		}

		def xmlToTraditionalKey: XmlParser[TraditionalKey] = {
			for {
				cancel <- xmlToCancel("cancel").maybe
				fifths <- fifths("fifths")(node)
				mode <- mode("mode")(node).maybe
			} yield TraditionalKey(cancel, fifths, mode)
		}
	}

	class GroupCommonParser {

		import complexCommonParser._
		import xmlParser._
		import primativeNoteParser._

		def xmlToEditorial: XmlParser[Editorial] = {
			for {
				footnote <- xmlToFootnote.maybe
				level <- xmlToLevel.maybe
			} yield Editorial(footnote, level)
		}

		def xmlToEditorialVoice: XmlParser[EditorialVoice] = {
			for {
				footnote <- xmlToFootnote.maybe
				level <- xmlToLevel.maybe
				voice <- xmlToVoice.maybe
			} yield EditorialVoice(footnote, level, voice)
		}

		def xmlToEditorialVoiceDirection: XmlParser[EditorialVoiceDirection] = {
			for {
				footnote <- xmlToFootnote.maybe
				level <- xmlToLevel.maybe
				voice <- xmlToVoice.maybe
			} yield EditorialVoiceDirection(footnote, level, voice)
		}

		def xmlToFootnote: XmlParser[FootNote] = {
			xmlToFormattedText("footnote")
		}

		def xmlToLevel: XmlParser[Level] = {
			complexCommonParser.xmlToLevel("level")
		}

		def xmlToStaff: XmlParser[Staff] = {
			xmlToString("staff").map(s => Staff(s.toInt))
		}

		def xmlToTuning: XmlParser[Tuning] = {
			for {
				step <- step("tuning-step")(node)
				alter <- semitones("tuning-alter")(node).maybe
				octave <- octave("tuning-octave")(node)
			} yield Tuning(step, alter, octave)
		}

		def xmlToVoice: XmlParser[Voice] = {
			xmlToString("voice")
		}
	}

	class GroupDirectionParser {

		import complexCommonParser._
		import complexDirectionParser._
		import complexNoteParser._
		import xmlParser._
		import primativeNoteParser._

		def xmlToBeatUnit: XmlParser[BeatUnit] = {
			for {
				beatUnit <- noteTypeValue("beat-unit")(node)
				beatUnitDot <- xmlToEmpty("beat-unit-dot").many
			} yield BeatUnit(beatUnit, beatUnitDot)
		}

		def xmlToHarmonyChord: XmlParser[HarmonyChord] = {
			for {
				choice <- xmlToRoot("root").map(RootChoice)
					.orElse(xmlToStyleText("function").map(StyleTextChoice))
				kind <- xmlToKind("kind")
				inversion <- xmlToInversion("inversion").maybe
				bass <- xmlToBass("bass").maybe
				degree <- xmlToDegree("degree").many
			} yield HarmonyChord(choice, kind, inversion, bass, degree)
		}
	}

	class GroupLayoutParser {

		import complexLayoutParser._
		import xmlParser._
		import primativeCommonParser._

		def xmlToAllMargins: XmlParser[AllMargins] = {
			for {
				leftRight <- xmlToLeftRightMargins
				top <- tenths("top-margin")(node)
				bottom <- tenths("bottom-margin")(node)
			} yield AllMargins(leftRight, top, bottom)
		}

		def xmlToLayout: XmlParser[Layout] = {
			for {
				page <- xmlToPageLayout("page-layout").maybe
				system <- xmlToSystemLayout("system-layout").maybe
				staff <- xmlToStaffLayout("staff-layout").many
			} yield Layout(page, system, staff)
		}

		def xmlToLeftRightMargins: XmlParser[LeftRightMargins] = {
			for {
				left <- tenths("left-margin")(node)
				right <- tenths("right-margin")(node)
			} yield LeftRightMargins(left, right)
		}
	}

	class GroupNoteParser {

		import complexCommonParser._
		import complexNoteParser._
		import xmlParser._
		import primativeCommonParser._
		import primativeNoteParser._

		def xmlToDuration: XmlParser[Duration] = positiveDivisions("duration")(node)

		def xmlToDisplayStepOctave: XmlParser[DisplayStepOctave] = {
			for {
				step <- step("display-step")(node)
				octave <- octave("display-octave")(node)
			} yield DisplayStepOctave(step, octave)
		}

		def xmlToFullNote: XmlParser[FullNote] = {
			for {
				chord <- xmlToEmpty("chord").maybe
				choice <- xmlToPitch("pitch").map(PitchChoice)
					.orElse(xmlToUnpitched("unpitched").map(UnpitchedChoice))
					.orElse(xmlToRest("rest").map(RestChoice))
			} yield FullNote(chord, choice)
		}
	}

	class GroupScoreParser {

		import complexAttributesParser._
		import complexBarlineParser._
		import complexDirectionParser._
		import complexIdentityParser._
		import complexLinkParser._
		import complexNoteParser._
		import complexScoreParser._
		import xmlParser._

		def xmlToMusicData: XmlParser[MusicData] = {
			xmlToNote("note").map(NoteChoice)
				.orElse(xmlToBackup("backup").map(BackupChoice))
				.orElse(xmlToForward("forward").map(ForwardChoice))
				.orElse(xmlToDirection("direction").map(DirectionChoice))
				.orElse(xmlToAttributes("attributes").map(AttributesChoice))
				.orElse(xmlToHarmony("harmony").map(HarmonyChoice))
				.orElse(xmlToFiguredBass("figured-bass").map(FiguredBassChoice))
				.orElse(xmlToPrint("print").map(PrintChoice))
				.orElse(xmlToSound("sound").map(SoundChoice))
				.orElse(xmlToBarline("barline").map(BarlineChoice))
				.orElse(xmlToGrouping("grouping").map(GroupingChoice))
				.orElse(xmlToLink("link").map(LinkChoice))
				.orElse(xmlToBookmark("bookmark").map(BookmarkChoice))
				.many
				.map(MusicData)
		}

		def xmlToPartGroup: XmlParser[PartGroup] = {
			complexScoreParser.xmlToPartGroup("part-group")
		}

		def xmlToScoreHeader: XmlParser[ScoreHeader] = {
			for {
				work <- xmlToWork("work").maybe
				movNum <- nodeWithName("movement-number").map(_.text).maybe
				movTitle <- nodeWithName("movement-title").map(_.text).maybe
				identification <- xmlToIdentification("identification").maybe
				defaults <- xmlToDefaults("defaults").maybe
				credit <- xmlToCredit("credit").many
				partList <- xmlToPartList("part-list")
			} yield ScoreHeader(work, movNum, movTitle, identification, defaults, credit, partList)
		}

		def xmlToScorePart: XmlParser[ScorePart] = {
			complexScoreParser.xmlToScorePart("score-part")
		}
	}
}
