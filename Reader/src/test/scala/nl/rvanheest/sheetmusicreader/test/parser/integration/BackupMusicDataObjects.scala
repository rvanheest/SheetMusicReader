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

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsCommon.LevelDisplay
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.{FormattedText, Level}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexNote.Backup
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.Editorial
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.BackupChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.{PositiveDivisions, SymbolSize_Cue, YN_No, YN_Yes}

object BackupMusicDataObjects {
	val backup0 = BackupChoice(Backup(
		PositiveDivisions(15.14),
		Editorial(
			Option(FormattedText("foobar")),
			Option(Level(
				"foo",
				Option(YN_No),
				LevelDisplay(Option(YN_Yes), Option(YN_No), Option(SymbolSize_Cue)))))))

	val backup1 = BackupChoice(Backup(PositiveDivisions(14.13)))
}
