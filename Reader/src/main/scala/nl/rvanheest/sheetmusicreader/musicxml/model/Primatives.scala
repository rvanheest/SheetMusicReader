package nl.rvanheest.sheetmusicreader.musicxml.model

object Primatives {

	object PrimativeAttributes {
		/**
			* The cancel-location type is used to indicate where a key signature cancellation appears
			* relative to a new key signature: to the left, to the right, or before the barline and
			* to the left. It is left by default. For mid-measure key elements, a cancel-location of
			* before-barline should be treated like a cancel-location of left.
			*/
		sealed abstract class CancelLocation
		case object CancelLocation_Left extends CancelLocation
		case object CancelLocation_Right extends CancelLocation
		case object CancelLocation_BeforeBarline extends CancelLocation

		/**
			* The clef-sign element represents the different clef symbols. The jianpu sign indicates
			* that the music that follows should be in jianpu numbered notation, just as the TAB sign
			* indicates that the music that follows should be in tablature notation.
			* Unlike TAB, a jianpu sign does not correspond to a visual clef notation.
			*/
		sealed abstract class ClefSign
		case object ClefSign_G extends ClefSign
		case object ClefSign_F extends ClefSign
		case object ClefSign_C extends ClefSign
		case object ClefSign_Percussion extends ClefSign
		case object ClefSign_TAB extends ClefSign
		case object ClefSign_Jianpu extends ClefSign
		case object ClefSign_None extends ClefSign

		/**
			* The fifths type represents the number of flats or sharps in a traditional key signature.
			* Negative numbers are used for flats and positive numbers for sharps, reflecting the key's
			* placement within the circle of fifths (hence the type name).
			*/
		type Fifths = Int

		/**
			* The mode type is used to specify major/minor and other mode distinctions. Valid mode values
			* include major, minor, dorian, phrygian, lydian, mixolydian, aeolian, ionian, locrian, and none.
			*/
		type Mode = String

		/**
			* The show-frets type indicates whether to show tablature frets as numbers (0, 1, 2) or
			* letters (a, b, c). The default choice is numbers.
			*/
		sealed abstract class ShowFrets
		case object ShowFrets_Numbers extends ShowFrets
		case object ShowFrets_Letters extends ShowFrets

		/**
			* The staff-line type indicates the line on a given staff. Staff lines are numbered from bottom
			* to top, with 1 being the bottom line on a staff. Staff line values can be used to specify
			* positions outside the staff, such as a C clef positioned in the middle of a grand staff.
			*/
		type StaffLine = Int

		/**
			* The staff-number type indicates staff numbers within a multi-staff part. Staves are numbered
			* from top to bottom, with 1 being the top staff on a part.
			*/
		case class StaffNumber(num: Int) {
			require(num > 0, s"the staff number must be larger than 0, but was $num")
		}

		/**
			* The staff-type value can be ossia, cue, editorial, regular, or alternate. An alternate staff
			* indicates one that shares the same musical data as the prior staff, but displayed differently
			* (e.g., treble and bass clef, standard notation and tab).
			*/
		sealed abstract class StaffType
		case object StaffType_Ossia extends StaffType
		case object StaffType_Cue extends StaffType
		case object StaffType_Editorial extends StaffType
		case object StaffType_Regular extends StaffType
		case object StaffType_Alternate extends StaffType

		/**
			* The time-relation type indicates the symbol used to represent the interchangeable aspect of
			* dual time signatures.
			*/
		sealed abstract class TimeRelation
		case object TimeRelation_Parentheses extends TimeRelation
		case object TimeRelation_Bracket extends TimeRelation
		case object TimeRelation_Equals extends TimeRelation
		case object TimeRelation_Slash extends TimeRelation
		case object TimeRelation_Space extends TimeRelation
		case object TimeRelation_Hyphen extends TimeRelation

		/**
			* The time-separator type indicates how to display the arrangement between the beats and beat-type
			* values in a time signature. The default value is none. The horizontal, diagonal, and vertical
			* values represent horizontal, diagonal lower-left to upper-right, and vertical lines respectively.
			* For these values, the beats and beat-type values are arranged on either side of the separator line.
			* The none value represents no separator with the beats and beat-type arranged vertically.
			* The adjacent value represents no separator with the beats and beat-type arranged horizontally.
			*/
		sealed abstract class TimeSeparator
		case object TimeSeparator_None extends TimeSeparator
		case object TimeSeparator_Horizontal extends TimeSeparator
		case object TimeSeparator_Diagonal extends TimeSeparator
		case object TimeSeparator_Vertical extends TimeSeparator
		case object TimeSeparator_Adjacent extends TimeSeparator

		/**
			* The time-symbol type indicates how to display a time signature. The normal value is the usual
			* fractional display, and is the implied symbol type if none is specified.
			* Other options are the common and cut time symbols, as well as a single number with an implied
			* denominator. The note symbol indicates that the beat-type should be represented with the
			* corresponding downstem note rather than a number. The dotted-note symbol indicates that the
			* beat-type should be represented with a dotted downstem note that corresponds to three times
			* the beat-type value, and a numerator that is one third the beats value.
			*/
		sealed abstract class TimeSymbol
		case object TimeSymbol_Common extends TimeSymbol
		case object TimeSymbol_Cut extends TimeSymbol
		case object TimeSymbol_SingleNumber extends TimeSymbol
		case object TimeSymbol_Note extends TimeSymbol
		case object TimeSymbol_DottedNote extends TimeSymbol
		case object TimeSymbol_Normal extends TimeSymbol
	}

	object PrimativeBarline {
		/**
			* The backward-forward type is used to specify repeat directions. The start of the repeat has
			* a forward direction while the end of the repeat has a backward direction.
			*/
		sealed abstract class BackwardForward
		case object BF_Backward extends BackwardForward
		case object BF_Forward extends BackwardForward

		/**
			* The bar-style type represents barline style information. Choices are regular, dotted, dashed,
			* heavy, light-light, light-heavy, heavy-light, heavy-heavy, tick (a short stroke through the
			* top line), short (a partial barline between the 2nd and 4th lines), and none.
			*/
		sealed abstract class BarStyle
		case object BarStyle_Regular extends BarStyle
		case object BarStyle_Dotted extends BarStyle
		case object BarStyle_Dashed extends BarStyle
		case object BarStyle_Heavy extends BarStyle
		case object BarStyle_LightLight extends BarStyle
		case object BarStyle_LightHeavy extends BarStyle
		case object BarStyle_HeavyLight extends BarStyle
		case object BarStyle_HeavyHeavy extends BarStyle
		case object BarStyle_Tick extends BarStyle
		case object BarStyle_Short extends BarStyle
		case object BarStyle_None extends BarStyle

		/**
			* The ending-number type is used to specify either a comma-separated list of positive integers
			* without leading zeros, or a string of zero or more spaces. It is used for the number attribute
			* of the ending element. The zero or more spaces version is used when software knows that an
			* ending is present, but cannot determine the type of the ending.
			*/
		case class EndingNumber(ending: String) {
			require(ending matches "([ ]*)|([1-9][0-9]*(, ?[1-9][0-9]*)*)",
				s"""ending number "$ending" does not match the expected pattern""")
		}

		/**
			* The right-left-middle type is used to specify barline location.
			*/
		sealed abstract class RightLeftMiddle
		case object RLM_Right extends RightLeftMiddle
		case object RLM_Left extends RightLeftMiddle
		case object RLM_Middle extends RightLeftMiddle

