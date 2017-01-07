/**
 * Copyright (C) 2016 Richard van Heest (richard.v.heest@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.rvanheest.sheetmusicreader.test.parser.integration

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsDirection.ImageAttributes
import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsLink.{ElementPosition, LinkAttributes}
import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsScore.GroupNameText
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLayout.Scaling
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLink.{Bookmark, Link}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexScore._
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.Editorial
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.ScoreHeader
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.{Color => PrimColor, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeNote.{AccidentalValue_Flat, AccidentalValue_Natural, AccidentalValue_Sharp}
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeScore.{GroupBarlineValue_Yes, GroupSymbolValue_Brace}

object HeaderObjects {
	val opus = LinkAttributes("#href", "simple", Option("roleOfOpus"),
		Option("titleOfOpus"), "new", "none")

	val work = Work(Option("workNumber"), Option("workTitle"), Option(opus))

	val movementNumber = "movementNumber"

	val movementTitle = "movementTitle"

	val defaults = {
		val scaling = new Scaling(6.2831, 40)
		val layout = LayoutObjects.layout
		val appearance = LayoutObjects.appearance
		val musicFont = EmptyFont(Font(
			Option(CommaSeparatedText("Font2")),
			Option(FontStyle_Normal),
			Option(FS_Double(19)),
			Option(FontWeight_Bold)))
		val wordFont = EmptyFont(Font(
			Option(CommaSeparatedText("Font3")),
			Option(FontStyle_Normal),
			Option(FS_CssFontSize(CssFontSize_Large)),
			Option(FontWeight_Bold)))
		val lyricFont = List(
			LyricFont(
				Option("5"),
				Option("test"),
				Font(
					Option(CommaSeparatedText("Font2")),
					Option(FontStyle_Normal),
					Option(FS_Double(19)),
					Option(FontWeight_Bold))),
			LyricFont())
		val lyricsLanguage = List(
			LyricLanguage(Option("num"), Option("name1"), "lang1"),
			LyricLanguage(lang = "lang2"))

		Defaults(Option(scaling), layout, Option(appearance), Option(musicFont),
			Option(wordFont), lyricFont, lyricsLanguage)
	}

	val credit1 = {
		val creditType = List("foo", "bar")
		val link = List(
			Link(
				LinkAttributes("href1", "simple"),
				Option("name"),
				ElementPosition(Option("elementX"), Option(2))),
			Link(LinkAttributes("href2", "simple")))
		val bookmark = List(
			Bookmark("bm1", Option("nm1"), ElementPosition(Option("elementY"))),
			Bookmark("bm2", Option("nm2")))
		val creditChoice = CreditImageChoice(ImageAttributes(
			source = "src",
			imgType = "imgType",
			valignImage = VAlignImage(Option(VAlignImage_Top))))
		val page = 1

		Credit(creditType, link, bookmark, creditChoice, Option(page))
	}

	val credit2 = {
		val creditType = List("foobar", "barfoo")
		val link = List(
			Link(LinkAttributes("href3", "simple")),
			Link(LinkAttributes("href4", "simple")))
		val bookmark = List(
			Bookmark("bm3"),
			Bookmark("bm4"))
		val creditChoice = CreditPairChoice(
			creditWords = FormattedText("foobar"),
			links = List(
				(List(
					Link(LinkAttributes("href5", "simple")),
					Link(LinkAttributes("href6", "simple"))),
					List(Bookmark("x")),
					FormattedText(
						"word1",
						TextFormatting(
							Justify(Option(LCR_Left)),
							PrintStyleAlign(),
							TextDecoration(Option(NumberOfLines(0)), Option(NumberOfLines(0)), Option(NumberOfLines(0))),
							TextRotation(Option(RotationDegrees(20))),
							LetterSpacing(Option(NON_Double(20))),
							LineHeight(Option(NON_Normal)),
							Option("langTest"),
							Option("default"),
							TextDirection(Option(TextDirection_LRO)),
							Enclosure(Option(EnclosureShape_Triangle))))),
				(Nil, Nil, FormattedText("word2"))))

		Credit(creditType, link, bookmark, creditChoice, Option.empty)
	}

	val credits = List(credit1, credit2)

	val partGroups = List(
		PartGroup(
			Option(GroupName("groupName",
				GroupNameText(justify = Justify(Option(LCR_Center))))),
			Option(NameDisplay(List(
				AccidentalTextNameDisplayChoice(AccidentalText(AccidentalValue_Sharp)),
				AccidentalTextNameDisplayChoice(AccidentalText(AccidentalValue_Natural)),
				FormattedTextNameDisplayChoice(FormattedText("s1")),
				AccidentalTextNameDisplayChoice(AccidentalText(AccidentalValue_Flat)),
				FormattedTextNameDisplayChoice(FormattedText("s2"))))),
			Option(GroupName("groupAbbreviation")),
			Option(NameDisplay()),
			Option(GroupSymbol(
				GroupSymbolValue_Brace,
				color = Color(Option(PrimColor("#800080"))))),
			Option(GroupBarline(
				GroupBarlineValue_Yes,
				Color(Option(PrimColor("#800080"))))),
			Option(()),
			Editorial(
				Option(FormattedText("footnote text")),
				Option(Level(
					"level text",
					Option(YN_No),
					LevelDisplay(Option(YN_Yes), Option(YN_Yes), Option(SymbolSize_Cue))))),
			SS_Start,
			"1"),
		PartGroup(
			groupName = Option(GroupName("groupName2")),
			groupAbbreviation = Option(GroupName("groupAbbreviation2")),
			ssType = SS_Start,
			number = "2"),
		PartGroup(
			ssType = SS_Stop,
			number = "3"))

	val scorePart = ScorePart(
		partName = PartName("S/A"),
		partAbbreviation = Option(PartName("S/A")),
		scoreInstrument = List(ScoreInstrument(instrumentName = "Midi_53", id = "P1-I1")),
		midi = List(
			(Option(MidiDevice("device1", Option(Midi16(5)), Option("P1-I1"))), Option.empty),
			(Option(MidiDevice("device2")),
				Option(MidiInstrument(
					midiChannel = Option(Midi16(1)),
					midiProgram = Option(Midi128(1)),
					volume = Option(Percent(80.0)),
					pan = Option(RotationDegrees(0.0)), id = "P1-I1"))),
			(Option.empty, Option(MidiInstrument(pan = Option(RotationDegrees(120)), id = "P1-I1")))),
		id = "P1")

	val partScoreChoices = List(
		PartGroupChoice(PartGroup(
			ssType = SS_Stop)),
		ScorePartChoice(ScorePart(
			partName = PartName("name p2"),
			scoreInstrument = List(
				ScoreInstrument(
					instrumentName = "Midi_54",
					instrumentChoice = Option(SoloChoice(())),
					id = "P2-I2"),
				ScoreInstrument(
					instrumentName = "Midi_54a",
					id = "P2_I3")),
			id = "P2")),
		ScorePartChoice(ScorePart(
			partName = PartName("name p3"),
			scoreInstrument = List(
				ScoreInstrument(
					instrumentName = "Midi_55",
					instrumentChoice = Option(EnsembleChoice(PositiveIntegerOrEmpty(Option(55)))),
					id = "P3-I2")),
			id = "P3")),
		PartGroupChoice(PartGroup(ssType = SS_Start)))

	val partList = PartList(partGroups, scorePart, partScoreChoices)

	val header = ScoreHeader(Option(work), Option(movementNumber), Option(movementTitle),
		Option(IdentificationObjects.identification), Option(defaults), credits, partList)
}
