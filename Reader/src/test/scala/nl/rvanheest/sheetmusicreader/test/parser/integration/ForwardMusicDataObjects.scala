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
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexNote.Forward
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.{EditorialVoice, Staff}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.ForwardChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.PositiveDivisions

object ForwardMusicDataObjects {
	val forward0 = ForwardChoice(Forward(
		PositiveDivisions(13.12),
		EditorialVoice(
			Option(FormattedText("test")),
			Option(Level("test123")),
			Option("voice123")),
		Option(
			Staff(11))))

	val forward1 = ForwardChoice(Forward(
		PositiveDivisions(12.11),
		staff = Option(Staff(10))))

	val forward2 = ForwardChoice(Forward(PositiveDivisions(11.10)))
}
