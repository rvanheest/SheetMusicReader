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

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexDirection.{Feature, Grouping}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.GroupingChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.{SSS_Single, SSS_Start}

object GroupingMusicDataObjects {

	val grouping0 = GroupingChoice(Grouping(
		feature = List(
			Feature("feature 1", Option("type1")),
			Feature("feature 2")),
		groupingType = SSS_Single))

	val grouping1 = GroupingChoice(Grouping(
		groupingType = SSS_Start,
		number = "2",
		memberOf = Option("myMemberOf")))
}
