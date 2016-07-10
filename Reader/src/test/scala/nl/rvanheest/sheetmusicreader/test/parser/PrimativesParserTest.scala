package nl.rvanheest.sheetmusicreader.test.parser

import nl.rvanheest.sheetmusicreader.monadics.{MonadPlus, OptionIsMonadPlus, TryIsMonadPlus}
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeAttributes._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeBarline._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon._
import nl.rvanheest.sheetmusicreader.musicxml.parser.{PrimativesParser, XmlParser}
import org.junit.Test
import org.junit.Assert._

import scala.util.Try
import scala.xml.Node

trait PrimativesParserTest[M[+ _]] extends PrimativesParser[M] with XmlParser[M] {

	import XmlParser._

	type TestParser[T] = PrimativeParser[T] => XmlParser[T]
	type TestParser2[T] = PrimativeParser[T] => TestParser[T]

	def isEmpty[T](m: M[T]): Boolean

	def nodeTest[T, S](prim: TestParser[T])(xs: (Node, S)*) = {
		xs.foreach {
			case (xml, expected) => assertEquals(mp.create(expected), prim(node).eval(xml))
		}
	}

	def nodeError[T, S](prim: TestParser[T])(xmls: Node*) = {
		xmls.map(xml => prim(node).eval(xml))
			.foreach(mt => assertTrue(s"$mt is not empty", isEmpty(mt)))
	}
}

trait PrimativeAttributesParserTest[M[+ _]] extends PrimativesParserTest[M] {

	import primativeAttributesParser._

	@Test def cancelLocationTest() = {
		nodeTest(cancelLocation("foo"))(
			(<foo>left</foo>, CancelLocation_Left),
			(<foo>right</foo>, CancelLocation_Right),
			(<foo>before-barline</foo>, CancelLocation_BeforeBarline)
		)
		nodeError(cancelLocation("foo"))(<foo>xxx</foo>)
	}

	@Test def clefSignTest() = {
		nodeTest(clefSign("foo"))(
			(<foo>G</foo>, ClefSign_G),
			(<foo>F</foo>, ClefSign_F),
			(<foo>C</foo>, ClefSign_C),
			(<foo>percussion</foo>, ClefSign_Percussion),
			(<foo>TAB</foo>, ClefSign_TAB),
			(<foo>jianpu</foo>, ClefSign_Jianpu),
			(<foo>none</foo>, ClefSign_None)
		)
		nodeError(clefSign("foo"))(<foo>xxx</foo>)
	}

	@Test def fifthsTest() = {
		nodeTest(fifths("foo"))(
			(<foo>5</foo>, 5)
		)
		nodeError(fifths("foo"))(<foo>3.14</foo>, <foo>xxx</foo>)
	}

	@Test def modeTest() = {
		nodeTest(mode("foo"))(
			(<foo>mode123</foo>, "mode123")
		)
	}

	@Test def showFretsTest() = {
		nodeTest(showFrets("foo"))(
			(<foo>numbers</foo>, ShowFrets_Numbers),
			(<foo>letters</foo>, ShowFrets_Letters)
		)
		nodeError(showFrets("foo"))(<foo>xxx</foo>)
	}

	@Test def staffLineTest() = {
		nodeTest(staffLine("foo"))(
			(<foo>5</foo>, 5)
		)
		nodeError(staffLine("foo"))(<foo>3.14</foo>, <foo>xxx</foo>)
	}

	@Test def staffNumberTest() = {
		nodeTest(staffNumber("foo"))(
			(<foo>5</foo>, StaffNumber(5))
		)
		nodeError(staffNumber("foo"))(<foo>0</foo>, <foo>3.14</foo>, <foo>xxx</foo>)
	}

	@Test def staffTypeTest() = {
		nodeTest(staffType("foo"))(
			(<foo>ossia</foo>, StaffType_Ossia),
			(<foo>cue</foo>, StaffType_Cue),
			(<foo>editorial</foo>, StaffType_Editorial),
			(<foo>regular</foo>, StaffType_Regular),
			(<foo>alternate</foo>, StaffType_Alternate)
		)
		nodeError(staffType("foo"))(<foo>xxx</foo>)
	}

