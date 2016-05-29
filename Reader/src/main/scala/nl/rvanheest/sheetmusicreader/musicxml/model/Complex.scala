package nl.rvanheest.sheetmusicreader.musicxml.model

object Complex {

	object ComplexAttributes {

		import AttributeGroups.AttributeGroupsCommon.{Color, _}
		import Complex.ComplexCommon.Empty
		import Group.GroupAttributes.{NonTraditionalKey, TimeSignature, TraditionalKey}
		import Group.GroupCommon.{Editorial, Tuning}
		import Primatives.PrimativeAttributes._
		import Primatives.PrimativeCommon._
		import Primatives.PrimativeNote.{Octave, Semitones}
		import Primatives.PrimativeScore.GroupSymbolValue

		/**
			* Directives are like directions, but can be grouped together with attributes for convenience.
			* This is typically used for tempo markings at the beginning of a piece of music. This element has
			* been deprecated in Version 2.0 in favor of the directive attribute for direction elements.
			* Language names come from ISO 639, with optional country subcodes from ISO 3166.
			*/
		case class Directive(dir: String,
												 printStyle: PrintStyle = PrintStyle(),
												 lang: Option[String] = Option.empty)

		/**
			* The attributes element contains musical information that typically changes on measure boundaries.
			* This includes key and time signatures, clefs, transpositions, and staving. When attributes are
			* changed mid-measure, it affects the music in score order, not in MusicXML document order.
			*
			* Musical notation duration is commonly represented as fractions. The divisions element indicates
			* how many divisions per quarter note are used to indicate a note's duration. For example, if
			* duration = 1 and divisions = 2, this is an eighth note duration. Duration and divisions are used
			* directly for generating sound output, so they must be chosen to take tuplets into account.
			* Using a divisions element lets us use just one number to represent a duration for each note in
			* the score, while retaining the full power of a fractional representation. If maximum
			* compatibility with Standard MIDI 1.0 files is important, do not have the divisions value
			* exceed 16383.
			*
			* The key element represents a key signature. Both traditional and non-traditional key signatures
			* are supported. The optional number attribute refers to staff numbers. If absent, the key
			* signature applies to all staves in the part.
			*
			* Time signatures are represented by the beats element for the numerator and the beat-type element
			* for the denominator.
			*
			* The staves element is used if there is more than one staff represented in the given part
			* (e.g., 2 staves for typical piano parts). If absent, a value of 1 is assumed. Staves are ordered
			* from top to bottom in a part in numerical order, with staff 1 above staff 2.
			*
			* The part-symbol element indicates how a symbol for a multi-staff part is indicated in the score.
			*
			* The instruments element is only used if more than one instrument is represented in the part
			* (e.g., oboe I and II where they play together most of the time). If absent, a value of 1
			* is assumed.
			*
			* Clefs are represented by a combination of sign, line, and clef-octave-change elements.
			*
			* The staff-details element is used to indicate different types of staves.
			*
			* If the part is being encoded for a transposing instrument in written vs. concert pitch,
			* the transposition must be encoded in the transpose element using the transpose type.
			*
			* Directives are like directions, but can be grouped together with attributes for convenience.
			* This is typically used for tempo markings at the beginning of a piece of music.
			* This element has been deprecated in Version 2.0 in favor of the directive attribute for
			* direction elements. Language names come from ISO 639, with optional country subcodes
			* from ISO 3166.
			*
			* A measure-style indicates a special way to print partial to multiple measures within a part.
			* This includes multiple rests over several measures, repeats of beats, single, or multiple
			* measures, and use of slash notation.
			*/
		case class Attributes(editorial: Editorial = Editorial(),
													divisions: Option[PositiveDivisions] = Option.empty,
													key: List[Key] = List(),
													time: List[Time] = List(),
													staves: Option[Int] = Option.empty,
													partSymbol: Option[PartSymbol] = Option.empty,
													instruments: Option[Int] = Option.empty,
													clef: List[Clef] = List(),
													staffDetails: List[StaffDetails] = List(),
													transpose: List[Transpose] = List(),
													directive: List[Directive] = List(),
													measureStyle: List[MeasureStyle] = List()) {
			require(staves forall (_ >= 0))
			require(instruments forall (_ >= 0))
		}

		/**
			* The beat-repeat type is used to indicate that a single beat (but possibly many notes) is repeated.
			* Both the start and stop of the beat being repeated should be specified. The slashes attribute
			* specifies the number of slashes to use in the symbol. The use-dots attribute indicates whether
			* or not to use dots as well (for instance, with mixed rhythm patterns). By default, the value for
			* slashes is 1 and the value for use-dots is no.
			*
			* The beat-repeat element specifies a notation style for repetitions. The actual music being repeated
			* needs to be repeated within the MusicXML file. This element specifies the notation that indicates
			* the repeat.
			*/
		case class BeatRepeat(slash: Option[Group.GroupAttributes.Slash] = Option.empty,
													ssType: StartStop,
													slashes: Option[Int] = Option.empty,
													useDots: Option[YesNo] = Option.empty) {
			require(slashes forall (_ >= 1))
		}

		/**
			* A cancel element indicates that the old key signature should be cancelled before the new one
			* appears. This will always happen when changing to C major or A minor and need not be specified
			* then. The cancel value matches the fifths value of the cancelled key signature (e.g., a cancel
			* of -2 will provide an explicit cancellation for changing from B flat major to F major).
			* The optional location attribute indicates whether the cancellation appears relative to the new
			* key signature.
			*/
		case class Cancel(cancel: Fifths, location: Option[CancelLocation] = Option.empty)

		/**
			* Clefs are represented by a combination of sign, line, and clef-octave-change elements.
			* The optional number attribute refers to staff numbers within the part. A value of 1 is assumed
			* if not present.
			*
			* Sometimes clefs are added to the staff in non-standard line positions, either to indicate cue
			* passages, or when there are multiple clefs present simultaneously on one staff.
			* In this situation, the additional attribute is set to "yes" and the line value is ignored.
			* The size attribute is used for clefs where the additional attribute is "yes". It is typically
			* used to indicate cue clefs.
			*
			* Sometimes clefs at the start of a measure need to appear after the barline rather than before,
			* as for cues or for use after a repeated section. The after-barline attribute is set to "yes" in
			* this situation. The attribute is ignored for mid-measure clefs.
			*
			* Clefs appear at the start of each system unless the print-object attribute has been set to "no"
			* or the additional attribute has been set to "yes".
			*
			* The sign element represents the clef symbol.
			*
			* Line numbers are counted from the bottom of the staff. Standard values are 2 for the G sign
			* (treble clef), 4 for the F sign (bass clef), 3 for the C sign (alto clef) and 5 for TAB
			* (on a 6-line staff).
			*
			* The clef-octave-change element is used for transposing clefs. A treble clef for tenors would
			* have a value of -1.
			*/
		case class Clef(sign: ClefSign, line: Option[StaffLine] = Option.empty,
										clefOctaveChange: Option[Int] = Option.empty,
										number: Option[StaffNumber] = Option.empty,
										additional: Option[YesNo] = Option.empty,
										size: Option[SymbolSize] = Option.empty,
										afterBarline: Option[YesNo] = Option.empty,
										printStyle: PrintStyle = PrintStyle(),
										printObject: PrintObject = PrintObject())

		/**
			* The interchangeable type is used to represent the second in a pair of interchangeable dual time
			* signatures, such as the 6/8 in 3/4 (6/8). A separate symbol attribute value is available
			* compared to the time element's symbol attribute, which applies to the first of the dual time
			* signatures. The parentheses attribute value is yes by default.
			*/
		case class Interchangeable(timeRelation: Option[TimeRelation] = Option.empty,
															 timeSignature: List[TimeSignature],
															 symbol: Option[TimeSymbol] = Option.empty,
															 separator: Option[TimeSeparator] = Option.empty) {
			require(timeSignature.nonEmpty)
		}

		sealed abstract class KeyChoice
		case class TraditionalKeyChoice(traditional: TraditionalKey) extends KeyChoice
		case class NonTraditionalKeysChoice(nonTraditionals: List[NonTraditionalKey] = List()) extends KeyChoice

		/**
			* The key type represents a key signature. Both traditional and non-traditional key signatures are
			* supported. The optional number attribute refers to staff numbers. If absent, the key signature
			* applies to all staves in the part. Key signatures appear at the start of each system unless the
			* print-object attribute has been set to "no".
			*
			* The optional list of key-octave elements is used to specify in which octave each element of the
			* key signature appears.
			*/
		case class Key(key: KeyChoice = NonTraditionalKeysChoice(),
									 keyOctave: List[KeyOctave] = List(),
									 number: Option[StaffNumber] = Option.empty,
									 printStyle: PrintStyle = PrintStyle(),
									 printObject: PrintObject = PrintObject())

		/**
			* The key-octave element specifies in which octave an element of a key signature appears.
			* The content specifies the octave value using the same values as the display-octave element.
			* The number attribute is a positive integer that refers to the key signature element in
			* left-to-right order. If the cancel attribute is set to yes, then this number refers to an
			* element specified by the cancel element. It is no by default.
			*/
		case class KeyOctave(octave: Octave, number: Int, cancel: Option[YesNo] = Option.empty) {
			require(number >= 1)
		}

		/**
			* The measure-repeat type is used for both single and multiple measure repeats. The text of the
			* element indicates the number of measures to be repeated in a single pattern. The slashes
			* attribute specifies the number of slashes to use in the repeat sign. It is 1 if not specified.
			* Both the start and the stop of the measure-repeat must be specified. The text of the element
			* is ignored when the type is stop.
			*
			* The measure-repeat element specifies a notation style for repetitions. The actual music being
			* repeated needs to be repeated within the MusicXML file. This element specifies the notation
			* that indicates the repeat.
			*/
		case class MeasureRepeat(posIntOrEmpty: PositiveIntegerOrEmpty = PositiveIntegerOrEmpty(),
														 ssType: StartStop,
														 slashes: Option[Int] = Option.empty) {
			require(slashes forall (_ >= 1))
		}

		sealed abstract class MeasureStyleChoice
		case class MultipleRestChoice(multipleRest: MultipleRest) extends MeasureStyleChoice
		case class MeasureRepeatChoice(measureRepeat: MeasureRepeat) extends MeasureStyleChoice
		case class BeatRepeatChoice(beatRepeat: BeatRepeat) extends MeasureStyleChoice
		case class SlashChoice(slash: Slash) extends MeasureStyleChoice

		/**
			* A measure-style indicates a special way to print partial to multiple measures within a part.
			* This includes multiple rests over several measures, repeats of beats, single, or multiple
			* measures, and use of slash notation.
			*
			* The multiple-rest and measure-repeat symbols indicate the number of measures covered in the
			* element content. The beat-repeat and slash elements can cover partial measures. All but the
			* multiple-rest element use a type attribute to indicate starting and stopping the use of the
			* style. The optional number attribute specifies the staff number from top to bottom on the
			* system, as with clef.
			*/
		case class MeasureStyle(choice: MeasureStyleChoice,
														number: Option[StaffNumber] = Option.empty,
														font: Font = Font(),
														color: Color = Color())

		/**
			* The text of the multiple-rest type indicates the number of measures in the multiple rest.
			* Multiple rests may use the 1-bar / 2-bar / 4-bar rest symbols, or a single shape.
			* The use-symbols attribute indicates which to use; it is no if not specified. The element text
			* is ignored when the type is stop.
			*/
		case class MultipleRest(rest: PositiveIntegerOrEmpty = PositiveIntegerOrEmpty(),
														useSymbols: Option[YesNo] = Option.empty)

		/**
			* The part-symbol type indicates how a symbol for a multi-staff part is indicated in the score;
			* brace is the default value. The top-staff and bottom-staff elements are used when the brace
			* does not extend across the entire part. For example, in a 3-staff organ part, the top-staff
			* will typically be 1 for the right hand, while the bottom-staff will typically be 2 for the
			* left hand. Staff 3 for the pedals is usually outside the brace.
			*/
		case class PartSymbol(symbol: GroupSymbolValue,
													topStaff: Option[StaffNumber] = Option.empty,
													bottomStaff: Option[StaffNumber] = Option.empty,
													position: Position = Position(),
													color: Color = Color())

		/**
			* The slash type is used to indicate that slash notation is to be used. If the slash is on every
			* beat, use-stems is no (the default). To indicate rhythms but not pitches, use-stems is set to
			* yes. The type attribute indicates whether this is the start or stop of a slash notation style.
			* The use-dots attribute works as for the beat-repeat element, and only has effect if use-stems
			* is no.
			*/
		case class Slash(slash: Option[Group.GroupAttributes.Slash] = Option.empty,
										 ssType: StartStop,
										 useDots: Option[YesNo] = Option.empty,
										 useStems: Option[YesNo] = Option.empty)

		/**
			* The staff-details element is used to indicate different types of staves. The optional number
			* attribute specifies the staff number from top to bottom on the system, as with clef.
			* The print-object attribute is used to indicate when a staff is not printed in a part,
			* usually in large scores where empty parts are omitted. It is yes by default. If print-spacing
			* is yes while print-object is no, the score is printed in cutaway format where vertical space
			* is left for the empty part.
			*
			* The staff-lines element specifies the number of lines for a non 5-line staff.
			*
			* The capo element indicates at which fret a capo should be placed on a fretted instrument.
			* This changes the open tuning of the strings specified by staff-tuning by the specified number
			* of half-steps.
			*
			* The staff-size element indicates how large a staff space is on this staff, expressed as a
			* percentage of the work's default scaling. Values less than 100 make the staff space smaller
			* while values over 100 make the staff space larger. A staff-type of cue, ossia, or editorial
			* implies a staff-size of less than 100, but the exact value is implementation-dependent unless
			* specified here. Staff size affects staff height only, not the relationship of the staff to the
			* left and right margins.
			*/
		case class StaffDetails(staffType: Option[StaffType] = Option.empty,
														staffLines: Option[Int] = Option.empty,
														staffTuning: List[StaffTuning] = List(),
														capo: Option[Int] = Option.empty,
														staffSize: Option[NonNegativeDecimal] = Option.empty,
														number: Option[StaffNumber] = Option.empty,
														showFrets: Option[ShowFrets] = Option.empty,
														printObject: PrintObject = PrintObject(),
														printSpacing: PrintSpacing = PrintSpacing()) {
			require(staffLines forall (_ >= 0))
			require(capo forall (_ >= 0))
		}

		/**
			* The staff-tuning type specifies the open, non-capo tuning of the lines on a tablature staff.
			*/
		case class StaffTuning(tuning: Tuning, line: Option[StaffLine] = Option.empty)

		sealed abstract class TimeChoice
		case class SignatureTimeChoice(timeSignature: List[TimeSignature],
																	 interchangeable: Option[Interchangeable] = Option.empty) extends TimeChoice {
			require(timeSignature.nonEmpty)
		}
		case class SenzaMisuraTimeChoice(s: String) extends TimeChoice

		/**
			* Time signatures are represented by the beats element for the numerator and the beat-type element
			* for the denominator. The symbol attribute is used indicate common and cut time symbols as well as
			* a single number display. Multiple pairs of beat and beat-type elements are used for composite
			* time signatures with multiple denominators, such as 2/4 + 3/8. A composite such as 3+2/8
			* requires only one beat/beat-type pair.
			*
			* The print-object attribute allows a time signature to be specified but not printed, as is the
			* case for excerpts from the middle of a score. The value is "yes" if not present. The optional
			* number attribute refers to staff numbers within the part. If absent, the time signature applies
			* to all staves in the part.
			*
			* A senza-misura element explicitly indicates that no time signature is present. The optional
			* element content indicates the symbol to be used, if any, such as an X. The time element's symbol
			* attribute is not used when a senza-misura element is present.
			*/
		case class Time(timeChoice: TimeChoice,
										number: Option[StaffNumber] = Option.empty,
										symbol: Option[TimeSymbol] = Option.empty,
										separator: Option[TimeSeparator] = Option.empty,
										printStyleAlign: PrintStyleAlign = PrintStyleAlign(),
										printObject: PrintObject = PrintObject())