		/**
			* The start-stop-discontinue type is used to specify ending types. Typically, the start type is
			* associated with the left barline of the first measure in an ending. The stop and discontinue
			* types are associated with the right barline of the last measure in an ending. Stop is used when
			* the ending mark concludes with a downward jog, as is typical for first endings. Discontinue is
			* used when there is no downward jog, as is typical for second endings that do not conclude a piece.
			*/
		sealed abstract class StartStopDiscontinue
		case object SSD_Start extends StartStopDiscontinue
		case object SSD_Stop extends StartStopDiscontinue
		case object SSD_Discontinue extends StartStopDiscontinue

		/**
			* The winged attribute indicates whether the repeat has winged extensions that appear above and
			* below the barline. The straight and curved values represent single wings, while the
			* double-straight and double-curved values represent double wings. The none value indicates no
			* wings and is the default.
			*/
		sealed abstract class Winged
		case object Winged_None extends Winged
		case object Winged_Straight extends Winged
		case object Winged_Curved extends Winged
		case object Winged_DoubleStraight extends Winged
		case object Winged_DoubleCurved extends Winged
	}

	object PrimativeCommon {
		/**
			* The above-below type is used to indicate whether one element
			* appears above or below another element.
			*/
		sealed abstract class AboveBelow
		case object AB_Above extends AboveBelow
		case object AB_Below extends AboveBelow

		/**
			* The MusicXML format supports six levels of beaming, up to 1024th notes.
			* Unlike the number-level type, the beam-level type identifies concurrent beams in a beam group.
			* It does not distinguish overlapping beams such as grace notes within regular notes,
			* or beams used in different voices.
			*/
		case class BeamLevel(number: Int) {
			require(number >= 1, s"the beam level must be larger than or equal to 1, but was $number")
			require(number <= 8, s"the beam level must be smaller than or equal to 8, but was $number")
		}

		/**
			* The color type indicates the color of an element. Color may be represented as hexadecimal
			* RGB triples, as in HTML, or as hexadecimal ARGB tuples, with the A indicating alpha of transparency.
			* An alpha value of 00 is totally transparent; FF is totally opaque.
			* If RGB is used, the A value is assumed to be FF.
			* For instance, the RGB value "#800080" represents purple.
			* An ARGB value of "#40800080" would be a transparent purple.
			* As in SVG 1.1, colors are defined in terms of the sRGB color space (IEC 61966).
			*/
		case class Color(color: String) {
			require(color matches "#[\\dA-F]{6}([\\dA-F][\\dA-F])?",
				s"""color "$color" does not match the expected pattern""")
		}

		/**
			* The comma-separated-text type is used to specify a comma-separated list of text elements,
			* as is used by the font-family attribute.
			*/
		case class CommaSeparatedText(text: String) {
			require(text matches "[^,]+(, ?[^,]+)*",
				s"""comma separated text "$text" does not match the expected pattern""")
		}

		/**
			* The css-font-size type includes the CSS font sizes used as an alternative to a numeric point size.
			*/
		sealed abstract class CssFontSize
		case object CssFontSize_xxSmall extends CssFontSize
		case object CssFontSize_xSmall extends CssFontSize
		case object CssFontSize_Small extends CssFontSize
		case object CssFontSize_Medium extends CssFontSize
		case object CssFontSize_Large extends CssFontSize
		case object CssFontSize_xLarge extends CssFontSize
		case object CssFontSize_xxLarge extends CssFontSize

		/**
			* The divisions type is used to express values in terms of the musical divisions defined by the
			* divisions element. It is preferred that these be integer values both for MIDI interoperability
			* and to avoid roundoff errors.
			*/
		type Divisions = Double

		/**
			* The enclosure-shape type describes the shape and presence / absence of an enclosure around text
			* or symbols. A bracket enclosure is similar to a rectangle with the bottom line missing, as is
			* common in jazz notation.
			*/
		sealed abstract class EnclosureShape
		case object EnclosureShape_Rectangle extends EnclosureShape
		case object EnclosureShape_Square extends EnclosureShape
		case object EnclosureShape_Oval extends EnclosureShape
		case object EnclosureShape_Circle extends EnclosureShape
		case object EnclosureShape_Bracket extends EnclosureShape
		case object EnclosureShape_Triangle extends EnclosureShape
		case object EnclosureShape_Diamond extends EnclosureShape
		case object EnclosureShape_None extends EnclosureShape

		/**
			* The fermata-shape type represents the shape of the fermata sign.
			* The empty value is equivalent to the normal value.
			*/
		sealed abstract class FermataShape
		case object FermataShape_Normal extends FermataShape
		case object FermataShape_Angled extends FermataShape
		case object FermataShape_Square extends FermataShape
		case object FermataShape_Empty extends FermataShape

		/**
			* The font-size can be one of the CSS font sizes or a numeric point size.
			*/
		sealed abstract class FontSize
		case class FS_Double(size: Double) extends FontSize
		case class FS_CssFontSize(size: CssFontSize) extends FontSize

		/**
			* The font-style type represents a simplified version of the CSS font-style property.
			*/
		sealed abstract class FontStyle
		case object FontStyle_Normal extends FontStyle
		case object FontStyle_Italic extends FontStyle

		/**
			* The font-weight type represents a simplified version of the CSS font-weight property.
			*/
		sealed abstract class FontWeight
		case object FontWeight_Normal extends FontWeight
		case object FontWeight_Bold extends FontWeight

		/**
			* The left-center-right type is used to define horizontal alignment and text justification.
			*/
		sealed abstract class LeftCenterRight
		case object LCR_Left extends LeftCenterRight
		case object LCR_Center extends LeftCenterRight
		case object LCR_Right extends LeftCenterRight

		/**
			* The left-right type is used to indicate whether one element appears to the left or
			* the right of another element.
			*/
		sealed abstract class LeftRight
		case object LR_Left extends LeftRight
		case object LR_Right extends LeftRight

		/**
			* The line-shape type distinguishes between straight and curved lines.
			*/
		sealed abstract class LineShape
		case object LineShape_Straight extends LineShape
		case object LineShape_Curved extends LineShape

		/**
			* The line-type type distinguishes between solid, dashed, dotted, and wavy lines.
			*/
		sealed abstract class LineType
		case object LineType_Solid extends LineType
		case object LineType_Dashed extends LineType
		case object LineType_Dotted extends LineType
		case object LineType_Wavy extends LineType

		/**
			* The midi-16 type is used to express MIDI 1.0 values that range from 1 to 16.
			*/
		case class Midi16(value: Int) {
			require(value >= 1,
				s"midi16 value must be larger than or equal to 1, but was $value")
			require(value <= 16,
				s"midi16 value must be smaller than or equal to 16, but was $value")
		}

		/**
			* The midi-16 type is used to express MIDI 1.0 values that range from 1 to 128.
			*/
		case class Midi128(value: Int) {
			require(value >= 1,
				s"midi128 value must be larger than or equal to 1, but was $value")
			require(value <= 128,
				s"midi128 value must be smaller than or equal to 128, but was $value")
		}

