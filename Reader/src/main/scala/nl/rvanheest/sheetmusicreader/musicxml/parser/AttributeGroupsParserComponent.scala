package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsCommon.{XPosition, YPosition, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsDirection.{ImageAttributes, PrintAttributes}
import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsLink.{ElementPosition, LinkAttributes}
import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsScore.{GroupNameText, MeasureAttributes, PartAttributes, PartNameText}

import scala.language.postfixOps
import scala.xml.{NamespaceBinding, TopScope}

trait AttributeGroupsParserComponent {
	this: PrimativesParserComponent
		with XmlParserComponent =>

	protected val attributeGroupsCommonParser: AttributeGroupsCommonParser
	protected val attributeGroupsDirectionParser: AttributeGroupsDirectionParser
	protected val attributeGroupsLinkParser: AttributeGroupsLinkParser
	protected val attributeGroupsScoreParser: AttributeGroupsScoreParser

	class AttributeGroupsCommonParser {

		import xmlParser._
		import primativeCommonParser._

		def xmlToBendSound: XmlParser[BendSound] = {
			for {
				accelerate <- yesNo("accelerate")(attribute).maybe
				beats <- trillBeats("beats")(attribute).maybe
				firstBeat <- percent("first-beat")(attribute).maybe
				lastBeat <- percent("last-beat")(attribute).maybe
			} yield BendSound(accelerate, beats, firstBeat, lastBeat)
		}

		def xmlToBezier: XmlParser[Bezier] = {
			for {
				offset <- divisions("bezier-offset")(attribute).maybe
				offset2 <- divisions("bezier-offset2")(attribute).maybe
				x <- tenths("bezier-x")(attribute).maybe
				y <- tenths("bezier-y")(attribute).maybe
				x2 <- tenths("bezier-x2")(attribute).maybe
				y2 <- tenths("bezier-y2")(attribute).maybe
			} yield Bezier(offset, offset2, x, y, x2, y2)
		}

		def xmlToColor: XmlParser[Color] = {
			color("color")(attribute).maybe.map(Color)
		}

		def xmlToDashedFormatting: XmlParser[DashedFormatting] = {
			for {
				dash <- tenths("dash-length")(attribute).maybe
				space <- tenths("space-length")(attribute).maybe
			} yield DashedFormatting(dash, space)
		}

		def xmlToDirective: XmlParser[Directive] = {
			yesNo("directive")(attribute).maybe.map(Directive)
		}

		def xmlToDocumentAttributes: XmlParser[DocumentAttributes] = {
			(attributeId("version") <|> Parser.from("1.0")).map(DocumentAttributes)
		}

		def xmlToEnclosure: XmlParser[Enclosure] = {
			enclosureShape("enclosure")(attribute).maybe.map(Enclosure)
		}

		def xmlToFont: XmlParser[Font] = {
			for {
				family <- commaSeparatedText("font-family")(attribute).maybe
				style <- fontStyle("font-style")(attribute).maybe
				size <- fontSize("font-size")(attribute)(attribute).maybe
				weight <- fontWeight("font-weight")(attribute).maybe
			} yield Font(family, style, size, weight)
		}

		def xmlToHAlign: XmlParser[HAlign] = {
			leftCenterRight("halign")(attribute).maybe.map(HAlign)
		}

		def xmlToJustify: XmlParser[Justify] = {
			leftCenterRight("justify")(attribute).maybe.map(Justify)
		}

		def xmlToLetterSpacing: XmlParser[LetterSpacing] = {
			numberOrNormal("letter-spacing")(attribute).maybe.map(LetterSpacing)
		}

		def xmlToLevelDisplay: XmlParser[LevelDisplay] = {
			for {
				parentheses <- yesNo("parentheses")(attribute).maybe
				bracket <- yesNo("bracket")(attribute).maybe
				size <- symbolSize("size")(attribute).maybe
			} yield LevelDisplay(parentheses, bracket, size)
		}

		def xmlToLineHeight: XmlParser[LineHeight] = {
			numberOrNormal("line-height")(attribute).maybe.map(LineHeight)
		}

		def xmlToLineShape: XmlParser[LineShape] = {
			lineShape("line-shape")(attribute).maybe.map(LineShape)
		}

		def xmlToLineType: XmlParser[LineType] = {
			lineType("line-type")(attribute).maybe.map(LineType)
		}

		def xmlToOrientation: XmlParser[Orientation] = {
			overUnder("orientation")(attribute).maybe.map(Orientation)
		}

		def xmlToPlacement: XmlParser[Placement] = {
			aboveBelow("placement")(attribute).maybe.map(Placement)
		}

		def xmlToPosition: XmlParser[Position] = {
			for {
				defaultX <- tenths("default-x")(attribute).maybe
				defaultY <- tenths("default-y")(attribute).maybe
				relativeX <- tenths("relative-x")(attribute).maybe
				relativeY <- tenths("relative-y")(attribute).maybe
			} yield Position(defaultX, defaultY, relativeX, relativeY)
		}

		def xmlToPrintObject: XmlParser[PrintObject] = {
			yesNo("print-object")(attribute).maybe.map(PrintObject)
		}

		def xmlToPrintSpacing: XmlParser[PrintSpacing] = {
			yesNo("print-spacing")(attribute).maybe.map(PrintSpacing)
		}

		def xmlToPrintStyle: XmlParser[PrintStyle] = {
			for {
				position <- xmlToPosition
				font <- xmlToFont
				color <- xmlToColor
			} yield PrintStyle(position, font, color)
		}

		def xmlToPrintStyleAlign: XmlParser[PrintStyleAlign] = {
			for {
				printStyle <- xmlToPrintStyle
				halign <- xmlToHAlign
				valign <- xmlToVAlign
			} yield PrintStyleAlign(printStyle, halign, valign)
		}

		def xmlToPrintout: XmlParser[Printout] = {
			for {
				printObject <- xmlToPrintObject
				dot <- yesNo("print-dot")(attribute).maybe
				spacing <- xmlToPrintSpacing
				lyric <- yesNo("print-lyric")(attribute).maybe
			} yield Printout(printObject, dot, spacing, lyric)
		}

		def xmlToTextDecoration: XmlParser[TextDecoration] = {
			for {
				underline <- numberOfLines("underline")(attribute).maybe
				overline <- numberOfLines("overline")(attribute).maybe
				lineThrough <- numberOfLines("line-through")(attribute).maybe
			} yield TextDecoration(underline, overline, lineThrough)
		}

		def xmlToTextDirection: XmlParser[TextDirection] = {
			textDirection("dir")(attribute).maybe.map(TextDirection)
		}

		def xmlToTextFormatting: XmlParser[TextFormatting] = {
			for {
				justify <- xmlToJustify
				printStyleAlign <- xmlToPrintStyleAlign
				textDecoration <- xmlToTextDecoration
				textRotation <- xmlToTextRotation
				letterSpacing <- xmlToLetterSpacing
				lineHeight <- xmlToLineHeight
				lang <- attributeId("{http://www.w3.org/XML/1998/namespace}lang").maybe
				space <- attributeId("{http://www.w3.org/XML/1998/namespace}space").maybe
				textDirection <- xmlToTextDirection
				enclosure <- xmlToEnclosure
			} yield TextFormatting(justify, printStyleAlign, textDecoration, textRotation, letterSpacing,
				lineHeight, lang, space, textDirection, enclosure)
		}

		def xmlToTextRotation: XmlParser[TextRotation] = {
			rotationDegrees("rotation")(attribute).maybe.map(TextRotation)
		}

		def xmlToTrillSound: XmlParser[TrillSound] = {
			for {
				startNote <- startNote("start-note")(attribute).maybe
				trillStep <- trillStep("trill-step")(attribute).maybe
				twoNoteTurn <- twoNoteTurn("two-note-turn")(attribute).maybe
				accelerate <- yesNo("accelerate")(attribute).maybe
				beats <- trillBeats("beats")(attribute).maybe
				secondBeat <- percent("second-beat")(attribute).maybe
				lastBeat <- percent("last-beat")(attribute).maybe
			} yield TrillSound(startNote, trillStep, twoNoteTurn, accelerate, beats, secondBeat, lastBeat)
		}

		def xmlToVAlign: XmlParser[VAlign] = {
			vAlign("valign")(attribute).maybe.map(VAlign)
		}

		def xmlToVAlignImage: XmlParser[VAlignImage] = {
			vAlignImage("valign")(attribute).maybe.map(VAlignImage)
		}

		def xmlToXPosition: XmlParser[XPosition] = {
			for {
				defaultX <- tenths("default-x")(attribute).maybe
				defaultY <- tenths("default-y")(attribute).maybe
				relativeX <- tenths("relative-x")(attribute).maybe
				relativeY <- tenths("relative-y")(attribute).maybe
			} yield XPosition(defaultX, defaultY, relativeX, relativeY)
		}

		def xmlToYPosition: XmlParser[YPosition] = {
			for {
				defaultX <- tenths("default-x")(attribute).maybe
				defaultY <- tenths("default-y")(attribute).maybe
				relativeX <- tenths("relative-x")(attribute).maybe
				relativeY <- tenths("relative-y")(attribute).maybe
			} yield YPosition(defaultX, defaultY, relativeX, relativeY)
		}
	}

	class AttributeGroupsDirectionParser {

		import attributeGroupsCommonParser._
		import xmlParser._
		import primativeCommonParser._

		def xmlToImageAttributes: XmlParser[ImageAttributes] = {
			for {
				source <- attributeId("source")
				imgType <- attributeId("type")
				position <- xmlToPosition
				halign <- xmlToHAlign
				valignImage <- xmlToVAlignImage
			} yield ImageAttributes(source, imgType, position, halign, valignImage)
		}

		def xmlToPrintAttributes: XmlParser[PrintAttributes] = {
			for {
				staffSpacing <- tenths("staff-spacing")(attribute).maybe
				newSystem <- yesNo("new-system")(attribute).maybe
				newPage <- yesNo("new-page")(attribute).maybe
				blankPage <- attribute("blank-page")(_.toInt).maybe
				pageNumber <- attributeId("page-number").maybe
			} yield PrintAttributes(staffSpacing, newSystem, newPage, blankPage, pageNumber)
		}
	}

	class AttributeGroupsLinkParser {

		import xmlParser._

		def xmlToElementPosition: XmlParser[ElementPosition] = {
			for {
				element <- attributeId("element").maybe
				position <- attribute("position")(_.toInt).maybe
			} yield ElementPosition(element, position)
		}

		def xmlToLinkAttributes: XmlParser[LinkAttributes] = {
			implicit val xlink = "http://www.w3.org/1999/xlink"
			implicit val nb = NamespaceBinding("xlink", xlink, TopScope)

			for {
				href <- namespaceAttribute("href")
				laType <- namespaceAttribute("type").satisfy("simple" ==) <|> Parser.from("simple") // should ALWAYS be "simple"!
				role <- namespaceAttribute("role").maybe
				title <- namespaceAttribute("title").maybe
				show <- namespaceAttribute("show") <|> Parser.from("replace")
				actuate <- namespaceAttribute("actuate") <|> Parser.from("onRequest")
			} yield LinkAttributes(href, laType, role, title, show, actuate)
		}
	}

	class AttributeGroupsScoreParser {

		import attributeGroupsCommonParser._
		import xmlParser._
		import primativeCommonParser._

		def xmlToGroupNameText: XmlParser[GroupNameText] = {
			for {
				printStyle <- xmlToPrintStyle
				justify <- xmlToJustify
			} yield GroupNameText(printStyle, justify)
		}

		def xmlToMeasureAttributes: XmlParser[MeasureAttributes] = {
			for {
				number <- attributeId("number")
				implicitAttr <- yesNo("implicit")(attribute).maybe
				nonControlling <- yesNo("non-controlling")(attribute).maybe
				width <- tenths("width")(attribute).maybe
			} yield MeasureAttributes(number, implicitAttr, nonControlling, width)
		}

		def xmlToPartAttributes: XmlParser[PartAttributes] = {
			attributeId("id")
		}

		def xmlToPartNameText: XmlParser[PartNameText] = {
			for {
				printStyle <- xmlToPrintStyle
				printObject <- xmlToPrintObject
				justify <- xmlToJustify
			} yield PartNameText(printStyle, printObject, justify)
		}
	}
}