		/**
			* The transpose type represents what must be added to a written pitch to get a correct sounding
			* pitch. The optional number attribute refers to staff numbers, from top to bottom on the system.
			* If absent, the transposition applies to all staves in the part. Per-staff transposition is
			* most often used in parts that represent multiple instruments.
			*
			* The diatonic element specifies the number of pitch steps needed to go from written to sounding
			* pitch. This allows for correct spelling of enharmonic transpositions.
			*
			* The chromatic element represents the number of semitones needed to get from written to sounding
			* pitch. This value does not include octave-change values; the values for both elements need to
			* be added to the written pitch to get the correct sounding pitch.
			*
			* The octave-change element indicates how many octaves to add to get from written pitch to
			* sounding pitch.
			*
			* If the double element is present, it indicates that the music is doubled one octave down from
			* what is currently written (as is the case for mixed cello / bass parts in orchestral literature).
			*/
		case class Transpose(diatonic: Option[Int] = Option.empty,
												 chromatic: Semitones,
												 octaveChange: Option[Int] = Option.empty,
												 double: Option[Empty] = Option.empty,
												 number: Option[StaffNumber] = Option.empty)
	}

	object ComplexBarline {

		import AttributeGroups.AttributeGroupsCommon.{Color, _}
		import Complex.ComplexCommon.{EmptyPrintStyleAlign, Fermata, WavyLine}
		import Group.GroupCommon.Editorial
		import Primatives.PrimativeBarline._
		import Primatives.PrimativeCommon.{Divisions, Tenths}

		/**
			* The bar-style-color type contains barline style and color information.
			*/
		case class BarStyleColor(barStyle: BarStyle, color: Color = Color())

		/**
			* If a barline is other than a normal single barline, it should be represented by a barline type
			* that describes it. This includes information about repeats and multiple endings, as well as line
			* style. Barline data is on the same level as the other musical data in a score - a child of a
			* measure in a partwise score, or a part in a timewise score. This allows for barlines within
			* measures, as in dotted barlines that subdivide measures in complex meters. The two fermata
			* elements allow for fermatas on both sides of the barline (the lower one inverted).
			*
			* Barlines have a location attribute to make it easier to process barlines independently of the
			* other musical data in a score. It is often easier to set up measures separately from entering
			* notes. The location attribute must match where the barline element occurs within the rest of
			* the musical data in the score. If location is left, it should be the first element in the measure,
			* aside from the print, bookmark, and link elements. If location is right, it should be the last
			* element, again with the possible exception of the print, bookmark, and link elements. If no
			* location is specified, the right barline is the default. The segno, coda, and divisions attributes
			* work the same way as in the sound element. They are used for playback when barline elements
			* contain segno or coda child elements.
			*/
		case class Barline(barStyle: Option[BarStyleColor] = Option.empty,
											 editorial: Editorial = Editorial(),
											 wavyLine: Option[WavyLine] = Option.empty,
											 segno: Option[EmptyPrintStyleAlign] = Option.empty,
											 coda: Option[EmptyPrintStyleAlign] = Option.empty,
											 fermata: List[Fermata] = List(),
											 ending: Option[Ending] = Option.empty,
											 repeat: Option[Repeat] = Option.empty,
											 location: RightLeftMiddle = RLM_Right,
											 segnoAttr: Option[String] = Option.empty,
											 codaArrt: Option[String] = Option.empty,
											 divisions: Option[Divisions] = Option.empty) {
			require(fermata.size <= 2)
		}

		/**
			* The ending type represents multiple (e.g. first and second) endings. Typically, the start type is
			* associated with the left barline of the first measure in an ending. The stop and discontinue types
			* are associated with the right barline of the last measure in an ending. Stop is used when the
			* ending mark concludes with a downward jog, as is typical for first endings. Discontinue is used
			* when there is no downward jog, as is typical for second endings that do not conclude a piece.
			* The length of the jog can be specified using the end-length attribute. The text-x and text-y
			* attributes are offsets that specify where the baseline of the start of the ending text appears,
			* relative to the start of the ending line.
			*
			* The number attribute reflects the numeric values of what is under the ending line. Single endings
			* such as "1" or comma-separated multiple endings such as "1,2" may be used. The ending element text
			* is used when the text displayed in the ending is different than what appears in the number
			* attribute. The print-object element is used to indicate when an ending is present but not printed,
			* as is often the case for many parts in a full score.
			*/
		case class Ending(ending: String, number: EndingNumber,
											endingTypes: StartStopDiscontinue,
											printObject: PrintObject = PrintObject(),
											printStyle: PrintStyle = PrintStyle(),
											endLength: Option[Tenths] = Option.empty,
											textX: Option[Tenths] = Option.empty,
											textY: Option[Tenths] = Option.empty)

		/**
			* The repeat type represents repeat marks. The start of the repeat has a forward direction while
			* the end of the repeat has a backward direction. Backward repeats that are not part of an ending
			* can use the times attribute to indicate the number of times the repeated section is played.
			*/
		case class Repeat(direction: BackwardForward,
											time: Option[Int] = Option.empty,
											winged: Option[Winged] = Option.empty) {
			require(time.forall(_ > 0))
		}
	}

	object ComplexCommon {

		import AttributeGroups.AttributeGroupsCommon.{Color, _}
		import Primatives.PrimativeCommon._
		import Primatives.PrimativeNote.AccidentalValue

		/**
			* The accidental-text type represents an element with an accidental value and
			* text-formatting attributes.
			*/
		case class AccidentalText(value: AccidentalValue, formatting: TextFormatting = TextFormatting())

		sealed abstract class DynamicSymbolsChoice
		case class DynamicSymbolChoice(symbol: DynamicSymbols.DynamicSymbols) extends DynamicSymbolsChoice
		case class DynamicStringChoice(string: String) extends DynamicSymbolsChoice

		/**
			* Dynamics can be associated either with a note or a general musical direction.
			* To avoid inconsistencies between and amongst the letter abbreviations for dynamics
			* (what is sf vs. sfz, standing alone or with a trailing dynamic that is not always piano),
			* we use the actual letters as the names of these dynamic elements. The other-dynamics element
			* allows other dynamic marks that are not covered here, but many of those should perhaps be
			* included in a more general musical direction element. Dynamics elements may also be combined
			* to create marks not covered by a single element, such as sfmp.
			*
			* These letter dynamic symbols are separated from crescendo, decrescendo, and wedge indications.
			* Dynamic representation is inconsistent in scores. Many things are assumed by the composer and
			* left out, such as returns to original dynamics. Systematic representations are quite complex:
			* for example, Humdrum has at least 3 representation formats related to dynamics. The MusicXML
			* format captures what is in the score, but does not try to be optimal for analysis or synthesis
			* of dynamics.
			*/
		case class Dynamics(symbol: List[DynamicSymbolsChoice] = List(),
												printStyleAlign: PrintStyleAlign = PrintStyleAlign(),
												placement: Placement = Placement(),
												textDecoration: TextDecoration = TextDecoration(),
												enclosure: Enclosure = Enclosure())
		object DynamicSymbols extends Enumeration {
			type DynamicSymbols = Value
			val p, pp, ppp, pppp, ppppp, pppppp = Value
			val f, ff, fff, ffff, fffff, ffffff = Value
			val mp, mf, sf, sfp, sfpp, fp, rf, rfz, sfz, sffz, fz = Value
		}

		/**
			* The empty type represents an empty element with no attributes.
			*/
		type Empty = Unit

		/**
			* The empty-placement type represents an empty element with print-style and placement attributes.
			*/
		case class EmptyPlacement(printStyle: PrintStyle = PrintStyle(),
															placement: Placement = Placement())

		/**
			* The empty-print-style type represents an empty element with print-style attribute group.
			*/
		case class EmptyPrintStyle(printStyle: PrintStyle = PrintStyle())

		/**
			* The empty-print-style-align type represents an empty element with print-style-align attribute group.
			*/
		case class EmptyPrintStyleAlign(printStyleAlgin: PrintStyleAlign = PrintStyleAlign())

		/**
			* The empty-print-style-align-object type represents an empty element with print-object
			* and print-style-align attribute groups.
			*/
		case class EmptyPrintObjectStyleAlign(printObject: PrintObject = PrintObject(),
																					printStyleAlign: PrintStyleAlign = PrintStyleAlign())

		/**
			* The empty-trill-sound type represents an empty element with print-style, placement,
			* and trill-sound attributes.
			*/
		case class EmptyTrillSound(printStyle: PrintStyle = PrintStyle(),
															 placement: Placement = Placement(),
															 trillSound: TrillSound = TrillSound())

		/**
			* The horizontal-turn type represents turn elements that are horizontal rather than vertical.
			* These are empty elements with print-style, placement, trill-sound, and slash attributes.
			* If the slash attribute is yes, then a vertical line is used to slash the turn;
			* it is no by default.
			*/
		case class HorizontalTurn(printSize: PrintStyle = PrintStyle(),
															placement: Placement = Placement(),
															trillSound: TrillSound = TrillSound(),
															slash: Option[YesNo] = Option.empty)

		/**
			* The fermata text content represents the shape of the fermata sign. An empty fermata element
			* represents a normal fermata. The fermata type is upright if not specified.
			*/
		case class Fermata(shape: FermataShape,
											 fermataType: Option[UprightInverted] = Option.empty,
											 printStyle: PrintStyle = PrintStyle())

		/**
			* Fingering is typically indicated 1,2,3,4,5. Multiple fingerings may be given, typically to
			* substitute fingerings in the middle of a note. The substitution and alternate values are "no"
			* if the attribute is not present. For guitar and other fretted instruments, the fingering element
			* represents the fretting finger; the pluck element represents the plucking finger.
			*/
		case class Fingering(finger: String,
												 substitution: Option[YesNo] = Option.empty,
												 alternate: Option[YesNo] = Option.empty,
												 printStyle: PrintStyle = PrintStyle(),
												 placement: Placement = Placement())

		/**
			* The formatted-text type represents a text element with text-formatting attributes.
			*/
		case class FormattedText(text: String, textFormatting: TextFormatting = TextFormatting())

		/**
			* The fret element is used with tablature notation and chord diagrams. Fret numbers
			* start with 0 for an open string and 1 for the first fret.
			*/
		case class Fret(num: Int, font: Font = Font(), color: Color = Color()) {
			require(num >= 0)
		}

		/**
			* The level type is used to specify editorial information for different MusicXML elements.
			* If the reference attribute for the level element is yes, this indicates editorial information
			* that is for display only and should not affect playback. For instance, a modern edition of
			* older music may set reference="yes" on the attributes containing the music's original clef,
			* key, and time signature. It is no by default.
			*/
		case class Level(level: String,
										 reference: Option[YesNo] = Option.empty,
										 levelDisplay: LevelDisplay = LevelDisplay())

		/**
			* The midi-device type corresponds to the DeviceName meta event in Standard MIDI Files.
			* The optional port attribute is a number from 1 to 16 that can be used with the unofficial
			* MIDI port (or cable) meta event. Unlike the DeviceName meta event, there can be multiple
			* midi-device elements per MusicXML part starting in MusicXML 3.0. The optional id attribute
			* refers to the score-instrument assigned to this device. If missing, the device assignment
			* affects all score-instrument elements in the score-part.
			*/
		case class MidiDevice(midi: String,
													port: Option[Midi16] = Option.empty,
													id: Option[String] = Option.empty)

		/**
			* The midi-instrument type defines MIDI 1.0 instrument playback. The midi-instrument element can
			* be a part of either the score-instrument element at the start of a part, or the sound element
			* within a part. The id attribute refers to the score-instrument affected by the change.
			*/
		case class MidiInstrument(midiChannel: Option[Midi16] = Option.empty,
															midiName: Option[String] = Option.empty,
															midiBank: Option[Midi16384] = Option.empty,
															midiProgram: Option[Midi128] = Option.empty,
															midiUnpitched: Option[Midi128] = Option.empty,
															volume: Option[Percent] = Option.empty,
															pan: Option[RotationDegrees] = Option.empty,
															elevation: Option[RotationDegrees] = Option.empty,
															id: String)

		sealed abstract class NameDisplayChoice
		case class FormattedTextNameDisplayChoice(text: FormattedText) extends NameDisplayChoice
		case class AccidentalTextNameDisplayChoice(text: AccidentalText) extends NameDisplayChoice

		/**
			* The name-display type is used for exact formatting of multi-font text in part and group names to
			* the left of the system. The print-object attribute can be used to determine what, if anything,
			* is printed at the start of each system. Enclosure for the display-text element is none by default.
			* Language for the display-text element is Italian ("it") by default.
			*/
		case class NameDisplay(name: List[NameDisplayChoice] = List(),
													 printObject: PrintObject = PrintObject())

		/**
			* The other-play element represents other types of playback. The required type attribute indicates
			* the type of playback to which the element content applies.
			*/
		case class OtherPlay(other: String, playType: String)

		sealed abstract class PlayChoice
		case class StringChoice(ipa: String) extends PlayChoice
		case class MuteChoice(mute: Mute) extends PlayChoice
		case class SemiPitchedChoice(semiPitched: SemiPitched) extends PlayChoice
		case class OtherPlayChoice(otherPlay: OtherPlay) extends PlayChoice

		/**
			* The play type, new in Version 3.0, specifies playback techniques to be used in conjunction with
			* the instrument-sound element. When used as part of a sound element, it applies to all notes
			* going forward in score order. In multi-instrument parts, the affected instrument should be
			* specified using the id attribute. When used as part of a note element, it applies to the current
			* note only.
			*
			* The ipa element represents International Phonetic Alphabet (IPA) sounds for vocal music.
			* String content is limited to IPA 2005 symbols represented in Unicode 6.0.
			*/
		case class Play(list: List[PlayChoice] = List(), id: Option[String] = Option.empty)

		/**
			* The string type is used with tablature notation, regular notation (where it is often circled),
			* and chord diagrams. String numbers start with 1 for the highest string.
			*/
		case class StringClass(sn: StringNumber,
													 printStyle: PrintStyle = PrintStyle(),
													 placement: Placement = Placement())

		/**
			* The typed-text type represents a text element with a type attributes.
			*/
		case class TypedText(s: String, textType: Option[String] = Option.empty)

		/**
			* Wavy lines are one way to indicate trills. When used with a measure element,
			* they should always have type="continue" set.
			*/
		case class WavyLine(sscType: StartStopContinue,
												number: Option[NumberLevel] = Option.empty,
												position: Position = Position(),
												placement: Placement = Placement(),
												color: Color = Color(),
												trillSound: TrillSound = TrillSound())
	}

	object ComplexDirection {

		import AttributeGroups.AttributeGroupsCommon.{Color, LineType, VAlign, VAlignImage, _}
		import AttributeGroups.AttributeGroupsDirection.{ImageAttributes, PrintAttributes}
		import Complex.ComplexCommon._
		import Complex.ComplexLayout.MeasureLayout
		import Complex.ComplexNote.TimeModification
		import Group.GroupCommon.{Editorial, EditorialVoiceDirection, Staff, Tuning}
		import Group.GroupDirection.{BeatUnit, HarmonyChord}
		import Group.GroupLayout.Layout
		import Primatives.PrimativeCommon._
		import Primatives.PrimativeDirection._
		import Primatives.PrimativeNote._

		/**
			* The accord type represents the tuning of a single string in the scordatura element.
			* It uses the same group of elements as the staff-tuning element. Strings are numbered
			* from high to low.
			*/
		case class Accord(tuning: Tuning, string: Option[StringNumber])

		/**
			* The accordion-registration type is use for accordion registration symbols. These are circular
			* symbols divided horizontally into high, middle, and low sections that correspond to 4', 8', and
			* 16' pipes. Each accordion-high, accordion-middle, and accordion-low element represents the
			* presence of one or more dots in the registration diagram. An accordion-registration element
			* needs to have at least one of the child elements present.
			*
			* The accordion-high element indicates the presence of a dot in the high (4') section of the
			* registration symbol.
			*
			* The accordion-middle element indicates the presence of 1 to 3 dots in the middle (8') section
			* of the registration symbol.
			*
			* The accordion-low element indicates the presence of a dot in the low (16') section of the
			* registration symbol.
			*/
		case class AccordionRegistration(accordionHigh: Option[Empty] = Option.empty,
																		 accordionMiddle: Option[AccordionMiddle] = Option.empty,
																		 accordionLow: Option[Empty] = Option.empty,
																		 printStyleAlign: PrintStyleAlign = PrintStyleAlign())

