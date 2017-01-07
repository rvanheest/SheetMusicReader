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
