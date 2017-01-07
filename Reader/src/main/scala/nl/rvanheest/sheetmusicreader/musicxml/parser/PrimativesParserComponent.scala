package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeAttributes._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeBarline._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeDirection._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeLayout._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeNote._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeScore._

trait PrimativesParserComponent {
	this: XmlParserComponent =>

	type PrimativeParser[T] = String => (String => T) => XmlParser[T]

	protected val primativeAttributesParser: PrimativeAttributesParser
	protected val primativeBarlineParser: PrimativeBarlineParser
	protected val primativeCommonParser: PrimativeCommonParser
	protected val primativeDirectionParser: PrimativeDirectionParser
	protected val primativeLayoutParser: PrimativeLayoutParser
	protected val primativeNoteParser: PrimativeNoteParser
	protected val primativeScoreParser: PrimativeScoreParser

	class PrimativeAttributesParser {
		def cancelLocation(name: String)(parser: PrimativeParser[CancelLocation]): XmlParser[CancelLocation] = {
			parser(name) {
				case "left" => CancelLocation_Left
				case "right" => CancelLocation_Right
				case "before-barline" => CancelLocation_BeforeBarline
				case other => sys.error(s"$other is not supported as an instance of CancelLocation")
			}
		}

		def clefSign(name: String)(parser: PrimativeParser[ClefSign]): XmlParser[ClefSign] = {
			parser(name) {
				case "G" => ClefSign_G
				case "F" => ClefSign_F
				case "C" => ClefSign_C
				case "percussion" => ClefSign_Percussion
				case "TAB" => ClefSign_TAB
				case "jianpu" => ClefSign_Jianpu
				case "none" => ClefSign_None
				case other => sys.error(s"$other is not supported as an instance of ClefSign")
			}
		}

		def fifths(name: String)(parser: PrimativeParser[Fifths]): XmlParser[Fifths] = {
			parser(name)(_.toInt)
		}

		def mode(name: String)(parser: PrimativeParser[Mode]): XmlParser[Mode] = {
			parser(name)(identity)
		}

		def showFrets(name: String)(parser: PrimativeParser[ShowFrets]): XmlParser[ShowFrets] = {
			parser(name) {
				case "numbers" => ShowFrets_Numbers
				case "letters" => ShowFrets_Letters
				case other => sys.error(s"$other is not supported as an instance of ShowFrets")
			}
		}

		def staffLine(name: String)(parser: PrimativeParser[StaffLine]): XmlParser[StaffLine] = {
			parser(name)(_.toInt)
		}

		def staffNumber(name: String)(parser: PrimativeParser[StaffNumber]): XmlParser[StaffNumber] = {
			parser(name)(s => StaffNumber(s.toInt))
		}

		def staffType(name: String)(parser: PrimativeParser[StaffType]): XmlParser[StaffType] = {
			parser(name) {
				case "ossia" => StaffType_Ossia
				case "cue" => StaffType_Cue
				case "editorial" => StaffType_Editorial
				case "regular" => StaffType_Regular
				case "alternate" => StaffType_Alternate
				case other => sys.error(s"$other is not supported as an instance of StaffType")
			}
		}

		def timeRelation(name: String)(parser: PrimativeParser[TimeRelation]): XmlParser[TimeRelation] = {
			parser(name) {
				case "parentheses" => TimeRelation_Parentheses
				case "bracket" => TimeRelation_Bracket
				case "equals" => TimeRelation_Equals
				case "slash" => TimeRelation_Slash
				case "space" => TimeRelation_Space
				case "hyphen" => TimeRelation_Hyphen
				case other => sys.error(s"$other is not supported as an instance of TimeRelation")
			}
		}

		def timeSeparator(name: String)(parser: PrimativeParser[TimeSeparator]): XmlParser[TimeSeparator] = {
			parser(name) {
				case "none" => TimeSeparator_None
				case "horizontal" => TimeSeparator_Horizontal
				case "diagonal" => TimeSeparator_Diagonal
				case "vertical" => TimeSeparator_Vertical
				case "adjacent" => TimeSeparator_Adjacent
				case other => sys.error(s"$other is not supported as an instance of TimeRelation")
			}
		}

		def timeSymbol(name: String)(parser: PrimativeParser[TimeSymbol]): XmlParser[TimeSymbol] = {
			parser(name) {
				case "common" => TimeSymbol_Common
				case "cut" => TimeSymbol_Cut
				case "single-number" => TimeSymbol_SingleNumber
				case "note" => TimeSymbol_Note
				case "dotted-note" => TimeSymbol_DottedNote
				case "normal" => TimeSymbol_Normal
				case other => sys.error(s"$other is not supported as an instance of TimeRelation")
			}
		}
	}

	class PrimativeBarlineParser {
		def backwardForward(name: String)(parser: PrimativeParser[BackwardForward]): XmlParser[BackwardForward] = {
			parser(name) {
				case "backward" => BF_Backward
				case "forward" => BF_Forward
				case other => sys.error(s"$other is not supported as an instance of BackwardForward")
			}
		}

		def barStyle(name: String)(parser: PrimativeParser[BarStyle]): XmlParser[BarStyle] = {
			parser(name) {
				case "regular" => BarStyle_Regular
				case "dotted" => BarStyle_Dotted
				case "dashed" => BarStyle_Dashed
				case "heavy" => BarStyle_Heavy
				case "light-light" => BarStyle_LightLight
				case "light-heavy" => BarStyle_LightHeavy
				case "heavy-light" => BarStyle_HeavyLight
				case "heavy-heavy" => BarStyle_HeavyHeavy
				case "tick" => BarStyle_Tick
				case "short" => BarStyle_Short
				case "none" => BarStyle_None
				case other => sys.error(s"$other is not supported as an instance of BarStyle")
			}
		}

		def endingNumber(name: String)(parser: PrimativeParser[EndingNumber]): XmlParser[EndingNumber] = {
			parser(name)(EndingNumber)
		}

		def rightLeftMiddle(name: String)(parser: PrimativeParser[RightLeftMiddle]): XmlParser[RightLeftMiddle] = {
			parser(name) {
				case "right" => RLM_Right
				case "left" => RLM_Left
				case "middle" => RLM_Middle
				case other => sys.error(s"$other is not supported as an instance of RightLeftMiddle")
			}
		}