		/**
			* The barre element indicates placing a finger over multiple strings on a single fret. The type
			* is "start" for the lowest pitched string (e.g., the string with the highest MusicXML number)
			* and is "stop" for the highest pitched string.
			*/
		case class Barre(barreType: StartStop, color: Color = Color())

		/**
			* The bass type is used to indicate a bass note in popular music chord symbols, e.g. G/C.
			* It is generally not used in functional harmony, as inversion is generally not used in pop chord
			* symbols. As with root, it is divided into step and alter elements, similar to pitches.
			*/
		case class Bass(bassStep: BassStep, bassAlter: Option[BassAlter] = Option.empty)

		/**
			* The bass-alter type represents the chromatic alteration of the bass of the current chord within
			* the harmony element. In some chord styles, the text for the bass-step element may include
			* bass-alter information. In that case, the print-object attribute of the bass-alter element
			* can be set to no. The location attribute indicates whether the alteration should appear to the
			* left or the right of the bass-step; it is right by default.
			*/
		case class BassAlter(semitones: Semitones,
												 printObject: PrintObject = PrintObject(),
												 printStyle: PrintStyle = PrintStyle(),
												 location: Option[LeftRight] = Option.empty)

		/**
			* The bass-step type represents the pitch step of the bass of the current chord within the harmony
			* element. The text attribute indicates how the bass should appear in a score if not using the
			* element contents.
			*/
		case class BassStep(step: Step,
												text: Option[String] = Option.empty,
												printStyle: PrintStyle = PrintStyle())

		/**
			* The beater type represents pictograms for beaters, mallets, and sticks that do not have different
			* materials represented in the pictogram.
			*/
		case class Beater(beaterValue: BeaterValue, tip: Option[TipDirection] = Option.empty)

		/**
			* Brackets are combined with words in a variety of modern directions. The line-end attribute
			* specifies if there is a jog up or down (or both), an arrow, or nothing at the start or end of
			* the bracket. If the line-end is up or down, the length of the jog can be specified using the
			* end-length attribute. The line-type is solid by default.
			*/
		case class Bracket(bracketType: StartStopContinue,
											 number: Option[NumberLevel] = Option.empty,
											 lineEnd: LineEnd,
											 endLength: Option[Tenths] = Option.empty,
											 lineType: LineType = LineType(),
											 dashedFormatting: DashedFormatting = DashedFormatting(),
											 position: Position = Position(),
											 color: Color = Color())

		/**
			* The dashes type represents dashes, used for instance with cresc. and dim. marks.
			*/
		case class Dashes(dasheType: StartStopContinue,
											number: Option[NumberLevel] = Option.empty,
											dashedFormatting: DashedFormatting = DashedFormatting(),
											position: Position = Position(),
											color: Color = Color())

		/**
			* The degree type is used to add, alter, or subtract individual notes in the chord. The print-object
			* attribute can be used to keep the degree from printing separately when it has already taken into
			* account in the text attribute of the kind element. The degree-value and degree-type text attributes
			* specify how the value and type of the degree should be displayed.
			*
			* A harmony of kind "other" can be spelled explicitly by using a series of degree elements together
			* with a root.
			*/
		case class Degree(degreeValue: DegreeValue,
											degreeAlter: DegreeAlter,
											degreeType: DegreeType,
											printObject: PrintObject = PrintObject())

		/**
			* The degree-alter type represents the chromatic alteration for the current degree.
			* If the degree-type value is alter or subtract, the degree-alter value is relative to the degree
			* already in the chord based on its kind element. If the degree-type value is add, the degree-alter
			* is relative to a dominant chord (major and perfect intervals except for a minor seventh).
			* The plus-minus attribute is used to indicate if plus and minus symbols should be used instead of
			* sharp and flat symbols to display the degree alteration; it is no by default.
			*/
		case class DegreeAlter(semitones: Semitones,
													 printStyle: PrintStyle = PrintStyle(),
													 plusMinus: Option[YesNo] = Option.empty)

		/**
			* The degree-type type indicates if this degree is an addition, alteration, or subtraction relative
			* to the kind of the current chord. The value of the degree-type element affects the interpretation
			* of the value of the degree-alter element. The text attribute specifies how the type of the degree
			* should be displayed in a score.
			*/
		case class DegreeType(degreeTypeValue: DegreeTypeValue,
													text: Option[String] = Option.empty,
													printStyle: PrintStyle = PrintStyle())

		/**
			* The content of the degree-value type is a number indicating the degree of the chord (1 for the
			* root, 3 for third, etc). The text attribute specifies how the type of the degree should be
			* displayed in a score. The degree-value symbol attribute indicates that a symbol should be used
			* in specifying the degree. If the symbol attribute is present, the value of the text attribute
			* follows the symbol.
			*/
		case class DegreeValue(degreeValue: Int,
													 symbol: Option[DegreeSymbolValue] = Option.empty,
													 text: Option[String] = Option.empty,
													 printStyle: PrintStyle = PrintStyle()) {
			require(degreeValue > 0)
		}

		/**
			* A direction is a musical indication that is not attached to a specific note. Two or more may be
			* combined to indicate starts and stops of wedges, dashes, etc.
			*
			* By default, a series of direction-type elements and a series of child elements of a
			* direction-type within a single direction element follow one another in sequence visually. For a
			* series of direction-type children, non-positional formatting attributes are carried over from the
			* previous element by default.
			*/
		case class Direction(directionType: List[DirectionType],
												 offset: Option[Offset] = Option.empty,
												 editorialVoiceDirection: EditorialVoiceDirection = EditorialVoiceDirection(),
												 staff: Option[Staff] = Option.empty,
												 sound: Option[Sound] = Option.empty,
												 placement: Placement = Placement(),
												 directive: AttributeGroups.AttributeGroupsCommon.Directive = AttributeGroups.AttributeGroupsCommon.Directive()) {
			require(directionType.nonEmpty)
		}

		sealed abstract class DirectionTypeChoice
		case class RehearsalChoice(rehearsal: List[FormattedText]) extends DirectionTypeChoice {
			require(rehearsal.nonEmpty)
		}
		case class SegnoChoice(segno: List[EmptyPrintStyleAlign]) extends DirectionTypeChoice {
			require(segno.nonEmpty)
		}
		case class WordsChoice(words: List[FormattedText]) extends DirectionTypeChoice {
			require(words.nonEmpty)
		}
		case class CodaChoice(coda: List[EmptyPrintStyleAlign]) extends DirectionTypeChoice {
			require(coda.nonEmpty)
		}
		case class WedgeChoice(wedge: Wedge) extends DirectionTypeChoice
		case class DynamicsChoice(dynamics: List[Dynamics]) extends DirectionTypeChoice {
			require(dynamics.nonEmpty)
		}
		case class DashesChoice(dashes: Dashes) extends DirectionTypeChoice
		case class BracketChoice(bracket: Bracket) extends DirectionTypeChoice
		case class PedalChoice(pedal: Pedal) extends DirectionTypeChoice
		case class MetronomeChoice(metronome: Metronome) extends DirectionTypeChoice
		case class OctaveShiftChoice(octaveShift: OctaveShift) extends DirectionTypeChoice
		case class HarpPedalsChoice(harpPedals: HarpPedals) extends DirectionTypeChoice
		case class DampChoice(damp: EmptyPrintStyleAlign) extends DirectionTypeChoice
		case class DampAllChoice(dampAll: EmptyPrintStyleAlign) extends DirectionTypeChoice
		case class EyeglassesChoice(eyeglasses: EmptyPrintStyleAlign) extends DirectionTypeChoice
		case class StringMuteChoice(stringMute: StringMute) extends DirectionTypeChoice
		case class ScordaturaChoice(scordatura: Scordatura) extends DirectionTypeChoice
		case class ImageChoice(image: Image) extends DirectionTypeChoice
		case class PrincipalVoiceChoice(principalVoice: PrincipalVoice) extends DirectionTypeChoice
		case class AccordionRegistrationChoice(accordionRegistration: AccordionRegistration) extends DirectionTypeChoice
		case class PercussionChoice(percussion: List[Percussion]) extends DirectionTypeChoice {
			require(percussion.nonEmpty)
		}
		case class OtherDirectionChoice(otherDirection: OtherDirection) extends DirectionTypeChoice

		/**
			* Textual direction types may have more than 1 component due to multiple fonts. The dynamics
			* element may also be used in the notations element. Attribute groups related to print
			* suggestions apply to the individual direction-type, not to the overall direction.
			*
			* The rehearsal type specifies a rehearsal mark. Language is Italian ("it") by default.
			* Enclosure is square by default. Left justification is assumed if not specified.
			*
			* The segno element is the visual indicator of a segno sign. A sound element is needed to guide
			* playback applications reliably.
			*
			* The words element specifies a standard text direction. Left justification is assumed if not
			* specified. Language is Italian ("it") by default. Enclosure is none by default.
			*
			* The coda element is the visual indicator of a coda sign. A sound element is needed to guide
			* playback applications reliably.
			*
			* The damp element specifies a harp damping mark.
			*
			* The damp-all element specifies a harp damping mark for all strings.
			*
			* The eyeglasses element specifies the eyeglasses symbol, common in commercial music.
			*/
		type DirectionType = DirectionTypeChoice

		/**
			* The feature type is a part of the grouping element used for musical analysis.
			* The type attribute represents the type of the feature and the element content represents
			* its value. This type is flexible to allow for different analyses.
			*/
		case class Feature(feature: String, featureType: Option[String] = Option.empty)

		/**
			* The first-fret type indicates which fret is shown in the top space of the frame; it is fret
			* 1 if the element is not present. The optional text attribute indicates how this is represented
			* in the fret diagram, while the location attribute indicates whether the text appears to the left
			* or right of the frame.
			*/
		case class FirstFret(fret: Int,
												 text: Option[String] = Option.empty,
												 location: Option[LeftRight] = Option.empty)

		/**
			* The frame type represents a frame or fretboard diagram used together with a chord symbol.
			* The representation is based on the NIFF guitar grid with additional information.
			* The frame type's unplayed attribute indicates what to display above a string that has no
			* associated frame-note element. Typical values are x and the empty string. If the attribute
			* is not present, the display of the unplayed string is application-defined.
			*
			* The frame-strings element gives the overall size of the frame in vertical lines (strings).
			*
			* The frame-frets element gives the overall size of the frame in horizontal spaces (frets).
			*/
		case class Frame(frameStrings: Int,
										 frameFrets: Int,
										 firstFret: Option[FirstFret] = Option.empty,
										 frameNote: List[FrameNote],
										 position: Position = Position(),
										 color: Color = Color(),
										 halign: HAlign = HAlign(),
										 valignImage: VAlignImage = VAlignImage(),
										 heigth: Option[Tenths] = Option.empty,
										 width: Option[Tenths] = Option.empty,
										 unplayed: Option[String] = Option.empty) {
			require(frameStrings >= 1)
			require(frameFrets >= 1)
			require(frameNote.nonEmpty)
		}

		/**
			* The frame-note type represents each note included in the frame. An open string will have a
			* fret value of 0, while a muted string will not be associated with a frame-note element.
			*/
		case class FrameNote(string: StringClass,
												 fret: Fret, fingering: Option[Fingering] = Option.empty,
												 barre: Option[Barre] = Option.empty)

		/**
			* The grouping type is used for musical analysis. When the type attribute is "start" or "single",
			* it usually contains one or more feature elements. The number attribute is used for distinguishing
			* between overlapping and hierarchical groupings. The member-of attribute allows for easy
			* distinguishing of what grouping elements are in what hierarchy. Feature elements contained within
			* a "stop" type of grouping may be ignored.
			*
			* This element is flexible to allow for different types of analyses. Future versions of the MusicXML
			* format may add elements that can represent more standardized categories of analysis data, allowing
			* for easier data sharing.
			*/
		case class Grouping(feature: List[Feature] = List(),
												groupingType: StartStopSingle,
												number: String = "1",
												memberOf: Option[String] = Option.empty)

		/**
			* The harmony type is based on Humdrum's **harm encoding, extended to support chord symbols in
			* popular music as well as functional harmony analysis in classical music.
			*
			* If there are alternate harmonies possible, this can be specified using multiple harmony elements
			* differentiated by type. Explicit harmonies have all note present in the music; implied have some
			* notes missing but implied; alternate represents alternate analyses.
			*
			* The harmony object may be used for analysis or for chord symbols. The print-object attribute
			* controls whether or not anything is printed due to the harmony element. The print-frame attribute
			* controls printing of a frame or fretboard diagram. The print-style attribute group sets the
			* default for the harmony, but individual elements can override this with their own print-style
			* values.
			*/
		case class Harmony(harmonyChord: List[HarmonyChord],
											 frame: Option[Frame] = Option.empty,
											 offset: Option[Offset] = Option.empty,
											 editorial: Editorial = Editorial(),
											 staff: Option[Staff] = Option.empty,
											 harmonyType: Option[HarmonyType] = Option.empty,
											 printObject: PrintObject = PrintObject(),
											 printFrame: Option[YesNo] = Option.empty,
											 printStyle: PrintStyle = PrintStyle(),
											 placement: Placement = Placement()) {
			require(harmonyChord.nonEmpty)
		}

		/**
			* The harp-pedals type is used to create harp pedal diagrams. The pedal-step and pedal-alter
			* elements use the same values as the step and alter elements. For easiest reading, the pedal-tuning
			* elements should follow standard harp pedal order, with pedal-step values of D, C, B, E, F, G, and A.
			*/
		case class HarpPedals(pedalTuning: List[PedalTuning],
													printStyleAlign: PrintStyleAlign = PrintStyleAlign()) {
			require(pedalTuning.nonEmpty)
		}

		/**
			* The image type is used to include graphical images in a score.
			*/
		type Image = ImageAttributes

		/**
			* The inversion type represents harmony inversions. The value is a number indicating which
			* inversion is used: 0 for root position, 1 for first inversion, etc.
			*/
		case class Inversion(inv: Int, printStyle: PrintStyle = PrintStyle()) {
			require(inv > 0)
		}

		/**
			* Kind indicates the type of chord. Degree elements can then add, subtract, or alter from these
			* starting points
			*
			* The attributes are used to indicate the formatting of the symbol. Since the kind element is the
			* constant in all the harmony-chord groups that can make up a polychord, many formatting attributes
			* are here.
			*
			* The use-symbols attribute is yes if the kind should be represented when possible with harmony
			* symbols rather than letters and numbers. These symbols include:
			*
			* 		major: a triangle, like Unicode 25B3
			* 		minor: -, like Unicode 002D
			* 		augmented: +, like Unicode 002B
			* 		diminished: °, like Unicode 00B0
			* 		half-diminished: ø, like Unicode 00F8
			*
			* For the major-minor kind, only the minor symbol is used when use-symbols is yes. The major symbol
			* is set using the symbol attribute in the degree-value element. The corresponding degree-alter
			* value will usually be 0 in this case.
			*
			* The text attribute describes how the kind should be spelled in a score. If use-symbols is yes,
			* the value of the text attribute follows the symbol. The stack-degrees attribute is yes if the
			* degree elements should be stacked above each other. The parentheses-degrees attribute is yes if
			* all the degrees should be in parentheses. The bracket-degrees attribute is yes if all the degrees
			* should be in a bracket. If not specified, these values are implementation-specific.
			* The alignment attributes are for the entire harmony-chord group of which this kind element is
			* a part.
			*/
		case class Kind(kindValue: KindValue,
										useSymbols: Option[YesNo] = Option.empty,
										text: Option[String] = Option.empty,
										stackDegrees: Option[YesNo] = Option.empty,
										parenthesesDegrees: Option[YesNo] = Option.empty,
										bracketDegrees: Option[YesNo] = Option.empty,
										printStyle: PrintStyle = PrintStyle(),
										halign: HAlign = HAlign(),
										valign: VAlign = VAlign())