	@Test def timeRelationTest() = {
		nodeTest(timeRelation("foo"))(
			(<foo>parentheses</foo>, TimeRelation_Parentheses),
			(<foo>bracket</foo>, TimeRelation_Bracket),
			(<foo>equals</foo>, TimeRelation_Equals),
			(<foo>slash</foo>, TimeRelation_Slash),
			(<foo>space</foo>, TimeRelation_Space),
			(<foo>hyphen</foo>, TimeRelation_Hyphen)
		)
		nodeError(timeRelation("foo"))(<foo>xxx</foo>)
	}

	@Test def timeSeparatorTest() = {
		nodeTest(timeSeparator("foo"))(
			(<foo>none</foo>, TimeSeparator_None),
			(<foo>horizontal</foo>, TimeSeparator_Horizontal),
			(<foo>diagonal</foo>, TimeSeparator_Diagonal),
			(<foo>vertical</foo>, TimeSeparator_Vertical),
			(<foo>adjacent</foo>, TimeSeparator_Adjacent)
		)
		nodeError(timeSeparator("foo"))(<foo>xxx</foo>)
	}

	@Test def timeSymbolTest() = {
		nodeTest(timeSymbol("foo"))(
			(<foo>common</foo>, TimeSymbol_Common),
			(<foo>cut</foo>, TimeSymbol_Cut),
			(<foo>single-number</foo>, TimeSymbol_SingleNumber),
			(<foo>note</foo>, TimeSymbol_Note),
			(<foo>dotted-note</foo>, TimeSymbol_DottedNote),
			(<foo>normal</foo>, TimeSymbol_Normal)
		)
		nodeError(timeSymbol("foo"))(<foo>xxx</foo>)
	}
}

trait PrimativeBarlineParserTest[M[+ _]] extends PrimativesParserTest[M] {

	import primativeBarlineParser._

	@Test def backwardForwardTest() = {
		nodeTest(backwardForward("foo"))(
			(<foo>backward</foo>, BF_Backward),
			(<foo>forward</foo>, BF_Forward)
		)
		nodeError(backwardForward("foo"))(<foo>xxx</foo>)
	}

	@Test def barStyleTest() = {
		nodeTest(barStyle("foo"))(
			(<foo>regular</foo>, BarStyle_Regular),
			(<foo>dotted</foo>, BarStyle_Dotted),
			(<foo>dashed</foo>, BarStyle_Dashed),
			(<foo>heavy</foo>, BarStyle_Heavy),
			(<foo>light-light</foo>, BarStyle_LightLight),
			(<foo>light-heavy</foo>, BarStyle_LightHeavy),
			(<foo>heavy-light</foo>, BarStyle_HeavyLight),
			(<foo>heavy-heavy</foo>, BarStyle_HeavyHeavy),
			(<foo>tick</foo>, BarStyle_Tick),
			(<foo>short</foo>, BarStyle_Short),
			(<foo>none</foo>, BarStyle_None)
		)
		nodeError(barStyle("foo"))(<foo>xxx</foo>)
	}

	@Test def endingNumberTest() = {
		nodeTest(endingNumber("foo"))(
			(<foo>    </foo>, EndingNumber("    ")),
			(<foo>12</foo>, EndingNumber("12")),
			(<foo>12,34</foo>, EndingNumber("12,34")),
			(<foo>12, 34</foo>, EndingNumber("12, 34")),
			(<foo>12, 34, 567</foo>, EndingNumber("12, 34, 567")),
			(<foo>12,34, 567</foo>, EndingNumber("12,34, 567"))
		)
		nodeError(endingNumber("foo"))(
			<foo>xxx</foo>,
			<foo>  1234</foo>,
			<foo>12,  34</foo>
		)
	}

