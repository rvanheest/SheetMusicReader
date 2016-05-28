package nl.rvanheest.sheetmusicreader.parser

trait AttributeGroupsParser extends XmlParser with PrimativesParser {

	trait AttributeGroupsCommonParser extends PrimativeCommonParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsCommon._

		def xmlToBendSound = {
			for {
				accelerate <- yesNo("accelerate")(attribute).maybe
				beats <- trillBeats("beats")(attribute).maybe
				firstBeat <- percent("first-beat")(attribute).maybe
				lastBeat <- percent("last-beat")(attribute).maybe
			} yield BendSound(accelerate, beats, firstBeat, lastBeat)
		}

		def xmlToBezier = {
			for {
				offset <- divisions("bezier-offset")(attribute).maybe
				offset2 <- divisions("bezier-offset2")(attribute).maybe
				x <- tenths("bezier-x")(attribute).maybe
				y <- tenths("bezier-y")(attribute).maybe
				x2 <- tenths("bezier-x2")(attribute).maybe
				y2 <- tenths("bezier-y2")(attribute).maybe
			} yield Bezier(offset, offset2, x, y, x2, y2)
		}

		def xmlToColor = color("color")(attribute).maybe.map(Color)

		def xmlToDashedFormatting = {
			for {
				dash <- tenths("dash-length")(attribute).maybe
				space <- tenths("space-length")(attribute).maybe
			} yield DashedFormatting(dash, space)
		}

		def xmlToDirective = yesNo("directive")(attribute).maybe.map(Directive)

		def xmlToDocumentAttributes = (attributeId("version") <|> Parser.from("1.0")).map(DocumentAttributes)

		def xmlToEnclosure = enclosureShape("enclosure")(attribute).maybe.map(Enclosure)

		def xmlToFont = {
			for {
				family <- commaSeparatedText("font-family")(attribute).maybe
				style <- fontStyle("font-style")(attribute).maybe
				size <- fontSize("font-size")(attribute, attribute).maybe
				weight <- fontWeight("font-weight")(attribute).maybe
			} yield Font(family, style, size, weight)
		}

		def xmlToHAlign = leftCenterRight("halign")(attribute).maybe.map(HAlign)

		def xmlToJustify = leftCenterRight("justify")(attribute).maybe.map(Justify)

		def xmlToLetterSpacing = numberOrNormal("letter-spacing")(attribute).maybe.map(LetterSpacing)

		def xmlToLevelDisplay = {
			for {
				parentheses <- yesNo("parentheses")(attribute).maybe
				bracket <- yesNo("bracket")(attribute).maybe
				size <- symbolSize("size")(attribute).maybe
			} yield LevelDisplay(parentheses, bracket, size)
		}

		def xmlToLineHeight = numberOrNormal("line-height")(attribute).maybe.map(LineHeight)

		def xmlToLineShape = lineShape("line-shape")(attribute).maybe.map(LineShape)

		def xmlToLineType = lineType("line-type")(attribute).maybe.map(LineType)

		def xmlToOrientation = overUnder("orientation")(attribute).maybe.map(Orientation)

		def xmlToPlacement = aboveBelow("placement")(attribute).maybe.map(Placement)

		def xmlToPosition = {
			for {
				defaultX <- tenths("default-x")(attribute).maybe
				defaultY <- tenths("default-y")(attribute).maybe
				relativeX <- tenths("relative-x")(attribute).maybe
				relativeY <- tenths("relative-y")(attribute).maybe
			} yield Position(defaultX, defaultY, relativeX, relativeY)
		}

		def xmlToPrintObject = yesNo("print-object")(attribute).maybe.map(PrintObject)

		def xmlToPrintSpacing = yesNo("print-spacing")(attribute).maybe.map(PrintSpacing)

		def xmlToPrintStyle = {
			for {
				position <- xmlToPosition
				font <- xmlToFont
				color <- xmlToColor
			} yield PrintStyle(position, font, color)
		}

		def xmlToPrintStyleAlign = {
			for {
				printStyle <- xmlToPrintStyle
				halign <- xmlToHAlign
				valign <- xmlToVAlign
			} yield PrintStyleAlign(printStyle, halign, valign)
		}

		def xmlToPrintout = {
			for {
				printObject <- xmlToPrintObject
				dot <- yesNo("print-dot")(attribute).maybe
				spacing <- xmlToPrintSpacing
				lyric <- yesNo("print-lyric")(attribute).maybe
			} yield Printout(printObject, dot, spacing, lyric)
		}