		/**
			* The measure-numbering type describes how frequently measure numbers are displayed on this part.
			* The number attribute from the measure element is used for printing. Measures with an implicit
			* attribute set to "yes" never display a measure number, regardless of the measure-numbering setting.
			*/
		case class MeasureNumbering(value: MeasureNumberingValue,
																printStyleAlign: PrintStyleAlign = PrintStyleAlign())

		sealed abstract class BeatUnitChoice
		case class MinuteChoice(perMinute: PerMinute) extends BeatUnitChoice
		case class BeatChoice(beatUnit: BeatUnit) extends BeatUnitChoice

		/**
			* The metronome-relation element describes the relationship symbol that goes between the two sets
			* of metronome-note elements. The currently allowed value is equals, but this may expand in future
			* versions. If the element is empty, the equals value is used.
			*/
		case class MetronomeRelation(relation: String, note: List[MetronomeNote]) {
			require(note.nonEmpty)
		}

		sealed abstract class MetronomeTypeChoice
		case class MetronomeBeatChoice(beatUnit: BeatUnit, beatUnitChoice: BeatUnitChoice) extends MetronomeTypeChoice
		case class MetronomeNoteChoice(metronomeNote: List[MetronomeNote],
																	 metronomeRelation: Option[MetronomeRelation] = Option.empty) extends MetronomeTypeChoice {
			require(metronomeNote.nonEmpty)
		}

		/**
			* The metronome type represents metronome marks and other metric relationships. The beat-unit group
			* and per-minute element specify regular metronome marks. The metronome-note and metronome-relation
			* elements allow for the specification of more complicated metric relationships, such as swing
			* tempo marks where two eighths are equated to a quarter note / eighth note triplet. The parentheses
			* attribute indicates whether or not to put the metronome mark in parentheses; its value is no if
			* not specified.
			*/
		case class Metronome(typeChoice: MetronomeTypeChoice,
												 printStyleAlign: PrintStyleAlign = PrintStyleAlign(),
												 justify: Justify = Justify(),
												 parentheses: Option[YesNo] = Option.empty)

		/**
			* The metronome-beam type works like the beam type in defining metric relationships, but does not
			* include all the attributes available in the beam type.
			*/
		case class MetronomeBeam(beamValue: BeamValue, number: BeamLevel = BeamLevel(1))

		/**
			* The metronome-note type defines the appearance of a note within a metric relationship mark.
			*
			* The metronome-type element works like the type element in defining metric relationships.
			*
			* The metronome-dot element works like the dot element in defining metric relationships.
			*/
		case class MetronomeNote(metronomeType: NoteTypeValue, metronomeDot: List[Empty] = List(),
														 metronomeBeam: List[MetronomeBeam] = List(),
														 metronomeTuplet: Option[MetronomeTuplet] = Option.empty)

		/**
			* The metronome-tuplet type uses the same element structure as the time-modification element
			* along with some attributes from the tuplet element.
			*/
		case class MetronomeTuplet(timeModification: TimeModification,
															 ssType: StartStop,
															 bracket: Option[YesNo] = Option.empty,
															 showNumber: Option[ShowTuplet] = Option.empty)

		/**
			* The octave shift type indicates where notes are shifted up or down from their true pitched values
			* because of printing difficulty. Thus a treble clef line noted with 8va will be indicated with an
			* octave-shift down from the pitch data indicated in the notes. A size of 8 indicates one octave;
			* a size of 15 indicates two octaves.
			*/
		case class OctaveShift(udscType: UpDownStopContinue,
													 number: Option[NumberLevel] = Option.empty,
													 size: Int = 8,
													 dashedFormatting: DashedFormatting = DashedFormatting(),
													 printStyle: PrintStyle = PrintStyle())

		/**
			* An offset is represented in terms of divisions, and indicates where the direction will appear
			* relative to the current musical location. This affects the visual appearance of the direction.
			* If the sound attribute is "yes", then the offset affects playback too. If the sound attribute is
			* "no", then any sound associated with the direction takes effect at the current location.
			* The sound attribute is "no" by default for compatibility with earlier versions of the MusicXML
			* format. If an element within a direction includes a default-x attribute, the offset value will
			* be ignored when determining the appearance of that element.
			*/
		case class Offset(divisions: Divisions, sound: Option[YesNo] = Option.empty)

		/**
			* The other-direction type is used to define any direction symbols not yet in the current version
			* of the MusicXML format. This allows extended representation, though without application
			* interoperability.
			*/
		case class OtherDirection(dir: String,
															printObject: PrintObject = PrintObject(),
															printStyleAlign: PrintStyleAlign = PrintStyleAlign())

		/**
			* The pedal type represents piano pedal marks. The line attribute is yes if pedal lines are used.
			* The sign attribute is yes if Ped and * signs are used. For MusicXML 2.0 compatibility, the sign
			* attribute is yes by default if the line attribute is no, and is no by default if the line
			* attribute is yes. The change and continue types are used when the line attribute is yes.
			* The change type indicates a pedal lift and retake indicated with an inverted V marking.
			* The continue type allows more precise formatting across system breaks and for more complex
			* pedaling lines. The alignment attributes are ignored if the line attribute is yes.
			*/
		case class Pedal(ssccType: StartStopChangeContinue,
										 line: Option[YesNo] = Option.empty,
										 sign: Option[YesNo] = Option.empty,
										 printStyleAlign: PrintStyleAlign = PrintStyleAlign())

		/**
			* The pedal-tuning type specifies the tuning of a single harp pedal.
			*
			* The pedal-step element defines the pitch step for a single harp pedal.
			*
			* The pedal-alter element defines the chromatic alteration for a single harp pedal.
			*/
		case class PedalTuning(pedalStep: Step, pedalAlter: Semitones)

		/**
			* The per-minute type can be a number, or a text description including numbers. If a font is
			* specified, it overrides the font specified for the overall metronome element. This allows
			* separate specification of a music font for the beat-unit and a text font for the numeric value,
			* in cases where a single metronome font is not used.
			*/
		case class PerMinute(pm: String, font: Font = Font())

		sealed abstract class PercussionTypeChoice
		case class GlassPercussion(glass: Glass) extends PercussionTypeChoice
		case class MetalPercussion(metal: Metal) extends PercussionTypeChoice
		case class WoodPercussion(wood: Wood) extends PercussionTypeChoice
		case class PitchedPercussion(pitched: Pitched) extends PercussionTypeChoice
		case class MembranePercussion(membrane: Membrane) extends PercussionTypeChoice
		case class EffectPercussion(effect: Effect) extends PercussionTypeChoice
		case class TimpaniPercussion(timpani: Empty) extends PercussionTypeChoice
		case class BeaterPercussion(beater: Beater) extends PercussionTypeChoice
		case class StickPercussion(stick: Stick) extends PercussionTypeChoice
		case class StickLocationPercussion(stickLocation: StickLocation) extends PercussionTypeChoice
		case class OtherPercussion(otherPercussion: String) extends PercussionTypeChoice

		/**
			* The percussion element is used to define percussion pictogram symbols. Definitions for these
			* symbols can be found in Kurt Stone's "Music Notation in the Twentieth Century" on pages 206-212
			* and 223. Some values are added to these based on how usage has evolved in the 30 years since
			* Stone's book was published.
			*/
		case class Percussion(pTypeChoice: PercussionTypeChoice,
													printStyleAlign: PrintStyleAlign = PrintStyleAlign(),
													enclosure: Enclosure = Enclosure())

		/**
			* The principal-voice element represents principal and secondary voices in a score, either for
			* analysis or for square bracket symbols that appear in a score. The symbol attribute indicates
			* the type of symbol used at the start of the principal-voice. The content of the principal-voice
			* element is used for analysis and may be any text value. When used for analysis separate from
			* any printed score markings, the symbol attribute should be set to "none".
			*/
		case class PrincipalVoice(voice: String,
															ssType: StartStop,
															symbol: PrincipalVoiceSymbol,
															printStyleAlign: PrintStyleAlign = PrintStyleAlign())

		/**
			* The print type contains general printing parameters, including the layout elements defined in
			* the layout.mod file. The part-name-display and part-abbreviation-display elements used in the
			* score.mod file may also be used here to change how a part name or abbreviation is displayed
			* over the course of a piece. They take effect when the current measure or a succeeding measure
			* starts a new system.
			*
			* Layout elements in a print statement only apply to the current page, system, staff, or measure.
			* Music that follows continues to take the default values from the layout included in the
			* defaults element.
			*/
		case class Print(layout: Layout = Layout(),
										 measureLayout: Option[MeasureLayout] = Option.empty,
										 measureNumbering: Option[MeasureNumbering] = Option.empty,
										 partNameDisplay: Option[NameDisplay] = Option.empty,
										 partAbbreviationDisplay: Option[NameDisplay] = Option.empty,
										 printAttributes: PrintAttributes = PrintAttributes())

		/**
			* The root type indicates a pitch like C, D, E vs. a function indication like I, II, III.
			* It is used with chord symbols in popular music. The root element has a root-step and optional
			* root-alter element similar to the step and alter elements, but renamed to distinguish the
			* different musical meanings.
			*/
		case class Root(rootStep: RootStep, rootAlter: Option[RootAlter] = Option.empty)

		/**
			* The root-alter type represents the chromatic alteration of the root of the current chord within
			* the harmony element. In some chord styles, the text for the root-step element may include
			* root-alter information. In that case, the print-object attribute of the root-alter element can
			* be set to no. The location attribute indicates whether the alteration should appear to the left
			* or the right of the root-step; it is right by default.
			*/
		case class RootAlter(semiTones: Semitones,
												 printObject: PrintObject = PrintObject(),
												 printStyle: PrintStyle = PrintStyle(),
												 location: Option[LeftRight] = Option.empty)

		/**
			* The root-step type represents the pitch step of the root of the current chord within the harmony
			* element. The text attribute indicates how the root should appear in a score if not using the
			* element contents.
			*/
		case class RootStep(step: Step,
												text: Option[String] = Option.empty,
												printStyle: PrintStyle = PrintStyle())

		/**
			* Scordatura string tunings are represented by a series of accord elements, similar to the
			* staff-tuning elements. Strings are numbered from high to low.
			*/
		case class Scordatura(accord: List[Accord]) {
			require(accord.nonEmpty)
		}

		case class MidiProps(midiDevice: Option[MidiDevice] = Option.empty,
												 midiInstrument: Option[MidiInstrument] = Option.empty,
												 play: Option[Play] = Option.empty) {
			def isEmpty: Boolean = {
				midiDevice.isEmpty && midiInstrument.isEmpty && play.isEmpty
			}
		}

		/**
			* The sound element contains general playback parameters. They can stand alone within a
			* part/measure, or be a component element within a direction.
			*
			* Tempo is expressed in quarter notes per minute. If 0, the sound-generating program should prompt
			* the user at the time of compiling a sound (MIDI) file.
			*
			* Dynamics (or MIDI velocity) are expressed as a percentage of the default forte value
			* (90 for MIDI 1.0).
			*
			* Dacapo indicates to go back to the beginning of the movement. When used it always has the value
			* "yes".
			*
			* Segno and dalsegno are used for backwards jumps to a segno sign; coda and tocoda are used for
			* forward jumps to a coda sign. If there are multiple jumps, the value of these parameters can be
			* used to name and distinguish them. If segno or coda is used, the divisions attribute can also be
			* used to indicate the number of divisions per quarter note. Otherwise sound and MIDI generating
			* programs may have to recompute this.
			*
			* By default, a dalsegno or dacapo attribute indicates that the jump should occur the first time
			* through, while a tocoda attribute indicates the jump should occur the second time through. The
			* time that jumps occur can be changed by using the time-only attribute.
			*
			* Forward-repeat is used when a forward repeat sign is implied, and usually follows a bar line.
			* When used it always has the value of "yes".
			*
			* The fine attribute follows the final note or rest in a movement with a da capo or dal segno
			* direction. If numeric, the value represents the actual duration of the final note or rest,
			* which can be ambiguous in written notation and different among parts and voices. The value may
			* also be "yes" to indicate no change to the final duration.
			*
			* If the sound element applies only particular times through a repeat, the time-only attribute
			* indicates which times to apply the sound element.
			*
			* Pizzicato in a sound element effects all following notes. Yes indicates pizzicato, no
			* indicates arco.
			*
			* The pan and elevation attributes are deprecated in Version 2.0. The pan and elevation elements
			* in the midi-instrument element should be used instead. The meaning of the pan and elevation
			* attributes is the same as for the pan and elevation elements. If both are present,
			* the mid-instrument elements take priority.
			*
			* The damper-pedal, soft-pedal, and sostenuto-pedal attributes effect playback of the three
			* common piano pedals and their MIDI controller equivalents. The yes value indicates the pedal
			* is depressed; no indicates the pedal is released. A numeric value from 0 to 100 may also be
			* used for half pedaling. This value is the percentage that the pedal is depressed. A value of
			* 0 is equivalent to no, and a value of 100 is equivalent to yes.
			*
			* MIDI devices, MIDI instruments, and playback techniques are changed using the midi-device,
			* midi-instrument, and play elements. When there are multiple instances of these elements,
			* they should be grouped together by instrument using the id attribute values.
			*
			* The offset element is used to indicate that the sound takes place offset from the current score
			* position. If the sound element is a child of a direction element, the sound offset element
			* overrides the direction offset element if both elements are present. Note that the offset
			* reflects the intended musical position for the change in sound. It should not be used to
			* compensate for latency issues in particular hardware configurations.
			*/
		case class Sound(midiProps: List[MidiProps] = List(),
										 offset: Option[Offset] = Option.empty,
										 tempo: Option[NonNegativeDecimal] = Option.empty,
										 dynamics: Option[NonNegativeDecimal] = Option.empty,
										 dacapo: Option[YesNo] = Option.empty,
										 segno: Option[String] = Option.empty,
										 dalsegno: Option[String] = Option.empty,
										 coda: Option[String] = Option.empty,
										 tocoda: Option[String] = Option.empty,
										 divisions: Option[Divisions] = Option.empty,
										 forwardRepeat: Option[YesNo] = Option.empty,
										 fine: Option[String] = Option.empty,
										 timeOnly: Option[TimeOnly] = Option.empty,
										 pizzicato: Option[YesNo] = Option.empty,
										 pan: Option[RotationDegrees] = Option.empty,
										 elevation: Option[RotationDegrees] = Option.empty,
										 damperPedal: Option[YesNoNumber] = Option.empty,
										 softPedal: Option[YesNoNumber] = Option.empty,
										 sostenutoPedal: Option[YesNoNumber] = Option.empty)

		/**
			* The stick type represents pictograms where the material of the stick, mallet,
			* or beater is included.
			*/
		case class Stick(stickType: StickType,
										 stickMaterial: StickMaterial,
										 tip: Option[TipDirection] = Option.empty)

		/**
			* The string-mute type represents string mute on and mute off symbols.
			*/
		case class StringMute(ofType: OnOff, printStyleAlign: PrintStyleAlign = PrintStyleAlign())

		/**
			* The wedge type represents crescendo and diminuendo wedge symbols.
			* The type attribute is crescendo for the start of a wedge that is closed at the left side,
			* and diminuendo for the start of a wedge that is closed on the right side. Spread values are
			* measured in tenths; those at the start of a crescendo wedge or end of a diminuendo wedge are
			* ignored. The niente attribute is yes if a circle appears at the point of the wedge,
			* indicating a crescendo from nothing or diminuendo to nothing. It is no by default, and used
			* only when the type is crescendo, or the type is stop for a wedge that began with a diminuendo
			* type. The line-type is solid by default.
			*/
		case class Wedge(wtType: WedgeType,
										 number: Option[NumberLevel] = Option.empty,
										 spread: Option[Tenths] = Option.empty,
										 niente: Option[YesNo] = Option.empty,
										 lineType: LineType = LineType(),
										 dashedFormatting: DashedFormatting = DashedFormatting(),
										 position: Position = Position(), color: Color = Color())

	}

	object ComplexIdentity {

		import Complex.ComplexCommon.TypedText
		import Primatives.PrimativeCommon.{Date, YesNo}

		sealed abstract class EncodingChoice
		case class EncodingDate(encodingDate: Date) extends EncodingChoice
		case class EncodingEncoder(encoder: TypedText) extends EncodingChoice
		case class EncodingSoftware(software: String) extends EncodingChoice
		case class EncodingDescription(encodingDescription: String) extends EncodingChoice
		case class EncodingSupports(supports: Supports) extends EncodingChoice

