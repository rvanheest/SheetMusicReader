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
package nl.rvanheest.sheetmusicreader.musicxml.model

import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeNote.Octave

object Group {

	object GroupAttributes {

		import Complex.ComplexAttributes.Cancel
		import Complex.ComplexCommon.Empty
		import Primatives.PrimativeAttributes.{Fifths, Mode}
		import Primatives.PrimativeNote.{AccidentalValue, NoteTypeValue, Semitones, Step}

		/**
			* The non-traditional-key group represents a single alteration within a non-traditional key
			* signature. A sequence of these groups makes up a non-traditional key signature
			*
			* Non-traditional key signatures can be represented using the Humdrum/Scot concept of a list of
			* altered tones. The key-step element indicates the pitch step to be altered, represented using
			* the same names as in the step element.
			*
			* Non-traditional key signatures can be represented using the Humdrum/Scot concept of a list of
			* altered tones. The key-alter element represents the alteration for a given pitch step, represented
			* with semitones in the same manner as the alter element.
			*
			* Non-traditional key signatures can be represented using the Humdrum/Scot concept of a list of
			* altered tones. The key-accidental element indicates the accidental to be displayed in the key
			* signature, represented in the same manner as the accidental element. It is used for disambiguating
			* microtonal accidentals.
			*/
		case class NonTraditionalKey(keyStep: Step,
																 keyAlter: Semitones,
																 keyAccidental: Option[AccidentalValue] = Option.empty)

		/**
			* The slash group combines elements used for more complete specification of the slash and beat-repeat
			* measure-style elements. They have the same values as the type and dot elements, and define what the
			* beat is for the display of repetition marks. If not present, the beat is based on the current time
			* signature.
			*
			* The slash-type element indicates the graphical note type to use for the display of repetition marks.
			*
			* The slash-dot element is used to specify any augmentation dots in the note type used to display
			* repetition marks.
			*/
		case class Slash(slashType: NoteTypeValue, slashDot: List[Empty] = List())

		/**
			* Time signatures are represented by the beats element for the numerator and the beat-type element
			* for the denominator.
			*
			* The beats element indicates the number of beats, as found in the numerator of a time signature.
			*
			* The beat-type element indicates the beat unit, as found in the denominator of a time signature.
			*/
		case class TimeSignature(beats: String, beatType: String)

		/**
			* The traditional-key group represents a traditional key signature using the cycle of fifths.
			*/
		case class TraditionalKey(cancel: Option[Cancel] = Option.empty,
															fifths: Fifths,
															mode: Option[Mode] = Option.empty)

	}

	object GroupCommon {

		import Complex.ComplexCommon.FormattedText
		import Primatives.PrimativeNote.{Semitones, Step}

		/**
			* The editorial group specifies editorial information for a musical element.
			*/
		case class Editorial(footNote: Option[FootNote] = Option.empty,
												 level: Option[Level] = Option.empty)

		/**
			* The editorial-voice group supports the common combination of editorial and voice information
			* for a musical element.
			*/
		case class EditorialVoice(footnote: Option[FootNote] = Option.empty,
															level: Option[Level] = Option.empty,
															voice: Option[Voice] = Option.empty)

		/**
			* The editorial-voice-direction group supports the common combination of editorial and voice
			* information for a direction element. It is separate from the editorial-voice element because
			* extensions and restrictions might be different for directions than for the note and forward
			* elements.
			*/
		case class EditorialVoiceDirection(footnote: Option[FootNote] = Option.empty,
																			 level: Option[Level] = Option.empty,
																			 voice: Option[Voice] = Option.empty)

		/**
			* The footnote element specifies editorial information that appears in footnotes in the printed
			* score. It is defined within a group due to its multiple uses within the MusicXML schema.
			*/
		type FootNote = FormattedText

		/**
			* The level element specifies editorial information for different MusicXML elements. It is defined
			* within a group due to its multiple uses within the MusicXML schema.
			*/
		type Level = Complex.ComplexCommon.Level

		/**
			* The staff element is defined within a group due to its use by both notes and direction elements.
			*
			* Staff assignment is only needed for music notated on multiple staves. Used by both notes and
			* directions. Staff values are numbers, with 1 referring to the top-most staff in a part.
			*/
		case class Staff(staff: Int) {
			require(staff > 0)
		}

