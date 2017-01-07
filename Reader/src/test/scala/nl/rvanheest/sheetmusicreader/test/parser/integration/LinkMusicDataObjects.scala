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

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsLink.{ElementPosition, LinkAttributes}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLink.Link
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.LinkChoice

object LinkMusicDataObjects {

	val link = LinkChoice(Link(
		LinkAttributes("http://www.google.com/"),
		Option("link name"),
		ElementPosition(Option("abc"), Option(123))))
}
