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
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexAttributes.{Directive, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.{FormattedText, Level}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupAttributes.{NonTraditionalKey, TimeSignature, TraditionalKey}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.{Editorial, Tuning}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.AttributesChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeAttributes._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.{Color => PrimColor, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeNote._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeScore.GroupSymbolValue_Line

object AttributeMusicDataObjects {
	val attr0 = AttributesChoice(Attributes(
		Editorial(
			Option(FormattedText("footnote text")),
			Option(Level(
				"level text",
				Option(YN_Yes),
				LevelDisplay(Option(YN_Yes), Option(YN_Yes), Option(SymbolSize_Full))))),
		Option(PositiveDivisions(8)),
		List(
			Key(
				TraditionalKeyChoice(TraditionalKey(
					Option(Cancel(4, Option(CancelLocation_Left))),
					4,
					Option("test mode"))),
				List(
					KeyOctave(Octave(9), 1),
					KeyOctave(Octave(8), 2),
					KeyOctave(Octave(7), 3, Option(YN_Yes))),
				Option(StaffNumber(1))),
			Key(
				TraditionalKeyChoice(TraditionalKey(fifths = 5)),
				List(KeyOctave(Octave(6), 4)),
				Option(StaffNumber(3)),
				printObject = PrintObject(Option(YN_No))),
			Key(
				NonTraditionalKeysChoice(
					List(
						NonTraditionalKey(Step_A, 1.0),
						NonTraditionalKey(Step_B, 2.0, Option(AccidentalValue_Sharp)),
						NonTraditionalKey(Step_C, 3.0))),
				List(
					KeyOctave(Octave(5), 5),
					KeyOctave(Octave(4), 6))),
			Key(keyOctave = List(KeyOctave(Octave(3), 7), KeyOctave(Octave(2), 8))),
			Key()),
		List(
			Time(
				SignatureTimeChoice(
					List(TimeSignature("beats test", "beat type test")),
					Option(Interchangeable(
						Option(TimeRelation_Bracket),
						List(TimeSignature("beats", "beat type")),
						separator = Option(TimeSeparator_Adjacent)))),
				number = Option(StaffNumber(1)),
				symbol = Option(TimeSymbol_Cut)),
			Time(
				SignatureTimeChoice(List(
					TimeSignature("b1", "bt1"),
					TimeSignature("b2", "bt2"),
					TimeSignature("b3", "bt3"))),
				number = Option(StaffNumber(2)),
				separator = Option(TimeSeparator_Diagonal)),
			Time(
				SignatureTimeChoice(
					List(TimeSignature("b", "bt")),
					Option(Interchangeable(
						timeSignature = List(TimeSignature("b1", "bt1"), TimeSignature("b2", "bt2")),
						symbol = Option(TimeSymbol_Note), separator = Option(TimeSeparator_None)))),
				number = Option(StaffNumber(1)),
				symbol = Option(TimeSymbol_Cut)),
			Time(
				SenzaMisuraTimeChoice("senza-misura test"),
				separator = Option(TimeSeparator_None),
				printObject = PrintObject(Option(YN_Yes)))),
		Option(20),
		Option(PartSymbol(GroupSymbolValue_Line, Option(StaffNumber(20)), Option(StaffNumber(30)))),
		Option(15),
		List(
			Clef(
				ClefSign_G,
				Option(2),
				Option(-1),
				Option(StaffNumber(1)),
				Option(YN_Yes),
				Option(SymbolSize_Large),
				Option(YN_Yes)),
			Clef(ClefSign_F, printObject = PrintObject(Option(YN_No)))),
		List(
			StaffDetails(
				Option(StaffType_Ossia),
				Option(12),
				List(
					StaffTuning(Tuning(Step_A, Option(21), Octave(3)), Option(13)),
					StaffTuning(Tuning(Step_B, Option.empty, Octave(4)), Option(14))),
				Option(15),
				Option(NonNegativeDecimal(16.7))),
			StaffDetails(
				number = Option(StaffNumber(18)),
				showFrets = Option(ShowFrets_Letters),
				printObject = PrintObject(Option(YN_No)),
				printSpacing = PrintSpacing(Option(YN_Yes))),
			StaffDetails()),
		List(
			Transpose(Option(12), 13, Option(15), Option(())),
			Transpose(chromatic = 14, number = Option(StaffNumber(20)))),
		List(
			Directive(
				"dir1",
				printStyle = PrintStyle(color = Color(Option(PrimColor("#40800080")))),
				lang = Option("test123")),
			Directive("dir2")),
		List(
			MeasureStyle(
				MultipleRestChoice(MultipleRest(PositiveIntegerOrEmpty(Option(12)), Option(YN_Yes))),
				Option(StaffNumber(1))),
			MeasureStyle(
				MultipleRestChoice(MultipleRest()),
				number = Option(StaffNumber(2))),
			MeasureStyle(
				MeasureRepeatChoice(MeasureRepeat(PositiveIntegerOrEmpty(Option(14)), SS_Start)),
				color = Color(Option(PrimColor("#40800080")))),
			MeasureStyle(
				MeasureRepeatChoice(MeasureRepeat(ssType = SS_Stop))),
			MeasureStyle(
				BeatRepeatChoice(BeatRepeat(
					Option(Group.GroupAttributes.Slash(NoteTypeValue_Maxima, List((), ()))),
					SS_Start,
					Option(15))),
				Option(StaffNumber(3))),
			MeasureStyle(
				BeatRepeatChoice(BeatRepeat(
					Option(Group.GroupAttributes.Slash(NoteTypeValue_Half)),
					SS_Start,
					useDots = Option(YN_No))),
				Option(StaffNumber(4))),
			MeasureStyle(
				BeatRepeatChoice(BeatRepeat(ssType = SS_Start)),
				Option(StaffNumber(5))),
			MeasureStyle(
				SlashChoice(Slash(
					Option(Group.GroupAttributes.Slash(NoteTypeValue_th16, List(()))),
					SS_Start,
					useStems = Option(YN_No)))))))

	val attr1 = AttributesChoice()
}