		/**
			* The encoding element contains information about who did the digital encoding, when,
			* with what software, and in what aspects. Standard type values for the encoder element are music,
			* words, and arrangement, but other types may be used. The type attribute is only needed when
			* there are multiple encoder elements.
			*/
		case class Encoding(encodingChoice: List[EncodingChoice] = List())

		/**
			* Identification contains basic metadata about the score. It includes the information in MuseData
			* headers that may apply at a score-wide, movement-wide, or part-wide level. The creator, rights,
			* source, and relation elements are based on Dublin Core.
			*
			* The creator element is borrowed from Dublin Core. It is used for the creators of the score.
			* The type attribute is used to distinguish different creative contributions. Thus, there can be
			* multiple creators within an identification. Standard type values are composer, lyricist, and
			* arranger. Other type values may be used for different types of creative roles. The type attribute
			* should usually be used even if there is just a single creator element. The MusicXML format does
			* not use the creator / contributor distinction from Dublin Core.
			*
			* The rights element is borrowed from Dublin Core. It contains copyright and other intellectual
			* property notices. Words, music, and derivatives can have different types, so multiple rights
			* tags with different type attributes are supported. Standard type values are music, words,
			* and arrangement, but other types may be used. The type attribute is only needed when there are
			* multiple rights elements.
			*
			* The source for the music that is encoded. This is similar to the Dublin Core source element.
			*
			* A related resource for the music that is encoded. This is similar to the Dublin Core
			* relation element. Standard type values are music, words, and arrangement, but other types may
			* be used.
			*/
		case class Identification(creator: List[TypedText] = List(),
															rights: List[TypedText] = List(),
															endocing: Option[Encoding] = Option.empty,
															source: Option[String] = Option.empty,
															relation: List[TypedText] = List(),
															miscellaneous: Option[Miscellaneous] = Option.empty)

		/**
			* If a program has other metadata not yet supported in the MusicXML format, it can go in the
			* miscellaneous element. The miscellaneous type puts each separate part of metadata into its own
			* miscellaneous-field type.
			*/
		case class Miscellaneous(field: List[MiscellaneousField] = List())

		/**
			* If a program has other metadata not yet supported in the MusicXML format, each type of metadata
			* can go in a miscellaneous-field element. The required name attribute indicates the type of
			* metadata the element content represents.
			*/
		case class MiscellaneousField(field: String, name: String)

		/**
			* The supports type indicates if a MusicXML encoding supports a particular MusicXML element.
			* This is recommended for elements like beam, stem, and accidental, where the absence of an
			* element is ambiguous if you do not know if the encoding supports that element.
			* For Version 2.0, the supports element is expanded to allow programs to indicate support for
			* particular attributes or particular values. This lets applications communicate, for example,
			* that all system and/or page breaks are contained in the MusicXML file.
			*/
		case class Supports(ynType: YesNo,
												element: String,
												attribute: Option[String] = Option.empty,
												value: Option[String] = Option.empty)
	}

	object ComplexLayout {

		import Complex.ComplexCommon.EmptyPrintObjectStyleAlign
		import Group.GroupLayout.{AllMargins, LeftRightMargins}
		import Primatives.PrimativeAttributes.StaffNumber
		import Primatives.PrimativeCommon.{NonNegativeDecimal, Tenths}
		import Primatives.PrimativeLayout._

		/**
			* The appearance type controls general graphical settings for the music's final form appearance on
			* a printed page of display. This includes support for line widths, definitions for note sizes,
			* and standard distances between notation elements, plus an extension element for other aspects of
			* appearance.
			*/
		case class Appearance(lineWidth: List[LineWidth] = List(),
													noteSize: List[NoteSize] = List(),
													distance: List[Distance] = List(),
													otherAppearance: List[OtherAppearance] = List())

		/**
			* The distance element represents standard distances between notation elements in tenths.
			* The type attribute defines what type of distance is being defined. Valid values include hyphen
			* (for hyphens in lyrics) and beam.
			*/
		case class Distance(dist: Tenths, distType: DistanceType)

		/**
			* The line-width type indicates the width of a line type in tenths. The type attribute defines
			* what type of line is being defined. Values include beam, bracket, dashes, enclosure, ending,
			* extend, heavy barline, leger, light barline, octave shift, pedal, slur middle, slur tip, staff,
			* stem, tie middle, tie tip, tuplet bracket, and wedge. The text content is expressed in tenths.
			*/
		case class LineWidth(width: Tenths, widthType: LineWidthType)

		/**
			* The measure-layout type includes the horizontal distance from the previous measure.
			*
			* The measure-distance element specifies the horizontal distance from the previous measure.
			* This value is only used for systems where there is horizontal whitespace in the middle of
			* a system, as in systems with codas. To specify the measure width, use the width attribute of
			* the measure element.
			*/
		case class MeasureLayout(measureDistance: Option[Tenths] = Option.empty)

		/**
			* The note-size type indicates the percentage of the regular note size to use for notes with a cue
			* and large size as defined in the type element. The grace type is used for notes of cue size that
			* that include a grace element. The cue type is used for all other notes with cue size, whether
			* defined explicitly or implicitly via a cue element. The large type is used for notes of large size.
			* The text content represent the numeric percentage. A value of 100 would be identical to the size
			* of a regular note as defined by the music font.
			*/
		case class NoteSize(noteSize: NonNegativeDecimal, noteSizeType: NoteSizeType)

		/**
			* The other-appearance type is used to define any graphical settings not yet in the current version
			* of the MusicXML format. This allows extended representation, though without application
			* interoperability.
			*/
		case class OtherAppearance(appearance: String, tokenType: String)

		case class PageSize(pageHeigth: Tenths, pageWidth: Tenths)
		/**
			* Page layout can be defined both in score-wide defaults and in the print element.
			* Page margins are specified either for both even and odd pages, or via separate odd and even
			* page number values. The type is not needed when used as part of a print element.
			* If omitted when used in the defaults element, "both" is the default.
			*/
		case class PageLayout(pageSize: Option[PageSize] = Option.empty,
													pageMargins: List[PageMargins] = List()) {
			require(pageMargins.size <= 2)
		}

		/**
			* Page margins are specified either for both even and odd pages, or via separate odd and even
			* page number values. The type attribute is not needed when used as part of a print element.
			* If omitted when the page-margins type is used in the defaults element, "both" is the default value.
			*/
		case class PageMargins(margins: AllMargins, marginType: Option[MarginType] = Option.empty)

		/**
			* Margins, page sizes, and distances are all measured in tenths to keep MusicXML data in a
			* consistent coordinate system as much as possible. The translation to absolute units is done
			* with the scaling type, which specifies how many millimeters are equal to how many tenths.
			* For a staff height of 7 mm, millimeters would be set to 7 while tenths is set to 40.
			* The ability to set a formula rather than a single scaling factor helps avoid roundoff errors.
			*/
		case class Scaling(millimeters: Millimeters, tenths: Tenths)

		/**
			* Staff layout includes the vertical distance from the bottom line of the previous staff in
			* this system to the top line of the staff specified by the number attribute.
			* The optional number attribute refers to staff numbers within the part, from top to bottom
			* on the system. A value of 1 is assumed if not present. When used in the defaults element,
			* the values apply to all parts. This value is ignored for the first staff in a system.
			*/
		case class StaffLayout(staffDistance: Option[Tenths] = Option.empty,
													 number: Option[StaffNumber] = Option.empty)

		/**
			* The system-dividers element indicates the presence or absence of system dividers (also known
			* as system separation marks) between systems displayed on the same page. Dividers on the left
			* and right side of the page are controlled by the left-divider and right-divider elements
			* respectively. The default vertical position is half the system-distance value from the top of
			* the system that is below the divider. The default horizontal position is the left and right
			* system margin, respectively.
			*
			* When used in the print element, the system-dividers element affects the dividers that would
			* appear between the current system and the previous system.
			*/
		case class SystemDividers(leftDiv: EmptyPrintObjectStyleAlign = EmptyPrintObjectStyleAlign(),
															rightDiv: EmptyPrintObjectStyleAlign = EmptyPrintObjectStyleAlign())

		/**
			* A system is a group of staves that are read and played simultaneously. System layout includes
			* left and right margins and the vertical distance from the previous system. The system distance
			* is measured from the bottom line of the previous system to the top line of the current system.
			* It is ignored for the first system on a page. The top system distance is measured from the
			* page's top margin to the top line of the first system. It is ignored for all but the first
			* system on a page.
			*
			* Sometimes the sum of measure widths in a system may not equal the system width specified by the
			* layout elements due to roundoff or other errors. The behavior when reading MusicXML files in
			* these cases is application-dependent. For instance, applications may find that the system layout
			* data is more reliable than the sum of the measure widths, and adjust the measure widths
			* accordingly.
			*/
		case class SystemLayout(systemMargins: Option[SystemMargins] = Option.empty,
														systemDistance: Option[Tenths] = Option.empty,
														topSystemDistance: Option[Tenths] = Option.empty,
														systemDivider: Option[SystemDividers] = Option.empty)

		/**
			* System margins are relative to the page margins. Positive values indent and negative values
			* reduce the margin size.
			*/
		type SystemMargins = LeftRightMargins
	}

	object ComplexLink {
		import AttributeGroups.AttributeGroupsCommon.Position
		import AttributeGroups.AttributeGroupsLink.{ElementPosition, LinkAttributes}

		/**
			* The bookmark type serves as a well-defined target for an incoming simple XLink.
			*/
		case class Bookmark(id: String,
												name: Option[String] = Option.empty,
												elementPosition: ElementPosition = ElementPosition())

		/**
			* The link type serves as an outgoing simple XLink. It is also used to connect a MusicXML
			* score with a MusicXML opus. If a relative link is used within a document that is part of a
			* compressed MusicXML file, the link is relative to the  root folder of the zip file.
			*/
		case class Link(linkAttributes: LinkAttributes,
										name: Option[String] = Option.empty,
										elementPosition: ElementPosition = ElementPosition(),
										position: Position = Position())
	}

	object ComplexNote {

		import AttributeGroups.AttributeGroupsCommon.{Color, LineShape, LineType, TextDirection, _}
		import Complex.ComplexCommon._
		import Group.GroupCommon.{Editorial, EditorialVoice, Staff}
		import Group.GroupNote.{DisplayStepOctave, Duration, FullNote}
		import Primatives.PrimativeCommon._
		import Primatives.PrimativeNote._

		/**
			* The accidental type represents actual notated accidentals. Editorial and cautionary indications
			* are indicated by attributes. Values for these attributes are "no" if not present.
			* Specific graphic display such as parentheses, brackets, and size are controlled by the
			* level-display attribute group.
			*/
		case class Accidental(accidentalValue: AccidentalValue,
													cautionary: Option[YesNo] = Option.empty,
													editorial: Option[YesNo] = Option.empty,
													levelDisplay: LevelDisplay = LevelDisplay(),
													printStyle: PrintStyle = PrintStyle())

		/**
			* An accidental-mark can be used as a separate notation or as part of an ornament. When used in
			* an ornament, position and placement are relative to the ornament, not relative to the note.
			*/
		case class AccidentalMark(accidentalValue: AccidentalValue,
															printStyle: PrintStyle = PrintStyle(),
															placement: Placement = Placement())

		/**
			* The arpeggiate type indicates that this note is part of an arpeggiated chord. The number
			* attribute can be used to distinguish between two simultaneous chords arpeggiated separately
			* (different numbers) or together (same number). The up-down attribute is used if there is an
			* arrow on the arpeggio sign. By default, arpeggios go from the lowest to highest note.
			*/
		case class Arpeggiate(number: Option[NumberLevel] = Option.empty,
													direction: Option[UpDown] = Option.empty,
													position: Position = Position(),
													placement: Placement = Placement(),
													color: Color = Color())

		sealed abstract class ArticulationChoice
		case class AccentChoice(accent: EmptyPlacement = EmptyPlacement()) extends ArticulationChoice
		case class StrongAccentChoice(strongAccent: StrongAccent = StrongAccent()) extends ArticulationChoice
		case class StaccatoChoice(staccato: EmptyPlacement = EmptyPlacement()) extends ArticulationChoice
		case class TenutoChoice(tenuto: EmptyPlacement = EmptyPlacement()) extends ArticulationChoice
		case class DetachedLegatoChoice(detachedLegato: EmptyPlacement = EmptyPlacement()) extends ArticulationChoice
		case class StaccatissimoChoice(staccatissimo: EmptyPlacement = EmptyPlacement()) extends ArticulationChoice
		case class SpiccatoChoice(spiccato: EmptyPlacement = EmptyPlacement()) extends ArticulationChoice
		case class ScoopChoice(scoop: EmptyLine = EmptyLine()) extends ArticulationChoice
		case class PlopChoice(plop: EmptyLine = EmptyLine()) extends ArticulationChoice
		case class DoitChoice(diot: EmptyLine = EmptyLine()) extends ArticulationChoice
		case class FalloffChoice(fallOff: EmptyLine = EmptyLine()) extends ArticulationChoice
		case class BreathMarkChoice(breathMark: BreathMark) extends ArticulationChoice
		case class CaesuraChoice(caesura: EmptyPlacement = EmptyPlacement()) extends ArticulationChoice
		case class StressChoice(stress: EmptyPlacement = EmptyPlacement()) extends ArticulationChoice
		case class UnstressChoice(unstress: EmptyPlacement = EmptyPlacement()) extends ArticulationChoice
		case class OtherArticulationChoice(otherArticulation: PlacementText) extends ArticulationChoice

		/**
			* Articulations and accents are grouped together here.
			*
			* The accent element indicates a regular horizontal accent mark.
			*
			* The strong-accent element indicates a vertical accent mark.
			*
			* The staccato element is used for a dot articulation, as opposed to a stroke or a wedge.
			*
			* The tenuto element indicates a tenuto line symbol.
			*
			* The detached-legato element indicates the combination of a tenuto line and staccato dot symbol.
			*
			* The staccatissimo element is used for a wedge articulation, as opposed to a dot or a stroke.
			*
			* The spiccato element is used for a stroke articulation, as opposed to a dot or a wedge.
			*
			* The scoop element is an indeterminate slide attached to a single note. The scoop element appears
			* before the main note and comes from below the main pitch.
			*
			* The plop element is an indeterminate slide attached to a single note. The plop element appears
			* before the main note and comes from above the main pitch.
			*
			* The doit element is an indeterminate slide attached to a single note. The doit element appears
			* after the main note and goes above the main pitch.
			*
			* The falloff element is an indeterminate slide attached to a single note. The falloff element
			* appears before the main note and goes below the main pitch.
			*
			* The caesura element indicates a slight pause. It is notated using a "railroad tracks" symbol.
			*
			* The stress element indicates a stressed note.
			*
			* The unstress element indicates an unstressed note. It is often notated using a u-shaped symbol.
			*
			* The other-articulation element is used to define any articulations not yet in the MusicXML format.
			* This allows extended representation, though without application interoperability.
			*/
		case class Articulations(articulations: List[ArticulationChoice] = List())

		sealed abstract class ArrowChoice
		case class StraightArrowChoice(arrowDirection: ArrowDirection,
																	 arrowStyle: Option[ArrowStyle] = Option.empty) extends ArrowChoice
		case class CircleArrowChoice(circularArrow: CircularArrow) extends ArrowChoice

		/**
			* The arrow element represents an arrow used for a musical technical indication.
			*/
		case class Arrow(arrow: ArrowChoice,
										 printStyle: PrintStyle = PrintStyle(),
										 placement: Placement = Placement())

		/**
			* The backup and forward elements are required to coordinate multiple voices in one part,
			* including music on multiple staves. The backup type is generally used to move between voices
			* and staves. Thus the backup element does not include voice or staff elements. Duration values
			* should always be positive, and should not cross measure boundaries or mid-measure changes in
			* the divisions value.
			*/
		case class Backup(duration: Duration, editorial: Editorial = Editorial())