	@Test def rightLeftMiddleTest() = {
		nodeTest(rightLeftMiddle("foo"))(
			(<foo>right</foo>, RLM_Right),
			(<foo>left</foo>, RLM_Left),
			(<foo>middle</foo>, RLM_Middle)
		)
		nodeError(rightLeftMiddle("foo"))(<foo>xxx</foo>)
	}

	@Test def startStopDiscontinueTest() = {
		nodeTest(startStopDiscontinue("foo"))(
			(<foo>start</foo>, SSD_Start),
			(<foo>stop</foo>, SSD_Stop),
			(<foo>discontinue</foo>, SSD_Discontinue)
		)
		nodeError(startStopDiscontinue("foo"))(<foo>xxx</foo>)
	}

	@Test def wingedTest() = {
		nodeTest(winged("foo"))(
			(<foo>none</foo>, Winged_None),
			(<foo>straight</foo>, Winged_Straight),
			(<foo>curved</foo>, Winged_Curved),
			(<foo>double-straight</foo>, Winged_DoubleStraight),
			(<foo>double-curved</foo>, Winged_DoubleCurved)
		)
		nodeError(winged("foo"))(<foo>xxx</foo>)
	}
}

trait PrimativeCommonParserTest[M[+ _]] extends PrimativesParserTest[M] {

	import primativeCommonParser._

	@Test def aboveBelowTest() = {
		nodeTest(aboveBelow("foo"))(
			(<foo>above</foo>, AB_Above),
			(<foo>below</foo>, AB_Below)
		)
		nodeError(aboveBelow("foo"))(<foo>xxx</foo>)
	}

	@Test def beamLevelTest() = {
		nodeTest(beamLevel("foo"))(
			(<foo>5</foo>, BeamLevel(5))
		)
		nodeError(beamLevel("foo"))(
			<foo>0</foo>,
			<foo>9</foo>,
			<foo>3.14</foo>,
			<foo>xxx</foo>
		)
	}

