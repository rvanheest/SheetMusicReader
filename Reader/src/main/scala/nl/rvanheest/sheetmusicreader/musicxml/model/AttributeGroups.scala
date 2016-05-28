package nl.rvanheest.sheetmusicreader.musicxml.model

object AttributeGroups {

	object AttributeGroupsCommon {

		import Primatives.PrimativeCommon._

		/**
			* The bend-sound type is used for bend and slide elements, and is similar to the trill-sound
			* attribute group. Here the beats element refers to the number of discrete elements
			* (like MIDI pitch bends) used to represent a continuous bend or slide. The first-beat indicates
			* the percentage of the direction for starting a bend; the last-beat the percentage for ending it.
			* The default choices are:
			* 		accelerate = "no"
			* 		beats = "4"
			* 		first-beat = "25"
			* 		last-beat = "75"
			*/
		case class BendSound(accelerate: Option[YesNo] = Option.empty,
												 beats: Option[TrillBeats] = Option.empty,
												 firstBeat: Option[Percent] = Option.empty,
												 lastBeat: Option[Percent] = Option.empty)

		/**
			* The bezier attribute group is used to indicate the curvature of slurs and ties, representing the
			* control points for a cubic bezier curve. For ties, the bezier attribute group is used with the tied
			* element.
			*
			* Normal slurs, S-shaped slurs, and ties need only two bezier points: one associated with the start of
			* the slur or tie, the other with the stop. Complex slurs and slurs divided over system breaks can
			* specify additional bezier data at slur elements with a continue type.
			*
			* The bezier-offset, bezier-x, and bezier-y attributes describe the outgoing bezier point for slurs
			* and ties with a start type, and the incoming bezier point for slurs and ties with types of stop
			* or continue. The attributes bezier-offset2, bezier-x2, and bezier-y2 are only valid with slurs of
			* type continue, and describe the outgoing bezier point.
			*
			* The bezier-offset and bezier-offset2 attributes are measured in terms of musical divisions,
			* like the offset element. These are the recommended attributes for specifying horizontal position.
			* The other attributes are specified in tenths, relative to any position settings associated with
			* the slur or tied element.
			*/
		case class Bezier(offset: Option[Divisions] = Option.empty,
											offset2: Option[Divisions] = Option.empty,
											x: Option[Tenths] = Option.empty,
											y: Option[Tenths] = Option.empty,
											x2: Option[Tenths] = Option.empty,
											y2: Option[Tenths] = Option.empty)

		/**
			* The color attribute group indicates the color of an element.
			*/
		case class Color(color: Option[Primatives.PrimativeCommon.Color] = Option.empty)

		/**
			* The dashed-formatting entity represents the length of dashes and spaces in a dashed line.
			* Both the dash-length and space-length attributes are represented in tenths.
			* These attributes are ignored if the corresponding line-type attribute is not dashed.
			*/
		case class DashedFormatting(dashLength: Option[Tenths] = Option.empty,
																spaceLength: Option[Tenths] = Option.empty)

		/**
			* The directive attribute changes the default-x position of a direction.
			* It indicates that the left-hand side of the direction is aligned with the left-hand side of
			* the time signature. If no time signature is present, it is aligned with the left-hand side of
			* the first music notational element in the measure. If a default-x, justify,
			* or halign attribute is present, it overrides the directive attribute.
			*/
		case class Directive(directive: Option[YesNo] = Option.empty)

		/**
			* The document-attributes attribute group is used to specify the attributes for an entire MusicXML
			* document. Currently this is used for the version attribute.
			*
			* The version attribute was added in Version 1.1 for the score-partwise and score-timewise documents.
			* It provides an easier way to get version information than through the MusicXML public ID.
			* The default value is 1.0 to make it possible for programs that handle later versions to distinguish
			* earlier version files reliably. Programs that write MusicXML 1.1 or later files should set this
			* attribute.
			*/
		case class DocumentAttributes(version: String = "1.0")

		/**
			* The enclosure attribute group is used to specify the formatting of an enclosure around text
			* or symbols.
			*/
		case class Enclosure(enclosure: Option[EnclosureShape] = Option.empty)