		def startStopDiscontinue(name: String)(parser: PrimativeParser[StartStopDiscontinue]): XmlParser[StartStopDiscontinue] = {
			parser(name) {
				case "start" => SSD_Start
				case "stop" => SSD_Stop
				case "discontinue" => SSD_Discontinue
				case other => sys.error(s"$other is not supported as an instance of StartStopDiscontinue")
			}
		}

		def winged(name: String)(parser: PrimativeParser[Winged]): XmlParser[Winged] = {
			parser(name) {
				case "none" => Winged_None
				case "straight" => Winged_Straight
				case "curved" => Winged_Curved
				case "double-straight" => Winged_DoubleStraight
				case "double-curved" => Winged_DoubleCurved
				case other => sys.error(s"$other is not supported as an instance of Winged")
			}
		}
	}

	class PrimativeCommonParser {
		def aboveBelow(name: String)(parser: PrimativeParser[AboveBelow]): XmlParser[AboveBelow] = {
			parser(name) {
				case "above" => AB_Above
				case "below" => AB_Below
				case other => sys.error(s"$other is not supported as an instance of AboveBelow")
			}
		}

		def beamLevel(name: String)(parser: PrimativeParser[BeamLevel]): XmlParser[BeamLevel] = {
			parser(name)(s => BeamLevel(s.toInt))
		}

		def color(name: String)(parser: PrimativeParser[Color]): XmlParser[Color] = {
			parser(name)(Color)
		}

		def commaSeparatedText(name: String)(parser: PrimativeParser[CommaSeparatedText]): XmlParser[CommaSeparatedText] = {
			parser(name)(CommaSeparatedText)
		}

		def cssFontSize(name: String)(parser: PrimativeParser[CssFontSize]): XmlParser[CssFontSize] = {
			parser(name) {
				case "xx-small" => CssFontSize_xxSmall
				case "x-small" => CssFontSize_xSmall
				case "small" => CssFontSize_Small
				case "medium" => CssFontSize_Medium
				case "large" => CssFontSize_Large
				case "x-large" => CssFontSize_xLarge
				case "xx-large" => CssFontSize_xxLarge
				case other => sys.error(s"$other is not supported as an instance of CssFontSize")
			}
		}

		def divisions(name: String)(parser: PrimativeParser[Divisions]): XmlParser[Divisions] = {
			parser(name)(_.toDouble)
		}

		def enclosureShape(name: String)(parser: PrimativeParser[EnclosureShape]): XmlParser[EnclosureShape] = {
			parser(name) {
				case "rectangle" => EnclosureShape_Rectangle
				case "square" => EnclosureShape_Square
				case "oval" => EnclosureShape_Oval
				case "circle" => EnclosureShape_Circle
				case "bracket" => EnclosureShape_Bracket
				case "triangle" => EnclosureShape_Triangle
				case "diamond" => EnclosureShape_Diamond
				case "none" => EnclosureShape_None
				case other => sys.error(s"$other is not supported as an instance of EnclosureShape")
			}
		}

		def fermataShape(name: String)(parser: PrimativeParser[FermataShape]): XmlParser[FermataShape] = {
			parser(name) {
				case "normal" => FermataShape_Normal
				case "angled" => FermataShape_Angled
				case "square" => FermataShape_Square
				case "" => FermataShape_Empty
				case other => sys.error(s"$other is not supported as an instance of FermataShape")
			}
		}

		def fontSize(name: String)(cssParser: PrimativeParser[CssFontSize])(parser: PrimativeParser[FS_Double]): XmlParser[FontSize] = {
			cssFontSize(name)(cssParser).map(FS_CssFontSize) <|> parser(name)(s => FS_Double(s.toDouble))
		}

		def fontStyle(name: String)(parser: PrimativeParser[FontStyle]): XmlParser[FontStyle] = {
			parser(name) {
				case "normal" => FontStyle_Normal
				case "italic" => FontStyle_Italic
				case other => sys.error(s"$other is not supported as an instance of FontStyle")
			}
		}

		def fontWeight(name: String)(parser: PrimativeParser[FontWeight]): XmlParser[FontWeight] = {
			parser(name) {
				case "normal" => FontWeight_Normal
				case "bold" => FontWeight_Bold
				case other => sys.error(s"$other is not supported as an instance of FontWeight")
			}
		}

		def leftCenterRight(name: String)(parser: PrimativeParser[LeftCenterRight]): XmlParser[LeftCenterRight] = {
			parser(name) {
				case "left" => LCR_Left
				case "center" => LCR_Center
				case "right" => LCR_Right
				case other => sys.error(s"$other is not supported as an instance of LeftCenterRight")
			}
		}

		def leftRight(name: String)(parser: PrimativeParser[LeftRight]): XmlParser[LeftRight] = {
			parser(name) {
				case "left" => LR_Left
				case "right" => LR_Right
				case other => sys.error(s"$other is not supported as an instance of LeftRight")
			}
		}

		def lineShape(name: String)(parser: PrimativeParser[LineShape]): XmlParser[LineShape] = {
			parser(name) {
				case "straight" => LineShape_Straight
				case "curved" => LineShape_Curved
				case other => sys.error(s"$other is not supported as an instance of LineShape")
			}
		}

		def lineType(name: String)(parser: PrimativeParser[LineType]): XmlParser[LineType] = {
			parser(name) {
				case "solid" => LineType_Solid
				case "dashed" => LineType_Dashed
				case "dotted" => LineType_Dotted
				case "wavy" => LineType_Wavy
				case other => sys.error(s"$other is not supported as an instance of LineType")
			}
		}

		def midi16(name: String)(parser: PrimativeParser[Midi16]): XmlParser[Midi16] = {
			parser(name)(s => Midi16(s.toInt))
		}

		def midi128(name: String)(parser: PrimativeParser[Midi128]): XmlParser[Midi128] = {
			parser(name)(s => Midi128(s.toInt))
		}

		def midi16384(name: String)(parser: PrimativeParser[Midi16384]): XmlParser[Midi16384] = {
			parser(name)(s => Midi16384(s.toInt))
		}