		/**
			* The midi-16 type is used to express MIDI 1.0 values that range from 1 to 16,384.
			*/
		case class Midi16384(value: Int) {
			require(value >= 1,
				s"midi16384 value must be larger than or equal to 1, but was $value")
			require(value <= 16384,
				s"midi16384 value must be smaller than or equal to 16384, but was $value")
		}

		/**
			* The mute type represents muting for different instruments, including brass, winds, and strings.
			* The on and off values are used for undifferentiated mutes. The remaining values represent
			* specific mutes.
			*/
		sealed abstract class Mute
		case object Mute_On extends Mute
		case object Mute_Off extends Mute
		case object Mute_Straight extends Mute
		case object Mute_Cup extends Mute
		case object Mute_HarmonNoStem extends Mute
		case object Mute_HarmonStem extends Mute
		case object Mute_Bucket extends Mute
		case object Mute_Plunger extends Mute
		case object Mute_Hat extends Mute
		case object Mute_SoloTone extends Mute
		case object Mute_Practice extends Mute
		case object Mute_StopMute extends Mute
		case object Mute_StopHand extends Mute
		case object Mute_Echo extends Mute
		case object Mute_Palm extends Mute

		/**
			* The non-negative-decimal type specifies a non-negative decimal value.
			*/
		case class NonNegativeDecimal(num: Double) {
			require(num >= 0, s"a non negative decimal must be larger than or equal to 0, but was $num")
		}

		/**
			* Slurs, tuplets, and many other features can be concurrent and overlapping
			* within a single musical part. The number-level type distinguishes up to six
			* concurrent objects of the same type. A reading program should be prepared to
			* handle cases where the number-levels stop in an arbitrary order. Different
			* numbers are needed when the features overlap in MusicXML document order.
			* When a number-level value is implied, the value is 1 by default.
			*/
		case class NumberLevel(level: Int) {
			require(level >= 1, s"number level must be larger than or equal to 1, but was $level")
			require(level <= 6, s"number level must be smaller than or equal to 6, but was $level")
		}

		/**
			* The number-of-lines type is used to specify the number of lines in text decoration attributes.
			*/
		case class NumberOfLines(lines: Int) {
			require(lines >= 0, s"number of lines must be larger than or equal to 0, but was $lines")
			require(lines <= 3, s"number of lines must be smaller than or equal to 3, but was $lines")
		}

		/**
			* The number-or-normal values can be either a decimal number or the string "normal".
			* This is used by the line-height and letter-spacing attributes.
			*/
		sealed abstract class NumberOrNormal
		case class NON_Double(n: Double) extends NumberOrNormal
		case object NON_Normal extends NumberOrNormal

		/**
			* The over-under type is used to indicate whether the tips of curved lines such as slurs
			* and ties are overhand (tips down) or underhand (tips up).
			*/
		sealed abstract class OverUnder
		case object OU_Over extends OverUnder
		case object OU_Under extends OverUnder

		/**
			* The percent type specifies a percentage from 0 to 100.
			*/
		case class Percent(percent: Double) {
			require(percent >= 0,
				s"percent value must be larger than or equal to 0, but was $percent")
			require(percent <= 100,
				s"percent value must be smaller than or equal to 100, but was $percent")
		}

		/**
			* The positive-decimal type specifies a positive decimal value.
			*/
		case class PositiveDecimal(decimal: Double) {
			require(decimal > 0, s""""positive decimals" (which was $decimal) should be larger than 0""")
		}

		/**
			* The positive-divisions type restricts divisions values to positive numbers.
			*/
		case class PositiveDivisions(divs: Divisions) {
			require(divs > 0, s"divisions (which was $divs) should be larger than 0")
		}

		/**
			* The positive-integer-or-empty values can be either a positive integer or an empty string.
			*/
		case class PositiveIntegerOrEmpty(posInt: Option[Int] = Option.empty) {
			require(posInt.forall(_ > 0), s"this value must be larger than 0 or empty, but was $posInt")
		}

		/**
			* The rotation-degrees type specifies rotation, pan, and elevation values in degrees.
			* Values range from -180 to 180.
			*/
		case class RotationDegrees(degrees: Double) {
			require(degrees >= -180,
				s"the rotation degrees must be larger than or equal to -180, but was $degrees")
			require(degrees <= 180,
				s"the rotation degrees must be smaller than or equal to 180, but was $degrees")
		}

		/**
			* The semi-pitched type represents categories of indefinite pitch for percussion instruments.
			*/
		sealed abstract class SemiPitched
		case object SemiPitched_High extends SemiPitched
		case object SemiPitched_MediumHigh extends SemiPitched
		case object SemiPitched_Medium extends SemiPitched
		case object SemiPitched_MediumLow extends SemiPitched
		case object SemiPitched_Low extends SemiPitched
		case object SemiPitched_VeryLow extends SemiPitched

		/**
			* The start-note type describes the starting note of trills and mordents for playback,
			* relative to the current note.
			*/
		sealed abstract class StartNote
		case object StartNote_Upper extends StartNote
		case object StartNote_Main extends StartNote
		case object StartNote_Below extends StartNote

		/**
			* The start-stop type is used for an attribute of musical elements that can either start or stop,
			* such as tuplets.
			*
			* The values of start and stop refer to how an element appears in musical score order,
			* not in MusicXML document order. An element with a stop attribute may precede the corresponding
			* element with a start attribute within a MusicXML document. This is particularly common in
			* multi-staff music. For example, the stopping point for a tuplet may appear in staff 1 before the
			* starting point for the tuplet appears in staff 2 later in the document.
			*/
		sealed abstract class StartStop
		case object SS_Start extends StartStop
		case object SS_Stop extends StartStop

		/**
			* The start-stop-continue type is used for an attribute of musical elements that can either start
			* or stop, but also need to refer to an intermediate point in the symbol, as for complex slurs or
			* for formatting of symbols across system breaks.
			*
			* The values of start, stop, and continue refer to how an element appears in musical score order,
			* not in MusicXML document order. An element with a stop attribute may precede the corresponding
			* element with a start attribute within a MusicXML document. This is particularly common in
			* multi-staff music. For example, the stopping point for a slur may appear in staff 1 before the
			* starting point for the slur appears in staff 2 later in the document.
			*/
		sealed abstract class StartStopContinue
		case object SSC_Start extends StartStopContinue
		case object SSC_Stop extends StartStopContinue
		case object SSC_Continue extends StartStopContinue

		/**
			* The start-stop-single type is used for an attribute of musical elements that can be used for
			* either multi-note or single-note musical elements, as for tremolos.
			*/
		sealed abstract class StartStopSingle
		case object SSS_Start extends StartStopSingle
		case object SSS_Stop extends StartStopSingle
		case object SSS_Single extends StartStopSingle

		/**
			* The string-number type indicates a string number. Strings are numbered from high to low,
			* with 1 being the highest pitched string.
			*/
		case class StringNumber(num: Int) {
			require(num > 0, s"string number must be larger than 0, but was $num")
		}

		/**
			* The symbol-size type is used to indicate full vs. cue-sized vs. oversized symbols.
			* The large value for oversized symbols was added in version 1.1.
			*/
		sealed abstract class SymbolSize
		case object SymbolSize_Full extends SymbolSize
		case object SymbolSize_Cue extends SymbolSize
		case object SymbolSize_Large extends SymbolSize

