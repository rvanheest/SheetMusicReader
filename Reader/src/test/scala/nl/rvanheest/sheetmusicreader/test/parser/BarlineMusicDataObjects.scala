package nl.rvanheest.sheetmusicreader.test.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexBarline.{BarStyleColor, Barline, Ending, Repeat}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.{EmptyPrintStyleAlign, Fermata, WavyLine}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.BarlineChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeBarline._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon._

object BarlineMusicDataObjects {

	val barline0 = BarlineChoice(Barline(
		barStyle = Option(BarStyleColor(BarStyle_LightLight)),
		wavyLine = Option(WavyLine(SSC_Stop, Option(NumberLevel(3)))),
		segno = Option(EmptyPrintStyleAlign()),
		coda = Option(EmptyPrintStyleAlign()),
		fermata = List(
			Fermata(FermataShape_Angled, Option(UI_Upright)),
			Fermata(FermataShape_Normal)),
		ending = Option(Ending("myEnding", EndingNumber("12345"), SSD_Discontinue)),
		repeat = Option(Repeat(BF_Forward, Option(21), Option(Winged_DoubleCurved)))))

	val barline1 = BarlineChoice(Barline(
		location = RLM_Right,
		segnoAttr = Option("mySegno"),
		codaArrt = Option("myCoda"),
		divisions = Option(12.345)))
}