		def mute(name: String)(parser: PrimativeParser[Mute]): XmlParser[Mute] = {
			parser(name) {
				case "on" => Mute_On
				case "off" => Mute_Off
				case "straight" => Mute_Straight
				case "cup" => Mute_Cup
				case "harmon-no-stem" => Mute_HarmonNoStem
				case "harmon-stem" => Mute_HarmonStem
				case "bucket" => Mute_Bucket
				case "plunger" => Mute_Plunger
				case "hat" => Mute_Hat
				case "solotone" => Mute_SoloTone
				case "practice" => Mute_Practice
				case "stop-mute" => Mute_StopMute
				case "stop-hand" => Mute_StopHand
				case "echo" => Mute_Echo
				case "palm" => Mute_Palm
				case other => sys.error(s"$other is not supported as an instance of Mute")
			}
		}

		def nonNegativeDecimal(name: String)(parser: PrimativeParser[NonNegativeDecimal]): XmlParser[NonNegativeDecimal] = {
			parser(name)(s => NonNegativeDecimal(s.toDouble))
		}

		def numberLevel(name: String)(parser: PrimativeParser[NumberLevel]): XmlParser[NumberLevel] = {
			parser(name)(s => NumberLevel(s.toInt))
		}

		def numberOfLines(name: String)(parser: PrimativeParser[NumberOfLines]): XmlParser[NumberOfLines] = {
			parser(name)(s => NumberOfLines(s.toInt))
		}

		def numberOrNormal(name: String)(parser: PrimativeParser[NumberOrNormal]): XmlParser[NumberOrNormal] = {
			parser(name) {
				case "normal" => NON_Normal
				case other => NON_Double(other.toDouble)
			}
		}

		def overUnder(name: String)(parser: PrimativeParser[OverUnder]): XmlParser[OverUnder] = {
			parser(name) {
				case "over" => OU_Over
				case "under" => OU_Under
				case other => sys.error(s"$other is not supported as an instance of OverUnder")
			}
		}

		def percent(name: String)(parser: PrimativeParser[Percent]): XmlParser[Percent] = {
			parser(name)(s => Percent(s.toDouble))
		}

		def positiveDecimal(name: String)(parser: PrimativeParser[PositiveDecimal]): XmlParser[PositiveDecimal] = {
			parser(name)(s => PositiveDecimal(s.toDouble))
		}

		def positiveDivisions(name: String)(parser: PrimativeParser[PositiveDivisions]): XmlParser[PositiveDivisions] = {
			parser(name)(s => PositiveDivisions(s.toDouble))
		}

		def positiveIntegerOrEmpty(name: String)(parser: PrimativeParser[PositiveIntegerOrEmpty]): XmlParser[PositiveIntegerOrEmpty] = {
			parser(name)(s => PositiveIntegerOrEmpty(Option(s).filter(_.nonEmpty).map(_.toInt)))
		}

		def rotationDegrees(name: String)(parser: PrimativeParser[RotationDegrees]): XmlParser[RotationDegrees] = {
			parser(name)(s => RotationDegrees(s.toDouble))
		}

		def semiPitched(name: String)(parser: PrimativeParser[SemiPitched]): XmlParser[SemiPitched] = {
			parser(name) {
				case "high" => SemiPitched_High
				case "medium-high" => SemiPitched_MediumHigh
				case "medium" => SemiPitched_Medium
				case "medium-low" => SemiPitched_MediumLow
				case "low" => SemiPitched_Low
				case "very-low" => SemiPitched_VeryLow
				case other => sys.error(s"$other is not supported as an instance of SemiPitched")
			}
		}

		def startNote(name: String)(parser: PrimativeParser[StartNote]): XmlParser[StartNote] = {
			parser(name) {
				case "upper" => StartNote_Upper
				case "main" => StartNote_Main
				case "below" => StartNote_Below
				case other => sys.error(s"$other is not supported as an instance of StartNote")
			}
		}

		def startStop(name: String)(parser: PrimativeParser[StartStop]): XmlParser[StartStop] = {
			parser(name) {
				case "start" => SS_Start
				case "stop" => SS_Stop
				case other => sys.error(s"$other is not supported as an instance of StartStop")
			}
		}

		def startStopContinue(name: String)(parser: PrimativeParser[StartStopContinue]): XmlParser[StartStopContinue] = {
			parser(name) {
				case "start" => SSC_Start
				case "stop" => SSC_Stop
				case "continue" => SSC_Continue
				case other => sys.error(s"$other is not supported as an instance of StartStopContinue")
			}
		}

		def startStopSingle(name: String)(parser: PrimativeParser[StartStopSingle]): XmlParser[StartStopSingle] = {
			parser(name) {
				case "start" => SSS_Start
				case "stop" => SSS_Stop
				case "single" => SSS_Single
				case other => sys.error(s"$other is not supported as an instance of StartStopSingle")
			}
		}

		def stringNumber(name: String)(parser: PrimativeParser[StringNumber]): XmlParser[StringNumber] = {
			parser(name)(s => StringNumber(s.toInt))
		}

		def symbolSize(name: String)(parser: PrimativeParser[SymbolSize]): XmlParser[SymbolSize] = {
			parser(name) {
				case "full" => SymbolSize_Full
				case "cue" => SymbolSize_Cue
				case "large" => SymbolSize_Large
				case other => sys.error(s"$other is not supported as an instance of SymbolSize")
			}
		}

		def tenths(name: String)(parser: PrimativeParser[Tenths]): XmlParser[Tenths] = {
			parser(name)(_.toDouble)
		}

		def textDirection(name: String)(parser: PrimativeParser[TextDirection]): XmlParser[TextDirection] = {
			parser(name) {
				case "ltr" => TextDirection_LTR
				case "rtl" => TextDirection_RTL
				case "lro" => TextDirection_LRO
				case "rlo" => TextDirection_RLO
				case other => sys.error(s"$other is not supported as an instance of TextDirection")
			}
		}

		def timeOnly(name: String)(parser: PrimativeParser[TimeOnly]): XmlParser[TimeOnly] = {
			parser(name)(TimeOnly)
		}

		def topBottom(name: String)(parser: PrimativeParser[TopBottom]): XmlParser[TopBottom] = {
			parser(name) {
				case "top" => TB_Top
				case "bottom" => TB_Bottom
				case other => sys.error(s"$other is not supported as an instance of TopBottom")
			}
		}