		/**
			* The tenths type is a number representing tenths of interline staff space (positive or negative).
			* Both integer and decimal values are allowed, such as 5 for a half space and 2.5 for a quarter
			* space. Interline space is measured from the middle of a staff line.
			*
			* Distances in a MusicXML file are measured in tenths of staff space. Tenths are then scaled to
			* millimeters within the scaling element, used in the defaults element at the start of a score.
			* Individual staves can apply a scaling factor to adjust staff size. When a MusicXML element or
			* attribute refers to tenths, it means the global tenths defined by the scaling element, not the
			* local tenths as adjusted by the staff-size element.
			*/
		type Tenths = Double

		/**
			* The text-direction type is used to adjust and override the Unicode bidirectional text algorithm,
			* similar to the W3C Internationalization Tag Set recommendation. Values are
			* 		ltr (left-to-right embed)
			* 		rtl (right-to-left embed)
			* 		lro (left-to-right bidi-override)
			* 		rlo (right-to-left bidi-override).
			* The default value is ltr. This type is typically used by applications that store text in
			* left-to-right visual order rather than logical order. Such applications can use the lro value
			* to better communicate with other applications that more fully support bidirectional text.
			*/
		sealed abstract class TextDirection
		case object TextDirection_LTR extends TextDirection
		case object TextDirection_RTL extends TextDirection
		case object TextDirection_LRO extends TextDirection
		case object TextDirection_RLO extends TextDirection

		/**
			* The time-only type is used to indicate that a particular playback-related element only applies
			* particular times through a repeated section. The value is a comma-separated list of positive
			* integers arranged in ascending order, indicating which times through the repeated section that
			* the element applies.
			*/
		case class TimeOnly(time: String) {
			require(time matches "[1-9][0-9]*(, ?[1-9][0-9]*)*",
				s"""time ($time) does not match the given pattern""")
		}

		/**
			* The top-bottom type is used to indicate the top or bottom part of a vertical shape like
			* non-arpeggiate.
			*/
		sealed abstract class TopBottom
		case object TB_Top extends TopBottom
		case object TB_Bottom extends TopBottom

		/**
			* The trill-beats type specifies the beats used in a trill-sound or bend-sound attribute group.
			* It is a decimal value with a minimum value of 2.
			*/
		case class TrillBeats(trill: Double) {
			require(trill >= 2.0, s"trill beats must be larger than or equal to 2, but was $trill")
		}

		/**
			* The trill-step type describes the alternating note of trills and mordents for playback,
			* relative to the current note.
			*/
		sealed abstract class TrillStep
		case object TrillStep_Whole extends TrillStep
		case object TrillStep_Half extends TrillStep
		case object TrillStep_Unison extends TrillStep

		/**
			* The two-note-turn type describes the ending notes of trills and mordents for playback,
			* relative to the current note.
			*/
		sealed abstract class TwoNoteTurn
		case object TwoNoteTurn_Whole extends TwoNoteTurn
		case object TwoNoteTurn_Half extends TwoNoteTurn
		case object TwoNoteTurn_None extends TwoNoteTurn

		/**
			* The up-down type is used for the direction of arrows and other pointed symbols like vertical
			* accents, indicating which way the tip is pointing.
			*/
		sealed abstract class UpDown
		case object UD_Up extends UpDown
		case object UD_Down extends UpDown

		/**
			* The upright-inverted type describes the appearance of a fermata element.
			* The value is upright if not specified.
			*/
		sealed abstract class UprightInverted
		case object UI_Upright extends UprightInverted
		case object UI_Inverted extends UprightInverted

		/**
			* The valign type is used to indicate vertical alignment to the top, middle,
			* bottom, or baseline of the text. Defaults are implementation-dependent.
			*/
		sealed abstract class VAlign
		case object VAlign_Top extends VAlign
		case object VAlign_Middle extends VAlign
		case object VAlign_Bottom extends VAlign
		case object VAlign_Baseline extends VAlign

		/**
			* The valign-image type is used to indicate vertical alignment for images and graphics,
			* so it does not include a baseline value. Defaults are implementation-dependent.
			*/
		sealed abstract class VAlignImage
		case object VAlignImage_Top extends VAlignImage
		case object VAlignImage_Middle extends VAlignImage
		case object VAlignImage_Bottom extends VAlignImage

		/**
			* The yes-no type is used for boolean-like attributes. We cannot use W3C XML Schema booleans
			* due to their restrictions on expression of boolean values.
			*/
		sealed abstract class YesNo
		case object YN_Yes extends YesNo
		case object YN_No extends YesNo

		/**
			* The yes-no-number type is used for attributes that can be either boolean or numeric values.
			*/
		sealed abstract class YesNoNumber
		case class YNN_YesNo(value: YesNo) extends YesNoNumber
		case class YNN_Double(value: Double) extends YesNoNumber

		/**
			* Calendar dates are represented yyyy-mm-dd format, following ISO 8601.
			* This is a W3C XML Schema date type, but without the optional timezone data.
			*/
		case class Date(date: String) {
			require(date matches "[^:Z]*", s""""date "$date" doesn't correspond to the specified pattern""")
		}
	}

	object PrimativeDirection {
		/**
			* The accordion-middle type may have values of 1, 2, or 3, corresponding to having 1 to 3 dots
			* in the middle section of the accordion registration symbol.
			*/
		case class AccordionMiddle(middle: Int) {
			require(middle >= 1)
			require(middle <= 3)
		}

		/**
			* The beater-value type represents pictograms for beaters, mallets, and sticks that do not have
			* different materials represented in the pictogram. The finger and hammer values are in addition
			* to Stone's list.
			*/
		sealed abstract class BeaterValue
		case object BeaterValue_Bow extends BeaterValue
		case object BeaterValue_ChimeHammer extends BeaterValue
		case object BeaterValue_Coin extends BeaterValue
		case object BeaterValue_Finger extends BeaterValue
		case object BeaterValue_Fingernail extends BeaterValue
		case object BeaterValue_Fist extends BeaterValue
		case object BeaterValue_GuiroScraper extends BeaterValue
		case object BeaterValue_Hammer extends BeaterValue
		case object BeaterValue_Hand extends BeaterValue
		case object BeaterValue_JazzStick extends BeaterValue
		case object BeaterValue_KnittingNeedle extends BeaterValue
		case object BeaterValue_MetalHammer extends BeaterValue
		case object BeaterValue_SnareStick extends BeaterValue
		case object BeaterValue_SpoonMallet extends BeaterValue
		case object BeaterValue_TriangleBeater extends BeaterValue
		case object BeaterValue_TriangleBeaterPlain extends BeaterValue
		case object BeaterValue_WireBrush extends BeaterValue

		/**
			* The degree-symbol-value type indicates indicates that a symbol should be used in specifying
			* the degree.
			*/
		sealed abstract class DegreeSymbolValue
		case object DegreeSymbolValue_Major extends DegreeSymbolValue
		case object DegreeSymbolValue_Minor extends DegreeSymbolValue
		case object DegreeSymbolValue_Augmented extends DegreeSymbolValue
		case object DegreeSymbolValue_Diminished extends DegreeSymbolValue
		case object DegreeSymbolValue_HalfDiminished extends DegreeSymbolValue

		/**
			* The degree-type-value type indicates whether the current degree element is an addition, alteration,
			* or subtraction to the kind of the current chord in the harmony element.
			*/
		sealed abstract class DegreeTypeValue
		case object DegreeTypeValue_Add extends DegreeTypeValue
		case object DegreeTypeValue_Alter extends DegreeTypeValue
		case object DegreeTypeValue_Subtract extends DegreeTypeValue