		/**
			* The font attribute group gathers together attributes for determining the font within a credit
			* or direction. They are based on the text styles for Cascading Style Sheets.
			* The font-family is a comma-separated list of font names. These can be specific font styles
			* such as Maestro or Opus, or one of several generic font styles: music, engraved, handwritten,
			* text, serif, sans-serif, handwritten, cursive, fantasy, and monospace. The music, engraved,
			* and handwritten values refer to music fonts; the rest refer to text fonts. The fantasy style
			* refers to decorative text such as found in older German-style printing. The font-style can be
			* normal or italic. The font-size can be one of the CSS sizes (xx-small, x-small, small, medium,
			* large, x-large, xx-large) or a numeric point size. The font-weight can be normal or bold.
			* The default is application-dependent, but is a text font vs. a music font.
			*/
		case class Font(family: Option[CommaSeparatedText] = Option.empty,
										style: Option[FontStyle] = Option.empty,
										size: Option[FontSize] = Option.empty,
										weight: Option[FontWeight] = Option.empty)

		/**
			* In cases where text extends over more than one line, horizontal alignment and justify values can
			* be different. The most typical case is for credits, such as:
			* 		Words and music by
			* 			Pat Songwriter
			*
			* Typically this type of credit is aligned to the right, so that the position information refers to
			* the right-most part of the text. But in this example, the text is center-justified,
			* not right-justified.
			*
			* The halign attribute is used in these situations. If it is not present, its value is the same as
			* for the justify attribute.
			*/
		case class HAlign(halign: Option[LeftCenterRight] = Option.empty)

		/**
			* The justify attribute is used to indicate left, center, or right justification.
			* The default value varies for different elements. For elements where the justify attribute is
			* present but the halign attribute is not, the justify attribute indicates horizontal alignment
			* as well as justification.
			*/
		case class Justify(justify: Option[LeftCenterRight] = Option.empty)

		/**
			* The letter-spacing attribute specifies text tracking. Values are either "normal" or a number
			* representing the number of ems to add between each letter. The number may be negative in order
			* to subtract space. The default is normal, which allows flexibility of letter-spacing for purposes
			* of text justification.
			*/
		case class LetterSpacing(spacing: Option[NumberOrNormal] = Option.empty)

		/**
			* The level-display attribute group specifies three common ways to indicate editorial indications:
			* putting parentheses or square brackets around a symbol, or making the symbol a different size.
			* If not specified, they are left to application defaults. It is used by the level and accidental
			* elements.
			*/
		case class LevelDisplay(parentheses: Option[YesNo] = Option.empty, bracket: Option[YesNo] = Option.empty,
														size: Option[SymbolSize] = Option.empty)

		/**
			* The line-height attribute specifies text leading. Values are either "normal" or a number
			* representing the percentage of the current font height to use for leading.
			* The default is "normal". The exact normal value is implementation-dependent, but values
			* between 100 and 120 are recommended.
			*/
		case class LineHeight(height: Option[NumberOrNormal] = Option.empty)

		/**
			* The line-shape attribute distinguishes between straight and curved lines.
			*/
		case class LineShape(shape: Option[Primatives.PrimativeCommon.LineShape] = Option.empty)

		/**
			* The line-type attribute distinguishes between solid, dashed, dotted, and wavy lines.
			*/
		case class LineType(lineType: Option[Primatives.PrimativeCommon.LineType] = Option.empty)

		/**
			* The orientation attribute indicates whether slurs and ties are overhand (tips down) or
			* underhand (tips up). This is distinct from the placement attribute used by any notation type.
			*/
		case class Orientation(orientation: Option[OverUnder] = Option.empty)

		/**
			* The placement attribute indicates whether something is above or below another element,
			* such as a note or a notation.
			*/
		case class Placement(placement: Option[AboveBelow] = Option.empty)