		def trillBeats(name: String)(parser: PrimativeParser[TrillBeats]): XmlParser[TrillBeats] = {
			parser(name)(s => TrillBeats(s.toDouble))
		}

		def trillStep(name: String)(parser: PrimativeParser[TrillStep]): XmlParser[TrillStep] = {
			parser(name) {
				case "whole" => TrillStep_Whole
				case "half" => TrillStep_Half
				case "unison" => TrillStep_Unison
				case other => sys.error(s"$other is not supported as an instance of TrillStep")
			}
		}

		def twoNoteTurn(name: String)(parser: PrimativeParser[TwoNoteTurn]): XmlParser[TwoNoteTurn] = {
			parser(name) {
				case "whole" => TwoNoteTurn_Whole
				case "half" => TwoNoteTurn_Half
				case "none" => TwoNoteTurn_None
				case other => sys.error(s"$other is not supported as an instance of TwoNoteTurn")
			}
		}

		def upDown(name: String)(parser: PrimativeParser[UpDown]): XmlParser[UpDown] = {
			parser(name) {
				case "up" => UD_Up
				case "down" => UD_Down
				case other => sys.error(s"$other is not supported as an instance of UpDown")
			}
		}

		def uprightInverted(name: String)(parser: PrimativeParser[UprightInverted]): XmlParser[UprightInverted] = {
			parser(name) {
				case "upright" => UI_Upright
				case "inverted" => UI_Inverted
				case other => sys.error(s"$other is not supported as an instance of UprightInverted")
			}
		}

		def vAlign(name: String)(parser: PrimativeParser[VAlign]): XmlParser[VAlign] = {
			parser(name) {
				case "top" => VAlign_Top
				case "middle" => VAlign_Middle
				case "bottom" => VAlign_Bottom
				case "baseline" => VAlign_Baseline
				case other => sys.error(s"$other is not supported as an instance of VAlign")
			}
		}

		def vAlignImage(name: String)(parser: PrimativeParser[VAlignImage]): XmlParser[VAlignImage] = {
			parser(name) {
				case "top" => VAlignImage_Top
				case "middle" => VAlignImage_Middle
				case "bottom" => VAlignImage_Bottom
				case other => sys.error(s"$other is not supported as an instance of VAlignImage")
			}
		}

		def yesNo(name: String)(parser: PrimativeParser[YesNo]): XmlParser[YesNo] = {
			parser(name) {
				case "yes" => YN_Yes
				case "no" => YN_No
				case other => sys.error(s"$other is not supported as an instance of YesNo")
			}
		}

		def yesNoNumber(name: String)(yesNoParser: PrimativeParser[YesNo])(parser: PrimativeParser[YNN_Double]): XmlParser[YesNoNumber] = {
			yesNo(name)(yesNoParser).map(YNN_YesNo) <|> parser(name)(s => YNN_Double(s.toDouble))
		}

		def date(name: String)(parser: PrimativeParser[Date]): XmlParser[Date] = {
			parser(name)(Date)
		}
	}

	class PrimativeDirectionParser {
		def accordionMiddle(name: String)(parser: PrimativeParser[AccordionMiddle]): XmlParser[AccordionMiddle] = {
			parser(name)(s => AccordionMiddle(s.toInt))
		}

		def beaterValue(name: String)(parser: PrimativeParser[BeaterValue]): XmlParser[BeaterValue] = {
			parser(name) {
				case "bow" => BeaterValue_Bow
				case "chime hammer" => BeaterValue_ChimeHammer
				case "coin" => BeaterValue_Coin
				case "finger" => BeaterValue_Finger
				case "fingernail" => BeaterValue_Fingernail
				case "fist" => BeaterValue_Fist
				case "guiro scraper" => BeaterValue_GuiroScraper
				case "hammer" => BeaterValue_Hammer
				case "hand" => BeaterValue_Hand
				case "jazz stick" => BeaterValue_JazzStick
				case "knitting needle" => BeaterValue_KnittingNeedle
				case "metal hammer" => BeaterValue_MetalHammer
				case "snare stick" => BeaterValue_SnareStick
				case "spoon mallet" => BeaterValue_SpoonMallet
				case "triangle beater" => BeaterValue_TriangleBeater
				case "triangle beater plain" => BeaterValue_TriangleBeaterPlain
				case "wire brush" => BeaterValue_WireBrush
				case other => sys.error(s"$other is not supported as an instance of BeaterValue")
			}
		}

		def degreeSymbolValue(name: String)(parser: PrimativeParser[DegreeSymbolValue]): XmlParser[DegreeSymbolValue] = {
			parser(name) {
				case "major" => DegreeSymbolValue_Major
				case "minor" => DegreeSymbolValue_Minor
				case "augmented" => DegreeSymbolValue_Augmented
				case "diminished" => DegreeSymbolValue_Diminished
				case "half-diminished" => DegreeSymbolValue_HalfDiminished
				case other => sys.error(s"$other is not supported as an instance of DegreeSymbolValue")
			}
		}

		def degreeTypeValue(name: String)(parser: PrimativeParser[DegreeTypeValue]): XmlParser[DegreeTypeValue] = {
			parser(name) {
				case "add" => DegreeTypeValue_Add
				case "alter" => DegreeTypeValue_Alter
				case "subtract" => DegreeTypeValue_Subtract
				case other => sys.error(s"$other is not supported as an instance of DegreeTypeValue")
			}
		}

		def effect(name: String)(parser: PrimativeParser[Effect]): XmlParser[Effect] = {
			parser(name) {
				case "anvil" => Effect_Anvil
				case "auto horn" => Effect_AutoHorn
				case "bird whistle" => Effect_BirdWhistle
				case "cannon" => Effect_Cannon
				case "duck call" => Effect_DunkCall
				case "gun shot" => Effect_GunShot
				case "klaxon horn" => Effect_KlaxonHorn
				case "lions roar" => Effect_LionsRoar
				case "police whistle" => Effect_PoliceWhistle
				case "siren" => Effect_Siren
				case "slide whistle" => Effect_SlideWhistle
				case "thunder sheet" => Effect_ThunderSheet
				case "wind machine" => Effect_WindMachine
				case "wind whistle" => Effect_WindWhistle
				case other => sys.error(s"$other is not supported as an instance of Effect")
			}
		}