		/**
			* Beam values include begin, continue, end, forward hook, and backward hook. Up to eight concurrent
			* beams are available to cover up to 1024th notes. Each beam in a note is represented with a
			* separate beam element, starting with the eighth note beam using a number attribute of 1.
			*
			* Note that the beam number does not distinguish sets of beams that overlap, as it does for slur
			* and other elements. Beaming groups are distinguished by being in different voices and/or the
			* presence or absence of grace and cue elements.
			*
			* Beams that have a begin value can also have a fan attribute to indicate accelerandos and
			* ritardandos using fanned beams. The fan attribute may also be used with a continue value if
			* the fanning direction changes on that note. The value is "none" if not specified.
			*
			* The repeater attribute has been deprecated in MusicXML 3.0. Formerly used for tremolos, it
			* needs to be specified with a "yes" value for each beam using it.
			*/
		case class Beam(value: BeamValue,
										number: BeamLevel = BeamLevel(1),
										repeater: Option[YesNo] = Option.empty,
										fan: Option[Fan] = Option.empty,
										color: Color = Color())

		sealed abstract class BandChoice
		case class PreBand(preBand: Empty) extends BandChoice
		case class Release(release: Empty) extends BandChoice

		/**
			* The bend type is used in guitar and tablature. The bend-alter element indicates the number of
			* steps in the bend, similar to the alter element. As with the alter element, numbers like 0.5
			* can be used to indicate microtones. Negative numbers indicate pre-bends or releases;
			* the pre-bend and release elements are used to distinguish what is intended. A with-bar element
			* indicates that the bend is to be done at the bridge with a whammy or vibrato bar. The content
			* of the element indicates how this should be notated.
			*
			* The bend-alter element indicates the number of steps in the bend, similar to the alter element.
			* As with the alter element, numbers like 0.5 can be used to indicate microtones.
			* Negative numbers indicate pre-bends or releases; the pre-bend and release elements are used to
			* distinguish what is intended.
			*
			* The pre-bend element indicates that this is a pre-bend rather than a normal bend or a release.
			*
			* The release element indicates that this is a release rather than a normal bend or pre-bend.
			*
			* The with-bar element indicates that the bend is to be done at the bridge with a whammy or
			* vibrato bar. The content of the element indicates how this should be notated.
			*/
		case class Bend(bendAlter: Semitones,
										bandChoice: Option[BandChoice] = Option.empty,
										withBar: Option[PlacementText] = Option.empty,
										printStyle: PrintStyle = PrintStyle(),
										bendSound: BendSound = BendSound())

		/**
			* The breath-mark element indicates a place to take a breath.
			*/
		case class BreathMark(breathMarkValue: BreathMarkValue,
													printStyle: PrintStyle = PrintStyle(),
													placement: Placement = Placement())

		/**
			* The empty-line type represents an empty element with line-shape, line-type, dashed-formatting,
			* print-style and placement attributes.
			*/
		case class EmptyLine(lineShape: LineShape = LineShape(),
												 lineType: LineType = LineType(),
												 dashedFormatting: DashedFormatting = DashedFormatting(),
												 printStyle: PrintStyle = PrintStyle(),
												 placement: Placement = Placement())

		/**
			* The extend type represents lyric word extension / melisma lines as well as figured bass extensions.
			* The optional type and position attributes are added in Version 3.0 to provide better formatting
			* control.
			*/
		case class Extend(sscType: Option[StartStopContinue] = Option.empty,
											printStyle: PrintStyle = PrintStyle())

		/**
			* The figure type represents a single figure within a figured-bass element.
			*
			* Values for the prefix element include the accidental values sharp, flat, natural, double-sharp,
			* flat-flat, and sharp-sharp. The prefix element may contain additional values for symbols specific
			* to particular figured bass styles.
			*
			* A figure-number is a number. Overstrikes of the figure number are represented in the suffix element.
			*
			* Values for the suffix element include the accidental values sharp, flat, natural, double-sharp,
			* flat-flat, and sharp-sharp. Suffixes include both symbols that come after the figure number and
			* those that overstrike the figure number. The suffix value slash is used for slashed numbers
			* indicating chromatic alteration. The orientation and display of the slash usually depends on the
			* figure number. The suffix element may contain additional values for symbols specific to
			* particular figured bass styles.
			*/
		case class Figure(prefix: Option[StyleText] = Option.empty,
											figureNumber: Option[StyleText] = Option.empty,
											suffix: Option[StyleText] = Option.empty,
											extend: Option[Extend] = Option.empty)

		/**
			* The figured-bass element represents figured bass notation. Figured bass elements take their
			* position from the first regular note (not a grace note or chord note) that follows in score
			* order. The optional duration element is used to indicate changes of figures under a note.
			*
			* Figures are ordered from top to bottom. The value of parentheses is "no" if not present.
			*/
		case class FiguredBass(figure: List[Figure],
													 duration: Option[Duration] = Option.empty,
													 editorial: Editorial = Editorial(),
													 printStyle: PrintStyle = PrintStyle(),
													 printout: Printout = Printout(),
													 parentheses: Option[YesNo] = Option.empty) {
			require(figure.nonEmpty)
		}

		/**
			* The backup and forward elements are required to coordinate multiple voices in one part,
			* including music on multiple staves. The forward element is generally used within voices
			* and staves. Duration values should always be positive, and should not cross measure boundaries
			* or mid-measure changes in the divisions value.
			*/
		case class Forward(duration: Duration,
											 editorialVoice: EditorialVoice = EditorialVoice(),
											 staff: Option[Staff] = Option.empty)

		/**
			* Glissando and slide types both indicate rapidly moving from one pitch to the other so that
			* individual notes are not discerned. The distinction is similar to that between NIFF's glissando
			* and portamento elements. A glissando sounds the half notes in between the slide and defaults to
			* a wavy line. The optional text is printed alongside the line.
			*/
		case class Glissando(gliss: String,
												 ssType: StartStop,
												 number: NumberLevel = NumberLevel(1),
												 lineType: LineType = LineType(),
												 dashedFormatting: DashedFormatting = DashedFormatting(),
												 printStyle: PrintStyle = PrintStyle())

		/**
			* The grace type indicates the presence of a grace note. The slash attribute for a grace note is
			* yes for slashed eighth notes. The other grace note attributes come from MuseData sound
			* suggestions. The steal-time-previous attribute indicates the percentage of time to steal from
			* the previous note for the grace note. The steal-time-following attribute indicates the percentage
			* of time to steal from the following note for the grace note, as for appoggiaturas. The make-time
			* attribute indicates to make time, not steal time; the units are in real-time divisions for the
			* grace note.
			*/
		case class Grace(stealTimePrevious: Option[Percent] = Option.empty,
										 stealTimeFollowing: Option[Percent] = Option.empty,
										 makeTime: Option[Divisions] = Option.empty,
										 slash: Option[YesNo] = Option.empty)

		/**
			* The hammer-on and pull-off elements are used in guitar and fretted instrument notation.
			* Since a single slur can be marked over many notes, the hammer-on and pull-off elements are
			* separate so the individual pair of notes can be specified. The element content can be used
			* to specify how the hammer-on or pull-off should be notated. An empty element leaves this choice
			* up to the application.
			*/
		case class HammerOnPullOff(hammer: String,
															 ssType: StartStop,
															 number: NumberLevel = NumberLevel(1),
															 printStyle: PrintStyle = PrintStyle(),
															 placement: Placement = Placement())

		/**
			* The handbell element represents notation for various techniques used in handbell
			* and handchime music.
			*/
		case class Handbell(handbellValue: HandbellValue,
												printStyle: PrintStyle = PrintStyle(),
												placement: Placement = Placement())

		sealed abstract class HarmonicChoice
		case class Natural(natural: Empty) extends HarmonicChoice
		case class Artificial(artificial: Empty) extends HarmonicChoice

		sealed abstract class HarmonicPitchChoice
		case class BasePitch(basePitch: Empty) extends HarmonicPitchChoice
		case class TouchingPitch(touchingPitch: Empty) extends HarmonicPitchChoice
		case class SoundingPitch(soundingPitch: Empty) extends HarmonicPitchChoice

		/**
			* The harmonic type indicates natural and artificial harmonics. Allowing the type of pitch to be
			* specified, combined with controls for appearance/playback differences, allows both the notation
			* and the sound to be represented. Artificial harmonics can add a notated touching-pitch;
			* artificial pinch harmonics will usually not notate a touching pitch.
			* The attributes for the harmonic element refer to the use of the circular harmonic symbol,
			* typically but not always used with natural harmonics.
			*
			* The natural element indicates that this is a natural harmonic. These are usually notated at
			* base pitch rather than sounding pitch.
			*
			* The artificial element indicates that this is an artificial harmonic.
			*
			* The base pitch is the pitch at which the string is played before touching to create the harmonic.
			*
			* The touching-pitch is the pitch at which the string is touched lightly to produce the harmonic.
			*
			* The sounding-pitch is the pitch which is heard when playing the harmonic.
			*/
		case class Harmonic(harmonicChoice: Option[HarmonicChoice] = Option.empty,
												harmonicPitchChoice: Option[HarmonicPitchChoice] = Option.empty,
												printObject: PrintObject = PrintObject(),
												printStyle: PrintStyle = PrintStyle(),
												placement: Placement = Placement())

		/**
			* The heel and toe elements are used with organ pedals. The substitution value is "no" if the
			* attribute is not present.
			*/
		case class HeelToe(emptyPlacement: EmptyPlacement = EmptyPlacement(),
											 substitution: Option[YesNo] = Option.empty)

		/**
			* The hole type represents the symbols used for woodwind and brass fingerings as well as other
			* notations.
			*
			* The content of the optional hole-type element indicates what the hole symbol represents in terms
			* of instrument fingering or other techniques.
			*
			* The optional hole-shape element indicates the shape of the hole symbol; the default is a circle.
			*/
		case class Hole(holeType: Option[String] = Option.empty,
										holeClosed: HoleClosed,
										holeShape: Option[String] = Option.empty,
										printStyle: PrintStyle = PrintStyle(),
										placement: Placement = Placement())

		/**
			* The hole-closed type represents whether the hole is closed, open, or half-open. The optional
			* location attribute indicates which portion of the hole is filled in when the element value is half.
			*/
		case class HoleClosed(holeClosedValue: HoleClosedValue,
													location: Option[HoleClosedLocation] = Option.empty)

		/**
			* The instrument type distinguishes between score-instrument elements in a score-part.
			* The id attribute is an IDREF back to the score-instrument ID. If multiple score-instruments
			* are specified on a score-part, there should be an instrument element for each note in the part.
			*/
		type Instrument = String

		case class ElisionSyllabic(elision: TextFontColor, syllabic: Option[Syllabic] = Option.empty)
		case class ElisionSyllabicText(es: Option[ElisionSyllabic] = Option.empty, text: TextElementData)

		sealed abstract class LyricsChoice
		case class TextLyricsChoice(syllabic: Option[Syllabic] = Option.empty, text: TextElementData,
																est: List[ElisionSyllabicText] = List(),
																extend: Option[Extend] = Option.empty) extends LyricsChoice
		case class ExtendLyricsChoice(extend: Extend) extends LyricsChoice
		case class LaughingLyricsChoice(laughing: Empty) extends LyricsChoice
		case class HummingLyricsChoice(hummping: Empty) extends LyricsChoice

		/**
			* The lyric type represents text underlays for lyrics, based on Humdrum with support for other
			* formats. Two text elements that are not separated by an elision element are part of the same
			* syllable, but may have different text formatting. The MusicXML 2.0 XSD is more strict than the
			* 2.0 DTD in enforcing this by disallowing a second syllabic element unless preceded by an elision
			* element. The lyric number indicates multiple lines, though a name can be used as well (as in
			* Finale's verse / chorus / section specification). Justification is center by default; placement
			* is below by default. The content of the elision type is used to specify the symbol used to
			* display the elision. Common values are a no-break space (Unicode 00A0), an underscore
			* (Unicode 005F), or an undertie (Unicode 203F).
			*
			* The laughing element is taken from Humdrum.
			*
			* The humming element is taken from Humdrum.
			*
			* The end-line element comes from RP-017 for Standard MIDI File Lyric meta-events. It facilitates
			* lyric display for Karaoke and similar applications.
			*
			* The end-paragraph element comes from RP-017 for Standard MIDI File Lyric meta-events.
			* It facilitates lyric display for Karaoke and similar applications.
			*/
		case class Lyric(choice: LyricsChoice,
										 endLine: Option[Empty] = Option.empty,
										 endParagraph: Option[Empty] = Option.empty,
										 editorial: Editorial = Editorial(),
										 number: Option[String] = Option.empty,
										 name: Option[String] = Option.empty,
										 justify: Justify = Justify(),
										 position: Position = Position(),
										 placement: Placement = Placement(),
										 color: Color = Color(),
										 printObject: PrintObject = PrintObject())

		/**
			* The mordent type is used for both represents the mordent sign with the vertical line and
			* the inverted-mordent sign without the line. The long attribute is "no" by default.
			* The approach and departure attributes are used for compound ornaments, indicating how
			* the beginning and ending of the ornament look relative to the main part of the mordent.
			*/
		case class Mordent(emptyTrillSound: EmptyTrillSound = EmptyTrillSound(),
											 long: Option[YesNo] = Option.empty,
											 approach: Option[AboveBelow] = Option.empty,
											 departure: Option[AboveBelow] = Option.empty)

		/**
			* The non-arpeggiate type indicates that this note is at the top or bottom of a bracket indicating
			* to not arpeggiate these notes. Since this does not involve playback, it is only used on the top
			* or bottom notes, not on each note as for the arpeggiate type.
			*/
		case class NonArpeggiate(tbType: TopBottom,
														 number: Option[NumberLevel] = Option.empty,
														 position: Position = Position(),
														 placement: Placement = Placement(),
														 color: Color = Color())

		sealed abstract class NotationsChoice
		case class TiedNotation(tied: Tied) extends NotationsChoice
		case class SlurNotation(slur: Slur) extends NotationsChoice
		case class TupletNotation(tuplet: Tuplet) extends NotationsChoice
		case class GlissandoNotation(glissando: Glissando) extends NotationsChoice
		case class SlideNotation(slide: Slide) extends NotationsChoice
		case class OrnamentsNotation(ornaments: Ornaments = Ornaments()) extends NotationsChoice
		case class TechnicalNotation(technical: Technical = Technical()) extends NotationsChoice
		case class ArticulationsNotation(articulations: Articulations = Articulations()) extends NotationsChoice
		case class DynamicsNotation(dynamics: Dynamics = Dynamics()) extends NotationsChoice
		case class FermataNotation(fermata: Fermata) extends NotationsChoice
		case class ArpeggiateNotation(arpeggiate: Arpeggiate = Arpeggiate()) extends NotationsChoice
		case class NonArpeggiateNotation(nonArpeggiate: NonArpeggiate) extends NotationsChoice
		case class AccidentalMarkNotation(accidentalMark: AccidentalMark) extends NotationsChoice
		case class OtherNotationNotation(otherNotation: OtherNotation) extends NotationsChoice

		/**
			* Notations refer to musical notations, not XML notations. Multiple notations are allowed in order
			* to represent multiple editorial levels. The print-object attribute, added in Version 3.0,
			* allows notations to represent details of performance technique, such as fingerings,
			* without having them appear in the score.
			*/
		case class Notations(editorial: Editorial = Editorial(),
												 notations: List[NotationsChoice] = List(),
												 printObject: PrintObject = PrintObject())

		sealed abstract class NoteChoice
		case class GraceNoteChoice(grace: Grace = Grace(),
															 fullNote: FullNote,
															 tie: List[Tie] = List()) extends NoteChoice {
			require(tie.size <= 2)
		}
		case class CueNoteChoice(cue: Empty, fullNote: FullNote, duration: Duration) extends NoteChoice
		case class FullNoteChoice(fullNote: FullNote, duration: Duration, tie: List[Tie] = List()) extends NoteChoice {
			require(tie.size <= 2)
		}