		/**
			* The effect type represents pictograms for sound effect percussion instruments.
			* The cannon value is in addition to Stone's list.
			*/
		sealed abstract class Effect
		case object Effect_Anvil extends Effect
		case object Effect_AutoHorn extends Effect
		case object Effect_BirdWhistle extends Effect
		case object Effect_Cannon extends Effect
		case object Effect_DunkCall extends Effect
		case object Effect_GunShot extends Effect
		case object Effect_KlaxonHorn extends Effect
		case object Effect_LionsRoar extends Effect
		case object Effect_PoliceWhistle extends Effect
		case object Effect_Siren extends Effect
		case object Effect_SlideWhistle extends Effect
		case object Effect_ThunderSheet extends Effect
		case object Effect_WindMachine extends Effect
		case object Effect_WindWhistle extends Effect

		/**
			* The glass type represents pictograms for glass percussion instruments.
			*/
		sealed abstract class Glass
		case object WindChimes extends Glass

		/**
			* The harmony-type type differentiates different types of harmonies when alternate
			* harmonies are possible. Explicit harmonies have all note present in the music;
			* implied have some notes missing but implied; alternate represents alternate analyses.
			*/
		sealed abstract class HarmonyType
		case object HarmonyType_Explicit extends HarmonyType
		case object HarmonyType_Implied extends HarmonyType
		case object HarmonyType_Alternate extends HarmonyType

		/**
			* A kind-value indicates the type of chord. Degree elements can then add, subtract,
			* or alter from these starting points. Values include:
			*
			* Triads:
			* 		major (major third, perfect fifth)
			* 		minor (minor third, perfect fifth)
			* 		augmented (major third, augmented fifth)
			* 		diminished (minor third, diminished fifth)
			* Sevenths:
			* 		dominant (major triad, minor seventh)
			* 		major-seventh (major triad, major seventh)
			* 		minor-seventh (minor triad, minor seventh)
			* 		diminished-seventh (diminished triad, diminished seventh)
			* 		augmented-seventh (augmented triad, minor seventh)
			* 		half-diminished (diminished triad, minor seventh)
			* 		major-minor (minor triad, major seventh)
			* Sixths:
			* 		major-sixth (major triad, added sixth)
			* 		minor-sixth (minor triad, added sixth)
			* Ninths:
			* 		dominant-ninth (dominant-seventh, major ninth)
			* 		major-ninth (major-seventh, major ninth)
			* 		minor-ninth (minor-seventh, major ninth)
			* 11ths (usually as the basis for alteration):
			* 		dominant-11th (dominant-ninth, perfect 11th)
			* 		major-11th (major-ninth, perfect 11th)
			* 		minor-11th (minor-ninth, perfect 11th)
			* 13ths (usually as the basis for alteration):
			* 		dominant-13th (dominant-11th, major 13th)
			* 		major-13th (major-11th, major 13th)
			* 		minor-13th (minor-11th, major 13th)
			* Suspended:
			* 		suspended-second (major second, perfect fifth)
			* 		suspended-fourth (perfect fourth, perfect fifth)
			* Functional sixths:
			* 		Neapolitan
			* 		Italian
			* 		French
			* 		German
			* Other:
			* 		pedal (pedal-point bass)
			* 		power (perfect fifth)
			* 		Tristan
			*
			* The "other" kind is used when the harmony is entirely composed of add elements.
			* The "none" kind is used to explicitly encode absence of chords or functional harmony.
			*/
		sealed abstract class KindValue
		case object KindValue_Major extends KindValue
		case object KindValue_Minor extends KindValue
		case object KindValue_Augmented extends KindValue
		case object KindValue_Diminished extends KindValue
		case object KindValue_Dominant extends KindValue
		case object KindValue_MajorSeventh extends KindValue
		case object KindValue_MinorSeventh extends KindValue
		case object KindValue_DiminishedSeventh extends KindValue
		case object KindValue_AugmentedSeventh extends KindValue
		case object KindValue_HalfDiminished extends KindValue
		case object KindValue_MajorMinor extends KindValue
		case object KindValue_MajorSixth extends KindValue
		case object KindValue_MinorSixth extends KindValue
		case object KindValue_DominantNinth extends KindValue
		case object KindValue_MajorNinth extends KindValue
		case object KindValue_MinorNinth extends KindValue
		case object KindValue_Dominant11th extends KindValue
		case object KindValue_Major11th extends KindValue
		case object KindValue_Minor11th extends KindValue
		case object KindValue_Dominant13th extends KindValue
		case object KindValue_Major13th extends KindValue
		case object KindValue_Minor13th extends KindValue
		case object KindValue_SuspendedSecond extends KindValue
		case object KindValue_SuspendedFourth extends KindValue
		case object KindValue_Neapolitan extends KindValue
		case object KindValue_Italian extends KindValue
		case object KindValue_French extends KindValue
		case object KindValue_German extends KindValue
		case object KindValue_Pedal extends KindValue
		case object KindValue_Power extends KindValue
		case object KindValue_Tristan extends KindValue
		case object KindValue_Other extends KindValue
		case object KindValue_None extends KindValue

		/**
			* The line-end type specifies if there is a jog up or down (or both), an arrow,
			* or nothing at the start or end of a bracket.
			*/
		sealed abstract class LineEnd
		case object LineEnd_Up extends LineEnd
		case object LineEnd_Down extends LineEnd
		case object LineEnd_Both extends LineEnd
		case object LineEnd_Arrow extends LineEnd
		case object LineEnd_None extends LineEnd

		/**
			* The measure-numbering-value type describes how measure numbers are displayed on this part:
			* no numbers, numbers every measure, or numbers every system.
			*/
		sealed abstract class MeasureNumberingValue
		case object MNV_None extends MeasureNumberingValue
		case object MNV_Measure extends MeasureNumberingValue
		case object MNV_System extends MeasureNumberingValue

		/**
			* The membrane type represents pictograms for membrane percussion instruments.
			* The goblet drum value is in addition to Stone's list.
			*/
		sealed abstract class Membrane
		case object Membrane_BassDrum extends Membrane
		case object Membrane_BassDrumOnSide extends Membrane
		case object Membrane_Bongos extends Membrane
		case object Membrane_CongaDrum extends Membrane
		case object Membrane_GobletDrum extends Membrane
		case object Membrane_MilitaryDrum extends Membrane
		case object Membrane_SnareDrum extends Membrane
		case object Membrane_SnareDrumSnaresOff extends Membrane
		case object Membrane_Tambourine extends Membrane
		case object Membrane_TenorDrum extends Membrane
		case object Membrane_Timbales extends Membrane
		case object Membrane_Tomtom extends Membrane