		/**
			* The tuning group contains the sequence of elements common to the staff-tuning and accord elements.
			*
			* The tuning-step element is represented like the step element, with a different name to reflect
			* is different function.
			*
			* The tuning-alter element is represented like the alter element, with a different name to reflect
			* is different function.
			*
			* The tuning-octave element is represented like the octave element, with a different name to reflect
			* is different function.
			*/
		case class Tuning(tuningStep: Step,
											tuningAlter: Option[Semitones] = Option.empty,
											tuningOctave: Octave)

		/**
			* The voice is used to distinguish between multiple voices (what MuseData calls tracks) in individual
			* parts. It is defined within a group due to its multiple uses within the MusicXML schema.
			*/
		type Voice = String
	}

	object GroupDirection {

		import Complex.ComplexCommon.Empty
		import Complex.ComplexDirection._
		import Complex.ComplexNote.StyleText
		import Primatives.PrimativeNote.NoteTypeValue

		/**
			* The beat-unit group combines elements used repeatedly in the metronome element to specify
			* a note within a metronome mark.
			*
			* The beat-unit element indicates the graphical note type to use in a metronome mark.
			*
			* The beat-unit-dot element is used to specify any augmentation dots for a metronome mark note.
			*/
		case class BeatUnit(beatUnit: NoteTypeValue, beatUnitDot: List[Empty] = List())

		sealed abstract class HarmonyChordChoice
		case class RootChoice(root: Root) extends HarmonyChordChoice
		case class StyleTextChoice(styleText: StyleText) extends HarmonyChordChoice

		/**
			* A harmony element can contain many stacked chords (e.g. V of II). A sequence of harmony-chord
			* groups is used for this type of secondary function, where V of II would be represented by a
			* harmony-chord with a V function followed by a harmony-chord with a II function.
			*
			* A root is a pitch name like C, D, E, where a function is an indication like I, II, III.
			* It is an either/or choice to avoid data inconsistency.
			*
			* The function element is used to represent classical functional harmony with an indication
			* like I, II, III rather than C, D, E. It is relative to the key that is specified in the MusicXML
			* encoding.
			*/
		case class HarmonyChord(rootOrFunction: HarmonyChordChoice,
														kind: Kind,
														inversion: Option[Inversion] = Option.empty,
														bass: Option[Bass] = Option.empty,
														degree: List[Degree] = List())
	}

	object GroupLayout {

		import Complex.ComplexLayout.{PageLayout, StaffLayout, SystemLayout}
		import Primatives.PrimativeCommon.Tenths

		/**
			* The all-margins group specifies both horizontal and vertical margins in tenths.
			*/
		case class AllMargins(lrm: LeftRightMargins, topMargin: Tenths, bottomMargin: Tenths)

		/**
			* The layout group specifies the sequence of page, system, and staff layout elements that is
			* common to both the defaults and print elements.
			*/
		case class Layout(pageLayout: Option[PageLayout] = Option.empty,
											systemLayout: Option[SystemLayout] = Option.empty,
											staffLayout: List[StaffLayout] = List())

		/**
			* The left-right-margins group specifies horizontal margins in tenths.
			*/
		case class LeftRightMargins(left: Tenths, right: Tenths)
	}

	object GroupNote {

		import Complex.ComplexCommon.Empty
		import Complex.ComplexNote.{Pitch, Rest, Unpitched}
		import Primatives.PrimativeCommon.PositiveDivisions
		import Primatives.PrimativeNote.{Octave, Step}

		/**
			* The duration element is defined within a group due to its uses within the note, figure-bass,
			* backup, and forward elements.
			*
			* Duration is a positive number specified in division units. This is the intended duration
			* vs. notated duration (for instance, swing eighths vs. even eighths, or differences in dotted notes
			* in Baroque-era music). Differences in duration specific to an interpretation or performance should
			* use the note element's attack and release attributes.
			*/
		type Duration = PositiveDivisions