		/**
			* The position attributes are based on MuseData print suggestions. For most elements, any program
			* will compute a default x and y position. The position attributes let this be changed two ways.
			*
			* The default-x and default-y attributes change the computation of the default position.
			* For most elements, the origin is changed relative to the left-hand side of the note or the
			* musical position within the bar (x) and the top line of the staff (y).
			*
			* For the following elements, the default-x value changes the origin relative to the start of the
			* current measure:
			*
			* - note
			* - figured-bass
			* - harmony
			* - link
			* - directive
			* - measure-numbering
			* - all descendants of the part-list element
			* - all children of the direction-type element
			*
			* This origin is from the start of the entire measure, at either the left barline or the start of
			* the system.
			*
			* When the default-x attribute is used within a child element of the part-name-display,
			* part-abbreviation-display, group-name-display, or group-abbreviation-display elements,
			* it changes the origin relative to the start of the first measure on the system.
			* These values are used when the current measure or a succeeding measure starts a new system.
			* The same change of origin is used for the group-symbol element.
			*
			* For the note, figured-bass, and harmony elements, the default-x value is considered to have
			* adjusted the musical position within the bar for its descendant elements.
			*
			* Since the credit-words and credit-image elements are not related to a measure, in these cases
			* the default-x and default-y attributes adjust the origin relative to the bottom left-hand corner
			* of the specified page.
			*
			* The relative-x and relative-y attributes change the position relative to the default position,
			* either as computed by the individual program, or as overridden by the default-x and default-y
			* attributes.
			*
			* Positive x is right, negative x is left; positive y is up, negative y is down. All units are in
			* tenths of interline space. For stems, positive relative-y lengthens a stem while negative
			* relative-y shortens it.
			*
			* The default-x and default-y position attributes provide higher-resolution positioning data than
			* related features such as the placement attribute and the offset element. Applications reading a
			* MusicXML file that can understand both features should generally rely on the default-x and
			* default-y attributes for their greater accuracy. For the relative-x and relative-y attributes,
			* the offset element, placement attribute, and directive attribute provide context for the relative
			* position information, so the two features should be interpreted together.
			*
			* As elsewhere in the MusicXML format, tenths are the global tenths defined by the scaling element,
			* not the local tenths of a staff resized by the staff-size element.
			*/
		case class Position(defaultX: Option[Tenths] = Option.empty,
												defaultY: Option[Tenths] = Option.empty,
												relativeX: Option[Tenths] = Option.empty,
												relativeY: Option[Tenths] = Option.empty)

		/**
			* The print-object attribute specifies whether or not to print an object (e.g. a note or a rest).
			* It is yes by default.
			*/
		case class PrintObject(printObject: Option[YesNo] = Option.empty)

		/**
			* The print-spacing attribute controls whether or not spacing is left for an invisible note or
			* object. It is used only if no note, dot, or lyric is being printed. The value is yes
			* (leave spacing) by default.
			*/
		case class PrintSpacing(printSpacing: Option[YesNo] = Option.empty)

		/**
			* The print-style attribute group collects the most popular combination of printing attributes:
			* position, font, and color.
			*/
		case class PrintStyle(position: Position = Position(),
													font: Font = Font(),
													color: Color = Color())

		/**
			* The print-style-align attribute group adds the halign and valign attributes to the position,
			* font, and color attributes.
			*/
		case class PrintStyleAlign(printStyle: PrintStyle = PrintStyle(),
															 halign: HAlign = HAlign(),
															 valign: VAlign = VAlign())

		/**
			* The printout attribute group collects the different controls over printing an object
			* (e.g. a note or rest) and its parts, including augmentation dots and lyrics.
			* This is especially useful for notes that overlap in different voices, or for chord sheets
			* that contain lyrics and chords but no melody.
			*
			* By default, all these attributes are set to yes. If print-object is set to no, the print-dot
			* and print-lyric attributes are interpreted to also be set to no if they are not present.
			*/
		case class Printout(printObject: PrintObject = PrintObject(),
												printDot: Option[YesNo] = Option.empty,
												printSpacing: PrintSpacing = PrintSpacing(),
												printLyric: Option[YesNo] = Option.empty)

		/**
			* The text-decoration attribute group is based on the similar feature in XHTML and CSS.
			* It allows for text to be underlined, overlined, or struck-through. It extends the CSS version
			* by allow double or triple lines instead of just being on or off.
			*/
		case class TextDecoration(underline: Option[NumberOfLines] = Option.empty,
															overline: Option[NumberOfLines] = Option.empty,
															lineThrough: Option[NumberOfLines] = Option.empty)

		/**
			* The text-direction attribute is used to adjust and override the Unicode bidirectional text
			* algorithm, similar to the W3C Internationalization Tag Set recommendation.
			*/
		case class TextDirection(dir: Option[Primatives.PrimativeCommon.TextDirection] = Option.empty)

		/**
			* The text-formatting attribute group collects the common formatting attributes for text elements.
			* Default values may differ across the elements that use this group.
			*/
		case class TextFormatting(justify: Justify = Justify(),
															printStyleAlign: PrintStyleAlign = PrintStyleAlign(),
															textDecoration: TextDecoration = TextDecoration(),
															textRotation: TextRotation = TextRotation(),
															letterSpacing: LetterSpacing = LetterSpacing(),
															lineHeight: LineHeight = LineHeight(),
															lang: Option[String] = Option.empty,
															space: Option[String] = Option.empty,
															textDirection: TextDirection = TextDirection(),
															enclosure: Enclosure = Enclosure())