		/**
			* The metal type represents pictograms for metal percussion instruments.
			* The hi-hat value refers to a pictogram like Stone's high-hat cymbals but without the long
			* vertical line at the bottom.
			*/
		sealed abstract class Metal
		case object Metal_AlmGlocken extends Metal
		case object Metal_Bell extends Metal
		case object Metal_BellPlate extends Metal
		case object Metal_BrakeDrum extends Metal
		case object Metal_ChineseCymbal extends Metal
		case object Metal_Cowbell extends Metal
		case object Metal_CrashCymbals extends Metal
		case object Metal_Crotale extends Metal
		case object Metal_CymbalTongs extends Metal
		case object Metal_DomedGong extends Metal
		case object Metal_FingerCymbals extends Metal
		case object Metal_Flexatone extends Metal
		case object Metal_Gong extends Metal
		case object Metal_HiHat extends Metal
		case object Metal_HighHatCymbals extends Metal
		case object Metal_Handbell extends Metal
		case object Metal_Sistrum extends Metal
		case object Metal_SizzleCymbal extends Metal
		case object Metal_SleighBells extends Metal
		case object Metal_SuspendedCymbal extends Metal
		case object Metal_TamTam extends Metal
		case object Metal_Triangle extends Metal
		case object Metal_VietnameseHat extends Metal

		/**
			* The on-off type is used for notation elements such as string mutes.
			*/
		sealed abstract class OnOff
		case object OO_On extends OnOff
		case object OO_Off extends OnOff

		/**
			* The pitched type represents pictograms for pitched percussion instruments. The chimes and
			* tubular chimes values distinguish the single-line and double-line versions of the pictogram.
			* The mallet value is in addition to Stone's list.
			*/
		sealed abstract class Pitched
		case object Pitched_Chimes extends Pitched
		case object Pitched_Glockenspiel extends Pitched
		case object Pitched_Mallet extends Pitched
		case object Pitched_Marimba extends Pitched
		case object Pitched_TubularChimes extends Pitched
		case object Pitched_Vibraphone extends Pitched
		case object Pitched_Xylophone extends Pitched

		/**
			* The principal-voice-symbol type represents the type of symbol used to indicate the start of a
			* principal or secondary voice. The "plain" value represents a plain square bracket.
			* The value of "none" is used for analysis markup when the principal-voice element does not have
			* a corresponding appearance in the score.
			*/
		sealed abstract class PrincipalVoiceSymbol
		case object PVS_Hauptstimme extends PrincipalVoiceSymbol
		case object PVS_Nebenstimme extends PrincipalVoiceSymbol
		case object PVS_Plain extends PrincipalVoiceSymbol
		case object PVS_None extends PrincipalVoiceSymbol

		/**
			* The start-stop-change-continue type is used to distinguish types of pedal directions.
			*/
		sealed abstract class StartStopChangeContinue
		case object SSCC_Start extends StartStopChangeContinue
		case object SSCC_Stop extends StartStopChangeContinue
		case object SSCC_Change extends StartStopChangeContinue
		case object SSCC_Continue extends StartStopChangeContinue

		/**
			* The tip-direction type represents the direction in which the tip of a stick or beater points,
			* using Unicode arrow terminology.
			*/
		sealed abstract class TipDirection
		case object TipDirection_Up extends TipDirection
		case object TipDirection_Down extends TipDirection
		case object TipDirection_Left extends TipDirection
		case object TipDirection_Right extends TipDirection
		case object TipDirection_Northwest extends TipDirection
		case object TipDirection_Northeast extends TipDirection
		case object TipDirection_Southeast extends TipDirection
		case object TipDirection_Southwest extends TipDirection

		/**
			* The stick-location type represents pictograms for the location of sticks, beaters,
			* or mallets on cymbals, gongs, drums, and other instruments.
			*/
		sealed abstract class StickLocation
		case object StickLocation_Center extends StickLocation
		case object StickLocation_Rim extends StickLocation
		case object StickLocation_CymbalBell extends StickLocation
		case object StickLocation_CymbalEdge extends StickLocation

		/**
			* The stick-material type represents the material being displayed in a stick pictogram.
			*/
		sealed abstract class StickMaterial
		case object StickMaterial_Soft extends StickMaterial
		case object StickMaterial_Medium extends StickMaterial
		case object StickMaterial_Hard extends StickMaterial
		case object StickMaterial_Shaded extends StickMaterial
		case object StickMaterial_X extends StickMaterial

		/**
			* The stick-type type represents the shape of pictograms where the material in the stick,
			* mallet, or beater is represented in the pictogram.
			*/
		sealed abstract class StickType
		case object StickType_BassDrum extends StickType
		case object StickType_DoubleBassDrum extends StickType
		case object StickType_Timpani extends StickType
		case object StickType_Xylophone extends StickType
		case object StickType_Yarn extends StickType

		/**
			* The up-down-stop-continue type is used for octave-shift elements,
			* indicating the direction of the shift from their true pitched values
			* because of printing difficulty.
			*/
		sealed abstract class UpDownStopContinue
		case object UDSC_Up extends UpDownStopContinue
		case object UDSC_Down extends UpDownStopContinue
		case object UDSC_Stop extends UpDownStopContinue
		case object UDSC_Continue extends UpDownStopContinue

		/**
			* The wedge type is crescendo for the start of a wedge that is closed at the left side,
			* diminuendo for the start of a wedge that is closed on the right side, and stop for the end
			* of a wedge. The continue type is used for formatting wedges over a system break,
			* or for other situations where a single wedge is divided into multiple segments.
			*/
		sealed abstract class WedgeType
		case object WedgeType_Crescendo extends WedgeType
		case object WedgeType_Diminuendo extends WedgeType
		case object WedgeType_Stop extends WedgeType
		case object WedgeType_Continue extends WedgeType

		/**
			* The wood type represents pictograms for wood percussion instruments.
			* The maraca and maracas values distinguish the one- and two-maraca versions of the pictogram.
			* The vibraslap and castanets values are in addition to Stone's list.
			*/
		sealed abstract class Wood
		case object Wood_BoardClapper extends Wood
		case object Wood_Cabasa extends Wood
		case object Wood_Castanets extends Wood
		case object Wood_Claves extends Wood
		case object Wood_Guiro extends Wood
		case object Wood_LogDrum extends Wood
		case object Wood_Maraca extends Wood
		case object Wood_Maracas extends Wood
		case object Wood_Ratchet extends Wood
		case object Wood_SandpaperBlocks extends Wood
		case object Wood_SlitDrum extends Wood
		case object Wood_TempleBlock extends Wood
		case object Wood_Vibraslap extends Wood
		case object Wood_Woodblock extends Wood
	}

	object PrimativeLayout {
		/**
			* The distance-type defines what type of distance is being defined in a distance element.
			* Values include beam and hyphen. This is left as a string so that other application-specific
			* types can be defined, but it is made a separate type so that it can be redefined more strictly.
			*/
		type DistanceType = String

		/**
			* The line-width-type defines what type of line is being defined in a line-width element.
			* Values include beam, bracket, dashes, enclosure, ending, extend, heavy barline, leger,
			* light barline, octave shift, pedal, slur middle, slur tip, staff, stem, tie middle, tie tip,
			* tuplet bracket, and wedge. This is left as a string so that other application-specific types
			* can be defined, but it is made a separate type so that it can be redefined more strictly.
			*/
		type LineWidthType = String

		/**
			* The margin-type type specifies whether margins apply to even page, odd pages, or both.
			*/
		sealed abstract class MarginType
		case object MarginType_Odd extends MarginType
		case object MarginType_Even extends MarginType
		case object MarginType_Both extends MarginType

		/**
			* The millimeters type is a number representing millimeters. This is used in the scaling element
			* to provide a default scaling from tenths to physical units.
			*/
		type Millimeters = Double

