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
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexDirection._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexNote.TimeModification
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.{EditorialVoiceDirection, Staff, Tuning}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupDirection.BeatUnit
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.DirectionChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeDirection._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeNote._

object DirectionMusicDataObjects {
	val direction0 = DirectionChoice(Direction(
		List(
			RehearsalChoice(List(
				FormattedText(
					"rehearsal formatted text",
					TextFormatting(Justify(Option(LCR_Center)))),
				FormattedText("reh test"))),
			RehearsalChoice(List(FormattedText("reh test2"))),
			SegnoChoice(List(
				EmptyPrintStyleAlign(PrintStyleAlign(
					halign = HAlign(Option(LCR_Right)),
					valign = VAlign(Option(VAlign_Top)))),
				EmptyPrintStyleAlign())),
			SegnoChoice(List(EmptyPrintStyleAlign())),
			WordsChoice(List(
				FormattedText("word formatted text", TextFormatting(lang = Option("english"))),
				FormattedText("test1"))),
			WordsChoice(List(FormattedText("test2"))),
			CodaChoice(List(
				EmptyPrintStyleAlign(PrintStyleAlign(halign = HAlign(Option(LCR_Left)))),
				EmptyPrintStyleAlign())),
			CodaChoice(List(EmptyPrintStyleAlign())),
			WedgeChoice(Wedge(
				WedgeType_Crescendo,
				Option(NumberLevel(1)),
				Option(10),
				Option(YN_No))),
			WedgeChoice(Wedge(
				WedgeType_Diminuendo,
				lineType = LineType(Option(LineType_Wavy)),
				dashedFormatting = DashedFormatting(Option(9.8), Option(7.6)))),
			DynamicsChoice(List(
				Dynamics(
					List(
						DynamicSymbolChoice(DynamicSymbols.f),
						DynamicSymbolChoice(DynamicSymbols.ff),
						DynamicSymbolChoice(DynamicSymbols.fff),
						DynamicStringChoice("foobar")),
					placement = Placement(Option(AB_Above)),
					enclosure = Enclosure(Option(EnclosureShape_Diamond))),
				Dynamics(List(DynamicSymbolChoice(DynamicSymbols.fp))),
				Dynamics())),
			DashesChoice(Dashes(
				SSC_Start,
				Option(NumberLevel(2)),
				DashedFormatting(Option(3)))),
			BracketChoice(Bracket(
				SSC_Start,
				Option(NumberLevel(4)),
				LineEnd_Both,
				Option(5),
				LineType(Option(LineType_Solid)))),
			PedalChoice(Pedal(
				SSCC_Start,
				Option(YN_No),
				Option(YN_Yes),
				PrintStyleAlign(valign = VAlign(Option(VAlign_Top))))),
			MetronomeChoice(Metronome(
				MetronomeBeatChoice(
					BeatUnit(NoteTypeValue_Half, List((), ())),
					MinuteChoice(PerMinute("per minute"))),
				parentheses = Option(YN_Yes))),
			MetronomeChoice(Metronome(
				MetronomeBeatChoice(
					BeatUnit(NoteTypeValue_Half),
					BeatChoice(BeatUnit(NoteTypeValue_Whole, List(())))),
				justify = Justify(Option(LCR_Center)))),
			MetronomeChoice(Metronome(
				MetronomeBeatChoice(
					BeatUnit(NoteTypeValue_Half, List(())),
					BeatChoice(BeatUnit(NoteTypeValue_Whole, List(())))))),
			MetronomeChoice(Metronome(
				MetronomeNoteChoice(
					List(
						MetronomeNote(
							NoteTypeValue_Eighth,
							List((), ()),
							List(MetronomeBeam(BeamValue_Begin, BeamLevel(2)), MetronomeBeam(BeamValue_Continue)),
							Option(MetronomeTuplet(
								TimeModification(0, 1, Option((NoteTypeValue_Whole, List((), ())))),
								SS_Start,
								Option(YN_Yes),
								Option(ShowTuplet_Both)))),
						MetronomeNote(
							NoteTypeValue_Quarter,
							List(()),
							List(MetronomeBeam(BeamValue_ForwardHook)),
							Option(MetronomeTuplet(
								TimeModification(2, 3, Option((NoteTypeValue_Whole, List()))),
								SS_Stop))),
						MetronomeNote(
							NoteTypeValue_Half,
							metronomeTuplet = Option(MetronomeTuplet(TimeModification(4, 5), SS_Stop)))),
					Option(MetronomeRelation("metr-rel", List(
						MetronomeNote(
							NoteTypeValue_Half,
							metronomeTuplet = Option(MetronomeTuplet(TimeModification(4, 5), SS_Start))),
						MetronomeNote(NoteTypeValue_Whole))))))),
			MetronomeChoice(Metronome(
				MetronomeNoteChoice(
					List(
						MetronomeNote(NoteTypeValue_Breve),
						MetronomeNote(NoteTypeValue_Long)),
					Option(MetronomeRelation("metr-rel2", List(MetronomeNote(NoteTypeValue_Maxima))))))),
			MetronomeChoice(Metronome(
				MetronomeNoteChoice(List(MetronomeNote(NoteTypeValue_th1024))))),
			OctaveShiftChoice(OctaveShift(UDSC_Up)),
			OctaveShiftChoice(OctaveShift(
				UDSC_Down,
				Option(NumberLevel(1)),
				2,
				DashedFormatting(spaceLength = Option(3)))),
			HarpPedalsChoice(HarpPedals(
				List(PedalTuning(Step_A, 1.1), PedalTuning(Step_B, 2.2)))),
			HarpPedalsChoice(HarpPedals(List(PedalTuning(Step_C, 3.3)))),
			DampChoice(EmptyPrintStyleAlign()),
			DampAllChoice(EmptyPrintStyleAlign()),
			EyeglassesChoice(EmptyPrintStyleAlign()),
			StringMuteChoice(StringMute(OO_On)),
			ScordaturaChoice(Scordatura(List(
				Accord(Tuning(Step_D, Option(4.4), Octave(4)), Option(StringNumber(1))),
				Accord(Tuning(Step_E, tuningOctave = Octave(5)), Option(StringNumber(2)))))),
			ScordaturaChoice(Scordatura(List(
				Accord(Tuning(Step_F, tuningOctave = Octave(6)), Option(StringNumber(3)))))),
			ImageChoice(ImageAttributes(
				"source URI",
				"type token",
				halign = HAlign(Option(LCR_Left)))),
			PrincipalVoiceChoice(PrincipalVoice(
				"p_voice",
				SS_Start,
				PVS_Hauptstimme,
				PrintStyleAlign(valign = VAlign(Option(VAlign_Top))))),
			AccordionRegistrationChoice(AccordionRegistration(
				Option(()),
				Option(AccordionMiddle(2)),
				Option(()),
				PrintStyleAlign(valign = VAlign(Option(VAlign_Top))))),
			AccordionRegistrationChoice(AccordionRegistration(
				printStyleAlign = PrintStyleAlign(halign = HAlign(Option(LCR_Right))))),
			PercussionChoice(List(
				Percussion(
					GlassPercussion(WindChimes),
					enclosure = Enclosure(Option(EnclosureShape_Bracket))),
				Percussion(MetalPercussion(Metal_Bell)),
				Percussion(WoodPercussion(Wood_Cabasa)),
				Percussion(PitchedPercussion(Pitched_Chimes)),
				Percussion(MembranePercussion(Membrane_BassDrumOnSide)),
				Percussion(EffectPercussion(Effect_Anvil)),
				Percussion(TimpaniPercussion(())),
				Percussion(BeaterPercussion(Beater(BeaterValue_Hammer, Option(TipDirection_Southeast)))),
				Percussion(BeaterPercussion(Beater(BeaterValue_Finger))),
				Percussion(StickPercussion(Stick(
					StickType_BassDrum,
					StickMaterial_Soft,
					Option(TipDirection_Up)))),
				Percussion(StickPercussion(Stick(StickType_Timpani, StickMaterial_Shaded))),
				Percussion(StickLocationPercussion(StickLocation_Rim)),
				Percussion(OtherPercussion("other kind of perc.")))),
			OtherDirectionChoice(OtherDirection(
				"other direction",
				PrintObject(Option(YN_Yes))))),
		Option(Offset(12.3, Option(YN_No))),
		EditorialVoiceDirection(
			Option(FormattedText("footnote")),
			Option(Level("level")),
			Option("voice")),
		Option(Staff(4)),
		Option(Sound(
			List(
				MidiProps(Option(MidiDevice("device", Option(Midi16(8)), Option("P1-I1"))),
					Option(MidiInstrument(id = "P1-I1")),
					Option(Play(List(
						StringChoice("ipa1"),
						OtherPlayChoice(OtherPlay("other", "token1")),
						MuteChoice(Mute_SoloTone),
						StringChoice("ipa2"),
						SemiPitchedChoice(SemiPitched_MediumHigh)),
						Option("P1-I1")))),
				MidiProps(Option.empty,
					Option(MidiInstrument(id = "P1-I1")),
					Option.empty),
				MidiProps(Option(MidiDevice("device2")),
					Option.empty,
					Option(Play()))),
			Option(Offset(10.5, Option(YN_Yes))),
			Option(NonNegativeDecimal(1.2)),
			Option(NonNegativeDecimal(2.3)),
			Option(YN_Yes),
			Option("abc"),
			Option("def"),
			Option("ghi"),
			Option("jkl"),
			Option(3.4),
			Option(YN_No),
			Option("mno"),
			Option(TimeOnly("5")),
			Option(YN_Yes),
			Option(RotationDegrees(-72.0)),
			Option(RotationDegrees(72.0)),
			Option(YNN_YesNo(YN_Yes)),
			Option(YNN_Double(5.0)),
			Option(YNN_YesNo(YN_No))))))

	val direction1 = DirectionChoice(Direction(
		List(CodaChoice(List(EmptyPrintStyleAlign()))),
		sound = Option(Sound(offset = Option(Offset(5.25))))))

	val direction2 = DirectionChoice(Direction(
		List(CodaChoice(List(EmptyPrintStyleAlign()))),
		placement = Placement(Option(AB_Above)),
		directive = Directive(Option(YN_Yes))))
}