		/**
			* The rotation attribute is used to rotate text around the alignment point specified by the halign
			* and valign attributes. Positive values are clockwise rotations, while negative values are
			* counter-clockwise rotations.
			*/
		case class TextRotation(rotation: Option[RotationDegrees] = Option.empty)

		/**
			* The trill-sound attribute group includes attributes used to guide the sound of trills, mordents,
			* turns, shakes, and wavy lines, based on MuseData sound suggestions. The default choices are:
			*
			* 		start-note = "upper"
			* 		trill-step = "whole"
			* 		two-note-turn = "none"
			* 		accelerate = "no"
			* 		beats = "4".
			*
			* Second-beat and last-beat are percentages for landing on the indicated beat, with defaults of 25
			* and 75 respectively.
			*
			* For mordent and inverted-mordent elements, the defaults are different:
			*
			* 		The default start-note is "main", not "upper".
			* 		The default for beats is "3", not "4".
			* 		The default for second-beat is "12", not "25".
			* 		The default for last-beat is "24", not "75".
			*/
		case class TrillSound(startNote: Option[StartNote] = Option.empty,
													trillStep: Option[TrillStep] = Option.empty,
													twoNoteTurn: Option[TwoNoteTurn] = Option.empty,
													accelerate: Option[YesNo] = Option.empty,
													beats: Option[TrillBeats] = Option.empty,
													secondBeat: Option[Percent] = Option.empty,
													lastBeat: Option[Percent] = Option.empty)

		/**
			* The valign attribute is used to indicate vertical alignment to the top, middle, bottom,
			* or baseline of the text. Defaults are implementation-dependent.
			*/
		case class VAlign(valign: Option[Primatives.PrimativeCommon.VAlign] = Option.empty)

		/**
			* The valign-image attribute is used to indicate vertical alignment for images and graphics,
			* so it removes the baseline value. Defaults are implementation-dependent.
			*/
		case class VAlignImage(valign: Option[Primatives.PrimativeCommon.VAlignImage] = Option.empty)

		/**
			* The x-position attribute group is used for elements like notes where specifying x position
			* is common, but specifying y position is rare.
			*/
		case class XPosition(defaultX: Option[Tenths] = Option.empty,
												 defaultY: Option[Tenths] = Option.empty,
												 relativeX: Option[Tenths] = Option.empty,
												 relativeY: Option[Tenths] = Option.empty)

		/**
			* The y-position attribute group is used for elements like stems where specifying y position
			* is common, but specifying x position is rare.
			*/
		case class YPosition(defaultX: Option[Tenths] = Option.empty,
												 defaultY: Option[Tenths] = Option.empty,
												 relativeX: Option[Tenths] = Option.empty,
												 relativeY: Option[Tenths] = Option.empty)
	}

	object AttributeGroupsDirection {

		import AttributeGroups.AttributeGroupsCommon.{HAlign, Position, VAlignImage}
		import Primatives.PrimativeCommon.{Tenths, YesNo}

		/**
			* The image-attributes group is used to include graphical images in a score.
			* The required source attribute is the URL for the image file.
			* The required type attribute is the MIME type for the image file format.
			* Typical choices include application/postscript, image/gif, image/jpeg, image/png, and image/tiff.
			*/
		case class ImageAttributes(source: String,
															 imgType: String,
															 position: Position = Position(),
															 halign: HAlign = HAlign(),
															 valignImage: VAlignImage = VAlignImage())

		/**
			* The print-attributes group is used by the print element. The new-system and new-page attributes
			* indicate whether to force a system or page break, or to force the current music onto the same
			* system or page as the preceding music. Normally this is the first music data within a measure.
			* If used in multi-part music, they should be placed in the same positions within each part,
			* or the results are undefined. The page-number attribute sets the number of a new page;
			* it is ignored if new-page is not "yes". Version 2.0 adds a blank-page attribute.
			* This is a positive integer value that specifies the number of blank pages to insert before
			* the current measure. It is ignored if new-page is not "yes". These blank pages have no music,
			* but may have text or images specified by the credit element. This is used to allow a combination
			* of pages that are all text, or all text and images, together with pages of music.
			*
			* The staff-spacing attribute specifies spacing between multiple staves in tenths of staff space.
			* This is deprecated as of Version 1.1; the staff-layout element should be used instead.
			* If both are present, the staff-layout values take priority.
			*/
		case class PrintAttributes(staffSpacing: Option[Tenths] = Option.empty,
															 newSystem: Option[YesNo] = Option.empty,
															 newPage: Option[YesNo] = Option.empty,
															 blankPage: Option[Int] = Option.empty,
															 pageNumber: Option[String] = Option.empty) {
			require(blankPage.forall(_ > 0))
		}
	}

