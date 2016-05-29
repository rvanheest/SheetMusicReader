package nl.rvanheest.sheetmusicreader.parser

trait GroupParser extends ComplexParser {

	trait GroupAttributesParser extends ComplexAttributesParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupAttributes._

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

	trait GroupCommonParser extends ComplexCommonParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon._

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
			xmlToLevel("level")
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

	trait GroupDirectionParser extends ComplexDirectionParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupDirection._

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

	trait GroupLayoutParser extends ComplexLayoutParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupLayout._

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

	trait GroupNoteParser extends ComplexNoteParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupNote._

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

	trait GroupScoreParser extends GroupAttributesParser with
																 ComplexBarlineParser with
																 ComplexScoreParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore._

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
			xmlToPartGroup("part-group")
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
			xmlToScorePart("score-part")
		}
	}
}