	@Test def colorTest() = {
		nodeTest(color("foo"))(
			(<foo>#4F3E2D</foo>, Color("#4F3E2D")),
			(<foo>#40800080</foo>, Color("#40800080"))
		)
		nodeError(color("foo"))(
			<foo>4F3E2D</foo>,
			<foo>#4F3G2D</foo>,
			<foo>3.14</foo>,
			<foo>#xxx</foo>
		)
	}

	@Test def commaSeparatedTextTest() = {
		nodeTest(commaSeparatedText("foo"))(
			(<foo>abc</foo>, CommaSeparatedText("abc")),
			(<foo>abc,def</foo>, CommaSeparatedText("abc,def")),
			(<foo>abc, def</foo>, CommaSeparatedText("abc, def"))
		)
		nodeError(commaSeparatedText("foo"))(
			<foo>abc,</foo>,
			<foo>,abc</foo>,
			<foo>abc,def,</foo>
		)
	}

	@Test def cssFontSideTest() = {
		nodeTest(cssFontSize("foo"))(
			(<foo>xx-small</foo>, CssFontSize_xxSmall),
			(<foo>x-small</foo>, CssFontSize_xSmall),
			(<foo>small</foo>, CssFontSize_Small),
			(<foo>medium</foo>, CssFontSize_Medium),
			(<foo>large</foo>, CssFontSize_Large),
			(<foo>x-large</foo>, CssFontSize_xLarge),
			(<foo>xx-large</foo>, CssFontSize_xxLarge)
		)
		nodeError(cssFontSize("foo"))(<foo>xxx</foo>)
	}

	@Test def divisionsTest() = {
		nodeTest(divisions("foo"))(
			(<foo>1.0</foo>, 1.0),
			(<foo>2</foo>, 2.0),
			(<foo>3.14</foo>, 3.14)
		)
		nodeError(divisions("foo"))(<foo>xxx</foo>)
	}

	@Test def enclosureShapeTest() = {
		nodeTest(enclosureShape("foo"))(
			(<foo>rectangle</foo>, EnclosureShape_Rectangle),
			(<foo>square</foo>, EnclosureShape_Square),
			(<foo>oval</foo>, EnclosureShape_Oval),
			(<foo>circle</foo>, EnclosureShape_Circle),
			(<foo>bracket</foo>, EnclosureShape_Bracket),
			(<foo>triangle</foo>, EnclosureShape_Triangle),
			(<foo>diamond</foo>, EnclosureShape_Diamond),
			(<foo>none</foo>, EnclosureShape_None)
		)
		nodeError(enclosureShape("foo"))(<foo>xxx</foo>)
	}

	@Test def fermataShapeTest() = {
		nodeTest(fermataShape("foo"))(
			(<foo>normal</foo>, FermataShape_Normal),
			(<foo>angled</foo>, FermataShape_Angled),
			(<foo>square</foo>, FermataShape_Square),
			(<foo></foo>, FermataShape_Empty)
		)
		nodeError(fermataShape("foo"))(<foo>xxx</foo>)
	}

	@Test def fontSizeTest() = {
		import XmlParser._

		assertEquals(mp.create(FS_CssFontSize(CssFontSize_Small)), fontSize("foo")(node)(node).eval(<foo>small</foo>))
		assertEquals(mp.create(FS_Double(5.0)), fontSize("foo")(node)(node).eval(<foo>5.0</foo>))
		assertEquals(mp.create(FS_Double(3.0)), fontSize("foo")(node)(node).eval(<foo>3</foo>))

		assertTrue(isEmpty(fontSize("foo")(node)(node).eval(<foo>xxx</foo>)))
	}

	@Test def fontStyleTest() = {
		nodeTest(fontStyle("foo"))(
			(<foo>normal</foo>, FontStyle_Normal),
			(<foo>italic</foo>, FontStyle_Italic)
		)
		nodeError(fontStyle("foo"))(<foo>xxx</foo>)
	}

	@Test def fontWeightTest() = {
		nodeTest(fontWeight("foo"))(
			(<foo>normal</foo>, FontWeight_Normal),
			(<foo>bold</foo>, FontWeight_Bold)
		)
		nodeError(fontWeight("foo"))(<foo>xxx</foo>)
	}

	@Test def leftCenterRightTest() = {
		nodeTest(leftCenterRight("foo"))(
			(<foo>left</foo>, LCR_Left),
			(<foo>center</foo>, LCR_Center),
			(<foo>right</foo>, LCR_Right)
		)
		nodeError(leftCenterRight("foo"))(<foo>xxx</foo>)
	}

	@Test def leftRightTest() = {
		nodeTest(leftRight("foo"))(
			(<foo>left</foo>, LR_Left),
			(<foo>right</foo>, LR_Right)
		)
		nodeError(leftRight("foo"))(<foo>xxx</foo>)
	}

	@Test def lineShapeTest() = {
		nodeTest(lineShape("foo"))(
			(<foo>straight</foo>, LineShape_Straight),
			(<foo>curved</foo>, LineShape_Curved)
		)
		nodeError(lineShape("foo"))(<foo>xxx</foo>)
	}

	@Test def lineTypeTest() = {
		nodeTest(lineType("foo"))(
			(<foo>solid</foo>, LineType_Solid),
			(<foo>dashed</foo>, LineType_Dashed),
			(<foo>dotted</foo>, LineType_Dotted),
			(<foo>wavy</foo>, LineType_Wavy)
		)
		nodeError(lineType("foo"))(<foo>xxx</foo>)
	}

	@Test def midi16Test() = {
		nodeTest(midi16("foo"))(
			(<foo>1</foo>, Midi16(1)),
			(<foo>8</foo>, Midi16(8)),
			(<foo>16</foo>, Midi16(16))
		)
		nodeError(midi16("foo"))(<foo>-3</foo>, <foo>0</foo>, <foo>17</foo>, <foo>3.14</foo>, <foo>xxx</foo>)
	}

	@Test def midi128Test() = {
		nodeTest(midi128("foo"))(
			(<foo>1</foo>, Midi128(1)),
			(<foo>8</foo>, Midi128(8)),
			(<foo>128</foo>, Midi128(128))
		)
		nodeError(midi128("foo"))(<foo>-3</foo>, <foo>0</foo>, <foo>129</foo>, <foo>3.14</foo>, <foo>xxx</foo>)
	}

	@Test def midi16384Test() = {
		nodeTest(midi16384("foo"))(
			(<foo>1</foo>, Midi16384(1)),
			(<foo>8</foo>, Midi16384(8)),
			(<foo>16384</foo>, Midi16384(16384))
		)
		nodeError(midi16384("foo"))(<foo>-3</foo>, <foo>0</foo>, <foo>16385</foo>, <foo>3.14</foo>, <foo>xxx</foo>)
	}

	@Test def muteTest() = {
		nodeTest(mute("foo"))(
			(<foo>on</foo>, Mute_On),
			(<foo>off</foo>, Mute_Off),
			(<foo>straight</foo>, Mute_Straight),
			(<foo>cup</foo>, Mute_Cup),
			(<foo>harmon-no-stem</foo>, Mute_HarmonNoStem),
			(<foo>harmon-stem</foo>, Mute_HarmonStem),
			(<foo>bucket</foo>, Mute_Bucket),
			(<foo>plunger</foo>, Mute_Plunger),
			(<foo>hat</foo>, Mute_Hat),
			(<foo>solotone</foo>, Mute_SoloTone),
			(<foo>practice</foo>, Mute_Practice),
			(<foo>stop-mute</foo>, Mute_StopMute),
			(<foo>stop-hand</foo>, Mute_StopHand),
			(<foo>echo</foo>, Mute_Echo),
			(<foo>palm</foo>, Mute_Palm)
		)
		nodeError(mute("foo"))(<foo>xxx</foo>)
	}

	@Test def nonNegativeDecimalTest() = {
		nodeTest(nonNegativeDecimal("foo"))(
			(<foo>0.0</foo>, NonNegativeDecimal(0.0)),
			(<foo>1.2</foo>, NonNegativeDecimal(1.2))
		)
		nodeError(nonNegativeDecimal("foo"))(<foo>xxx</foo>)
	}

	@Test def numberLevelTest() = {
		nodeTest(numberLevel("foo"))(
			(<foo>1</foo>, NumberLevel(1)),
			(<foo>3</foo>, NumberLevel(3)),
			(<foo>6</foo>, NumberLevel(6))
		)
		nodeError(numberLevel("foo"))(
			<foo>-3</foo>,
			<foo>0</foo>,
			<foo>7</foo>,
			<foo>3.14</foo>,
			<foo>xxx</foo>
		)
	}

	@Test def numberOfLinesTest() = {
		nodeTest(numberOfLines("foo"))(
			(<foo>0</foo>, NumberOfLines(0)),
			(<foo>2</foo>, NumberOfLines(2)),
			(<foo>3</foo>, NumberOfLines(3))
		)
		nodeError(numberOfLines("foo"))(<foo>-1</foo>, <foo>4</foo>, <foo>3.14</foo>, <foo>xxx</foo>)
	}

	@Test def numberOrNormalTest() = {
		nodeTest(numberOrNormal("foo"))(
			(<foo>normal</foo>, NON_Normal),
			(<foo>3.14</foo>, NON_Double(3.14))
		)
		nodeError(numberOrNormal("foo"))(<foo>xxx</foo>)
	}

	@Test def overUnderTest() = {
		nodeTest(overUnder("foo"))(
			(<foo>over</foo>, OU_Over),
			(<foo>under</foo>, OU_Under)
		)
		nodeError(overUnder("foo"))(<foo>xxx</foo>)
	}

	@Test def percentTest() = {
		nodeTest(percent("foo"))(
			(<foo>0.0</foo>, Percent(0.0)),
			(<foo>3.14</foo>, Percent(3.14)),
			(<foo>1</foo>, Percent(1.0)),
			(<foo>100</foo>, Percent(100.0))
		)
		nodeError(percent("foo"))(<foo>-1.1</foo>, <foo>101.9</foo>, <foo>xxx</foo>)
	}

	@Test def positiveDecimalTest() = {
		nodeTest(positiveDecimal("foo"))(
			(<foo>3.14</foo>, PositiveDecimal(3.14)),
			(<foo>1</foo>, PositiveDecimal(1.0))
		)

		assertTrue(isEmpty(positiveDecimal("foo")(XmlParser.node).eval(<foo>-102.3</foo>)))

		nodeError(positiveDecimal("foo"))(<foo>-102.3</foo>, <foo>0.0</foo>, <foo>xxx</foo>)
	}

	@Test def positiveDivisionsTest() = {
		nodeTest(positiveDivisions("foo"))(
			(<foo>3.14</foo>, PositiveDivisions(3.14)),
			(<foo>1</foo>, PositiveDivisions(1.0))
		)
		nodeError(positiveDivisions("foo"))(<foo>-102.3</foo>, <foo>0.0</foo>, <foo>xxx</foo>)
	}

	@Test def positiveIntegerOrEmptyTest() = {
		nodeTest(positiveIntegerOrEmpty("foo"))(
			(<foo>1</foo>, PositiveIntegerOrEmpty(Option(1))),
			(<foo/>, PositiveIntegerOrEmpty(Option.empty))
		)
		nodeError(positiveIntegerOrEmpty("foo"))(<foo>0</foo>, <foo>-1</foo>, <foo>xxx</foo>)
	}

	@Test def rotationDegreesTest() = {
		nodeTest(rotationDegrees("foo"))(
			(<foo>3.14</foo>, RotationDegrees(3.14)),
			(<foo>1</foo>, RotationDegrees(1.0)),
			(<foo>-180</foo>, RotationDegrees(-180)),
			(<foo>180</foo>, RotationDegrees(180))
		)
		nodeError(rotationDegrees("foo"))(<foo>-190</foo>, <foo>190</foo>, <foo>xxx</foo>)
	}

	@Test def semiPitchedTest() = {
		nodeTest(semiPitched("foo"))(
			(<foo>high</foo>, SemiPitched_High),
			(<foo>medium-high</foo>, SemiPitched_MediumHigh),
			(<foo>medium</foo>, SemiPitched_Medium),
			(<foo>medium-low</foo>, SemiPitched_MediumLow),
			(<foo>low</foo>, SemiPitched_Low),
			(<foo>very-low</foo>, SemiPitched_VeryLow)
		)
		nodeError(semiPitched("foo"))(<foo>xxx</foo>)
	}

	@Test def startNoteTest() = {
		nodeTest(startNote("foo"))(
			(<foo>upper</foo>, StartNote_Upper),
			(<foo>main</foo>, StartNote_Main),
			(<foo>below</foo>, StartNote_Below)
		)
		nodeError(startNote("foo"))(<foo>xxx</foo>)
	}

	@Test def startStopTest() = {
		nodeTest(startStop("foo"))(
			(<foo>start</foo>, SS_Start),
			(<foo>stop</foo>, SS_Stop)
		)
		nodeError(startStop("foo"))(<foo>xxx</foo>)
	}

	@Test def startStopContinueTest() = {
		nodeTest(startStopContinue("foo"))(
			(<foo>start</foo>, SSC_Start),
			(<foo>stop</foo>, SSC_Stop),
			(<foo>continue</foo>, SSC_Continue)
		)
		nodeError(startStopContinue("foo"))(<foo>xxx</foo>)
	}

	@Test def startStopSingleTest() = {
		nodeTest(startStopSingle("foo"))(
			(<foo>start</foo>, SSS_Start),
			(<foo>stop</foo>, SSS_Stop),
			(<foo>single</foo>, SSS_Single)
		)
		nodeError(startStopSingle("foo"))(<foo>xxx</foo>)
	}

	@Test def startStringNumberTest() = {
		nodeTest(stringNumber("foo"))(
			(<foo>1</foo>, StringNumber(1))
		)
		nodeError(stringNumber("foo"))(<foo>-2</foo>, <foo>0</foo>, <foo>xxx</foo>)
	}

	@Test def symbolSizeTest() = {
		nodeTest(symbolSize("foo"))(
			(<foo>full</foo>, SymbolSize_Full),
			(<foo>cue</foo>, SymbolSize_Cue),
			(<foo>large</foo>, SymbolSize_Large)
		)
		nodeError(symbolSize("foo"))(<foo>xx</foo>)
	}

	@Test def tenthsTest() = {
		nodeTest(tenths("foo"))(
			(<foo>3.14</foo>, 3.14),
			(<foo>-3.14</foo>, -3.14),
			(<foo>0.0</foo>, 0.0),
			(<foo>1</foo>, 1.0)
		)
		nodeError(tenths("foo"))(<foo>xxx</foo>)
	}

	@Test def textDirectionTest() = {
		nodeTest(textDirection("foo"))(
			(<foo>ltr</foo>, TextDirection_LTR),
			(<foo>rtl</foo>, TextDirection_RTL),
			(<foo>lro</foo>, TextDirection_LRO),
			(<foo>rlo</foo>, TextDirection_RLO)
		)
		nodeError(textDirection("foo"))(<foo>xxx</foo>)
	}

	@Test def timeOnlyTest() = {
		nodeTest(timeOnly("foo"))(
			(<foo>1</foo>, TimeOnly("1")),
			(<foo>1234</foo>, TimeOnly("1234")),
			(<foo>1234, 109</foo>, TimeOnly("1234, 109")),
			(<foo>1234, 100, 921</foo>, TimeOnly("1234, 100, 921")),
			(<foo>1, 123</foo>, TimeOnly("1, 123")),
			(<foo>1234,109</foo>, TimeOnly("1234,109")),
			(<foo>1234, 100,921</foo>, TimeOnly("1234, 100,921")),
			(<foo>1,123</foo>, TimeOnly("1,123"))
		)
		nodeError(timeOnly("foo"))(<foo>123d12</foo>, <foo>xxx</foo>)
	}

	@Test def topBottomTest() = {
		nodeTest(topBottom("foo"))(
			(<foo>top</foo>, TB_Top),
			(<foo>bottom</foo>, TB_Bottom)
		)
		nodeError(topBottom("foo"))(<foo>xxx</foo>)
	}

	@Test def trillBeatsTest() = {
		nodeTest(trillBeats("foo"))(
			(<foo>2.0</foo>, TrillBeats(2.0)),
			(<foo>3</foo>, TrillBeats(3))
		)
		nodeError(trillBeats("foo"))(<foo>-3.14</foo>, <foo>1.9999</foo>, <foo>0</foo>, <foo>xxx</foo>)
	}

	@Test def trillStepTest() = {
		nodeTest(trillStep("foo"))(
			(<foo>whole</foo>, TrillStep_Whole),
			(<foo>half</foo>, TrillStep_Half),
			(<foo>unison</foo>, TrillStep_Unison)
		)
		nodeError(trillStep("foo"))(<foo>xxx</foo>)
	}

	@Test def twoNoteTurnTest() = {
		nodeTest(twoNoteTurn("foo"))(
			(<foo>whole</foo>, TwoNoteTurn_Whole),
			(<foo>half</foo>, TwoNoteTurn_Half),
			(<foo>none</foo>, TwoNoteTurn_None)
		)
		nodeError(twoNoteTurn("foo"))(<foo>xxx</foo>)
	}

	@Test def upDownTest() = {
		nodeTest(upDown("foo"))(
			(<foo>up</foo>, UD_Up),
			(<foo>down</foo>, UD_Down)
		)
		nodeError(upDown("foo"))(<foo>xxx</foo>)
	}

	@Test def uprightInvertedTest() = {
		nodeTest(uprightInverted("foo"))(
			(<foo>upright</foo>, UI_Upright),
			(<foo>inverted</foo>, UI_Inverted)
		)
		nodeError(uprightInverted("foo"))(<foo>xxx</foo>)
	}

	@Test def vAlignTest() = {
		nodeTest(vAlign("foo"))(
			(<foo>top</foo>, VAlign_Top),
			(<foo>middle</foo>, VAlign_Middle),
			(<foo>bottom</foo>, VAlign_Bottom),
			(<foo>baseline</foo>, VAlign_Baseline)
		)
		nodeError(vAlign("foo"))(<foo>xxx</foo>)
	}

	@Test def vAlignImageTest() = {
		nodeTest(vAlignImage("foo"))(
			(<foo>top</foo>, VAlignImage_Top),
			(<foo>middle</foo>, VAlignImage_Middle),
			(<foo>bottom</foo>, VAlignImage_Bottom)
		)
		nodeError(vAlignImage("foo"))(<foo>xxx</foo>)
	}

	@Test def yesNoTest() = {
		nodeTest(yesNo("foo"))(
			(<foo>yes</foo>, YN_Yes),
			(<foo>no</foo>, YN_No)
		)
		nodeError(yesNo("foo"))(<foo>xxx</foo>)
	}

	@Test def yesNoNumberTest() = {
		import XmlParser._

		assertEquals(mp.create(YNN_YesNo(YN_Yes)), yesNoNumber("foo")(node)(node).eval(<foo>yes</foo>))
		assertEquals(mp.create(YNN_YesNo(YN_No)), yesNoNumber("foo")(node)(node).eval(<foo>no</foo>))
		assertEquals(mp.create(YNN_Double(3.5)), yesNoNumber("foo")(node)(node).eval(<foo>3.5</foo>))
		assertEquals(mp.create(YNN_Double(-3.5)), yesNoNumber("foo")(node)(node).eval(<foo>-3.5</foo>))

		assertTrue(isEmpty(yesNoNumber("foo")(node)(node).eval(<foo>xxx</foo>)))
	}

	@Test def dateTest() = {
		nodeTest(date("foo"))(
			(<foo>1992-07-30</foo>, Date("1992-07-30")),
			(<foo>1992-07-30T</foo>, Date("1992-07-30T"))
		)
		nodeError(date("foo"))(
			<foo>1992-07-30T13:15:30</foo>,
			<foo>1992-07-30Z</foo>,
			<foo>1992-07-30T13:15:30Z</foo>
		)
	}
}

trait PrimativeDirectionParserTest[M[+_]] extends PrimativesParserTest[M] {

	import primativeDirectionParser._
}

trait PrimativeLayoutParserTest[M[+_]] extends PrimativesParserTest[M] {

	import primativeLayoutParser._
}

trait PrimativeNoteParserTest[M[+_]] extends PrimativesParserTest[M] {

	import primativeNoteParser._
}

trait PrimativeScoreParserTest[M[+_]] extends PrimativesParserTest[M] {

	import primativeScoreParser._
}

trait PrimativesParserTestSequence[M[+ _]] extends PrimativeAttributesParserTest[M]
																									 with PrimativeBarlineParserTest[M]
																									 with PrimativeCommonParserTest[M]
																									 with PrimativeDirectionParserTest[M]
																									 with PrimativeLayoutParserTest[M]
																									 with PrimativeNoteParserTest[M]
																									 with PrimativeScoreParserTest[M]

class PrimativesParserOptionTest extends PrimativesParserTestSequence[Option] {
	protected implicit val mp: MonadPlus[Option] = OptionIsMonadPlus

	def isEmpty[T](m: Option[T]): Boolean = m.isEmpty
}

class PrimativesParserTryTest extends PrimativesParserTestSequence[Try] {
	protected implicit val mp: MonadPlus[Try] = TryIsMonadPlus

	def isEmpty[T](m: Try[T]): Boolean = m.isFailure
}
