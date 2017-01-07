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
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexDirection._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexNote.StyleText
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.{Editorial, Staff}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupDirection.{HarmonyChord, RootChoice, StyleTextChoice}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.HarmonyChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeDirection._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeNote._

object HarmonyMusicDataObjects {
	val harm0 = HarmonyChoice(Harmony(
		List(
			HarmonyChord(
				RootChoice(Root(
					RootStep(Step_A, Option("text for root-step")),
					Option(RootAlter(
						3.1415,
						PrintObject(Option(YN_Yes)),
						location = Option(LR_Right))))),
				Kind(
					KindValue_Major,
					Option(YN_Yes),
					Option("test text"),
					Option(YN_Yes),
					Option(YN_Yes),
					Option(YN_Yes),
					halign = HAlign(Option(LCR_Left))),
				Option(Inversion(12)),
				Option(Bass(
					BassStep(Step_C, Option("foobar")),
					Option(BassAlter(12.345, location = Option(LR_Left))))),
				List(
					Degree(
						DegreeValue(12, Option(DegreeSymbolValue_Major), Option("test-text")),
						DegreeAlter(1.23, plusMinus = Option(YN_Yes)),
						DegreeType(DegreeTypeValue_Add, Option("text for test")),
						PrintObject(Option(YN_No))),
					Degree(
						DegreeValue(23, Option(DegreeSymbolValue_Diminished)),
						DegreeAlter(2.34),
						DegreeType(DegreeTypeValue_Alter)))),
			HarmonyChord(
				RootChoice(Root(RootStep(Step_B))),
				Kind(KindValue_Minor),
				bass = Option(Bass(BassStep(Step_D)))),
			HarmonyChord(
				StyleTextChoice(StyleText("style text")),
				Kind(KindValue_Dominant),
				degree = List(Degree(
					DegreeValue(34),
					DegreeAlter(3.45),
					DegreeType(DegreeTypeValue_Subtract))))),
		Option(Frame(
			5,
			4,
			Option(FirstFret(3, Option("foobar"), Option(LR_Left))),
			List(
				FrameNote(
					StringClass(StringNumber(9), placement = Placement(Option(AB_Above))),
					Fret(2),
					Option(Fingering("foobar", Option(YN_Yes), Option(YN_Yes))),
					Option(Barre(SS_Start))),
				FrameNote(
					StringClass(StringNumber(8)),
					Fret(1))),
			halign = HAlign(Option(LCR_Left)),
			heigth = Option(10),
			width = Option(9),
			unplayed = Option("test"))),
		Option(Offset(3.1415, Option(YN_No))),
		Editorial(
			Option(FormattedText("formatted text", TextFormatting(Justify(Option(LCR_Center))))),
			Option(Level("level", Option(YN_Yes), LevelDisplay(Option(YN_No))))),
		Option(Staff(10)),
		Option(HarmonyType_Implied),
		printFrame = Option(YN_Yes)))

	val harm1 = HarmonyChoice(Harmony(
		List(HarmonyChord(
			RootChoice(Root(RootStep(Step_C))),
			Kind(KindValue_Major),
			bass = Option(Bass(BassStep(Step_E))))),
		Option(Frame(5, 4, frameNote = List(FrameNote(StringClass(StringNumber(3)), Fret(1))))),
		Option(Offset(3.14)),
		Editorial(level = Option(Level("level only")))))

	val harm2 = HarmonyChoice(Harmony(
		List(HarmonyChord(
			RootChoice(Root(RootStep(Step_C))),
			Kind(KindValue_Major),
			bass = Option(Bass(BassStep(Step_E)))))))
}