		def glass(name: String)(parser: PrimativeParser[Glass]): XmlParser[Glass] = {
			parser(name) {
				case "wind chimes" => WindChimes
				case other => sys.error(s"$other is not supported as an instance of Glass")
			}
		}

		def harmonyType(name: String)(parser: PrimativeParser[HarmonyType]): XmlParser[HarmonyType] = {
			parser(name) {
				case "explicit" => HarmonyType_Explicit
				case "implied" => HarmonyType_Implied
				case "alternate" => HarmonyType_Alternate
				case other => sys.error(s"$other is not supported as an instance of HarmonyType")
			}
		}

		def kindValue(name: String)(parser: PrimativeParser[KindValue]): XmlParser[KindValue] = {
			parser(name) {
				case "major" => KindValue_Major
				case "minor" => KindValue_Minor
				case "augmented" => KindValue_Augmented
				case "diminished" => KindValue_Diminished
				case "dominant" => KindValue_Dominant
				case "major-seventh" => KindValue_MajorSeventh
				case "minor-seventh" => KindValue_MinorSeventh
				case "diminished-seventh" => KindValue_DiminishedSeventh
				case "augmented-seventh" => KindValue_AugmentedSeventh
				case "half-diminished" => KindValue_HalfDiminished
				case "major-minor" => KindValue_MajorMinor
				case "major-sixth" => KindValue_MajorSixth
				case "minor-sixth" => KindValue_MinorSixth
				case "dominant-ninth" => KindValue_DominantNinth
				case "major-ninth" => KindValue_MajorNinth
				case "minor-ninth" => KindValue_MinorNinth
				case "dominant-11th" => KindValue_Dominant11th
				case "major-11th" => KindValue_Major11th
				case "minor-11th" => KindValue_Minor11th
				case "dominant-13th" => KindValue_Dominant13th
				case "major-13th" => KindValue_Major13th
				case "minor-13th" => KindValue_Minor13th
				case "suspended-second" => KindValue_SuspendedSecond
				case "suspended-fourth" => KindValue_SuspendedFourth
				case "Neapolitan" => KindValue_Neapolitan
				case "Italian" => KindValue_Italian
				case "French" => KindValue_French
				case "German" => KindValue_German
				case "pedal" => KindValue_Pedal
				case "power" => KindValue_Power
				case "Tristan" => KindValue_Tristan
				case "other" => KindValue_Other
				case "none" => KindValue_None
				case other => sys.error(s"$other is not supported as an instance of KindValue")
			}
		}

		def lineEnd(name: String)(parser: PrimativeParser[LineEnd]): XmlParser[LineEnd] = {
			parser(name) {
				case "up" => LineEnd_Up
				case "down" => LineEnd_Down
				case "both" => LineEnd_Both
				case "arrow" => LineEnd_Arrow
				case "none" => LineEnd_None
				case other => sys.error(s"$other is not supported as an instance of LineEnd")
			}
		}

		def measureNumberingValue(name: String)(parser: PrimativeParser[MeasureNumberingValue]): XmlParser[MeasureNumberingValue] = {
			parser(name) {
				case "none" => MNV_None
				case "measure" => MNV_Measure
				case "system" => MNV_System
				case other => sys.error(s"$other is not supported as an instance of MeasureNumberingValue")
			}
		}

		def membrane(name: String)(parser: PrimativeParser[Membrane]): XmlParser[Membrane] = {
			parser(name) {
				case "bass drum" => Membrane_BassDrum
				case "bass drum on side" => Membrane_BassDrumOnSide
				case "bongos" => Membrane_Bongos
				case "conga drum" => Membrane_CongaDrum
				case "goblet drum" => Membrane_GobletDrum
				case "military drum" => Membrane_MilitaryDrum
				case "snare drum" => Membrane_SnareDrum
				case "snare drum snares off" => Membrane_SnareDrumSnaresOff
				case "tambourine" => Membrane_Tambourine
				case "tenor drum" => Membrane_TenorDrum
				case "timbales" => Membrane_Timbales
				case "tomtom" => Membrane_Tomtom
				case other => sys.error(s"$other is not supported as an instance of Membrane")
			}
		}

		def metal(name: String)(parser: PrimativeParser[Metal]): XmlParser[Metal] = {
			parser(name) {
				case "almglocken" => Metal_AlmGlocken
				case "bell" => Metal_Bell
				case "bell plate" => Metal_BellPlate
				case "brake drum" => Metal_BrakeDrum
				case "Chinese cymbal" => Metal_ChineseCymbal
				case "cowbell" => Metal_Cowbell
				case "crash cymbals" => Metal_CrashCymbals
				case "crotale" => Metal_Crotale
				case "cymbal tongs" => Metal_CymbalTongs
				case "domed gong" => Metal_DomedGong
				case "finger cymbals" => Metal_FingerCymbals
				case "flexatone" => Metal_Flexatone
				case "gong" => Metal_Gong
				case "hi-hat" => Metal_HiHat
				case "high-hat cymbals" => Metal_HighHatCymbals
				case "handbell" => Metal_Handbell
				case "sistrum" => Metal_Sistrum
				case "sizzle cymbal" => Metal_SizzleCymbal
				case "sleigh bells" => Metal_SleighBells
				case "suspended cymbal" => Metal_SuspendedCymbal
				case "tam tam" => Metal_TamTam
				case "triangle" => Metal_Triangle
				case "Vietnamese hat" => Metal_VietnameseHat
				case other => sys.error(s"$other is not supported as an instance of Metal")
			}
		}

		def onOff(name: String)(parser: PrimativeParser[OnOff]): XmlParser[OnOff] = {
			parser(name) {
				case "on" => OO_On
				case "off" => OO_Off
				case other => sys.error(s"$other is not supported as an instance of OnOff")
			}
		}