	object AttributeGroupsLink {
		/**
			* The element and position attributes are new as of Version 2.0.
			* They allow for bookmarks and links to be positioned at higher resolution than the level of
			* music-data elements. When no element and position attributes are present, the bookmark or
			* link element refers to the next sibling element in the MusicXML file. The element attribute
			* specifies an element type for a descendant of the next sibling element that is not a link or
			* bookmark. The position attribute specifies the position of this descendant element, where the
			* first position is 1. The position attribute is ignored if the element attribute is not present.
			* For instance, an element value of "beam" and a position value of "2" defines the link or
			* bookmark to refer to the second beam descendant of the next sibling element that is not a link
			* or bookmark. This is equivalent to an XPath test of [.//beam[2]] done in the context of the
			* sibling element.
			*/
		case class ElementPosition(element: Option[String] = Option.empty,
															 position: Option[Int] = Option.empty) {
			require(position.forall(_ > 0))
		}

		/**
			* The link-attributes group includes all the simple XLink attributes supported in the
			* MusicXML format.
			*/
		case class LinkAttributes(href: String,
															attrType: String = "simple",
															role: Option[String] = Option.empty,
															title: Option[String] = Option.empty,
															show: String = "replace",
															actuate: String = "onRequest") {
			require(attrType equals "simple")
		}
	}

	object AttributeGroupsScore {

		import AttributeGroupsCommon.{Justify, PrintObject, PrintStyle}
		import Primatives.PrimativeCommon.{Tenths, YesNo}

		/**
			* The group-name-text attribute group is used by the group-name and group-abbreviation elements.
			* The print-style and justify attribute groups are deprecated in MusicXML 2.0 in favor of the new
			* group-name-display and group-abbreviation-display elements.
			*/
		case class GroupNameText(printStyle: PrintStyle = PrintStyle(), justify: Justify = Justify())

		/**
			* The measure-attributes group is used by the measure element. Measures have a required number
			* attribute (going from partwise to timewise, measures are grouped via the number).
			*
			* The implicit attribute is set to "yes" for measures where the measure number should never appear,
			* such as pickup measures and the last half of mid-measure repeats. The value is "no" if not specified.
			*
			* The non-controlling attribute is intended for use in multimetric music like the Don Giovanni minuet.
			* If set to "yes", the left barline in this measure does not coincide with the left barline of measures
			* in other parts. The value is "no" if not specified.
			*
			* In partwise files, the number attribute should be the same for measures in different parts that share
			* the same left barline. While the number attribute is often numeric, it does not have to be.
			* Non-numeric values are typically used together with the implicit or non-controlling attributes
			* being set to "yes". For a pickup measure, the number attribute is typically set to "0" and the
			* implicit attribute is typically set to "yes". Further details about measure numbering can be
			* defined using the measure-numbering element.
			*
			* Measure width is specified in tenths. These are the global tenths specified in the scaling element,
			* not local tenths as modified by the staff-size element.	The width covers the entire measure from
			* barline or system start to barline or system end.
			*/
		case class MeasureAttributes(number: String,
																 attrImplicit: Option[YesNo] = Option.empty,
																 nonControlling: Option[YesNo] = Option.empty,
																 width: Option[Tenths] = Option.empty)

		/**
			* In either partwise or timewise format, the part element has an id attribute that is an IDREF back
			* to a score-part in the part-list.
			*/
		type PartAttributes = String

		/**
			* The part-name-text attribute group is used by the part-name and part-abbreviation elements.
			* The print-style and justify attribute groups are deprecated in MusicXML 2.0 in favor of the new
			* part-name-display and part-abbreviation-display elements.
			*/
		case class PartNameText(printStyle: PrintStyle = PrintStyle(),
														printObject: PrintObject = PrintObject(),
														justify: Justify = Justify())
	}
}