		/**
			* The display-step-octave group contains the sequence of elements used by both the rest and
			* unpitched elements. This group is used to place rests and unpitched elements on the staff
			* without implying that these elements have pitch. Positioning follows the current clef. If
			* percussion clef is used, the display-step and display-octave elements are interpreted as if in
			* treble clef, with a G in octave 4 on line 2. If not present, the note is placed on the middle
			* line of the staff, generally used for a one-line staff.
			*/
		case class DisplayStepOctave(displayStep: Step, displayOctave: Octave)

		sealed abstract class FullNoteChoice
		case class PitchChoice(pitch: Pitch) extends FullNoteChoice
		case class UnpitchedChoice(unpitched: Unpitched) extends FullNoteChoice
		case class RestChoice(rest: Rest) extends FullNoteChoice

		/**
			* The full-note group is a sequence of the common note elements between cue/grace notes and
			* regular (full) notes: pitch, chord, and rest information, but not duration (cue and grace notes
			* do not have duration encoded). Unpitched elements are used for unpitched percussion,
			* speaking voice, and other musical elements lacking determinate pitch.
			*
			* The chord element indicates that this note is an additional chord tone with the preceding note.
			* The duration of this note can be no longer than the preceding note. In MuseData, a missing
			* duration indicates the same length as the previous note, but the MusicXML format requires a
			* duration for chord notes too.
			*/
		case class FullNote(chord: Option[Empty] = Option.empty, choice: FullNoteChoice)
	}

	object GroupScore {

		import Complex.ComplexAttributes.Attributes
		import Complex.ComplexBarline.Barline
		import Complex.ComplexDirection._
		import Complex.ComplexIdentity.Identification
		import Complex.ComplexLink.{Bookmark, Link}
		import Complex.ComplexNote.{Backup, FiguredBass, Forward, Note}
		import Complex.ComplexScore.{Credit, Defaults, PartList, Work}


		sealed abstract class MusicDataChoice
		case class NoteChoice(note: Note) extends MusicDataChoice
		case class BackupChoice(backup: Backup) extends MusicDataChoice
		case class ForwardChoice(forward: Forward) extends MusicDataChoice
		case class DirectionChoice(direction: Direction) extends MusicDataChoice
		case class AttributesChoice(attributes: Attributes = Attributes()) extends MusicDataChoice
		case class HarmonyChoice(harmony: Harmony) extends MusicDataChoice
		case class FiguredBassChoice(bass: FiguredBass) extends MusicDataChoice
		case class PrintChoice(print: Print = Print()) extends MusicDataChoice
		case class SoundChoice(sound: Sound = Sound()) extends MusicDataChoice
		case class BarlineChoice(barline: Barline = Barline()) extends MusicDataChoice
		case class GroupingChoice(grouping: Grouping) extends MusicDataChoice
		case class LinkChoice(link: Link) extends MusicDataChoice
		case class BookmarkChoice(bookmark: Bookmark) extends MusicDataChoice

		/**
			* The music-data group contains the basic musical data that is either associated with a part
			* or a measure, depending on whether the partwise or timewise hierarchy is used.
			*/
		case class MusicData(data: List[MusicDataChoice] = List())

		/**
			* The part-group element is defined within a group due to its multiple uses within the
			* part-list element.
			*/
		type PartGroup = Complex.ComplexScore.PartGroup

		/**
			* The score-header group contains basic score metadata about the work and movement,
			* score-wide defaults for layout and fonts, credits that appear on the first or following pages,
			* and the part list.
			*
			* The movement-number element specifies the number of a movement.
			*
			* The movement-title element specifies the title of a movement, not including its number.
			*/
		case class ScoreHeader(work: Option[Work] = Option.empty,
													 movementNumber: Option[String] = Option.empty,
													 movementTitle: Option[String] = Option.empty,
													 identification: Option[Identification] = Option.empty,
													 defaults: Option[Defaults] = Option.empty,
													 credit: List[Credit] = List(),
													 partList: PartList)

		/**
			* The score-part element is defined within a group due to its multiple uses within the
			* part-list element.
			*
			* Each MusicXML part corresponds to a track in a Standard MIDI Format 1 file.
			* The score-instrument elements are used when there are multiple instruments per track.
			* The midi-device element is used to make a MIDI device or port assignment for the given track.
			* Initial midi-instrument assignments may be made here as well.
			*/
		type ScorePart = Complex.ComplexScore.ScorePart
	}
}
