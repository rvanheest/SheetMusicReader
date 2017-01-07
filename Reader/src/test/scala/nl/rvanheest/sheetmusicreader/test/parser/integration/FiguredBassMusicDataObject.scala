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

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.{FormattedText, Level}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexNote.{Extend, Figure, FiguredBass, StyleText}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.Editorial
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.FiguredBassChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.{PositiveDivisions, SSC_Continue, YN_No, YN_Yes}

object FiguredBassMusicDataObject {
	val figuredBass0 = FiguredBassChoice(FiguredBass(
		figure = List(
			Figure(
				Option(StyleText("prefix 1")),
				Option(StyleText("figure number 1")),
				Option(StyleText("suffix 1")),
				Option(Extend(Option(SSC_Continue)))),
			Figure(suffix = Option(StyleText("suffix 2")))
		),
		duration = Option(PositiveDivisions(3)),
		editorial = Editorial(
			footNote = Option(FormattedText("figured bass footnote")),
			level = Option(Level("figured bass level", Option(YN_No)))
		)
	))

	val figuredBass1 = FiguredBassChoice(FiguredBass(
		figure = List(Figure()),
		editorial = Editorial(level = Option(Level("figured bass level 2")))
	))

	val figuredBass2 = FiguredBassChoice(FiguredBass(
		figure = List(Figure()),
		parentheses = Option(YN_Yes)
	))
}