		def pitched(name: String)(parser: PrimativeParser[Pitched]): XmlParser[Pitched] = {
			parser(name) {
				case "chimes" => Pitched_Chimes
				case "glockenspiel" => Pitched_Glockenspiel
				case "mallet" => Pitched_Mallet
				case "marimba" => Pitched_Marimba
				case "tubular chimes" => Pitched_TubularChimes
				case "vibraphone" => Pitched_Vibraphone
				case "xylophone" => Pitched_Xylophone
				case other => sys.error(s"$other is not supported as an instance of Pitched")
			}
		}

		def principalVoiceSymbol(name: String)(parser: PrimativeParser[PrincipalVoiceSymbol]): XmlParser[PrincipalVoiceSymbol] = {
			parser(name) {
				case "Hauptstimme" => PVS_Hauptstimme
				case "Nebenstimme" => PVS_Nebenstimme
				case "plain" => PVS_Plain
				case "none" => PVS_None
				case other => sys.error(s"$other is not supported as an instance of PrincipalVoiceSymbol")
			}
		}

		def startStopChangeContinue(name: String)(parser: PrimativeParser[StartStopChangeContinue]): XmlParser[StartStopChangeContinue] = {
			parser(name) {
				case "start" => SSCC_Start
				case "stop" => SSCC_Stop
				case "change" => SSCC_Change
				case "continue" => SSCC_Continue
				case other => sys.error(s"$other is not supported as an instance of StartStopChangeContinue")
			}
		}

		def tipDirection(name: String)(parser: PrimativeParser[TipDirection]): XmlParser[TipDirection] = {
			parser(name) {
				case "up" => TipDirection_Up
				case "down" => TipDirection_Down
				case "left" => TipDirection_Left
				case "right" => TipDirection_Right
				case "northwest" => TipDirection_Northwest
				case "northeast" => TipDirection_Northeast
				case "southeast" => TipDirection_Southeast
				case "southwest" => TipDirection_Southwest
				case other => sys.error(s"$other is not supported as an instance of TipDirection")
			}
		}

		def stickLocation(name: String)(parser: PrimativeParser[StickLocation]): XmlParser[StickLocation] = {
			parser(name) {
				case "center" => StickLocation_Center
				case "rim" => StickLocation_Rim
				case "cymbal bell" => StickLocation_CymbalBell
				case "cymbal edge" => StickLocation_CymbalEdge
				case other => sys.error(s"$other is not supported as an instance of StickLocation")
			}
		}

		def stickMaterial(name: String)(parser: PrimativeParser[StickMaterial]): XmlParser[StickMaterial] = {
			parser(name) {
				case "soft" => StickMaterial_Soft
				case "medium" => StickMaterial_Medium
				case "hard" => StickMaterial_Hard
				case "shaded" => StickMaterial_Shaded
				case "x" => StickMaterial_X
				case other => sys.error(s"$other is not supported as an instance of StickMaterial")
			}
		}

		def stickType(name: String)(parser: PrimativeParser[StickType]): XmlParser[StickType] = {
			parser(name) {
				case "bass drum" => StickType_BassDrum
				case "double bass drum" => StickType_DoubleBassDrum
				case "timpani" => StickType_Timpani
				case "xylophone" => StickType_Xylophone
				case "yarn" => StickType_Yarn
				case other => sys.error(s"$other is not supported as an instance of StickType")
			}
		}

		def upDownStopContinue(name: String)(parser: PrimativeParser[UpDownStopContinue]): XmlParser[UpDownStopContinue] = {
			parser(name) {
				case "up" => UDSC_Up
				case "down" => UDSC_Down
				case "stop" => UDSC_Stop
				case "continue" => UDSC_Continue
				case other => sys.error(s"$other is not supported as an instance of UpDownStopContinue")
			}
		}

		def wedgeType(name: String)(parser: PrimativeParser[WedgeType]): XmlParser[WedgeType] = {
			parser(name) {
				case "crescendo" => WedgeType_Crescendo
				case "diminuendo" => WedgeType_Diminuendo
				case "stop" => WedgeType_Stop
				case "continue" => WedgeType_Continue
				case other => sys.error(s"$other is not supported as an instance of WedgeType")
			}
		}

		def wood(name: String)(parser: PrimativeParser[Wood]): XmlParser[Wood] = {
			parser(name) {
				case "board clapper" => Wood_BoardClapper
				case "cabasa" => Wood_Cabasa
				case "castanets" => Wood_Castanets
				case "claves" => Wood_Claves
				case "guiro" => Wood_Guiro
				case "log drum" => Wood_LogDrum
				case "maraca" => Wood_Maraca
				case "maracas" => Wood_Maracas
				case "ratchet" => Wood_Ratchet
				case "sandpaper blocks" => Wood_SandpaperBlocks
				case "slit drum" => Wood_SlitDrum
				case "temple block" => Wood_TempleBlock
				case "vibraslap" => Wood_Vibraslap
				case "wood block" => Wood_Woodblock
				case other => sys.error(s"$other is not supported as an instance of Wood")
			}
		}
	}

	class PrimativeLayoutParser {
		def distanceType(name: String)(parser: PrimativeParser[DistanceType]): XmlParser[DistanceType] = {
			parser(name)(identity)
		}

		def lineWidthType(name: String)(parser: PrimativeParser[LineWidthType]): XmlParser[LineWidthType] = {
			parser(name)(identity)
		}

		def marginType(name: String)(parser: PrimativeParser[MarginType]): XmlParser[MarginType] = {
			parser(name) {
				case "odd" => MarginType_Odd
				case "even" => MarginType_Even
				case "both" => MarginType_Both
				case other => sys.error(s"$other is not supported as an instance of MarginType")
			}
		}

		def millimeters(name: String)(parser: PrimativeParser[Millimeters]): XmlParser[Millimeters] = {
			parser(name)(_.toDouble)
		}

		def noteSizeType(name: String)(parser: PrimativeParser[NoteSizeType]): XmlParser[NoteSizeType] = {
			parser(name) {
				case "cue" => NoteSizeType_Cue
				case "grace" => NoteSizeType_Grace
				case "large" => NoteSizeType_Large
				case other => sys.error(s"$other is not supported as an instance of NoteSizeType")
			}
		}
	}