		/**
			* Notes are the most common type of MusicXML data. The MusicXML format keeps the MuseData distinction
			* between elements used for sound information and elements used for notation information
			* (e.g., tie is used for sound, tied for notation). Thus grace notes do not have a duration element.
			* Cue notes have a duration element, as do forward elements, but no tie elements. Having these two
			* types of information available can make interchange considerably easier, as some programs handle
			* one type of information much more readily than the other.
			*
			* The dynamics and end-dynamics attributes correspond to MIDI 1.0's Note On and Note Off velocities,
			* respectively. They are expressed in terms of percentages of the default forte value
			* (90 for MIDI 1.0). The attack and release attributes are used to alter the starting and stopping
			* time of the note from when it would otherwise occur based on the flow of durations - information
			* that is specific to a performance. They are expressed in terms of divisions, either positive or
			* negative. A note that starts a tie should not have a release attribute, and a note that stops a
			* tie should not have an attack attribute. If a note is played only particular times through a
			* repeat, the time-only attribute shows which times to play the note. The pizzicato attribute is
			* used when just this note is sounded pizzicato, vs. the pizzicato element which changes overall
			* playback between pizzicato and arco.
			*
			* The cue element indicates the presence of a cue note.
			*
			* One dot element is used for each dot of prolongation. The placement element is used to specify
			* whether the dot should appear above or below the staff line. It is ignored for notes that appear
			* on a staff space.
			*/
		case class Note(noteChoice: NoteChoice,
										instrument: Option[Instrument] = Option.empty,
										editorialVoice: EditorialVoice = EditorialVoice(),
										noteType: Option[NoteType] = Option.empty,
										dot: List[EmptyPlacement] = List(),
										accidental: Option[Accidental] = Option.empty,
										timeModification: Option[TimeModification] = Option.empty,
										stem: Option[Stem] = Option.empty,
										notehead: Option[NoteHead] = Option.empty,
										noteheadText: Option[NoteHeadText] = Option.empty,
										staff: Option[Staff] = Option.empty,
										beam: List[Beam] = List(),
										notations: List[Notations] = List(),
										lyric: List[Lyric] = List(),
										play: Option[Play] = Option.empty,
										xPosition: XPosition = XPosition(),
										font: Font = Font(),
										color: Color = Color(),
										printout: Printout = Printout(),
										dynamics: Option[NonNegativeDecimal] = Option.empty,
										endDynamics: Option[NonNegativeDecimal] = Option.empty,
										attack: Option[Divisions] = Option.empty,
										release: Option[Divisions] = Option.empty,
										timeOnly: Option[TimeOnly] = Option.empty,
										pizzicato: Option[YesNo] = Option.empty) {
			require(beam.size <= 8)
		}

		/**
			* The note-type type indicates the graphic note type. Values range from 256th to long.
			* The size attribute indicates full, cue, or large size, with full the default for regular notes
			* and cue the default for cue and grace notes.
			*/
		case class NoteType(value: NoteTypeValue, size: Option[SymbolSize] = Option.empty)

		/**
			* The notehead element indicates shapes other than the open and closed ovals associated with note
			* durations.
			*
			* For the enclosed shapes, the default is to be hollow for half notes and longer, and filled
			* otherwise. The filled attribute can be set to change this if needed.
			*
			* If the parentheses attribute is set to yes, the notehead is parenthesized. It is no by default.
			*/
		case class NoteHead(value: NoteHeadValue,
												filled: Option[YesNo] = Option.empty,
												parentheses: Option[YesNo] = Option.empty,
												font: Font = Font(),
												color: Color = Color())

		sealed abstract class NoteHeadTextChoice
		case class DisplayTextChoice(displayText: FormattedText) extends NoteHeadTextChoice
		case class AccidentalTextChoice(accidentalText: AccidentalText) extends NoteHeadTextChoice

		/**
			* The notehead-text type represents text that is displayed inside a notehead, as is done in some
			* educational music. It is not needed for the numbers used in tablature or jianpu notation.
			* The presence of a TAB or jianpu clefs is sufficient to indicate that numbers are used.
			* The display-text and accidental-text elements allow display of fully formatted text and accidentals.
			*/
		case class NoteHeadText(choice: List[NoteHeadTextChoice]) {
			require(choice.nonEmpty)
		}

		sealed abstract class OrnamentsChoice
		case class TrillMarkOrnamentsChoice(trillMark: EmptyTrillSound = EmptyTrillSound()) extends OrnamentsChoice
		case class TurnOrnamentsChoice(turn: HorizontalTurn = HorizontalTurn()) extends OrnamentsChoice
		case class DelayedTurnOrnamentsChoice(delayedTurn: HorizontalTurn = HorizontalTurn()) extends OrnamentsChoice
		case class InvertedTurnOrnamentsChoice(invertedTurn: HorizontalTurn = HorizontalTurn()) extends OrnamentsChoice
		case class DelayedInvertedTurnedOrnamentsChoice(delayedInvertedTurn: HorizontalTurn = HorizontalTurn()) extends OrnamentsChoice
		case class VerticalTurnOrnamentsChoice(verticalTurn: EmptyTrillSound = EmptyTrillSound()) extends OrnamentsChoice
		case class ShakeOrnamentsChoice(shake: EmptyTrillSound = EmptyTrillSound()) extends OrnamentsChoice
		case class WavyLineOrnamentsChoice(wavyLine: WavyLine) extends OrnamentsChoice
		case class MordentOrnamentsChoice(mordent: Mordent = Mordent()) extends OrnamentsChoice
		case class InvertedMordentOrnamentsChoice(invertedMordent: Mordent = Mordent()) extends OrnamentsChoice
		case class SchleiferOrnamentsChoice(schleifer: EmptyPlacement = EmptyPlacement()) extends OrnamentsChoice
		case class TremoloOrnamentsChoice(tremolo: Tremolo) extends OrnamentsChoice
		case class OtherOrnamentOrnamentsChoice(otherOrnament: PlacementText) extends OrnamentsChoice

		case class OrnamentsContent(choice: OrnamentsChoice, accidentalMark: List[AccidentalMark] = List())

		/**
			* Ornaments can be any of several types, followed optionally by accidentals.
			* The accidental-mark element's content is represented the same as an accidental element,
			* but with a different name to reflect the different musical meaning.
			*
			* The trill-mark element represents the trill-mark symbol.
			*
			* The turn element is the normal turn shape which goes up then down.
			*
			* The delayed-turn element indicates a normal turn that is delayed until the end of the current note.
			*
			* The inverted-turn element has the shape which goes down and then up.
			*
			* The delayed-inverted-turn element indicates an inverted turn that is delayed until the end of
			* the current note.
			*
			* The vertical-turn element has the turn symbol shape arranged vertically going from upper left to
			* lower right.
			*
			* The shake element has a similar appearance to an inverted-mordent element.
			*
			* The mordent element represents the sign with the vertical line. The long attribute is "no"
			* by default.
			*
			* The inverted-mordent element represents the sign without the vertical line. The long attribute
			* is "no" by default.
			*
			* The name for this ornament is based on the German, to avoid confusion with the more common slide
			* element defined earlier.
			*
			* The other-ornament element is used to define any ornaments not yet in the MusicXML format.
			* This allows extended representation, though without application interoperability.
			*/
		case class Ornaments(content: List[OrnamentsContent] = List())

		/**
			* The other-notation type is used to define any notations not yet in the MusicXML format.
			* This allows extended representation, though without application interoperability.
			* It handles notations where more specific extension elements such as other-dynamics and
			* other-technical are not appropriate.
			*/
		case class OtherNotation(notation: String,
														 sssType: StartStopSingle,
														 number: NumberLevel = NumberLevel(1),
														 printObject: PrintObject = PrintObject(),
														 printStyle: PrintStyle = PrintStyle(),
														 placement: Placement = Placement())

		/**
			* Pitch is represented as a combination of the step of the diatonic scale,
			* the chromatic alteration, and the octave.
			*/
		case class Pitch(step: Step, alter: Option[Semitones] = Option.empty, octave: Octave)

		/**
			* The placement-text type represents a text element with print-style and placement attribute groups.
			*/
		case class PlacementText(placementText: String,
														 printStyle: PrintStyle = PrintStyle(),
														 placement: Placement = Placement())

		/**
			* The rest element indicates notated rests or silences. Rest elements are usually empty,
			* but placement on the staff can be specified using display-step and display-octave elements.
			* If the measure attribute is set to yes, this indicates this is a complete measure rest.
			*/
		case class Rest(displayStepOctave: Option[DisplayStepOctave] = Option.empty,
										measure: Option[YesNo] = Option.empty)

		/**
			* Glissando and slide types both indicate rapidly moving from one pitch to the other so that
			* individual notes are not discerned. The distinction is similar to that between NIFF's glissando
			* and portamento elements. A slide is continuous between two notes and defaults to a solid line.
			* The optional text for a is printed alongside the line.
			*/
		case class Slide(slide: String, ssType: StartStop,
										 number: NumberLevel = NumberLevel(1),
										 lineType: LineType = LineType(),
										 dashedFormatting: DashedFormatting = DashedFormatting(),
										 printStyle: PrintStyle = PrintStyle(),
										 bendSound: BendSound = BendSound())

		/**
			* Slur types are empty. Most slurs are represented with two elements: one with a start type,
			* and one with a stop type. Slurs can add more elements using a continue type. This is typically
			* used to specify the formatting of cross-system slurs, or to specify the shape of very complex slurs.
			*/
		case class Slur(sscType: StartStopContinue,
										number: NumberLevel = NumberLevel(1),
										lineType: LineType = LineType(),
										dashedFormatting: DashedFormatting = DashedFormatting(),
										position: Position = Position(),
										placement: Placement = Placement(),
										orientation: Orientation = Orientation(),
										bezier: Bezier = Bezier(),
										color: Color = Color())

		/**
			* Stems can be down, up, none, or double. For down and up stems, the position attributes can be
			* used to specify stem length. The relative values specify the end of the stem relative to the
			* program default. Default values specify an absolute end stem position. Negative values of
			* relative-y that would flip a stem instead of shortening it are ignored. A stem element
			* associated with a rest refers to a stemlet.
			*/
		case class Stem(stem: StemValue, yPosition: YPosition = YPosition(), color: Color = Color())

		/**
			* The strong-accent type indicates a vertical accent mark. The type attribute indicates if
			* the point of the accent is down or up.
			*/
		case class StrongAccent(emptyPlacement: EmptyPlacement = EmptyPlacement(), udType: UpDown = UD_Up)

		/**
			* The style-text type represents a text element with a print-style attribute group.
			*/
		case class StyleText(text: String, printStyle: PrintStyle = PrintStyle())

		sealed abstract class TechnicalChoice
		case class UpBowTechnicalChoice(upBow: EmptyPlacement = EmptyPlacement()) extends TechnicalChoice
		case class DownBowTechnicalChoice(downBow: EmptyPlacement = EmptyPlacement()) extends TechnicalChoice
		case class HarmonicTechnicalChoice(harmonic: Harmonic = Harmonic()) extends TechnicalChoice
		case class OpenStringTechnicalChoice(openString: EmptyPlacement = EmptyPlacement()) extends TechnicalChoice
		case class ThumbPositionTechnicalChoice(ThumbPosition: EmptyPlacement = EmptyPlacement()) extends TechnicalChoice
		case class FingeringTechnicalChoice(fingering: Fingering) extends TechnicalChoice
		case class PluckTechnicalChoice(pluck: PlacementText) extends TechnicalChoice
		case class DoubleTongueTechnicalChoice(doubleTongue: EmptyPlacement = EmptyPlacement()) extends TechnicalChoice
		case class TripleTongueTechnicalChoice(tripleTongue: EmptyPlacement = EmptyPlacement()) extends TechnicalChoice
		case class StoppedTechnicalChoice(stopped: EmptyPlacement = EmptyPlacement()) extends TechnicalChoice
		case class SnapPizzicatoTechnicalChoice(snapPizzicato: EmptyPlacement = EmptyPlacement()) extends TechnicalChoice
		case class FretTechnicalChoice(fret: Fret) extends TechnicalChoice
		case class StringTechnicalChoice(string: StringClass) extends TechnicalChoice
		case class HammerOnTechnicalChoice(hammerOn: HammerOnPullOff) extends TechnicalChoice
		case class PullOffTechnicalChoice(pullOff: HammerOnPullOff) extends TechnicalChoice
		case class BendTechnicalChoice(bend: Bend) extends TechnicalChoice
		case class TapTechnicalChoice(tap: PlacementText) extends TechnicalChoice
		case class HeelTechnicalChoice(heel: HeelToe = HeelToe()) extends TechnicalChoice
		case class ToeTechnicalChoice(toe: HeelToe = HeelToe()) extends TechnicalChoice
		case class FingernailsTechnicalChoice(fingernails: EmptyPlacement = EmptyPlacement()) extends TechnicalChoice
		case class HoleTechnicalChoice(hole: Hole) extends TechnicalChoice
		case class ArrowTechnicalChoice(arrow: Arrow) extends TechnicalChoice
		case class HandbellTechnicalChoice(handbell: Handbell) extends TechnicalChoice
		case class OtherTechnicalChoice(otherTechnical: PlacementText) extends TechnicalChoice

		/**
			* Technical indications give performance information for individual instruments.
			*
			* The up-bow element represents the symbol that is used both for up-bowing on bowed instruments,
			* and up-stroke on plucked instruments.
			*
			* The down-bow element represents the symbol that is used both for down-bowing on bowed instruments,
			* and down-stroke on plucked instruments.
			*
			* The open-string element represents the zero-shaped open string symbol.
			*
			* The thumb-position element represents the thumb position symbol. This is a circle with a line,
			* where the line does not come within the circle. It is distinct from the snap pizzicato symbol,
			* where the line comes inside the circle.
			*
			* The pluck element is used to specify the plucking fingering on a fretted instrument,
			* where the fingering element refers to the fretting fingering. Typical values are p, i, m, a for
			* pulgar/thumb, indicio/index, medio/middle, and anular/ring fingers.
			*
			* The double-tongue element represents the double tongue symbol (two dots arranged horizontally).
			*
			* The triple-tongue element represents the triple tongue symbol (three dots arranged horizontally).
			*
			* The stopped element represents the stopped symbol, which looks like a plus sign.
			*
			* The snap-pizzicato element represents the snap pizzicato symbol. This is a circle with a line,
			* where the line comes inside the circle. It is distinct from the thumb-position symbol,
			* where the line does not come inside the circle.
			*
			* The tap element indicates a tap on the fretboard. The element content allows specification of
			* the notation; + and T are common choices. If empty, the display is application-specific.
			*
			* The fingernails element is used in notation for harp and other plucked string instruments.
			*
			* The other-technical element is used to define any technical indications not yet in the MusicXML
			* format. This allows extended representation, though without application interoperability.
			*/
		case class Technical(choice: List[TechnicalChoice] = List())

		/**
			* The text-element-data type represents a syllable or portion of a syllable for lyric text underlay.
			* A hyphen in the string content should only be used for an actual hyphenated word.
			* Language names for text elements come from ISO 639, with optional country subcodes from ISO 3166.
			*/
		case class TextElementData(text: String,
															 font: Font = Font(),
															 color: Color = Color(),
															 textDecoration: TextDecoration = TextDecoration(),
															 textRotation: TextRotation = TextRotation(),
															 letterSpacing: LetterSpacing = LetterSpacing(),
															 lang: Option[String] = Option.empty,
															 textDirection: TextDirection = TextDirection())

		/**
			* The text-font-color type represents text with optional font and color information.
			* It is used for the elision element.
			*/
		case class TextFontColor(text: String,
														 font: Font = Font(),
														 color: Color = Color(),
														 textDecoration: TextDecoration = TextDecoration(),
														 textRotation: TextRotation = TextRotation(),
														 letterSpacing: LetterSpacing = LetterSpacing(),
														 lang: Option[String] = Option.empty,
														 textDirection: TextDirection = TextDirection())

		/**
			* The tie element indicates that a tie begins or ends with this note. If the tie element applies
			* only particular times through a repeat, the time-only attribute indicates which times to
			* apply it. The tie element indicates sound; the tied element indicates notation.
			*/
		case class Tie(ssType: StartStop, timeOnly: Option[TimeOnly] = Option.empty)