		def xmlToTextDecoration = {
			for {
				underline <- numberOfLines("underline")(attribute).maybe
				overline <- numberOfLines("overline")(attribute).maybe
				lineThrough <- numberOfLines("line-through")(attribute).maybe
			} yield TextDecoration(underline, overline, lineThrough)
		}

		def xmlToTextDirection = textDirection("dir")(attribute).maybe.map(TextDirection)

		def xmlToTextFormatting = {
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

		def xmlToTextRotation = rotationDegrees("rotation")(attribute).maybe.map(TextRotation)

		def xmlToTrillSound = {
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

		def xmlToVAlign = vAlign("valign")(attribute).maybe.map(VAlign)

		def xmlToVAlignImage = vAlignImage("valign")(attribute).maybe.map(VAlignImage)

		def xmlToXPosition = {
			for {
				defaultX <- tenths("default-x")(attribute).maybe
				defaultY <- tenths("default-y")(attribute).maybe
				relativeX <- tenths("relative-x")(attribute).maybe
				relativeY <- tenths("relative-y")(attribute).maybe
			} yield XPosition(defaultX, defaultY, relativeX, relativeY)
		}

		def xmlToYPosition = {
			for {
				defaultX <- tenths("default-x")(attribute).maybe
				defaultY <- tenths("default-y")(attribute).maybe
				relativeX <- tenths("relative-x")(attribute).maybe
				relativeY <- tenths("relative-y")(attribute).maybe
			} yield YPosition(defaultX, defaultY, relativeX, relativeY)
		}
	}

	trait AttributeGroupsDirectionParser extends AttributeGroupsCommonParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsDirection._

		def xmlToImageAttributes = {
			for {
				source <- attributeId("source")
				imgType <- attributeId("type")
				position <- xmlToPosition
				halign <- xmlToHAlign
				valignImage <- xmlToVAlignImage
			} yield ImageAttributes(source, imgType, position, halign, valignImage)
		}

		def xmlToPrintAttributes = {
			for {
				staffSpacing <- tenths("staff-spacing")(attribute).maybe
				newSystem <- yesNo("new-system")(attribute).maybe
				newPage <- yesNo("new-page")(attribute).maybe
				blankPage <- attribute("blank-page")(_.toInt).maybe
				pageNumber <- attributeId("page-number").maybe
			} yield PrintAttributes(staffSpacing, newSystem, newPage, blankPage, pageNumber)
		}
	}

	trait AttributeGroupsLinkParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsLink._

		import scala.xml.{NamespaceBinding, TopScope}

		def xmlToElementPosition = {
			for {
				element <- attributeId("element").maybe
				position <- attribute("position")(_.toInt).maybe
			} yield ElementPosition(element, position)
		}

		def xmlToLinkAttributes = {
			implicit val xlink = "http://www.w3.org/1999/xlink"
			implicit val nb = NamespaceBinding("xlink", xlink, TopScope)

			for {
				href <- namespaceAttribute("href")
				// type is not queried, since it ALWAYS has to be "simple"
				role <- namespaceAttribute("role").maybe
				title <- namespaceAttribute("title").maybe
				show <- namespaceAttribute("show") <|> Parser.from("replace")
				actuate <- namespaceAttribute("actuate") <|> Parser.from("onRequest")
			} yield LinkAttributes(href, "simple", role, title, show, actuate)
		}
	}

	trait AttributeGroupsScoreParser extends AttributeGroupsCommonParser {

		import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsScore._

		def xmlToGroupNameText = {
			for {
				printStyle <- xmlToPrintStyle
				justify <- xmlToJustify
			} yield GroupNameText(printStyle, justify)
		}

		def xmlToMeasureAttributes = {
			for {
				number <- attributeId("number")
				implicitAttr <- yesNo("implicit")(attribute).maybe
				nonControlling <- yesNo("non-controlling")(attribute).maybe
				width <- tenths("width")(attribute).maybe
			} yield MeasureAttributes(number, implicitAttr, nonControlling, width)
		}

		def xmlToPartAttributes: XmlParser[PartAttributes] = attributeId("id")

		def xmlToPartNameText = {
			for {
				printStyle <- xmlToPrintStyle
				printObject <- xmlToPrintObject
				justify <- xmlToJustify
			} yield PartNameText(printStyle, printObject, justify)
		}
	}
}