	class PrimativeNoteParser {
		def accidentalValue(name: String)(parser: PrimativeParser[AccidentalValue]): XmlParser[AccidentalValue] = {
			parser(name) {
				case "sharp" => AccidentalValue_Sharp
				case "natural" => AccidentalValue_Natural
				case "flat" => AccidentalValue_Flat
				case "double-sharp" => AccidentalValue_DoubleSharp
				case "sharp-sharp" => AccidentalValue_SharpSharp
				case "flat-flat" => AccidentalValue_FlatFlat
				case "natural-sharp" => AccidentalValue_NaturalSharp
				case "natural-flat" => AccidentalValue_NaturalFlat
				case "quarter-flat" => AccidentalValue_QuarterFlat
				case "quarter-sharp" => AccidentalValue_QuarterSharp
				case "three-quarters-flat" => AccidentalValue_ThreeQuartersFlat
				case "three-quarters-sharp" => AccidentalValue_ThreeQuartersSharp
				case "sharp-down" => AccidentalValue_SharpDown
				case "sharp-up" => AccidentalValue_SharpUp
				case "natural-down" => AccidentalValue_NaturalDown
				case "natural-up" => AccidentalValue_NaturalUp
				case "flat-down" => AccidentalValue_FlatDown
				case "flat-up" => AccidentalValue_FlatUp
				case "triple-sharp" => AccidentalValue_TripleSharp
				case "triple-flat" => AccidentalValue_TripleFlat
				case "slash-quarter-sharp" => AccidentalValue_SlashQuarterSharp
				case "slash-sharp" => AccidentalValue_SlashSharp
				case "slash-flat" => AccidentalValue_SlashFlat
				case "double-slash-flat" => AccidentalValue_DoubleSlashFlat
				case "sharp-1" => AccidentalValue_Sharp1
				case "sharp-2" => AccidentalValue_Sharp2
				case "sharp-3" => AccidentalValue_Sharp3
				case "sharp-5" => AccidentalValue_Sharp5
				case "flat-1" => AccidentalValue_Flat1
				case "flat-2" => AccidentalValue_Flat2
				case "flat-3" => AccidentalValue_Flat3
				case "flat-4" => AccidentalValue_Flat4
				case "sori" => AccidentalValue_Sori
				case "koron" => AccidentalValue_Koron
				case other => sys.error(s"$other is not supported as an instance of AccidentalValue")
			}
		}

		def arrowDirection(name: String)(parser: PrimativeParser[ArrowDirection]): XmlParser[ArrowDirection] = {
			parser(name) {
				case "left" => ArrowDirection_Left
				case "up" => ArrowDirection_Up
				case "right" => ArrowDirection_Right
				case "down" => ArrowDirection_Down
				case "northwest" => ArrowDirection_Northwest
				case "northeast" => ArrowDirection_Northeast
				case "southeast" => ArrowDirection_Southeast
				case "southwest" => ArrowDirection_Southwest
				case "left right" => ArrowDirection_LeftRight
				case "up down" => ArrowDirection_UpDown
				case "northwest southeast" => ArrowDirection_NorthwestSoutheast
				case "northeast southwest" => ArrowDirection_NortheastSouthWest
				case "other" => ArrowDirection_Other
				case other => sys.error(s"$other is not supported as an instance of ArrowDirection")
			}
		}

		def arrowStyle(name: String)(parser: PrimativeParser[ArrowStyle]): XmlParser[ArrowStyle] = {
			parser(name) {
				case "single" => ArrowStyle_Single
				case "double" => ArrowStyle_Double
				case "filled" => ArrowStyle_Filled
				case "hollow" => ArrowStyle_Hollow
				case "paired" => ArrowStyle_Paired
				case "combined" => ArrowStyle_Combined
				case "other" => ArrowStyle_Other
				case other => sys.error(s"$other is not supported as an instance of ArrowStyle")
			}
		}

		def beamValue(name: String)(parser: PrimativeParser[BeamValue]): XmlParser[BeamValue] = {
			parser(name) {
				case "begin" => BeamValue_Begin
				case "continue" => BeamValue_Continue
				case "end" => BeamValue_End
				case "forward hook" => BeamValue_ForwardHook
				case "backward hook" => BeamValue_BackwardHook
				case other => sys.error(s"$other is not supported as an instance of BeamValue")
			}
		}

		def breathMarkValue(name: String)(parser: PrimativeParser[BreathMarkValue]): XmlParser[BreathMarkValue] = {
			parser(name) {
				case "" => BreathMarkValue_EMPTY
				case "comma" => BreathMarkValue_Comma
				case "tick" => BreathMarkValue_Tick
				case other => sys.error(s"$other is not supported as an instance of BreathMarkValue")
			}
		}

		def circularArrow(name: String)(parser: PrimativeParser[CircularArrow]): XmlParser[CircularArrow] = {
			parser(name) {
				case "clockwise" => CircularArrow_ClockWise
				case "anticlockwise" => CircularArrow_Anticlockwise
				case other => sys.error(s"$other is not supported as an instance of CircularArrow")
			}
		}

		def fan(name: String)(parser: PrimativeParser[Fan]): XmlParser[Fan] = {
			parser(name) {
				case "accel" => Fan_Accel
				case "rit" => Fan_Rit
				case "none" => Fan_None
				case other => sys.error(s"$other is not supported as an instance of Fan")
			}
		}

		def handbellValue(name: String)(parser: PrimativeParser[HandbellValue]): XmlParser[HandbellValue] = {
			parser(name) {
				case "damp" => HandbellValue_Damp
				case "echo" => HandbellValue_Echo
				case "gyro" => HandbellValue_Gyro
				case "hand martellato" => HandbellValue_HandMartellato
				case "mallet lift" => HandbellValue_MalletLift
				case "mallet table" => HandbellValue_MalletTable
				case "martellato" => HandbellValue_Martellato
				case "martellato lift" => HandbellValue_MartellatoLift
				case "muted martellato" => HandbellValue_MutedMartellato
				case "pluck lift" => HandbellValue_PluckLift
				case "swing" => HandbellValue_Swing
				case other => sys.error(s"$other is not supported as an instance of HandBellValue")
			}
		}

