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

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsScore.MeasureAttributes
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.MusicData
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.YN_Yes
import nl.rvanheest.sheetmusicreader.musicxml.model.Root.MeasureScorePartwise

object Measure1Objects {

	import AttributeMusicDataObjects._
	import BackupMusicDataObjects._
	import BarlineMusicDataObjects._
	import BookmarkMusicDataObjects._
	import DirectionMusicDataObjects._
	import FiguredBassMusicDataObject._
	import ForwardMusicDataObjects._
	import GroupingMusicDataObjects._
	import HarmonyMusicDataObjects._
	import LinkMusicDataObjects._
	import NoteMusicDataObjects._
	import PrintMusicDataObjects._
	import SoundMusicDataObjects._

	val musicData = MusicData(List(
		print0, print1,
		attr0, attr1,
		harm0, harm1, harm2,
		backup0, backup1,
		forward0, forward1, forward2,
		direction0, direction1, direction2,
		note0, note1, note2, note3,
		figuredBass0, figuredBass1, figuredBass2,
		sound, barline0, barline1, grouping0, grouping1,
		link, bookmark))

	val attributes = MeasureAttributes("1_1", Option(YN_Yes), Option(YN_Yes), Option(2))

	val measure1_1 = MeasureScorePartwise(musicData, attributes)
}
