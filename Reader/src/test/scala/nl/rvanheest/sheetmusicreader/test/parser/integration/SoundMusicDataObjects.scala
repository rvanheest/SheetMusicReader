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

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexDirection.Sound
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.SoundChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon._

object SoundMusicDataObjects {

	val sound = SoundChoice(Sound(
		tempo = Option(NonNegativeDecimal(2.68)),
		dynamics = Option(NonNegativeDecimal(3.14)),
		dacapo = Option(YN_No),
		segno = Option("mySegno"),
		dalsegno = Option("myDalsegno"),
		coda = Option("myCoda"),
		tocoda = Option("myTocoda"),
		divisions = Option(-1.4142),
		forwardRepeat = Option(YN_Yes),
		fine = Option("myFine"),
		timeOnly = Option(TimeOnly("12569, 686923, 6694")),
		pizzicato = Option(YN_No),
		pan = Option(RotationDegrees(53)),
		elevation = Option(RotationDegrees(35)),
		damperPedal = Option(YNN_Double(4)),
		softPedal = Option(YNN_YesNo(YN_No)),
		sostenutoPedal = Option(YNN_YesNo(YN_Yes))
	))
}