		/**
			* The note-size-type type indicates the type of note being defined by a note-size element.
			* The grace type is used for notes of cue size that that include a grace element.
			* The cue type is used for all other notes with cue size, whether defined explicitly or implicitly
			* via a cue element. The large type is used for notes of large size.
			*/
		sealed abstract class NoteSizeType
		case object NoteSizeType_Cue extends NoteSizeType
		case object NoteSizeType_Grace extends NoteSizeType
		case object NoteSizeType_Large extends NoteSizeType
	}

	object PrimativeNote {
		/**
			* The accidental-value type represents notated accidentals supported by MusicXML.
			* In the MusicXML 2.0 DTD this was a string with values that could be included.
			* The XSD strengthens the data typing to an enumerated list.
			* The quarter- and three-quarters- accidentals are Tartini-style quarter-tone accidentals.
			* The -down and -up accidentals are quarter-tone accidentals that include arrows pointing down or up.
			* The slash- accidentals are used in Turkish classical music. The numbered sharp and flat accidentals
			* are superscripted versions of the accidental signs, used in Turkish folk music. The sori and koron
			* accidentals are microtonal sharp and flat accidentals used in Iranian and Persian music.
			*/
		sealed abstract class AccidentalValue
		case object AccidentalValue_Sharp extends AccidentalValue
		case object AccidentalValue_Natural extends AccidentalValue
		case object AccidentalValue_Flat extends AccidentalValue
		case object AccidentalValue_DoubleSharp extends AccidentalValue
		case object AccidentalValue_SharpSharp extends AccidentalValue
		case object AccidentalValue_FlatFlat extends AccidentalValue
		case object AccidentalValue_NaturalSharp extends AccidentalValue
		case object AccidentalValue_NaturalFlat extends AccidentalValue
		case object AccidentalValue_QuarterFlat extends AccidentalValue
		case object AccidentalValue_QuarterSharp extends AccidentalValue
		case object AccidentalValue_ThreeQuartersFlat extends AccidentalValue
		case object AccidentalValue_ThreeQuartersSharp extends AccidentalValue
		case object AccidentalValue_SharpDown extends AccidentalValue
		case object AccidentalValue_SharpUp extends AccidentalValue
		case object AccidentalValue_NaturalDown extends AccidentalValue
		case object AccidentalValue_NaturalUp extends AccidentalValue
		case object AccidentalValue_FlatDown extends AccidentalValue
		case object AccidentalValue_FlatUp extends AccidentalValue
		case object AccidentalValue_TripleSharp extends AccidentalValue
		case object AccidentalValue_TripleFlat extends AccidentalValue
		case object AccidentalValue_SlashQuarterSharp extends AccidentalValue
		case object AccidentalValue_SlashSharp extends AccidentalValue
		case object AccidentalValue_SlashFlat extends AccidentalValue
		case object AccidentalValue_DoubleSlashFlat extends AccidentalValue
		case object AccidentalValue_Sharp1 extends AccidentalValue
		case object AccidentalValue_Sharp2 extends AccidentalValue
		case object AccidentalValue_Sharp3 extends AccidentalValue
		case object AccidentalValue_Sharp5 extends AccidentalValue
		case object AccidentalValue_Flat1 extends AccidentalValue
		case object AccidentalValue_Flat2 extends AccidentalValue
		case object AccidentalValue_Flat3 extends AccidentalValue
		case object AccidentalValue_Flat4 extends AccidentalValue
		case object AccidentalValue_Sori extends AccidentalValue
		case object AccidentalValue_Koron extends AccidentalValue

		/**
			* The arrow-direction type represents the direction in which an arrow points,
			* using Unicode arrow terminology.
			*/
		sealed abstract class ArrowDirection
		case object ArrowDirection_Left extends ArrowDirection
		case object ArrowDirection_Up extends ArrowDirection
		case object ArrowDirection_Right extends ArrowDirection
		case object ArrowDirection_Down extends ArrowDirection
		case object ArrowDirection_Northwest extends ArrowDirection
		case object ArrowDirection_Northeast extends ArrowDirection
		case object ArrowDirection_Southeast extends ArrowDirection
		case object ArrowDirection_Southwest extends ArrowDirection
		case object ArrowDirection_LeftRight extends ArrowDirection
		case object ArrowDirection_UpDown extends ArrowDirection
		case object ArrowDirection_NorthwestSoutheast extends ArrowDirection
		case object ArrowDirection_NortheastSouthWest extends ArrowDirection
		case object ArrowDirection_Other extends ArrowDirection

		/**
			* The arrow-style type represents the style of an arrow, using Unicode arrow terminology.
			* Filled and hollow arrows indicate polygonal single arrows. Paired arrows are duplicate
			* single arrows in the same direction. Combined arrows apply to double direction arrows like
			* left right, indicating that an arrow in one direction should be combined with an arrow in
			* the other direction.
			*/
		sealed abstract class ArrowStyle
		case object ArrowStyle_Single extends ArrowStyle
		case object ArrowStyle_Double extends ArrowStyle
		case object ArrowStyle_Filled extends ArrowStyle
		case object ArrowStyle_Hollow extends ArrowStyle
		case object ArrowStyle_Paired extends ArrowStyle
		case object ArrowStyle_Combined extends ArrowStyle
		case object ArrowStyle_Other extends ArrowStyle

		/**
			* The beam-value type represents the type of beam associated with each of 8 beam levels
			* (up to 1024th notes) available for each note.
			*/
		sealed abstract class BeamValue
		case object BeamValue_Begin extends BeamValue
		case object BeamValue_Continue extends BeamValue
		case object BeamValue_End extends BeamValue
		case object BeamValue_ForwardHook extends BeamValue
		case object BeamValue_BackwardHook extends BeamValue

		/**
			* The breath-mark-value type represents the symbol used for a breath mark.
			*/
		sealed abstract class BreathMarkValue
		case object BreathMarkValue_EMPTY extends BreathMarkValue
		case object BreathMarkValue_Comma extends BreathMarkValue
		case object BreathMarkValue_Tick extends BreathMarkValue

		/**
			* The circular-arrow type represents the direction in which a circular arrow points,
			* using Unicode arrow terminology.
			*/
		sealed abstract class CircularArrow
		case object CircularArrow_ClockWise extends CircularArrow
		case object CircularArrow_Anticlockwise extends CircularArrow

		/**
			* The fan type represents the type of beam fanning present on a note,
			* used to represent accelerandos and ritardandos.
			*/
		sealed abstract class Fan
		case object Fan_Accel extends Fan
		case object Fan_Rit extends Fan
		case object Fan_None extends Fan

		/**
			* The handbell-value type represents the type of handbell technique being notated.
			*/
		sealed abstract class HandbellValue
		case object HandbellValue_Damp extends HandbellValue
		case object HandbellValue_Echo extends HandbellValue
		case object HandbellValue_Gyro extends HandbellValue
		case object HandbellValue_HandMartellato extends HandbellValue
		case object HandbellValue_MalletLift extends HandbellValue
		case object HandbellValue_MalletTable extends HandbellValue
		case object HandbellValue_Martellato extends HandbellValue
		case object HandbellValue_MartellatoLift extends HandbellValue
		case object HandbellValue_MutedMartellato extends HandbellValue
		case object HandbellValue_PluckLift extends HandbellValue
		case object HandbellValue_Swing extends HandbellValue