		/**
			* The tied type represents the notated tie. The tie element represents the tie sound.
			*
			* The number attribute is rarely needed to disambiguate ties, since note pitches will usually
			* suffice. The attribute is implied rather than defaulting to 1 as with most elements.
			* It is available for use in more complex tied notation situations.
			*/
		case class Tied(sscType: StartStopContinue,
										number: Option[NumberLevel] = Option.empty,
										lineType: LineType = LineType(),
										dashedFormatting: DashedFormatting = DashedFormatting(),
										position: Position = Position(),
										placement: Placement = Placement(),
										orientation: Orientation = Orientation(),
										bezier: Bezier = Bezier(),
										color: Color = Color())

		/**
			* Time modification indicates tuplets, double-note tremolos, and other durational changes.
			* A time-modification element shows how the cumulative, sounding effect of tuplets and double-note
			* tremolos compare to the written note type represented by the type and dot elements.
			* Nested tuplets and other notations that use more detailed information need both the
			* time-modification and tuplet elements to be represented accurately.
			*
			* The actual-notes element describes how many notes are played in the time usually occupied by
			* the number in the normal-notes element.
			*
			* The normal-notes element describes how many notes are usually played in the time occupied by
			* the number in the actual-notes element.
			*
			* If the type associated with the number in the normal-notes element is different than the
			* current note type (e.g., a quarter note within an eighth note triplet), then the normal-notes
			* type (e.g. eighth) is specified in the normal-type and normal-dot elements.
			*
			* The normal-dot element is used to specify dotted normal tuplet types.
			*/
		case class TimeModification(actualNotes: Int,
																normalNotes: Int,
																tuple: Option[(NoteTypeValue, List[Empty])] = Option.empty) {
			require(actualNotes >= 0)
			require(normalNotes >= 0)
		}

		/**
			* The tremolo ornament can be used to indicate either single-note or double-note tremolos.
			* Single-note tremolos use the single type, while double-note tremolos use the start and stop types.
			* The default is "single" for compatibility with Version 1.1. The text of the element indicates
			* the number of tremolo marks and is an integer from 0 to 8. Note that the number of attached
			* beams is not included in this value, but is represented separately using the beam element.
			*
			* When using double-note tremolos, the duration of each note in the tremolo should correspond to
			* half of the notated type value. A time-modification element should also be added with an
			* actual-notes value of 2 and a normal-notes value of 1. If used within a tuplet, this 2/1 ratio
			* should be multiplied by the existing tuplet ratio.
			*
			* Using repeater beams for indicating tremolos is deprecated as of MusicXML 3.0.
			*/
		case class Tremolo(tremoloMarks: TremoloMarks,
											 sssType: StartStopSingle = SSS_Single,
											 printStyle: PrintStyle = PrintStyle(),
											 placement: Placement = Placement())

		/**
			* A tuplet element is present when a tuplet is to be displayed graphically, in addition to the sound
			* data provided by the time-modification elements. The number attribute is used to distinguish
			* nested tuplets. The bracket attribute is used to indicate the presence of a bracket.
			* If unspecified, the results are implementation-dependent. The line-shape attribute is used to
			* specify whether the bracket is straight or in the older curved or slurred style. It is straight
			* by default.
			*
			* Whereas a time-modification element shows how the cumulative, sounding effect of tuplets and
			* double-note tremolos compare to the written note type, the tuplet element describes how this
			* is displayed. The tuplet element also provides more detailed representation information than
			* the time-modification element, and is needed to represent nested tuplets and other complex
			* tuplets accurately.
			*
			* The show-number attribute is used to display either the number of actual notes, the number of
			* both actual and normal notes, or neither. It is actual by default. The show-type attribute is
			* used to display either the actual type, both the actual and normal types, or neither. It is
			* none by default.
			*
			* The tuplet-actual element provide optional full control over how the actual part of the tuplet
			* is displayed, including number and note type (with dots). If any of these elements are absent,
			* their values are based on the time-modification element.
			*
			* The tuplet-normal element provide optional full control over how the normal part of the tuplet
			* is displayed, including number and note type (with dots). If any of these elements are absent,
			* their values are based on the time-modification element.
			*/
		case class Tuplet(tupletActual: Option[TupletPortion] = Option.empty,
											tupletNormal: Option[TupletPortion] = Option.empty,
											ssType: StartStop,
											number: Option[NumberLevel] = Option.empty,
											bracket: Option[YesNo] = Option.empty,
											showNumber: Option[ShowTuplet] = Option.empty,
											showType: Option[ShowTuplet] = Option.empty,
											lineShape: LineShape = LineShape(),
											position: Position = Position(),
											placement: Placement = Placement())

		/**
			* The tuplet-dot type is used to specify dotted normal tuplet types.
			*/
		case class TupletDot(font: Font = Font(), color: Color = Color())

		/**
			* The tuplet-number type indicates the number of notes for this portion of the tuplet.
			*/
		case class TupletNumber(number: Int, font: Font = Font(), color: Color = Color()) {
			require(number > 0)
		}

		/**
			* The tuplet-portion type provides optional full control over tuplet specifications.
			* It allows the number and note type (including dots) to be set for the actual and normal
			* portions of a single tuplet. If any of these elements are absent, their values are based on
			* the time-modification element.
			*/
		case class TupletPortion(tupletNumber: Option[TupletNumber] = Option.empty,
														 tupletType: Option[TupletType] = Option.empty,
														 tupletDot: List[TupletDot] = List())

		/**
			* The tuplet-type type indicates the graphical note type of the notes for this portion of the tuplet.
			*/
		case class TupletType(value: NoteTypeValue, font: Font = Font(), color: Color = Color())

		/**
			* The unpitched type represents musical elements that are notated on the staff but lack definite
			* pitch, such as unpitched percussion and speaking voice.
			*/
		case class Unpitched(displayStepOctave: Option[DisplayStepOctave] = Option.empty)
	}

	object ComplexScore {

		import AttributeGroups.AttributeGroupsCommon.{Color, _}
		import AttributeGroups.AttributeGroupsLink.LinkAttributes
		import AttributeGroups.AttributeGroupsScore.{GroupNameText, PartNameText}
		import Complex.ComplexCommon._
		import Complex.ComplexDirection.Image
		import Complex.ComplexIdentity.Identification
		import Complex.ComplexLayout.{Appearance, Scaling}
		import Complex.ComplexLink.{Bookmark, Link}
		import Group.GroupCommon.Editorial
		import Group.GroupLayout.Layout
		import Primatives.PrimativeCommon.{PositiveIntegerOrEmpty, StartStop}
		import Primatives.PrimativeScore.{GroupBarlineValue, GroupSymbolValue}

		sealed abstract class CreditChoice
		case class CreditImageChoice(creditImage: Image) extends CreditChoice
		case class CreditPairChoice(creditWords: FormattedText,
																links: List[(List[Link], List[Bookmark], FormattedText)] = List()) extends CreditChoice

		/**
			* The credit type represents the appearance of the title, composer, arranger, lyricist, copyright,
			* dedication, and other text and graphics that commonly appears on the first page of a score.
			* The credit-words and credit-image elements are similar to the words and image elements for
			* directions. However, since the credit is not part of a measure, the default-x and default-y
			* attributes adjust the origin relative to the bottom left-hand corner of the first page.
			* The enclosure for credit-words is none by default.
			*
			* By default, a series of credit-words elements within a single credit element follow one another
			* in sequence visually. Non-positional formatting attributes are carried over from the previous
			* element by default.
			*
			* The page attribute for the credit element, new in Version 2.0, specifies the page number where
			* the credit should appear. This is an integer value that starts with 1 for the first page.
			* Its value is 1 by default. Since credits occur before the music, these page numbers do not refer
			* to the page numbering specified by the print element's page-number attribute.
			*
			* The credit-type element, new in Version 3.0, indicates the purpose behind a credit.
			* Multiple types of data may be combined in a single credit, so multiple elements may be used.
			* Standard values include page number, title, subtitle, composer, arranger, lyricist, and rights.
			*/
		case class Credit(creditType: List[String] = List(),
											link: List[Link] = List(),
											bookmark: List[Bookmark] = List(),
											creditChoice: CreditChoice,
											page: Option[Int] = Option.empty) {
			require(page forall (_ >= 1))
		}

		/**
			* The defaults type specifies score-wide defaults for scaling, layout, and appearance.
			*/
		case class Defaults(scaling: Option[Scaling] = Option.empty,
												layout: Layout = Layout(),
												appearance: Option[Appearance] = Option.empty,
												musicFont: Option[EmptyFont] = Option.empty,
												wordFont: Option[EmptyFont] = Option.empty,
												lyricFont: List[LyricFont] = List(),
												lyricLanguage: List[LyricLanguage] = List())

		/**
			* The empty-font type represents an empty element with font attributes.
			*/
		case class EmptyFont(font: Font = Font())

		/**
			* The group-barline type indicates if the group should have common barlines.
			*/
		case class GroupBarline(groupBarlineValue: GroupBarlineValue, color: Color = Color())

		/**
			* The group-name type describes the name or abbreviation of a part-group element.
			* Formatting attributes in the group-name type are deprecated in Version 2.0 in favor of the
			* new group-name-display and group-abbreviation-display elements.
			*/
		case class GroupName(groupName: String, groupNameText: GroupNameText = GroupNameText())

		/**
			* The group-symbol type indicates how the symbol for a group is indicated in the score.
			*/
		case class GroupSymbol(groupSymbolValue: GroupSymbolValue,
													 position: Position = Position(),
													 color: Color = Color())

		/**
			* The lyric-font type specifies the default font for a particular name and number of lyric.
			*/
		case class LyricFont(number: Option[String] = Option.empty,
												 name: Option[String] = Option.empty,
												 font: Font = Font())

		/**
			* The lyric-language type specifies the default language for a particular name and number of lyric.
			*/
		case class LyricLanguage(number: Option[String] = Option.empty,
														 name: Option[String] = Option.empty,
														 lang: String)

		/**
			* The opus type represents a link to a MusicXML opus document that composes multiple MusicXML
			* scores into a collection.
			*/
		type Opus = LinkAttributes

		/**
			* The part-group element indicates groupings of parts in the score, usually indicated by braces and
			* brackets. Braces that are used for multi-staff parts should be defined in the attributes element
			* for that part. The part-group start element appears before the first score-part in the group.
			* The part-group stop element appears after the last score-part in the group.
			*
			* The number attribute is used to distinguish overlapping and nested part-groups, not the sequence
			* of groups. As with parts, groups can have a name and abbreviation. Values for the child elements
			* are ignored at the stop of a group.
			*
			* A part-group element is not needed for a single multi-staff part. By default, multi-staff parts
			* include a brace symbol and (if appropriate given the bar-style) common barlines. The symbol
			* formatting for a multi-staff part can be more fully specified using the part-symbol element.
			*
			* Formatting specified in the group-name-display element overrides formatting specified in the
			* group-name element.
			*
			* Formatting specified in the group-abbreviation-display element overrides formatting specified
			* in the group-abbreviation element.
			*
			* The group-time element indicates that the displayed time signatures should stretch across all
			* parts and staves in the group.
			*/
		case class PartGroup(groupName: Option[GroupName] = Option.empty,
												 groupNameDisplay: Option[NameDisplay] = Option.empty,
												 groupAbbreviation: Option[GroupName] = Option.empty,
												 groupAbbreviationDisplay: Option[NameDisplay] = Option.empty,
												 groupSymbol: Option[GroupSymbol] = Option.empty,
												 groupBarline: Option[GroupBarline] = Option.empty,
												 groupTime: Option[Empty] = Option.empty,
												 editorial: Editorial = Editorial(),
												 ssType: StartStop, number: String = "1")

		sealed abstract class PartGroupOrScoreChoice
		case class PartGroupChoice(partGroup: Group.GroupScore.PartGroup) extends PartGroupOrScoreChoice
		case class ScorePartChoice(scorePart: Group.GroupScore.ScorePart) extends PartGroupOrScoreChoice

		/**
			* The part-list identifies the different musical parts in this movement. Each part has an ID that
			* is used later within the musical data. Since parts may be encoded separately and combined later,
			* identification elements are present at both the score and score-part levels. There must be at
			* least one score-part, combined as desired with part-group elements that indicate braces and
			* brackets. Parts are ordered from top to bottom in a score based on the order in which they
			* appear in the part-list.
			*/
		case class PartList(partGroup: List[Group.GroupScore.PartGroup] = List(),
												scorePart: Group.GroupScore.ScorePart,
												groupOrScore: List[PartGroupOrScoreChoice] = List())

		/**
			* The part-name type describes the name or abbreviation of a score-part element.
			* Formatting attributes for the part-name element are deprecated in Version 2.0 in favor of the
			* new part-name-display and part-abbreviation-display elements.
			*/
		case class PartName(name: String, partNameText: PartNameText = PartNameText())

		sealed abstract class InstrumentChoice
		case class SoloChoice(solo: Empty) extends InstrumentChoice
		case class EnsembleChoice(ensemble: PositiveIntegerOrEmpty) extends InstrumentChoice

		/**
			* The score-instrument type represents a single instrument within a score-part. As with the
			* score-part type, each score-instrument has a required ID attribute, a name, and an optional
			* abbreviation.
			*
			* A score-instrument type is also required if the score specifies MIDI 1.0 channels, banks,
			* or programs. An initial midi-instrument assignment can also be made here. MusicXML software
			* should be able to automatically assign reasonable channels and instruments without these elements
			* in simple cases, such as where part names match General MIDI instrument names.
			*
			* The instrument-name element is typically used within a software application, rather than appearing
			* on the printed page of a score.
			*
			* The optional instrument-abbreviation element is typically used within a software application,
			* rather than appearing on the printed page of a score.
			*
			* The instrument-sound element describes the default timbre of the score-instrument.
			* This description is independent of a particular virtual or MIDI instrument specification and
			* allows playback to be shared more easily between applications and libraries.
			*
			* The solo element was added in Version 2.0. It is present if performance is intended by a solo
			* instrument.
			*
			* The ensemble element was added in Version 2.0. It is present if performance is intended by an
			* ensemble such as an orchestral section. The text of the ensemble element contains the size of
			* the section, or is empty if the ensemble size is not specified.
			*/
		case class ScoreInstrument(instrumentName: String,
															 instrumentAbbreviation: Option[String] = Option.empty,
															 instrumentSound: Option[String] = Option.empty,
															 instrumentChoice: Option[InstrumentChoice] = Option.empty,
															 virtualInstrument: Option[VirtualInstrument] = Option.empty,
															 id: String)

		/**
			* Each MusicXML part corresponds to a track in a Standard MIDI Format 1 file. The score-instrument
			* elements are used when there are multiple instruments per track. The midi-device element is used
			* to make a MIDI device or port assignment for the given track or specific MIDI instruments.
			* Initial midi-instrument assignments may be made here as well.
			*
			* The group element allows the use of different versions of the part for different purposes.
			* Typical values include score, parts, sound, and data. Ordering information that is directly
			* encoded in MuseData can be derived from the ordering within a MusicXML score or opus.
			*/
		case class ScorePart(identification: Option[Identification] = Option.empty,
												 partName: PartName,
												 partNameDisplay: Option[NameDisplay] = Option.empty,
												 partAbbreviation: Option[PartName] = Option.empty,
												 partAbbreviationDisplay: Option[NameDisplay] = Option.empty,
												 group: List[String] = List(),
												 scoreInstrument: List[ScoreInstrument] = List(),
												 midi: List[(Option[MidiDevice], Option[MidiInstrument])] = List(),
												 id: String)

		/**
			* The virtual-instrument element defines a specific virtual instrument used for an instrument sound.
			*
			* The virtual-library element indicates the virtual instrument library name.
			*
			* The virtual-name element indicates the library-specific name for the virtual instrument.
			*/
		case class VirtualInstrument(virtualLibrary: Option[String] = Option.empty,
																 virtualName: Option[String] = Option.empty)

		/**
			* Works are optionally identified by number and title. The work type also may indicate a link to
			* the opus document that composes multiple scores into a collection.
			*
			* The work-number element specifies the number of a work, such as its opus number.
			*
			* The work-title element specifies the title of a work, not including its opus or other work number.
			*/
		case class Work(workNumber: Option[String] = Option.empty,
										workTitle: Option[String] = Option.empty,
										opus: Option[Opus] = Option.empty)
	}
}