		def holeClosedLocation(name: String)(parser: PrimativeParser[HoleClosedLocation]): XmlParser[HoleClosedLocation] = {
			parser(name) {
				case "right" => HoleClosedLocation_Right
				case "bottom" => HoleClosedLocation_Bottom
				case "left" => HoleClosedLocation_Left
				case "top" => HoleClosedLocation_Top
				case other => sys.error(s"$other is not supported as an instance of HoleClosedLocation")
			}
		}

		def holeClosedValue(name: String)(parser: PrimativeParser[HoleClosedValue]): XmlParser[HoleClosedValue] = {
			parser(name) {
				case "yes" => HoleClosedValue_Yes
				case "no" => HoleClosedValue_No
				case "half" => HoleClosedValue_Half
				case other => sys.error(s"$other is not supported as an instance of HoleClosedValue")
			}
		}

		def noteTypeValue(name: String)(parser: PrimativeParser[NoteTypeValue]): XmlParser[NoteTypeValue] = {
			parser(name) {
				case "1024th" => NoteTypeValue_th1024
				case "512th" => NoteTypeValue_th512
				case "256th" => NoteTypeValue_th256
				case "128th" => NoteTypeValue_th128
				case "64th" => NoteTypeValue_th64
				case "32th" => NoteTypeValue_nd32
				case "16th" => NoteTypeValue_th16
				case "eighth" => NoteTypeValue_Eighth
				case "quarter" => NoteTypeValue_Quarter
				case "half" => NoteTypeValue_Half
				case "whole" => NoteTypeValue_Whole
				case "breve" => NoteTypeValue_Breve
				case "long" => NoteTypeValue_Long
				case "maxima" => NoteTypeValue_Maxima
				case other => sys.error(s"$other is not supported as an instance of NoteTypeValue")
			}
		}

		def noteHeadValue(name: String)(parser: PrimativeParser[NoteHeadValue]): XmlParser[NoteHeadValue] = {
			parser(name) {
				case "slash" => NoteHeadValue_Slash
				case "triangle" => NoteHeadValue_Triangle
				case "diamond" => NoteHeadValue_Diamond
				case "square" => NoteHeadValue_Square
				case "cross" => NoteHeadValue_Cross
				case "x" => NoteHeadValue_X
				case "circle-x" => NoteHeadValue_CircleX
				case "inverted triangle" => NoteHeadValue_InvertedTriangle
				case "arrow down" => NoteHeadValue_ArrowDown
				case "arrow up" => NoteHeadValue_ArrowUp
				case "slashed" => NoteHeadValue_Slashed
				case "back slashed" => NoteHeadValue_BackSlashed
				case "normal" => NoteHeadValue_Normal
				case "cluster" => NoteHeadValue_Cluster
				case "circle dot" => NoteHeadValue_CircleDot
				case "left triangle" => NoteHeadValue_LeftTriangle
				case "rectangle" => NoteHeadValue_Rectangle
				case "none" => NoteHeadValue_None
				case "do" => NoteHeadValue_Do
				case "re" => NoteHeadValue_Re
				case "mi" => NoteHeadValue_Mi
				case "fa" => NoteHeadValue_Fa
				case "fa up" => NoteHeadValue_FaUp
				case "so" => NoteHeadValue_So
				case "la" => NoteHeadValue_La
				case "ti" => NoteHeadValue_Ti
				case other => sys.error(s"$other is not supported as an instance of NoteHeadValue")
			}
		}

		def octave(name: String)(parser: PrimativeParser[Octave]): XmlParser[Octave] = {
			parser(name)(s => Octave(s.toInt))
		}

		def semitones(name: String)(parser: PrimativeParser[Semitones]): XmlParser[Semitones] = {
			parser(name)(_.toDouble)
		}

		def showTuplet(name: String)(parser: PrimativeParser[ShowTuplet]): XmlParser[ShowTuplet] = {
			parser(name) {
				case "actual" => ShowTuplet_Actual
				case "both" => ShowTuplet_Both
				case "none" => ShowTuplet_None
				case other => sys.error(s"$other is not supported as an instance of ShowTuplet")
			}
		}

		def stemValue(name: String)(parser: PrimativeParser[StemValue]): XmlParser[StemValue] = {
			parser(name) {
				case "down" => StemValue_Down
				case "up" => StemValue_Up
				case "double" => StemValue_Double
				case "none" => StemValue_None
				case other => sys.error(s"$other is not supported as an instance of StemValue")
			}
		}

		def step(name: String)(parser: PrimativeParser[Step]): XmlParser[Step] = {
			parser(name) {
				case "A" => Step_A
				case "B" => Step_B
				case "C" => Step_C
				case "D" => Step_D
				case "E" => Step_E
				case "F" => Step_F
				case "G" => Step_G
				case other => sys.error(s"$other is not supported as an instance of Step")
			}
		}

		def syllabic(name: String)(parser: PrimativeParser[Syllabic]): XmlParser[Syllabic] = {
			parser(name) {
				case "single" => Syllabic_Single
				case "begin" => Syllabic_Begin
				case "end" => Syllabic_End
				case "middle" => Syllabic_Middle
				case other => sys.error(s"$other is not supported as an instance of Syllabic")
			}
		}

		def tremoloMarks(name: String)(parser: PrimativeParser[TremoloMarks]): XmlParser[TremoloMarks] = {
			parser(name)(s => TremoloMarks(s.toInt))
		}
	}

	class PrimativeScoreParser {
		def groupBarlineValue(name: String)(parser: PrimativeParser[GroupBarlineValue]): XmlParser[GroupBarlineValue] = {
			parser(name) {
				case "yes" => GroupBarlineValue_Yes
				case "no" => GroupBarlineValue_No
				case "Mensurstrich" => GroupBarlineValue_Mensurstrich
				case other => sys.error(s"$other is not supported as an instance of GroupBarlineValue")
			}
		}

		def groupSymbolValue(name: String)(parser: PrimativeParser[GroupSymbolValue]): XmlParser[GroupSymbolValue] = {
			parser(name) {
				case "none" => GroupSymbolValue_None
				case "brace" => GroupSymbolValue_Brace
				case "line" => GroupSymbolValue_Line
				case "bracket" => GroupSymbolValue_Bracket
				case "square" => GroupSymbolValue_Square
				case other => sys.error(s"$other is not supported as an instance of GroupBarlineValue")
			}
		}
	}
}
