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
package nl.rvanheest.sheetmusicreader.musicxml.model

object Root {

	import AttributeGroups.AttributeGroupsCommon.DocumentAttributes
	import AttributeGroups.AttributeGroupsScore.{MeasureAttributes, PartAttributes}
	import Group.GroupScore.{MusicData, ScoreHeader}

	/**
		* The score is the root element for the schema. It includes the score-header group,
		* followed either by a series of parts with measures inside (score-partwise) or a series of
		* measures with parts inside (score-timewise). Having distinct top-level elements for partwise
		* and timewise scores makes it easy to ensure that an XSLT stylesheet does not try to transform
		* a document already in the desired format.
		*/

	/**
		* The score-partwise element is the root element for a partwise MusicXML score.
		* It includes a score-header group followed by a series of parts with measures inside.
		* The document-attributes attribute group includes the version attribute.
		*/
	case class ScorePartwise(header: ScoreHeader, part: List[PartScorePartwise],
													 documentAttributes: DocumentAttributes = DocumentAttributes()) {
		require(part.nonEmpty)
	}
	case class PartScorePartwise(measure: List[MeasureScorePartwise], partAttributes: PartAttributes) {
		require(measure.nonEmpty)
	}
	case class MeasureScorePartwise(musicData: MusicData, measureAttributes: MeasureAttributes)


	/**
		* The score-timewise element is the root element for a timewise MusicXML score.
		* It includes a score-header group followed by a series of measures with parts inside.
		* The document-attributes attribute group includes the version attribute.
		*/
	case class ScoreTimewise(header: ScoreHeader, measure: List[MeasureScoreTimewise],
													 documentAttributes: DocumentAttributes = DocumentAttributes()) {
		require(measure.nonEmpty)
	}
	case class MeasureScoreTimewise(part: List[PartScoreTimewise],
																	measureAttributes: MeasureAttributes) {
		require(part.nonEmpty)
	}
	case class PartScoreTimewise(musicData: MusicData = MusicData(), partAttributes: PartAttributes)
}
