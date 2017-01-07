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

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.{AccidentalText, AccidentalTextNameDisplayChoice, NameDisplay}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexDirection.{MeasureNumbering, Print}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLayout.MeasureLayout
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupLayout.Layout
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.PrintChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeDirection.MNV_None
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeNote.{AccidentalValue_Flat, AccidentalValue_Sharp}

object PrintMusicDataObjects {
	val print0 = PrintChoice(Print(
		Layout(),
		Option(MeasureLayout(Option(12))),
		Option(MeasureNumbering(MNV_None)),
		Option(NameDisplay(List(AccidentalTextNameDisplayChoice(AccidentalText(AccidentalValue_Sharp))))),
		Option(NameDisplay(List(AccidentalTextNameDisplayChoice(AccidentalText(AccidentalValue_Flat)))))))

	val print1 = PrintChoice()
}