		/**
			* The hole-closed-location type indicates which portion of the hole is filled in
			* when the corresponding hole-closed-value is half.
			*/
		sealed abstract class HoleClosedLocation
		case object HoleClosedLocation_Right extends HoleClosedLocation
		case object HoleClosedLocation_Bottom extends HoleClosedLocation
		case object HoleClosedLocation_Left extends HoleClosedLocation
		case object HoleClosedLocation_Top extends HoleClosedLocation

		/**
			* The hole-closed-value type represents whether the hole is closed, open, or half-open.
			*/
		sealed abstract class HoleClosedValue
		case object HoleClosedValue_Yes extends HoleClosedValue
		case object HoleClosedValue_No extends HoleClosedValue
		case object HoleClosedValue_Half extends HoleClosedValue

		/**
			* The note-type type is used for the MusicXML type element and represents the graphic note type,
			* from 1024th (shortest) to maxima (longest).
			*/
		sealed abstract class NoteTypeValue
		case object NoteTypeValue_th1024 extends NoteTypeValue
		case object NoteTypeValue_th512 extends NoteTypeValue
		case object NoteTypeValue_th256 extends NoteTypeValue
		case object NoteTypeValue_th128 extends NoteTypeValue
		case object NoteTypeValue_th64 extends NoteTypeValue
		case object NoteTypeValue_nd32 extends NoteTypeValue
		case object NoteTypeValue_th16 extends NoteTypeValue
		case object NoteTypeValue_Eighth extends NoteTypeValue
		case object NoteTypeValue_Quarter extends NoteTypeValue
		case object NoteTypeValue_Half extends NoteTypeValue
		case object NoteTypeValue_Whole extends NoteTypeValue
		case object NoteTypeValue_Breve extends NoteTypeValue
		case object NoteTypeValue_Long extends NoteTypeValue
		case object NoteTypeValue_Maxima extends NoteTypeValue

		/**
			* The notehead type indicates shapes other than the open and closed ovals associated with
			* note durations. The values do, re, mi, fa, fa up, so, la, and ti correspond to
			* Aikin's 7-shape system.  The fa up shape is typically used with upstems; the fa shape is
			* typically used with downstems or no stems.
			*
			* The arrow shapes differ from triangle and inverted triangle by being centered on the stem.
			* Slashed and back slashed notes include both the normal notehead and a slash. The triangle shape
			* has the tip of the triangle pointing up; the inverted triangle shape has the tip of the triangle
			* pointing down. The left triangle shape is a right triangle with the hypotenuse facing up and to
			* the left.
			*/
		sealed abstract class NoteHeadValue
		case object NoteHeadValue_Slash extends NoteHeadValue
		case object NoteHeadValue_Triangle extends NoteHeadValue
		case object NoteHeadValue_Diamond extends NoteHeadValue
		case object NoteHeadValue_Square extends NoteHeadValue
		case object NoteHeadValue_Cross extends NoteHeadValue
		case object NoteHeadValue_X extends NoteHeadValue
		case object NoteHeadValue_CircleX extends NoteHeadValue
		case object NoteHeadValue_InvertedTriangle extends NoteHeadValue
		case object NoteHeadValue_ArrowDown extends NoteHeadValue
		case object NoteHeadValue_ArrowUp extends NoteHeadValue
		case object NoteHeadValue_Slashed extends NoteHeadValue
		case object NoteHeadValue_BackSlashed extends NoteHeadValue
		case object NoteHeadValue_Normal extends NoteHeadValue
		case object NoteHeadValue_Cluster extends NoteHeadValue
		case object NoteHeadValue_CircleDot extends NoteHeadValue
		case object NoteHeadValue_LeftTriangle extends NoteHeadValue
		case object NoteHeadValue_Rectangle extends NoteHeadValue
		case object NoteHeadValue_None extends NoteHeadValue
		case object NoteHeadValue_Do extends NoteHeadValue
		case object NoteHeadValue_Re extends NoteHeadValue
		case object NoteHeadValue_Mi extends NoteHeadValue
		case object NoteHeadValue_Fa extends NoteHeadValue
		case object NoteHeadValue_FaUp extends NoteHeadValue
		case object NoteHeadValue_So extends NoteHeadValue
		case object NoteHeadValue_La extends NoteHeadValue
		case object NoteHeadValue_Ti extends NoteHeadValue

		/**
			* Octaves are represented by the numbers 0 to 9, where 4 indicates the octave started by middle C.
			*/
		case class Octave(octave: Int) {
			require(octave >= 0)
			require(octave <= 9)
		}

		/**
			* The semitones type is a number representing semitones, used for chromatic alteration.
			* A value of -1 corresponds to a flat and a value of 1 to a sharp.
			* Decimal values like 0.5 (quarter tone sharp) are used for microtones.
			*/
		type Semitones = Double

		/**
			* The show-tuplet type indicates whether to show a part of a tuplet relating to the tuplet-actual
			* element, both the tuplet-actual and tuplet-normal elements, or neither.
			*/
		sealed abstract class ShowTuplet
		case object ShowTuplet_Actual extends ShowTuplet
		case object ShowTuplet_Both extends ShowTuplet
		case object ShowTuplet_None extends ShowTuplet

		/**
			* The stem type represents the notated stem direction.
			*/
		sealed abstract class StemValue
		case object StemValue_Down extends StemValue
		case object StemValue_Up extends StemValue
		case object StemValue_Double extends StemValue
		case object StemValue_None extends StemValue

		/**
			* The step type represents a step of the diatonic scale, represented using the English letters
			* A through G.
			*/
		sealed abstract class Step
		case object Step_A extends Step
		case object Step_B extends Step
		case object Step_C extends Step
		case object Step_D extends Step
		case object Step_E extends Step
		case object Step_F extends Step
		case object Step_G extends Step

		/**
			* Lyric hyphenation is indicated by the syllabic type. The single, begin, end, and middle values
			* represent single-syllable words, word-beginning syllables, word-ending syllables, and mid-word
			* syllables, respectively.
			*/
		sealed abstract class Syllabic
		case object Syllabic_Single extends Syllabic
		case object Syllabic_Begin extends Syllabic
		case object Syllabic_End extends Syllabic
		case object Syllabic_Middle extends Syllabic

		/**
			* The number of tremolo marks is represented by a number from 0 to 8:
			* the same as beam-level with 0 added.
			*/
		case class TremoloMarks(trem: Int) {
			require(trem >= 0)
			require(trem <= 8)
		}
	}

	object PrimativeScore {
		/**
			* The group-barline-value type indicates if the group should have common barlines.
			*/
		sealed abstract class GroupBarlineValue
		case object GroupBarlineValue_Yes extends GroupBarlineValue
		case object GroupBarlineValue_No extends GroupBarlineValue
		case object GroupBarlineValue_Mensurstrich extends GroupBarlineValue

		/**
			* The group-symbol-value type indicates how the symbol for a group is indicated in the score.
			* The default value is none.
			*/
		sealed abstract class GroupSymbolValue
		case object GroupSymbolValue_None extends GroupSymbolValue
		case object GroupSymbolValue_Brace extends GroupSymbolValue
		case object GroupSymbolValue_Line extends GroupSymbolValue
		case object GroupSymbolValue_Bracket extends GroupSymbolValue
		case object GroupSymbolValue_Square extends GroupSymbolValue
	}
}
