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

import nl.rvanheest.sheetmusicreader.musicxml.model
import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsCommon.{Color, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.EmptyPrintObjectStyleAlign
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLayout._
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupLayout.{AllMargins, Layout, LeftRightMargins}
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeAttributes.StaffNumber
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.{Color => PrimColor, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeLayout.{MarginType_Odd, NoteSizeType_Cue, NoteSizeType_Grace}

object LayoutObjects {
	val appearance = {
		val lw1 = LineWidth(21, "foo")
		val lw2 = LineWidth(22, "bar")
		val ns1 = NoteSize(NonNegativeDecimal(0), NoteSizeType_Cue)
		val ns2 = NoteSize(NonNegativeDecimal(23), NoteSizeType_Grace)
		val d1 = Distance(24, "bar")
		val d2 = Distance(25, "foo")
		val oa1 = OtherAppearance("test", "fo")
		val oa2 = OtherAppearance("test2", "ba")

		Appearance(List(lw1, lw2), List(ns1, ns2), List(d1, d2), List(oa1, oa2))
	}

	val pageLayout = {
		val size = PageSize(1, 2)
		val margins = List(PageMargins(AllMargins(LeftRightMargins(3, 4), 5, 6)),
			PageMargins(AllMargins(LeftRightMargins(7, 8), 9, 10), Option(MarginType_Odd)))

		PageLayout(Option(size), margins)
	}

	val systemLayout = {
		val systemMargins = LeftRightMargins(11, 12)
		val leftPrintStyleAlign = PrintStyleAlign(
			printStyle = PrintStyle(
				position = Position(Option(15), Option(16), Option(17), Option(18)),
				font = Font(
					family = Option(CommaSeparatedText("Font1")),
					style = Option(FontStyle_Normal),
					size = Option(FS_CssFontSize(CssFontSize_Small)),
					weight = Option(FontWeight_Bold)),
				color = Color(Option(PrimColor("#40800080")))),
			halign = HAlign(Option(LCR_Center)),
			valign = VAlign(Option(VAlign_Baseline)))
		val rightPrintStyleAlign = PrintStyleAlign()
		val leftDiv = EmptyPrintObjectStyleAlign(PrintObject(Option(YN_Yes)), leftPrintStyleAlign)
		val rightDiv = EmptyPrintObjectStyleAlign(printStyleAlign = rightPrintStyleAlign)
		val systemDividers = SystemDividers(leftDiv, rightDiv)

		SystemLayout(Option(systemMargins), Option(13), Option(14), Option(systemDividers))
	}

	val layout = {
		val sl1 = StaffLayout(Option(20), Option(StaffNumber(5)))
		val sl2 = StaffLayout(number = Option(StaffNumber(1)))
		val sl3 = StaffLayout()

		Layout(Option(pageLayout), Option(systemLayout), List(sl1, sl2, sl3))
	}
}
