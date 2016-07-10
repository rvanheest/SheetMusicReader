package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexAttributes._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexBarline.{BarStyleColor, Barline, Ending, Repeat}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexDirection.{MetronomeTuplet, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexIdentity._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLayout._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLink.{Bookmark, Link}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexNote.{Tied, TimeModification, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexScore.{Work, _}
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeBarline.RLM_Right
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon._

trait ComplexParser[M[+_]] {
	this: GroupParser[M] with AttributeGroupsParser[M] with PrimativesParser[M] with XmlParser[M] =>

	protected val complexAttributesParser = new ComplexAttributesParser
	protected val complexBarlineParser = new ComplexBarlineParser
	protected val complexCommonParser = new ComplexCommonParser
	protected val complexDirectionParser = new ComplexDirectionParser
	protected val complexIdentityParser = new ComplexIdentityParser
	protected val complexLayoutParser = new ComplexLayoutParser
	protected val complexLinkParser = new ComplexLinkParser
	protected val complexNoteParser = new ComplexNoteParser
	protected val complexScoreParser = new ComplexScoreParser

	class ComplexAttributesParser {

		import attributeGroupsCommonParser._
		import complexCommonParser._
		import groupAttributesParser._
		import groupCommonParser._
		import xmlParser._
		import primativeAttributesParser._
		import primativeCommonParser._
		import primativeNoteParser._
		import primativeScoreParser._

		def xmlToDirective(name: String): XmlParser[Directive] = {
			for {
				printStyle <- xmlToPrintStyle
				lang <- attributeId("{http://www.w3.org/XML/1998/namespace}lang").maybe
				base <- xmlToString(name)
			} yield Directive(base, printStyle, lang)
		}

		def xmlToAttributes(name: String): XmlParser[Attributes] = {
			branchNode(name) {
				for {
					editorial <- xmlToEditorial
					divisions <- positiveDivisions("divisions")(node).maybe
					key <- xmlToKey("key").many
					time <- xmlToTime("time").many
					staves <- node("staves")(_.toInt).maybe
					partSymbol <- xmlToPartSymbol("part-symbol").maybe
					instruments <- node("instruments")(_.toInt).maybe
					clef <- xmlToClef("clef").many
					staffDetails <- xmlToStaffDetails("staff-details").many
					transpose <- xmlToTranspose("transpose").many
					directive <- xmlToDirective("directive").many
					measureStyle <- xmlToMeasureStyle("measure-style").many
				} yield Attributes(editorial, divisions, key, time, staves, partSymbol, instruments, clef, staffDetails, transpose, directive, measureStyle)
			}
		}

		def xmlToBeatRepeat(name: String): XmlParser[BeatRepeat] = {
			for {
				beatRepeatType <- startStop("type")(attribute)
				slashes <- attribute("slashes")(_.toInt).maybe
				useDots <- yesNo("use-dots")(attribute).maybe
				slash <- branchNode(name)(groupAttributesParser.xmlToSlash.maybe)
			} yield BeatRepeat(slash, beatRepeatType, slashes, useDots)
		}

		def xmlToCancel(name: String): XmlParser[Cancel] = {
			for {
				cancelLocation <- cancelLocation("location")(attribute).maybe
				base <- fifths(name)(node)
			} yield Cancel(base, cancelLocation)
		}

		def xmlToClef(name: String): XmlParser[Clef] = {
			for {
				number <- staffNumber("number")(attribute).maybe
				additional <- yesNo("additional")(attribute).maybe
				size <- symbolSize("size")(attribute).maybe
				afterBarline <- yesNo("after-barline")(attribute).maybe
				printStyle <- xmlToPrintStyle
				printObject <- xmlToPrintObject
				clef <- branchNode(name) {
					for {
						sign <- clefSign("sign")(node)
						line <- staffLine("line")(node).maybe
						clefOctaveChange <- node("clef-octave-change")(_.toInt).maybe
					} yield Clef(sign, line, clefOctaveChange, number, additional, size, afterBarline, printStyle, printObject)
				}
			} yield clef
		}

		def xmlToInterchangeable(name: String): XmlParser[Interchangeable] = {
			for {
				symbol <- timeSymbol("symbol")(attribute).maybe
				separator <- timeSeparator("separator")(attribute).maybe
				interchangeable <- branchNode(name) {
					for {
						timeRelation <- timeRelation("time-relation")(node).maybe
						timeSignature <- xmlToTimeSignature.many
					} yield Interchangeable(timeRelation, timeSignature, symbol, separator)
				}
			} yield interchangeable
		}

		def xmlToKey(name: String): XmlParser[Key] = {
			for {
				number <- staffNumber("number")(attribute).maybe
				printStyle <- xmlToPrintStyle
				printObject <- xmlToPrintObject
				key <- branchNode(name) {
					for {
						ntrKey <- xmlToTraditionalKey.map(TraditionalKeyChoice(_))
						  .orElse(xmlToNonTraditionalKey.many.map(NonTraditionalKeysChoice(_)))
						octave <- xmlToKeyOctave("key-octave").many
					} yield Key(ntrKey, octave, number, printStyle, printObject)
				}
			} yield key
		}

		def xmlToKeyOctave(name: String): XmlParser[KeyOctave] = {
			for {
				number <- attribute("number")(_.toInt)
				cancel <- yesNo("cancel")(attribute).maybe
				base <- octave(name)(node)
			} yield KeyOctave(base, number, cancel)
		}

		def xmlToMeasureRepeat(name: String): XmlParser[MeasureRepeat] = {
			for {
				ssType <- startStop("type")(attribute)
				slashes <- attribute("slashes")(_.toInt).maybe
				base <- positiveIntegerOrEmpty(name)(node)
			} yield MeasureRepeat(base, ssType, slashes)
		}

		def xmlToMeasureStyle(name: String): XmlParser[MeasureStyle] = {
			for {
				number <- staffNumber("number")(attribute).maybe
				font <- xmlToFont
				color <- xmlToColor
				choice <- branchNode(name) {
					xmlToMultipleRest("multiple-rest").map(MultipleRestChoice(_))
						.orElse(xmlToMeasureRepeat("measure-repeat").map(MeasureRepeatChoice(_)))
						.orElse(xmlToBeatRepeat("beat-repeat").map(BeatRepeatChoice(_)))
						.orElse(xmlToSlash("slash").map(SlashChoice(_)))
				}
			} yield MeasureStyle(choice, number, font, color)
		}

		def xmlToMultipleRest(name: String): XmlParser[MultipleRest] = {
			for {
				useSymbols <- yesNo("use-symbols")(attribute).maybe
				base <- positiveIntegerOrEmpty(name)(node)
			} yield MultipleRest(base, useSymbols)
		}

		def xmlToPartSymbol(name: String): XmlParser[PartSymbol] = {
			for {
				top <- staffNumber("top-staff")(attribute).maybe
				bottom <- staffNumber("bottom-staff")(attribute).maybe
				position <- xmlToPosition
				color <- xmlToColor
				base <- groupSymbolValue(name)(node)
			} yield PartSymbol(base, top, bottom, position, color)
		}

		def xmlToSlash(name: String): XmlParser[Slash] = {
			for {
				ssType <- startStop("type")(attribute)
				useDots <- yesNo("use-dots")(attribute).maybe
				useStems <- yesNo("use-stems")(attribute).maybe
				slash <- branchNode(name)(groupAttributesParser.xmlToSlash.maybe)
			} yield Slash(slash, ssType, useDots, useStems)
		}

		def xmlToStaffDetails(name: String): XmlParser[StaffDetails] = {
			for {
				number <- staffNumber("number")(attribute).maybe
				showFrets <- showFrets("show-frets")(attribute).maybe
				printObject <- xmlToPrintObject
				printSpacing <- xmlToPrintSpacing
				staffDetails <- branchNode(name) {
					for {
						staffType <- staffType("staff-type")(node).maybe
						staffLine <- node("staff-lines")(_.toInt).maybe
						staffTuning <- xmlToStaffTuning("staff-tuning").many
						capo <- node("capo")(_.toInt).maybe
						staffSize <- nonNegativeDecimal("staff-size")(node).maybe
					} yield StaffDetails(staffType, staffLine, staffTuning, capo, staffSize, number, showFrets, printObject, printSpacing)
				}
			} yield staffDetails
		}

		def xmlToStaffTuning(name: String): XmlParser[StaffTuning] = {
			for {
				line <- staffLine("line")(attribute).maybe
				tuning <- branchNode(name)(xmlToTuning)
			} yield StaffTuning(tuning, line)
		}

		def xmlToTime(name: String): XmlParser[Time] = {
			for {
				number <- staffNumber("number")(attribute).maybe
				symbol <- timeSymbol("symbol")(attribute).maybe
				separator <- timeSeparator("separator")(attribute).maybe
				printStyleAlign <- xmlToPrintStyleAlign
				printObject <- xmlToPrintObject
				choice <- branchNode(name) {
					def xmlToInnerTimeClass = {
						for {
							timeSignature <- xmlToTimeSignature.atLeastOnce
							interchangeable <- xmlToInterchangeable("interchangeable").maybe
						} yield SignatureTimeChoice(timeSignature, interchangeable)
					}
					xmlToInnerTimeClass.orElse(xmlToString("senza-misura").map(SenzaMisuraTimeChoice(_)))
				}
			} yield Time(choice, number, symbol, separator, printStyleAlign, printObject)
		}

		def xmlToTranspose(name: String): XmlParser[Transpose] = {
			for {
				number <- staffNumber("number")(attribute).maybe
				transpose <- branchNode(name) {
					for {
						diatonic <- node("diatonic")(_.toInt).maybe
						chromatic <- semitones("chromatic")(node)
						octaveChange <- node("octave-change")(_.toInt).maybe
						double <- xmlToEmpty("double").maybe
					} yield Transpose(diatonic, chromatic, octaveChange, double, number)
				}
			} yield transpose
		}
	}

	class ComplexBarlineParser {

		import attributeGroupsCommonParser._
		import complexCommonParser._
		import groupCommonParser._
		import xmlParser._
		import primativeBarlineParser._
		import primativeCommonParser._

		def xmlToBarStyleColor(name: String): XmlParser[BarStyleColor] = {
			for {
				color <- xmlToColor
				base <- barStyle(name)(node)
			} yield BarStyleColor(base, color)
		}

		def xmlToBarline(name: String): XmlParser[Barline] = {
			for {
				location <- rightLeftMiddle("location")(attribute) <|> Parser.from(RLM_Right)
				segnoAttr <- attributeId("segno").maybe
				codaAttr <- attributeId("coda").maybe
				divisions <- divisions("divisions")(attribute).maybe
				barline <- branchNode(name) {
					for {
						barStyle <- xmlToBarStyleColor("bar-style").maybe
						editorial <- xmlToEditorial
						wavyLine <- xmlToWavyLine("wavy-line").maybe
						segnoEl <- xmlToEmptyPrintStyleAlign("segno").maybe
						codaEl <- xmlToEmptyPrintStyleAlign("coda").maybe
						fermata <- xmlToFermata("fermata").many
						ending <- xmlToEnding("ending").maybe
						repeat <- xmlToRepeat("repeat").maybe
					} yield Barline(barStyle, editorial, wavyLine, segnoEl, codaEl, fermata, ending, repeat, location, segnoAttr, codaAttr, divisions)
				}
			} yield barline
		}

		def xmlToEnding(name: String): XmlParser[Ending] = {
			for {
				number <- endingNumber("number")(attribute)
				ssdType <- startStopDiscontinue("type")(attribute)
				printObject <- xmlToPrintObject
				printStyle <- xmlToPrintStyle
				endLength <- tenths("end-length")(attribute).maybe
				textX <- tenths("text-x")(attribute).maybe
				textY <- tenths("text-y")(attribute).maybe
				base <- xmlToString(name)
			} yield Ending(base, number, ssdType, printObject, printStyle, endLength, textX, textY)
		}

		def xmlToRepeat(name: String): XmlParser[Repeat] = {
			for {
				direction <- backwardForward("direction")(attribute)
				times <- attribute("times")(_.toInt).maybe
				winged <- winged("winged")(attribute).maybe
				_ <- nodeWithName(name)
			} yield Repeat(direction, times, winged)
		}
	}

	class ComplexCommonParser {

		import attributeGroupsCommonParser._
		import xmlParser._
		import primativeCommonParser._
		import primativeNoteParser._

		def xmlToAccidentalText(name: String): XmlParser[AccidentalText] = {
			for {
				attr <- xmlToTextFormatting
				base <- accidentalValue(name)(node)
			} yield AccidentalText(base, attr)
		}

		def xmlToDynamics(name: String): XmlParser[Dynamics] = {
			for {
				printStyleAlign <- xmlToPrintStyleAlign
				placement <- xmlToPlacement
				textDecoration <- xmlToTextDecoration
				enclosure <- xmlToEnclosure
				dynamics <- branchNode(name) {
					nodeWithName("p").map(_ => DynamicSymbolChoice(DynamicSymbols.p))
						.orElse(nodeWithName("pp").map(_ => DynamicSymbolChoice(DynamicSymbols.pp)))
						.orElse(nodeWithName("ppp").map(_ => DynamicSymbolChoice(DynamicSymbols.ppp)))
						.orElse(nodeWithName("pppp").map(_ => DynamicSymbolChoice(DynamicSymbols.pppp)))
						.orElse(nodeWithName("ppppp").map(_ => DynamicSymbolChoice(DynamicSymbols.ppppp)))
						.orElse(nodeWithName("pppppp").map(_ => DynamicSymbolChoice(DynamicSymbols.pppppp)))
						.orElse(nodeWithName("f").map(_ => DynamicSymbolChoice(DynamicSymbols.f)))
						.orElse(nodeWithName("ff").map(_ => DynamicSymbolChoice(DynamicSymbols.ff)))
						.orElse(nodeWithName("fff").map(_ => DynamicSymbolChoice(DynamicSymbols.fff)))
						.orElse(nodeWithName("ffff").map(_ => DynamicSymbolChoice(DynamicSymbols.ffff)))
						.orElse(nodeWithName("fffff").map(_ => DynamicSymbolChoice(DynamicSymbols.fffff)))
						.orElse(nodeWithName("ffffff").map(_ => DynamicSymbolChoice(DynamicSymbols.ffffff)))
						.orElse(nodeWithName("mp").map(_ => DynamicSymbolChoice(DynamicSymbols.mp)))
						.orElse(nodeWithName("mf").map(_ => DynamicSymbolChoice(DynamicSymbols.mf)))
						.orElse(nodeWithName("sf").map(_ => DynamicSymbolChoice(DynamicSymbols.sf)))
						.orElse(nodeWithName("sfp").map(_ => DynamicSymbolChoice(DynamicSymbols.sfp)))
						.orElse(nodeWithName("sfpp").map(_ => DynamicSymbolChoice(DynamicSymbols.sfpp)))
						.orElse(nodeWithName("fp").map(_ => DynamicSymbolChoice(DynamicSymbols.fp)))
						.orElse(nodeWithName("rf").map(_ => DynamicSymbolChoice(DynamicSymbols.rf)))
						.orElse(nodeWithName("rfz").map(_ => DynamicSymbolChoice(DynamicSymbols.rfz)))
						.orElse(nodeWithName("sfz").map(_ => DynamicSymbolChoice(DynamicSymbols.sfz)))
						.orElse(nodeWithName("sffz").map(_ => DynamicSymbolChoice(DynamicSymbols.sffz)))
						.orElse(nodeWithName("fz").map(_ => DynamicSymbolChoice(DynamicSymbols.fz)))
						.orElse(node("other-dynamics")(DynamicStringChoice(_)))
						.many
				}
			} yield Dynamics(dynamics, printStyleAlign, placement, textDecoration, enclosure)
		}

		def xmlToEmpty(name: String): XmlParser[Empty] = {
			nodeWithName(name).map(_ => ())
		}

		def xmlToEmptyPlacement(name: String): XmlParser[EmptyPlacement] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				_ <- nodeWithName(name)
			} yield EmptyPlacement(printStyle, placement)
		}

		def xmlToEmptyPrintStyle(name: String): XmlParser[EmptyPrintStyle] = {
			xmlToPrintStyle << nodeWithName(name) map EmptyPrintStyle
		}

		def xmlToEmptyPrintStyleAlign(name: String): XmlParser[EmptyPrintStyleAlign] = {
			xmlToPrintStyleAlign << nodeWithName(name) map EmptyPrintStyleAlign
		}

		def xmlToEmptyPrintObjectStyleAlign(name: String): XmlParser[EmptyPrintObjectStyleAlign] = {
			for {
				printObject <- xmlToPrintObject
				printStyleAlign <- xmlToPrintStyleAlign
				_ <- nodeWithName(name)
			} yield EmptyPrintObjectStyleAlign(printObject, printStyleAlign)
		}

		def xmlToEmptyTrillSound(name: String): XmlParser[EmptyTrillSound] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				trillSound <- xmlToTrillSound
				_ <- nodeWithName(name)
			} yield EmptyTrillSound(printStyle, placement, trillSound)
		}

		def xmlToHorizontalTurn(name: String): XmlParser[HorizontalTurn] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				trillSound <- xmlToTrillSound
				slash <- yesNo("slash")(attribute).maybe
				_ <- nodeWithName(name)
			} yield HorizontalTurn(printStyle, placement, trillSound, slash)
		}

		def xmlToFermata(name: String): XmlParser[Fermata] = {
			for {
				uriType <- uprightInverted("type")(attribute).maybe
				printStyle <- xmlToPrintStyle
				base <- fermataShape(name)(node)
			} yield Fermata(base, uriType, printStyle)
		}

		def xmlToFingering(name: String): XmlParser[Fingering] = {
			for {
				substitution <- yesNo("substitution")(attribute).maybe
				alternate <- yesNo("alternate")(attribute).maybe
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				base <- xmlToString(name)
			} yield Fingering(base, substitution, alternate, printStyle, placement)
		}

		def xmlToFormattedText(name: String): XmlParser[FormattedText] = {
			for {
				textFormatting <- xmlToTextFormatting
				base <- xmlToString(name)
			} yield FormattedText(base, textFormatting)
		}

		def xmlToFret(name: String): XmlParser[Fret] = {
			for {
				font <- xmlToFont
				color <- xmlToColor
				base <- xmlToString(name).map(_.toInt)
			} yield Fret(base, font, color)
		}

		def xmlToLevel(name: String): XmlParser[Level] = {
			for {
				reference <- yesNo("reference")(attribute).maybe
				levelDisplay <- xmlToLevelDisplay
				base <- xmlToString(name)
			} yield Level(base, reference, levelDisplay)
		}

		def xmlToMidiDevice(name: String): XmlParser[MidiDevice] = {
			for {
				port <- midi16("port")(attribute).maybe
				id <- attributeId("id").maybe
				base <- xmlToString(name)
			} yield MidiDevice(base, port, id)
		}

		def xmlToMidiInstrument(name: String): XmlParser[MidiInstrument] = {
			for {
				id <- attributeId("id")
				midiInstrument <- branchNode(name) {
					for {
						midiChannel <- midi16("midi-channel")(node).maybe
						midiName <- xmlToString("midi-name").maybe
						midiBank <- midi16384("midi-bank")(node).maybe
						midiProgram <- midi128("midi-program")(node).maybe
						midiUnpitched <- midi128("midi-unpitched")(node).maybe
						volume <- percent("volume")(node).maybe
						pan <- rotationDegrees("pan")(node).maybe
						elevation <- rotationDegrees("elevation")(node).maybe
					} yield MidiInstrument(midiChannel, midiName, midiBank, midiProgram, midiUnpitched, volume, pan, elevation, id)
				}
			} yield midiInstrument
		}

		def xmlToNameDisplay(name: String): XmlParser[NameDisplay] = {
			for {
				printObject <- xmlToPrintObject
				nameDisplay <- branchNode(name) {
					xmlToFormattedText("display-text").map(FormattedTextNameDisplayChoice(_))
						.orElse(xmlToAccidentalText("accidental-text").map(AccidentalTextNameDisplayChoice(_)))
						.many
						.map(NameDisplay(_, printObject))
				}
			} yield nameDisplay
		}

		def xmlToOtherPlay(name: String): XmlParser[OtherPlay] = {
			for {
				tokenType <- attributeId("type")
				base <- xmlToString(name)
			} yield OtherPlay(base, tokenType)
		}

		def xmlToPlay(name: String): XmlParser[Play] = {
			for {
				id <- attributeId("id").maybe
				play <- branchNode(name) {
					xmlToString("ipa").map(StringChoice)
						.orElse(mute("mute")(node).map(MuteChoice))
						.orElse(semiPitched("semi-pitched")(node).map(SemiPitchedChoice))
						.orElse(xmlToOtherPlay("other-play").map(OtherPlayChoice))
						.many
						.map(Play(_, id))
				}
			} yield play
		}

		def xmlToStringClass(name: String): XmlParser[StringClass] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				base <- stringNumber(name)(node)
			} yield StringClass(base, printStyle, placement)
		}

		def xmlToTypedText(name: String): XmlParser[TypedText] = {
			for {
				attr <- attributeId("type").maybe
				base <- xmlToString(name)
			} yield TypedText(base, attr)
		}

		def xmlToWavyLine(name: String): XmlParser[WavyLine] = {
			for {
				sscType <- startStopContinue("type")(attribute)
				number <- numberLevel("number")(attribute).maybe
				position <- xmlToPosition
				placement <- xmlToPlacement
				color <- xmlToColor
				trillSound <- xmlToTrillSound
				_ <- nodeWithName(name)
			} yield WavyLine(sscType, number, position, placement, color, trillSound)
		}
	}

	class ComplexDirectionParser {

		import attributeGroupsCommonParser._
		import attributeGroupsDirectionParser._
		import complexCommonParser._
		import complexLayoutParser._
		import complexNoteParser._
		import groupCommonParser._
		import groupDirectionParser._
		import groupLayoutParser._
		import xmlParser._
		import primativeCommonParser._
		import primativeDirectionParser._
		import primativeNoteParser._

		def xmlToAccord(name: String): XmlParser[Accord] = {
			for {
				string <- stringNumber("string")(attribute).maybe
				tuning <- branchNode(name)(xmlToTuning)
			} yield Accord(tuning, string)
		}

		def xmlToAccordionRegistration(name: String): XmlParser[AccordionRegistration] = {
			for {
				printStyleAlign <- xmlToPrintStyleAlign
				ar <- branchNode(name) {
					for {
						high <- xmlToEmpty("accordion-high").maybe
						middle <- accordionMiddle("accordion-middle")(node).maybe
						low <- xmlToEmpty("accordion-low").maybe
					} yield AccordionRegistration(high, middle, low, printStyleAlign)
				}
			} yield ar
		}

		def xmlToBarre(name: String): XmlParser[Barre] = {
			for {
				ssType <- startStop("type")(attribute)
				color <- xmlToColor
				_ <- nodeWithName(name)
			} yield Barre(ssType, color)
		}

		def xmlToBass(name: String): XmlParser[Bass] = {
			branchNode(name) {
				for {
					step <- xmlToBassStep("bass-step")
					alter <- xmlToBassAlter("bass-alter").maybe
				} yield Bass(step, alter)
			}
		}

		def xmlToBassAlter(name: String): XmlParser[BassAlter] = {
			for {
				printObject <- xmlToPrintObject
				printStyle <- xmlToPrintStyle
				location <- leftRight("location")(attribute).maybe
				base <- semitones(name)(node)
			} yield BassAlter(base, printObject, printStyle, location)
		}

		def xmlToBassStep(name: String): XmlParser[BassStep] = {
			for {
				text <- attributeId("text").maybe
				printStyle <- xmlToPrintStyle
				base <- step(name)(node)
			} yield BassStep(base, text, printStyle)
		}

		def xmlToBeater(name: String): XmlParser[Beater] = {
			for {
				tip <- tipDirection("tip")(attribute).maybe
				base <- beaterValue(name)(node)
			} yield Beater(base, tip)
		}

		def xmlToBracket(name: String): XmlParser[Bracket] = {
			for {
				sscType <- startStopContinue("type")(attribute)
				number <- numberLevel("number")(attribute).maybe
				lineEnd <- lineEnd("line-end")(attribute)
				endLength <- tenths("end-length")(attribute).maybe
				lineType <- xmlToLineType
				dashedFormatting <- xmlToDashedFormatting
				position <- xmlToPosition
				color <- xmlToColor
				_ <- nodeWithName(name)
			} yield Bracket(sscType, number, lineEnd, endLength, lineType, dashedFormatting, position, color)
		}

		def xmlToDashes(name: String): XmlParser[Dashes] = {
			for {
				sscType <- startStopContinue("type")(attribute)
				number <- numberLevel("number")(attribute).maybe
				dashedFormatting <- xmlToDashedFormatting
				position <- xmlToPosition
				color <- xmlToColor
				_ <- nodeWithName(name)
			} yield Dashes(sscType, number, dashedFormatting, position, color)
		}

		def xmlToDegree(name: String): XmlParser[Degree] = {
			for {
				printObject <- xmlToPrintObject
				degree <- branchNode(name) {
					for {
						value <- xmlToDegreeValue("degree-value")
						alter <- xmlToDegreeAlter("degree-alter")
						dType <- xmlToDegreeType("degree-type")
					} yield Degree(value, alter, dType, printObject)
				}
			} yield degree
		}

		def xmlToDegreeAlter(name: String): XmlParser[DegreeAlter] = {
			for {
				printStyle <- xmlToPrintStyle
				plusMinus <- yesNo("plus-minus")(attribute).maybe
				base <- semitones(name)(node)
			} yield DegreeAlter(base, printStyle, plusMinus)
		}

		def xmlToDegreeType(name: String): XmlParser[DegreeType] = {
			for {
				text <- attributeId("text").maybe
				printStyle <- xmlToPrintStyle
				base <- degreeTypeValue(name)(node)
			} yield DegreeType(base, text, printStyle)
		}

		def xmlToDegreeValue(name: String): XmlParser[DegreeValue] = {
			for {
				symbol <- degreeSymbolValue("symbol")(attribute).maybe
				text <- attributeId("text").maybe
				printStyle <- xmlToPrintStyle
				base <- node(name)(_.toInt)
			} yield DegreeValue(base, symbol, text, printStyle)
		}

		def xmlToDirection(name: String): XmlParser[Direction] = {
			for {
				placement <- xmlToPlacement
				directive <- xmlToDirective
				direction <- branchNode(name) {
					for {
						dType <- xmlToDirectionType("direction-type").atLeastOnce
						offset <- xmlToOffset("offset").maybe
						evd <- xmlToEditorialVoiceDirection
						staff <- xmlToStaff.maybe
						sound <- xmlToSound("sound").maybe
					} yield Direction(dType, offset, evd, staff, sound, placement, directive)
				}
			} yield direction
		}

		def xmlToDirectionType(name: String): XmlParser[DirectionType] = {
			branchNode(name) {
				xmlToFormattedText("rehearsal").atLeastOnce.map(RehearsalChoice)
					.orElse(xmlToEmptyPrintStyleAlign("segno").atLeastOnce.map(SegnoChoice))
					.orElse(xmlToFormattedText("words").atLeastOnce.map(WordsChoice))
					.orElse(xmlToEmptyPrintStyleAlign("coda").atLeastOnce.map(CodaChoice))
					.orElse(xmlToWedge("wedge").map(WedgeChoice))
					.orElse(xmlToDynamics("dynamics").atLeastOnce.map(DynamicsChoice))
					.orElse(xmlToDashes("dashes").map(DashesChoice))
					.orElse(xmlToBracket("bracket").map(BracketChoice))
					.orElse(xmlToPedal("pedal").map(PedalChoice))
					.orElse(xmlToMetronome("metronome").map(MetronomeChoice))
					.orElse(xmlToOctaveShift("octave-shift").map(OctaveShiftChoice))
					.orElse(xmlToHarpPedals("harp-pedals").map(HarpPedalsChoice))
					.orElse(xmlToEmptyPrintStyleAlign("damp").map(DampChoice))
					.orElse(xmlToEmptyPrintStyleAlign("damp-all").map(DampAllChoice))
					.orElse(xmlToEmptyPrintStyleAlign("eyeglasses").map(EyeglassesChoice))
					.orElse(xmlToStringMute("string-mute").map(StringMuteChoice))
					.orElse(xmlToScordatura("scordatura").map(ScordaturaChoice))
					.orElse(xmlToImage("image").map(ImageChoice))
					.orElse(xmlToPrincipalVoice("principal-voice").map(PrincipalVoiceChoice))
					.orElse(xmlToAccordionRegistration("accordion-registration").map(AccordionRegistrationChoice))
					.orElse(xmlToPercussion("percussion").atLeastOnce.map(PercussionChoice))
					.orElse(xmlToOtherDirection("other-direction").map(OtherDirectionChoice))
			}
		}

		def xmlToFeature(name: String): XmlParser[Feature] = {
			for {
				sType <- attributeId("type").maybe
				base <- xmlToString(name)
			} yield Feature(base, sType)
		}

		def xmlToFirstFret(name: String): XmlParser[FirstFret] = {
			for {
				text <- attributeId("text").maybe
				location <- leftRight("location")(attribute).maybe
				base <- node(name)(_.toInt)
			} yield FirstFret(base, text, location)
		}

		def xmlToFrame(name: String): XmlParser[Frame] = {
			for {
				position <- xmlToPosition
				color <- xmlToColor
				halign <- xmlToHAlign
				valignImage <- xmlToVAlignImage
				height <- tenths("height")(attribute).maybe
				width <- tenths("width")(attribute).maybe
				unplayed <- attributeId("unplayed").maybe
				frame <- branchNode(name) {
					for {
						strings <- node("frame-strings")(_.toInt)
						frets <- node("frame-frets")(_.toInt)
						fret <- xmlToFirstFret("first-fret").maybe
						note <- xmlToFrameNote("frame-note").many
					} yield Frame(strings, frets, fret, note, position, color, halign, valignImage, height, width, unplayed)
				}
			} yield frame
		}

		def xmlToFrameNote(name: String): XmlParser[FrameNote] = {
			branchNode(name) {
				for {
					string <- xmlToStringClass("string")
					fret <- xmlToFret("fret")
					fingering <- xmlToFingering("fingering").maybe
					barre <- xmlToBarre("barre").maybe
				} yield FrameNote(string, fret, fingering, barre)
			}
		}

		def xmlToGrouping(name: String): XmlParser[Grouping] = {
			for {
				sssType <- startStopSingle("type")(attribute)
				number <- attributeId("number") <|> Parser.from("1")
				memberOf <- attributeId("member-of").maybe
				feature <- branchNode(name)(xmlToFeature("feature").many)
			} yield Grouping(feature, sssType, number, memberOf)
		}

		def xmlToHarmony(name: String): XmlParser[Harmony] = {
			for {
				hType <- harmonyType("type")(attribute).maybe
				printObject <- xmlToPrintObject
				printFrame <- yesNo("print-frame")(attribute).maybe
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				harmony <- branchNode(name) {
					for {
						chord <- xmlToHarmonyChord.many
						frame <- xmlToFrame("frame").maybe
						offset <- xmlToOffset("offset").maybe
						editorial <- xmlToEditorial
						staff <- xmlToStaff.maybe
					} yield Harmony(chord, frame, offset, editorial, staff, hType, printObject, printFrame, printStyle, placement)
				}
			} yield harmony
		}

		def xmlToHarpPedals(name: String): XmlParser[HarpPedals] = {
			for {
				printStyleAlign <- xmlToPrintStyleAlign
				pedalTuning <- branchNode(name)(xmlToPedalTuning("pedal-tuning").atLeastOnce)
			} yield HarpPedals(pedalTuning, printStyleAlign)
		}

		def xmlToImage(name: String): XmlParser[Image] = {
			xmlToImageAttributes << nodeWithName(name)
		}

		def xmlToInversion(name: String): XmlParser[Inversion] = {
			for {
				printStyle <- xmlToPrintStyle
				base <- node(name)(_.toInt)
			} yield Inversion(base, printStyle)
		}

		def xmlToKind(name: String): XmlParser[Kind] = {
			for {
				useSymbols <- yesNo("use-symbols")(attribute).maybe
				text <- attributeId("text").maybe
				stackDegrees <- yesNo("stack-degrees")(attribute).maybe
				parenthesesDegrees <- yesNo("parentheses-degrees")(attribute).maybe
				bracketDegrees <- yesNo("bracket-degrees")(attribute).maybe
				printStyle <- xmlToPrintStyle
				halign <- xmlToHAlign
				valign <- xmlToVAlign
				base <- kindValue(name)(node)
			} yield Kind(base, useSymbols, text, stackDegrees, parenthesesDegrees, bracketDegrees, printStyle, halign, valign)
		}

		def xmlToMeasureNumbering(name: String): XmlParser[MeasureNumbering] = {
			for {
				printStyleAlign <- xmlToPrintStyleAlign
				base <- measureNumberingValue(name)(node)
			} yield MeasureNumbering(base, printStyleAlign)
		}

		def xmlToMetronome(name: String): XmlParser[Metronome] = {
			def xmlToMetronomeBeatChoice: XmlParser[MetronomeBeatChoice] = {
				for {
					beatUnit <- xmlToBeatUnit
					choice <- xmlToPerMinute("per-minute").map(MinuteChoice)
						.orElse(xmlToBeatUnit.map(BeatChoice))
				} yield MetronomeBeatChoice(beatUnit, choice)
			}
			def xmlToMetronomeNoteChoice: XmlParser[MetronomeNoteChoice] = {
				for {
					mNote <- xmlToMetronomeNote("metronome-note").atLeastOnce
					rest <- (for {
						mRelation <- xmlToString("metronome-relation")
						mNotes <- xmlToMetronomeNote("metronome-note").atLeastOnce
					} yield MetronomeRelation(mRelation, mNotes)).maybe
				} yield MetronomeNoteChoice(mNote, rest)
			}

			for {
				printStyleAlign <- xmlToPrintStyleAlign
				justify <- xmlToJustify
				parentheses <- yesNo("parentheses")(attribute).maybe
				choice <- branchNode(name)(xmlToMetronomeBeatChoice <|> xmlToMetronomeNoteChoice)
			} yield Metronome(choice, printStyleAlign, justify, parentheses)
		}

		def xmlToMetronomeBeam(name: String): XmlParser[MetronomeBeam] = {
			for {
				number <- beamLevel("number")(attribute) <|> Parser.from(BeamLevel(1))
				base <- beamValue(name)(node)
			} yield MetronomeBeam(base, number)
		}

		def xmlToMetronomeNote(name: String): XmlParser[MetronomeNote] = {
			branchNode(name) {
				for {
					mType <- noteTypeValue("metronome-type")(node)
					mDot <- xmlToEmpty("metronome-dot").many
					mBeam <- xmlToMetronomeBeam("metronome-beam").many
					mTuplet <- xmlToMetronomeTuplet("metronome-tuplet").maybe
				} yield MetronomeNote(mType, mDot, mBeam, mTuplet)
			}
		}

		def xmlToMetronomeTuplet(name: String): XmlParser[MetronomeTuplet] = {
			for {
				ssType <- startStop("type")(attribute)
				bracket <- yesNo("bracket")(attribute).maybe
				showNumber <- showTuplet("show-number")(attribute).maybe
				base <- xmlToTimeModification(name)
			} yield MetronomeTuplet(base, ssType, bracket, showNumber)
		}

		def xmlToOctaveShift(name: String): XmlParser[OctaveShift] = {
			for {
				udscType <- upDownStopContinue("type")(attribute)
				number <- numberLevel("number")(attribute).maybe
				size <- attribute("size")(_.toInt) <|> Parser.from(8)
				dashedFormatting <- xmlToDashedFormatting
				printStyle <- xmlToPrintStyle
				_ <- nodeWithName(name)
			} yield OctaveShift(udscType, number, size, dashedFormatting, printStyle)
		}

		def xmlToOffset(name: String): XmlParser[Offset] = {
			for {
				sound <- yesNo("sound")(attribute).maybe
				base <- divisions(name)(node)
			} yield Offset(base, sound)
		}

		def xmlToOtherDirection(name: String): XmlParser[OtherDirection] = {
			for {
				printObject <- xmlToPrintObject
				printStyleAlign <- xmlToPrintStyleAlign
				base <- xmlToString(name)
			} yield OtherDirection(base, printObject, printStyleAlign)
		}

		def xmlToPedal(name: String): XmlParser[Pedal] = {
			for {
				ssccType <- startStopChangeContinue("type")(attribute)
				line <- yesNo("line")(attribute).maybe
				sign <- yesNo("sign")(attribute).maybe
				printStyleAlign <- xmlToPrintStyleAlign
				_ <- nodeWithName(name)
			} yield Pedal(ssccType, line, sign, printStyleAlign)
		}

		def xmlToPedalTuning(name: String): XmlParser[PedalTuning] = {
			branchNode(name) {
				for {
					step <- step("pedal-step")(node)
					alter <- semitones("pedal-alter")(node)
				} yield PedalTuning(step, alter)
			}
		}

		def xmlToPerMinute(name: String): XmlParser[PerMinute] = {
			for {
				font <- xmlToFont
				base <- xmlToString(name)
			} yield PerMinute(base, font)
		}

		def xmlToPercussion(name: String): XmlParser[Percussion] = {
			for {
				printStyleAlign <- xmlToPrintStyleAlign
				enclosure <- xmlToEnclosure
				choice <- branchNode(name) {
					glass("glass")(node).map(GlassPercussion)
						.orElse(metal("metal")(node).map(MetalPercussion))
						.orElse(wood("wood")(node).map(WoodPercussion))
						.orElse(pitched("pitched")(node).map(PitchedPercussion))
						.orElse(membrane("membrane")(node).map(MembranePercussion))
						.orElse(effect("effect")(node).map(EffectPercussion))
						.orElse(xmlToEmpty("timpani").map(TimpaniPercussion))
						.orElse(xmlToBeater("beater").map(BeaterPercussion))
						.orElse(xmlToStick("stick").map(StickPercussion))
						.orElse(stickLocation("stick-location")(node).map(StickLocationPercussion))
						.orElse(xmlToString("other-percussion").map(OtherPercussion))
				}
			} yield Percussion(choice, printStyleAlign, enclosure)
		}

		def xmlToPrincipalVoice(name: String): XmlParser[PrincipalVoice] = {
			for {
				ssType <- startStop("type")(attribute)
				symbol <- principalVoiceSymbol("symbol")(attribute)
				printStyleAlign <- xmlToPrintStyleAlign
				base <- xmlToString(name)
			} yield PrincipalVoice(base, ssType, symbol, printStyleAlign)
		}

		def xmlToPrint(name: String): XmlParser[Print] = {
			for {
				printAttributes <- xmlToPrintAttributes
				print <- branchNode(name) {
					for {
						layout <- xmlToLayout
						measureLayout <- xmlToMeasureLayout("measure-layout").maybe
						measureNumbering <- xmlToMeasureNumbering("measure-numbering").maybe
						partNameDisplay <- xmlToNameDisplay("part-name-display").maybe
						partAbbreviationDisplay <- xmlToNameDisplay("part-abbreviation-display").maybe
					} yield Print(layout, measureLayout, measureNumbering, partNameDisplay, partAbbreviationDisplay, printAttributes)
				}
			} yield print
		}

		def xmlToRoot(name: String): XmlParser[Root] = {
			branchNode(name) {
				for {
					rootStep <- xmlToRootStep("root-step")
					rootAlter <- xmlToRootAlter("root-alter").maybe
				} yield Root(rootStep, rootAlter)
			}
		}

		def xmlToRootAlter(name: String): XmlParser[RootAlter] = {
			for {
				printObject <- xmlToPrintObject
				printStyle <- xmlToPrintStyle
				location <- leftRight("location")(attribute).maybe
				base <- semitones(name)(node)
			} yield RootAlter(base, printObject, printStyle, location)
		}

		def xmlToRootStep(name: String): XmlParser[RootStep] = {
			for {
				text <- attributeId("text").maybe
				printStyle <- xmlToPrintStyle
				base <- step(name)(node)
			} yield RootStep(base, text, printStyle)
		}

		def xmlToScordatura(name: String): XmlParser[Scordatura] = {
			branchNode(name)(xmlToAccord("accord").many.map(Scordatura))
		}

		def xmlToSound(name: String): XmlParser[Sound] = {
			def xmlToMidiProps = for {
				device <- xmlToMidiDevice("midi-device").maybe
				instrument <- xmlToMidiInstrument("midi-instrument").maybe
				play <- xmlToPlay("play").maybe
			} yield MidiProps(device, instrument, play)

			for {
				tempo <- nonNegativeDecimal("tempo")(attribute).maybe
				dynamics <- nonNegativeDecimal("dynamics")(attribute).maybe
				dacapo <- yesNo("dacapo")(attribute).maybe
				segno <- attributeId("segno").maybe
				dalsegno <- attributeId("dalsegno").maybe
				coda <- attributeId("coda").maybe
				tocoda <- attributeId("tocoda").maybe
				divisions <- divisions("divisions")(attribute).maybe
				forwardRepeat <- yesNo("forward-repeat")(attribute).maybe
				fine <- attributeId("fine").maybe
				timeOnly <- timeOnly("time-only")(attribute).maybe
				pizzicato <- yesNo("pizzicato")(attribute).maybe
				pan <- rotationDegrees("pan")(attribute).maybe
				elevation <- rotationDegrees("elevation")(attribute).maybe
				damperPedal <- yesNoNumber("damper-pedal")(attribute)(attribute).maybe
				softPedal <- yesNoNumber("soft-pedal")(attribute)(attribute).maybe
				sostenutoPedal <- yesNoNumber("sostenuto-pedal")(attribute)(attribute).maybe
				sound <- branchNode(name) {
					for {
						midiProps <- xmlToMidiProps.takeUntil(_.isEmpty)
						offset <- xmlToOffset("offset").maybe
					} yield Sound(midiProps, offset, tempo, dynamics, dacapo, segno, dalsegno, coda, tocoda, divisions, forwardRepeat, fine, timeOnly, pizzicato, pan, elevation, damperPedal, softPedal, sostenutoPedal)
				}
			} yield sound
		}

		def xmlToStick(name: String): XmlParser[Stick] = {
			for {
				tip <- tipDirection("tip")(attribute).maybe
				stick <- branchNode(name) {
					for {
						stickType <- stickType("stick-type")(node)
						stickMaterial <- stickMaterial("stick-material")(node)
					} yield Stick(stickType, stickMaterial, tip)
				}
			} yield stick
		}

		def xmlToStringMute(name: String): XmlParser[StringMute] = {
			for {
				onOffType <- onOff("type")(attribute)
				printStyleAlign <- xmlToPrintStyleAlign
				_ <- nodeWithName(name)
			} yield StringMute(onOffType, printStyleAlign)
		}

		def xmlToWedge(name: String): XmlParser[Wedge] = {
			for {
				wedgeType <- wedgeType("type")(attribute)
				number <- numberLevel("number")(attribute).maybe
				spread <- tenths("spread")(attribute).maybe
				niente <- yesNo("niente")(attribute).maybe
				lineType <- xmlToLineType
				dashedFormatting <- xmlToDashedFormatting
				position <- xmlToPosition
				color <- xmlToColor
				_ <- nodeWithName(name)
			} yield Wedge(wedgeType, number, spread, niente, lineType, dashedFormatting, position, color)
		}
	}

	class ComplexIdentityParser {

		import complexCommonParser._
		import xmlParser._
		import primativeCommonParser._

		def xmlToEncoding(name: String): XmlParser[Encoding] = {
			branchNode(name) {
				date("encoding-date")(node).map(EncodingDate(_))
					.orElse(xmlToTypedText("encoder").map(EncodingEncoder(_)))
					.orElse(xmlToString("software").map(EncodingSoftware(_)))
					.orElse(xmlToString("encoding-description").map(EncodingDescription(_)))
					.orElse(xmlToSupports("supports").map(EncodingSupports(_)))
					.many
					.map(Encoding(_))
			}
		}

		def xmlToIdentification(name: String): XmlParser[Identification] = {
			branchNode(name) {
				for {
					creator <- xmlToTypedText("creator").many
					rights <- xmlToTypedText("rights").many
					encoding <- xmlToEncoding("encoding").maybe
					source <- xmlToString("source").maybe
					relations <- xmlToTypedText("relation").many
					miscellaneous <- xmlToMiscellaneous("miscellaneous").maybe
				} yield Identification(creator, rights, encoding, source, relations, miscellaneous)
			}
		}

		def xmlToMiscellaneous(name: String): XmlParser[Miscellaneous] = {
			branchNode(name) {
				xmlToMiscellaneousField("miscellaneous-field").many.map(Miscellaneous)
			}
		}

		def xmlToMiscellaneousField(name: String): XmlParser[MiscellaneousField] = {
			for {
				n <- attributeId("name")
				base <- xmlToString(name)
			} yield MiscellaneousField(base, n)
		}

		def xmlToSupports(name: String): XmlParser[Supports] = {
			for {
				sType <- yesNo("type")(attribute)
				element <- attributeId("element")
				attr <- attributeId("attribute").maybe
				value <- attributeId("value").maybe
				_ <- nodeWithName(name) // consume since this node only contains attributes
			} yield Supports(sType, element, attr, value)
		}
	}

	class ComplexLayoutParser {

		import complexCommonParser._
		import groupLayoutParser._
		import xmlParser._
		import primativeAttributesParser._
		import primativeCommonParser._
		import primativeLayoutParser._

		def xmlToAppearance(name: String): XmlParser[Appearance] = {
			branchNode(name) {
				for {
					lineWidth <- xmlToLineWidth("line-width").many
					noteSize <- xmlToNoteSize("note-size").many
					distance <- xmlToDistance("distance").many
					otherAppearance <- xmlToOtherAppearance("other-appearance").many
				} yield Appearance(lineWidth, noteSize, distance, otherAppearance)
			}
		}

		def xmlToDistance(name: String): XmlParser[Distance] = {
			for {
				dType <- distanceType("type")(attribute)
				base <- tenths(name)(node)
			} yield Distance(base, dType)
		}

		def xmlToLineWidth(name: String): XmlParser[LineWidth] = {
			for {
				lwType <- lineWidthType("type")(attribute)
				base <- tenths(name)(node)
			} yield LineWidth(base, lwType)
		}

		def xmlToMeasureLayout(name: String): XmlParser[MeasureLayout] = {
			branchNode(name)(tenths("measure-distance")(node).maybe.map(MeasureLayout))
		}

		def xmlToNoteSize(name: String): XmlParser[NoteSize] = {
			for {
				nsType <- noteSizeType("type")(attribute)
				base <- nonNegativeDecimal(name)(node)
			} yield NoteSize(base, nsType)
		}

		def xmlToOtherAppearance(name: String): XmlParser[OtherAppearance] = {
			for {
				tType <- attributeId("type")
				base <- xmlToString(name)
			} yield OtherAppearance(base, tType)
		}

		def xmlToPageLayout(name: String): XmlParser[PageLayout] = {
			branchNode(name) {
				for {
					size <- (for {
						height <- tenths("page-height")(node)
						width <- tenths("page-width")(node)
					} yield PageSize(height, width)).maybe
					margins <- xmlToPageMargins("page-margins").many
				} yield PageLayout(size, margins)
			}
		}

		def xmlToPageMargins(name: String): XmlParser[PageMargins] = {
			for {
				mType <- marginType("type")(attribute).maybe
				allMargins <- branchNode(name)(xmlToAllMargins)
			} yield PageMargins(allMargins, mType)
		}

		def xmlToScaling(name: String): XmlParser[Scaling] = {
			branchNode(name) {
				for {
					millimeters <- millimeters("millimeters")(node)
					tenths <- tenths("tenths")(node)
				} yield Scaling(millimeters, tenths)
			}
		}

		def xmlToStaffLayout(name: String): XmlParser[StaffLayout] = {
			for {
				number <- staffNumber("number")(attribute).maybe
				staffDistance <- branchNode(name)(tenths("staff-distance")(node).maybe)
			} yield StaffLayout(staffDistance, number)
		}

		def xmlToSystemDividers(name: String): XmlParser[SystemDividers] = {
			branchNode(name) {
				for {
					left <- xmlToEmptyPrintObjectStyleAlign("left-divider")
					right <- xmlToEmptyPrintObjectStyleAlign("right-divider")
				} yield SystemDividers(left, right)
			}
		}

		def xmlToSystemLayout(name: String): XmlParser[SystemLayout] = {
			branchNode(name) {
				for {
					margins <- xmlToSystemMargins("system-margins").maybe
					distance <- tenths("system-distance")(node).maybe
					top <- tenths("top-system-distance")(node).maybe
					dividers <- xmlToSystemDividers("system-dividers").maybe
				} yield SystemLayout(margins, distance, top, dividers)
			}
		}

		def xmlToSystemMargins(name: String): XmlParser[SystemMargins] = {
			branchNode(name)(xmlToLeftRightMargins)
		}
	}

	class ComplexLinkParser {

		import attributeGroupsCommonParser._
		import attributeGroupsLinkParser._
		import xmlParser._

		def xmlToBookmark(name: String): XmlParser[Bookmark] = {
			for {
				id <- attributeId("id")
				n <- attributeId("name").maybe
				elementPosition <- xmlToElementPosition
				_ <- nodeWithName(name)
			} yield Bookmark(id, n, elementPosition)
		}

		def xmlToLink(name: String): XmlParser[Link] = {
			for {
				linkAttributes <- xmlToLinkAttributes
				n <- attributeId("name").maybe
				elementPosition <- xmlToElementPosition
				position <- xmlToPosition
				_ <- nodeWithName(name)
			} yield Link(linkAttributes, n, elementPosition, position)
		}
	}

	class ComplexNoteParser {

		import attributeGroupsCommonParser._
		import complexCommonParser._
		import groupCommonParser._
		import groupNoteParser._
		import xmlParser._
		import primativeCommonParser._
		import primativeNoteParser._

		def xmlToAccidental(name: String): XmlParser[Accidental] = {
			for {
				cautionary <- yesNo("cautionary")(attribute).maybe
				editorial <- yesNo("editorial")(attribute).maybe
				levelDisplay <- xmlToLevelDisplay
				printStyle <- xmlToPrintStyle
				base <- accidentalValue(name)(node)
			} yield Accidental(base, cautionary, editorial, levelDisplay, printStyle)
		}

		def xmlToAccidentalMark(name: String): XmlParser[AccidentalMark] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				base <- accidentalValue(name)(node)
			} yield AccidentalMark(base, printStyle, placement)
		}

		def xmlToArpeggiate(name: String): XmlParser[Arpeggiate] = {
			for {
				number <- numberLevel("number")(attribute).maybe
				direction <- upDown("direction")(attribute).maybe
				position <- xmlToPosition
				placement <- xmlToPlacement
				color <- xmlToColor
				_ <- nodeWithName(name)
			} yield Arpeggiate(number, direction, position, placement, color)
		}

		def xmltoArticulations(name: String): XmlParser[Articulations] = {
			branchNode(name) {
				xmlToEmptyPlacement("accent").map(AccentChoice)
					.orElse(xmlToStrongAccent("strong-accent").map(StrongAccentChoice))
					.orElse(xmlToEmptyPlacement("staccato").map(StaccatoChoice))
					.orElse(xmlToEmptyPlacement("tenuto").map(TenutoChoice))
					.orElse(xmlToEmptyPlacement("detached-legato").map(DetachedLegatoChoice))
					.orElse(xmlToEmptyPlacement("staccatissimo").map(StaccatissimoChoice))
					.orElse(xmlToEmptyPlacement("spiccato").map(SpiccatoChoice))
					.orElse(xmlToEmptyLine("scoop").map(ScoopChoice))
					.orElse(xmlToEmptyLine("plop").map(PlopChoice))
					.orElse(xmlToEmptyLine("doit").map(DoitChoice))
					.orElse(xmlToEmptyLine("falloff").map(FalloffChoice))
					.orElse(xmlToBreathMark("breath-mark").map(BreathMarkChoice))
					.orElse(xmlToEmptyPlacement("caesura").map(CaesuraChoice))
					.orElse(xmlToEmptyPlacement("stress").map(StressChoice))
					.orElse(xmlToEmptyPlacement("unstress").map(UnstressChoice))
					.orElse(xmlToPlacementText("other-articulation").map(OtherArticulationChoice))
					.many
					.map(Articulations)
			}
		}

		def xmlToArrow(name: String): XmlParser[Arrow] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				choice <- branchNode(name) {
					(for {
						dir <- arrowDirection("arrow-direction")(node)
						style <- arrowStyle("arrow-style")(node).maybe
					} yield StraightArrowChoice(dir, style))
						.orElse(circularArrow("circular-arrow")(node).map(CircleArrowChoice))
				}
			} yield Arrow(choice, printStyle, placement)
		}

		def xmlToBackup(name: String): XmlParser[Backup] = {
			branchNode(name) {
				for {
					duration <- xmlToDuration
					editorial <- xmlToEditorial
				} yield Backup(duration, editorial)
			}
		}

		def xmlToBeam(name: String): XmlParser[Beam] = {
			for {
				number <- beamLevel("number")(attribute) <|> Parser.from(BeamLevel(1))
				repeater <- yesNo("repeater")(attribute).maybe
				fan <- fan("fan")(attribute).maybe
				color <- xmlToColor
				base <- beamValue(name)(node)
			} yield Beam(base, number, repeater, fan, color)
		}

		def xmlToBend(name: String): XmlParser[Bend] = {
			for {
				printStyle <- xmlToPrintStyle
				bendSound <- xmlToBendSound
				bend <- branchNode(name) {
					for {
						bendAlter <- semitones("bend-alter")(node)
						choice <- xmlToEmpty("pre-bend").map(PreBand)
							.orElse(xmlToEmpty("release").map(Release))
							.maybe
						withBar <- xmlToPlacementText("with-bar").maybe
					} yield Bend(bendAlter, choice, withBar, printStyle, bendSound)
				}
			} yield bend
		}

		def xmlToBreathMark(name: String): XmlParser[BreathMark] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				base <- breathMarkValue(name)(node)
			} yield BreathMark(base, printStyle, placement)
		}

		def xmlToEmptyLine(name: String): XmlParser[EmptyLine] = {
			for {
				lineShape <- xmlToLineShape
				lineType <- xmlToLineType
				dashedFormatting <- xmlToDashedFormatting
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				_ <- nodeWithName(name)
			} yield EmptyLine(lineShape, lineType, dashedFormatting, printStyle, placement)
		}

		def xmlToExtend(name: String): XmlParser[Extend] = {
			for {
				sscType <- startStopContinue("type")(attribute).maybe
				printStyle <- xmlToPrintStyle
				_ <- nodeWithName(name)
			} yield Extend(sscType, printStyle)
		}

		def xmlToFigure(name: String): XmlParser[Figure] = {
			branchNode(name) {
				for {
					prefix <- xmlToStyleText("prefix").maybe
					figureNumber <- xmlToStyleText("figure-number").maybe
					suffix <- xmlToStyleText("suffix").maybe
					extend <- xmlToExtend("extend").maybe
				} yield Figure(prefix, figureNumber, suffix, extend)
			}
		}

		def xmlToFiguredBass(name: String): XmlParser[FiguredBass] = {
			for {
				printStyle <- xmlToPrintStyle
				printout <- xmlToPrintout
				parentheses <- yesNo("parentheses")(attribute).maybe
				figuredBass <- branchNode(name) {
					for {
						figure <- xmlToFigure("figure").atLeastOnce
						duration <- xmlToDuration.maybe
						editorial <- xmlToEditorial
					} yield FiguredBass(figure, duration, editorial, printStyle, printout, parentheses)
				}
			} yield figuredBass
		}

		def xmlToForward(name: String): XmlParser[Forward] = {
			branchNode(name) {
				for {
					duration <- xmlToDuration
					editorialVoice <- xmlToEditorialVoice
					staff <- xmlToStaff.maybe
				} yield Forward(duration, editorialVoice, staff)
			}
		}

		def xmlToGlissando(name: String): XmlParser[Glissando] = {
			for {
				ssType <- startStop("type")(attribute)
				number <- numberLevel("number")(attribute) <|> Parser.from(NumberLevel(1))
				lineType <- xmlToLineType
				dashedFormatting <- xmlToDashedFormatting
				printStyle <- xmlToPrintStyle
				base <- xmlToString(name)
			} yield Glissando(base, ssType, number, lineType, dashedFormatting, printStyle)
		}

		def xmlToGrace(name: String): XmlParser[Grace] = {
			for {
				prev <- percent("steal-time-previous")(attribute).maybe
				foll <- percent("steal-time-following")(attribute).maybe
				make <- divisions("make-time")(attribute).maybe
				slash <- yesNo("slash")(attribute).maybe
				_ <- nodeWithName(name)
			} yield Grace(prev, foll, make, slash)
		}

		def xmlToHammerOnPullOff(name: String): XmlParser[HammerOnPullOff] = {
			for {
				ssType <- startStop("type")(attribute)
				number <- numberLevel("number")(attribute) <|> Parser.from(NumberLevel(1))
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				base <- xmlToString(name)
			} yield HammerOnPullOff(base, ssType, number, printStyle, placement)
		}

		def xmlToHandbell(name: String): XmlParser[Handbell] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				base <- handbellValue(name)(node)
			} yield Handbell(base, printStyle, placement)
		}

		def xmlToHarmonic(name: String): XmlParser[Harmonic] = {
			for {
				printObject <- xmlToPrintObject
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				harmonic <- branchNode(name) {
					for {
						c1 <- xmlToEmpty("natural").map(Natural)
							.orElse(xmlToEmpty("artificial").map(Artificial))
							.maybe
						c2 <- xmlToEmpty("base-pitch").map(BasePitch)
							.orElse(xmlToEmpty("touching-pitch").map(TouchingPitch))
							.orElse(xmlToEmpty("sounding-pitch").map(SoundingPitch))
							.maybe
					} yield Harmonic(c1, c2, printObject, printStyle, placement)
				}
			} yield harmonic
		}

		def xmlToHeelToe(name: String): XmlParser[HeelToe] = {
			for {
				substitution <- yesNo("substitution")(attribute).maybe
				base <- xmlToEmptyPlacement(name)
			} yield HeelToe(base, substitution)
		}

		def xmlToHole(name: String): XmlParser[Hole] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				hole <- branchNode(name) {
					for {
						hType <- xmlToString("hole-type").maybe
						closed <- xmlToHoleClosed("hole-closed")
						shape <- xmlToString("hole-shape").maybe
					} yield Hole(hType, closed, shape, printStyle, placement)
				}
			} yield hole
		}

		def xmlToHoleClosed(name: String): XmlParser[HoleClosed] = {
			for {
				location <- holeClosedLocation("location")(attribute).maybe
				base <- holeClosedValue(name)(node)
			} yield HoleClosed(base, location)
		}

		def xmlToInstrument(name: String): XmlParser[Instrument] = {
			attributeId("id") << nodeWithName(name)
		}

		def xmlToLyric(name: String): XmlParser[Lyric] = {
			def toElisionSyllabic = {
				for {
					elision <- xmlToTextFontColor("elision")
					syllabic <- syllabic("syllabic")(node).maybe
				} yield ElisionSyllabic(elision, syllabic)
			}

			def toElisionSyllabicText = {
				for {
					subSeq <- toElisionSyllabic.maybe
					text <- xmlToTextElementData("text")
				} yield ElisionSyllabicText(subSeq, text)
			}

			def toTextLyricsChoice = {
				for {
					s <- syllabic("syllabic")(node).maybe
					text <- xmlToTextElementData("text")
					seq <- toElisionSyllabicText.many
					extend <- xmlToExtend("extend").maybe
				} yield TextLyricsChoice(s, text, seq, extend)
			}

			for {
				number <- attributeId("number").maybe
				n <- attributeId("name").maybe
				justify <- xmlToJustify
				position <- xmlToPosition
				placement <- xmlToPlacement
				color <- xmlToColor
				printObject <- xmlToPrintObject
				lyric <- branchNode(name) {
					for {
						choice <- toTextLyricsChoice
							.orElse(xmlToExtend("extend").map(ExtendLyricsChoice))
							.orElse(xmlToEmpty("laughing").map(LaughingLyricsChoice))
							.orElse(xmlToEmpty("humming").map(HummingLyricsChoice))
						endLine <- xmlToEmpty("end-line").maybe
						endParagraph <- xmlToEmpty("end-paragraph").maybe
						editorial <- xmlToEditorial
					} yield Lyric(choice, endLine, endParagraph, editorial, number, n, justify, position, placement, color, printObject)
				}
			} yield lyric
		}

		def xmlToMordent(name: String): XmlParser[Mordent] = {
			for {
				long <- yesNo("long")(attribute).maybe
				approach <- aboveBelow("approach")(attribute).maybe
				departure <- aboveBelow("departure")(attribute).maybe
				base <- xmlToEmptyTrillSound(name)
			} yield Mordent(base, long, approach, departure)
		}

		def xmlToNonArpeggiate(name: String): XmlParser[NonArpeggiate] = {
			for {
				tbType <- topBottom("type")(attribute)
				number <- numberLevel("number")(attribute).maybe
				position <- xmlToPosition
				placement <- xmlToPlacement
				color <- xmlToColor
				_ <- nodeWithName(name)
			} yield NonArpeggiate(tbType, number, position, placement, color)
		}

		def xmlToNotations(name: String): XmlParser[Notations] = {
			for {
				printObject <- xmlToPrintObject
				notations <- branchNode(name) {
					for {
						editorial <- xmlToEditorial
						choice <- xmlToTied("tied").map(TiedNotation)
							.orElse(xmlToSlur("slur").map(SlurNotation))
							.orElse(xmlToTuplet("tuplet").map(TupletNotation))
							.orElse(xmlToGlissando("glissando").map(GlissandoNotation))
							.orElse(xmlToSlide("slide").map(SlideNotation))
							.orElse(xmlToOrnaments("ornaments").map(OrnamentsNotation))
							.orElse(xmlToTechnical("technical").map(TechnicalNotation))
							.orElse(xmltoArticulations("articulations").map(ArticulationsNotation))
							.orElse(xmlToDynamics("dynamics").map(DynamicsNotation))
							.orElse(xmlToFermata("fermata").map(FermataNotation))
							.orElse(xmlToArpeggiate("arpeggiate").map(ArpeggiateNotation))
							.orElse(xmlToNonArpeggiate("non-arpeggiate").map(NonArpeggiateNotation))
							.orElse(xmlToAccidentalMark("accidental-mark").map(AccidentalMarkNotation))
							.orElse(xmlToOtherNotation("other-notation").map(OtherNotationNotation))
							.many
					} yield Notations(editorial, choice, printObject)
				}
			} yield notations
		}

		def xmlToNote(name: String): XmlParser[Note] = {
			def toGraceNoteChoice = {
				for {
					grace <- xmlToGrace("grace")
					fullNote <- xmlToFullNote
					tie <- xmlToTie("tie").many
				} yield GraceNoteChoice(grace, fullNote, tie)
			}

			def toCueNoteChoice = {
				for {
					cue <- xmlToEmpty("cue")
					fullNote <- xmlToFullNote
					duration <- xmlToDuration
				} yield CueNoteChoice(cue, fullNote, duration)
			}

			def toFullNoteChoice = {
				for {
					fullNote <- xmlToFullNote
					duration <- xmlToDuration
					tie <- xmlToTie("tie").many
				} yield FullNoteChoice(fullNote, duration, tie)
			}

			for {
				xPosition <- xmlToXPosition
				font <- xmlToFont
				color <- xmlToColor
				printout <- xmlToPrintout
				dynamics <- nonNegativeDecimal("dynamics")(attribute).maybe
				endDynamics <- nonNegativeDecimal("end-dynamics")(attribute).maybe
				attack <- divisions("attack")(attribute).maybe
				release <- divisions("release")(attribute).maybe
				timeOnly <- timeOnly("time-only")(attribute).maybe
				pizzicato <- yesNo("pizzicato")(attribute).maybe
				note <- branchNode(name) {
					for {
						choice <- toGraceNoteChoice <|> toCueNoteChoice <|> toFullNoteChoice
						instrument <- xmlToInstrument("instrument").maybe
						edVoice <- xmlToEditorialVoice
						nType <- xmlToNoteType("type").maybe
						dot <- xmlToEmptyPlacement("dot").many
						accidental <- xmlToAccidental("accidental").maybe
						timeMod <- xmlToTimeModification("time-modification").maybe
						stem <- xmlToStem("stem").maybe
						notehead <- xmlToNoteHead("notehead").maybe
						noteheadText <- xmlToNoteHeadText("notehead-text").maybe
						staff <- xmlToStaff.maybe
						beam <- xmlToBeam("beam").many
						notations <- xmlToNotations("notations").many
						lyric <- xmlToLyric("lyric").many
						play <- xmlToPlay("play").maybe
					} yield Note(choice, instrument, edVoice, nType, dot, accidental, timeMod, stem, notehead, noteheadText, staff, beam, notations, lyric, play, xPosition, font, color, printout, dynamics, endDynamics, attack, release, timeOnly, pizzicato)
				}
			} yield note
		}

		def xmlToNoteType(name: String): XmlParser[NoteType] = {
			for {
				size <- symbolSize("size")(attribute).maybe
				base <- noteTypeValue(name)(node)
			} yield NoteType(base, size)
		}

		def xmlToNoteHead(name: String): XmlParser[NoteHead] = {
			for {
				filled <- yesNo("filled")(attribute).maybe
				parentheses <- yesNo("parentheses")(attribute).maybe
				font <- xmlToFont
				color <- xmlToColor
				base <- noteHeadValue(name)(node)
			} yield NoteHead(base, filled, parentheses, font, color)
		}

		def xmlToNoteHeadText(name: String): XmlParser[NoteHeadText] = {
			branchNode(name) {
				xmlToFormattedText("display-text").map(DisplayTextChoice)
					.orElse(xmlToAccidentalText("accidental-text").map(AccidentalTextChoice))
					.many
					.map(NoteHeadText)
			}
		}

		def xmlToOrnaments(name: String): XmlParser[Ornaments] = {
			branchNode(name) {
				(for {
					choice <- xmlToEmptyTrillSound("trill-mark").map(TrillMarkOrnamentsChoice)
						.orElse(xmlToHorizontalTurn("turn").map(TurnOrnamentsChoice))
						.orElse(xmlToHorizontalTurn("delayed-turn").map(DelayedTurnOrnamentsChoice))
						.orElse(xmlToHorizontalTurn("inverted-turn").map(InvertedTurnOrnamentsChoice))
						.orElse(xmlToHorizontalTurn("delayed-inverted-turn").map(DelayedInvertedTurnedOrnamentsChoice))
						.orElse(xmlToEmptyTrillSound("vertical-turn").map(VerticalTurnOrnamentsChoice))
						.orElse(xmlToEmptyTrillSound("shake").map(ShakeOrnamentsChoice))
						.orElse(xmlToWavyLine("wavy-line").map(WavyLineOrnamentsChoice))
						.orElse(xmlToMordent("mordent").map(MordentOrnamentsChoice))
						.orElse(xmlToMordent("inverted-mordent").map(InvertedMordentOrnamentsChoice))
						.orElse(xmlToEmptyPlacement("schleifer").map(SchleiferOrnamentsChoice))
						.orElse(xmlToTremolo("tremolo").map(TremoloOrnamentsChoice))
						.orElse(xmlToPlacementText("other-ornament").map(OtherOrnamentOrnamentsChoice))
					accidentalMark <- xmlToAccidentalMark("accidental-mark").many
				} yield OrnamentsContent(choice, accidentalMark))
					.many
					.map(Ornaments)
			}
		}

		def xmlToOtherNotation(name: String): XmlParser[OtherNotation] = {
			for {
				sssType <- startStopSingle("type")(attribute)
				number <- numberLevel("number")(attribute) <|> Parser.from(NumberLevel(1))
				printObject <- xmlToPrintObject
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				base <- xmlToString(name)
			} yield OtherNotation(base, sssType, number, printObject, printStyle, placement)
		}

		def xmlToPitch(name: String): XmlParser[Pitch] = {
			branchNode(name) {
				for {
					step <- step("step")(node)
					alter <- semitones("alter")(node).maybe
					octave <- octave("octave")(node)
				} yield Pitch(step, alter, octave)
			}
		}

		def xmlToPlacementText(name: String): XmlParser[PlacementText] = {
			for {
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				base <- xmlToString(name)
			} yield PlacementText(base, printStyle, placement)
		}

		def xmlToRest(name: String): XmlParser[Rest] = {
			for {
				measure <- yesNo("measure")(attribute).maybe
				displayStepOctave <- branchNode(name)(xmlToDisplayStepOctave.maybe)
			} yield Rest(displayStepOctave, measure)
		}

		def xmlToSlide(name: String): XmlParser[Slide] = {
			for {
				ssType <- startStop("type")(attribute)
				number <- numberLevel("number")(attribute) <|> Parser.from(NumberLevel(1))
				lineType <- xmlToLineType
				dashedFormatting <- xmlToDashedFormatting
				printStyle <- xmlToPrintStyle
				bendSound <- xmlToBendSound
				base <- xmlToString(name)
			} yield Slide(base, ssType, number, lineType, dashedFormatting, printStyle, bendSound)
		}

		def xmlToSlur(name: String): XmlParser[Slur] = {
			for {
				sscType <- startStopContinue("type")(attribute)
				number <- numberLevel("number")(attribute) <|> Parser.from(NumberLevel(1))
				lineType <- xmlToLineType
				dashedFormatting <- xmlToDashedFormatting
				position <- xmlToPosition
				placement <- xmlToPlacement
				orientation <- xmlToOrientation
				bezier <- xmlToBezier
				color <- xmlToColor
				_ <- nodeWithName(name)
			} yield Slur(sscType, number, lineType, dashedFormatting, position, placement, orientation, bezier, color)
		}

		def xmlToStem(name: String): XmlParser[Stem] = {
			for {
				yPosition <- xmlToYPosition
				color <- xmlToColor
				base <- stemValue(name)(node)
			} yield Stem(base, yPosition, color)
		}

		def xmlToStrongAccent(name: String): XmlParser[StrongAccent] = {
			for {
				udType <- upDown("type")(attribute) <|> Parser.from(UD_Up)
				base <- xmlToEmptyPlacement(name)
			} yield StrongAccent(base, udType)
		}

		def xmlToStyleText(name: String): XmlParser[StyleText] = {
			for {
				printStyle <- xmlToPrintStyle
				base <- xmlToString(name)
			} yield StyleText(base, printStyle)
		}

		def xmlToTechnical(name: String): XmlParser[Technical] = {
			branchNode(name) {
				xmlToEmptyPlacement("up-bow").map(UpBowTechnicalChoice)
					.orElse(xmlToEmptyPlacement("down-bow").map(DownBowTechnicalChoice))
					.orElse(xmlToHarmonic("harmonic").map(HarmonicTechnicalChoice))
					.orElse(xmlToEmptyPlacement("open-string").map(OpenStringTechnicalChoice))
					.orElse(xmlToEmptyPlacement("thumb-position").map(ThumbPositionTechnicalChoice))
					.orElse(xmlToFingering("fingering").map(FingeringTechnicalChoice))
					.orElse(xmlToPlacementText("pluck").map(PluckTechnicalChoice))
					.orElse(xmlToEmptyPlacement("double-tongue").map(DoubleTongueTechnicalChoice))
					.orElse(xmlToEmptyPlacement("triple-tongue").map(TripleTongueTechnicalChoice))
					.orElse(xmlToEmptyPlacement("stopped").map(StoppedTechnicalChoice))
					.orElse(xmlToEmptyPlacement("snap-pizzicato").map(SnapPizzicatoTechnicalChoice))
					.orElse(xmlToFret("fret").map(FretTechnicalChoice))
					.orElse(xmlToStringClass("string").map(StringTechnicalChoice))
					.orElse(xmlToHammerOnPullOff("hammer-on").map(HammerOnTechnicalChoice))
					.orElse(xmlToHammerOnPullOff("pull-off").map(PullOffTechnicalChoice))
					.orElse(xmlToBend("bend").map(BendTechnicalChoice))
					.orElse(xmlToPlacementText("tap").map(TapTechnicalChoice))
					.orElse(xmlToHeelToe("heel").map(HeelTechnicalChoice))
					.orElse(xmlToHeelToe("toe").map(ToeTechnicalChoice))
					.orElse(xmlToEmptyPlacement("fingernails").map(FingernailsTechnicalChoice))
					.orElse(xmlToHole("hole").map(HoleTechnicalChoice))
					.orElse(xmlToArrow("arrow").map(ArrowTechnicalChoice))
					.orElse(xmlToHandbell("handbell").map(HandbellTechnicalChoice))
					.orElse(xmlToPlacementText("other-technical").map(OtherTechnicalChoice))
					.many
					.map(Technical)
			}
		}

		def xmlToTextElementData(name: String): XmlParser[TextElementData] = {
			for {
				font <- xmlToFont
				color <- xmlToColor
				textDecoration <- xmlToTextDecoration
				textRotation <- xmlToTextRotation
				letterSpacing <- xmlToLetterSpacing
				lang <- attributeId("{http://www.w3.org/XML/1998/namespace}lang").maybe
				textDirection <- xmlToTextDirection
				base <- xmlToString(name)
			} yield TextElementData(base, font, color, textDecoration, textRotation, letterSpacing, lang, textDirection)
		}

		def xmlToTextFontColor(name: String): XmlParser[TextFontColor] = {
			for {
				font <- xmlToFont
				color <- xmlToColor
				textDecoration <- xmlToTextDecoration
				textRotation <- xmlToTextRotation
				letterSpacing <- xmlToLetterSpacing
				lang <- attributeId("{http://www.w3.org/XML/1998/namespace}lang").maybe
				textDirection <- xmlToTextDirection
				base <- xmlToString(name)
			} yield TextFontColor(base, font, color, textDecoration, textRotation, letterSpacing, lang, textDirection)
		}

		def xmlToTie(name: String): XmlParser[Tie] = {
			for {
				ssType <- startStop("type")(attribute)
				timeOnly <- timeOnly("time-only")(attribute).maybe
				_ <- nodeWithName(name)
			} yield Tie(ssType, timeOnly)
		}

		def xmlToTied(name: String): XmlParser[Tied] = {
			for {
				sscType <- startStopContinue("type")(attribute)
				number <- numberLevel("number")(attribute).maybe
				lineType <- xmlToLineType
				dashedFormatting <- xmlToDashedFormatting
				position <- xmlToPosition
				placement <- xmlToPlacement
				orientation <- xmlToOrientation
				bezier <- xmlToBezier
				color <- xmlToColor
				_ <- nodeWithName(name)
			} yield Tied(sscType, number, lineType, dashedFormatting, position, placement, orientation, bezier, color)
		}

		def xmlToTimeModification(name: String): XmlParser[TimeModification] = {
			def toTuple = {
				for {
					normalType <- noteTypeValue("normal-type")(node)
					normalDot <- xmlToEmpty("normal-dot").many
				} yield (normalType, normalDot)
			}

			branchNode(name) {
				for {
					actual <- node("actual-notes")(_.toInt)
					normal <- node("normal-notes")(_.toInt)
					seq <- toTuple.maybe
				} yield TimeModification(actual, normal, seq)
			}
		}

		def xmlToTremolo(name: String): XmlParser[Tremolo] = {
			for {
				sssType <- startStopSingle("type")(attribute) <|> Parser.from(SSS_Single)
				printStyle <- xmlToPrintStyle
				placement <- xmlToPlacement
				base <- tremoloMarks(name)(node)
			} yield Tremolo(base, sssType, printStyle, placement)
		}

		def xmlToTuplet(name: String): XmlParser[Tuplet] = {
			for {
				ssType <- startStop("type")(attribute)
				number <- numberLevel("number")(attribute).maybe
				bracket <- yesNo("bracket")(attribute).maybe
				showNumber <- showTuplet("show-number")(attribute).maybe
				showType <- showTuplet("show-type")(attribute).maybe
				lineShape <- xmlToLineShape
				position <- xmlToPosition
				placement <- xmlToPlacement
				tuplet <- branchNode(name) {
					for {
						actual <- xmlToTupletPortion("tuplet-actual").maybe
						normal <- xmlToTupletPortion("tuplet-normal").maybe
					} yield Tuplet(actual, normal, ssType, number, bracket, showNumber, showType, lineShape, position, placement)
				}
			} yield tuplet
		}

		def xmlToTupletDot(name: String): XmlParser[TupletDot] = {
			for {
				font <- xmlToFont
				color <- xmlToColor
				_ <- nodeWithName(name)
			} yield TupletDot(font, color)
		}

		def xmlToTupletNumber(name: String): XmlParser[TupletNumber] = {
			for {
				font <- xmlToFont
				color <- xmlToColor
				base <- node(name)(_.toInt)
			} yield TupletNumber(base, font, color)
		}

		def xmlToTupletPortion(name: String): XmlParser[TupletPortion] = {
			branchNode(name) {
				for {
					number <- xmlToTupletNumber("tuplet-number").maybe
					tType <- xmlToTupletType("tuplet-type").maybe
					dot <- xmlToTupletDot("tuplet-dot").many
				} yield TupletPortion(number, tType, dot)
			}
		}

		def xmlToTupletType(name: String): XmlParser[TupletType] = {
			for {
				font <- xmlToFont
				color <- xmlToColor
				base <- noteTypeValue(name)(node)
			} yield TupletType(base, font, color)
		}

		def xmlToUnpitched(name: String): XmlParser[Unpitched] = {
			branchNode(name)(xmlToDisplayStepOctave.maybe.map(Unpitched))
		}
	}

	class ComplexScoreParser {

		import attributeGroupsCommonParser._
		import attributeGroupsLinkParser._
		import attributeGroupsScoreParser._
		import complexCommonParser._
		import complexDirectionParser._
		import complexIdentityParser._
		import complexLayoutParser._
		import complexLinkParser._
		import groupCommonParser._
		import groupLayoutParser._
		import xmlParser._
		import primativeCommonParser._
		import primativeScoreParser._

		def xmlToCredit(name: String): XmlParser[Credit] = {
			def toLinksBookmarksCreditWords = {
				for {
					links <- xmlToLink("link").many
					bookmarks <- xmlToBookmark("bookmark").many
					creditWords <- xmlToFormattedText("credit-words")
				} yield (links, bookmarks, creditWords)
			}

			def toCreditPairChoice = {
				for {
					words <- xmlToFormattedText("credit-words")
					seq <- toLinksBookmarksCreditWords.many
				} yield CreditPairChoice(words, seq)
			}

			for {
				page <- attribute("page")(_.toInt).maybe
				credit <- branchNode(name) {
					for {
						creditType <- xmlToString("credit-type").many
						link <- xmlToLink("link").many
						bookmark <- xmlToBookmark("bookmark").many
						choice <- xmlToImage("credit-image").map(CreditImageChoice) <|> toCreditPairChoice
					} yield Credit(creditType, link, bookmark, choice, page)
				}
			} yield credit
		}

		def xmlToDefaults(name: String): XmlParser[Defaults] = {
			branchNode(name) {
				for {
					scaling <- xmlToScaling("scaling").maybe
					layout <- xmlToLayout
					appearance <- xmlToAppearance("appearance").maybe
					musicFont <- xmlToEmptyFont("music-font").maybe
					wordFont <- xmlToEmptyFont("word-font").maybe
					lyricFont <- xmlToLyricFont("lyric-font").many
					lyricLanguage <- xmlToLyricLanguage("lyric-language").many
				} yield Defaults(scaling, layout, appearance, musicFont, wordFont, lyricFont, lyricLanguage)
			}
		}

		def xmlToEmptyFont(name: String): XmlParser[EmptyFont] = {
			for {
				font <- xmlToFont
				_ <- nodeWithName(name)
			} yield EmptyFont(font)
		}

		def xmlToGroupBarline(name: String): XmlParser[GroupBarline] = {
			for {
				color <- xmlToColor
				base <- groupBarlineValue(name)(node)
			} yield GroupBarline(base, color)
		}

		def xmlToGroupName(name: String): XmlParser[GroupName] = {
			for {
				groupNameText <- xmlToGroupNameText
				base <- xmlToString(name)
			} yield GroupName(base, groupNameText)
		}

		def xmlToGroupSymbol(name: String): XmlParser[GroupSymbol] = {
			for {
				position <- xmlToPosition
				color <- xmlToColor
				base <- groupSymbolValue(name)(node)
			} yield GroupSymbol(base, position, color)
		}

		def xmlToLyricFont(name: String): XmlParser[LyricFont] = {
			for {
				number <- attributeId("number").maybe
				n <- attributeId("name").maybe
				font <- xmlToFont
				_ <- nodeWithName(name)
			} yield LyricFont(number, n, font)
		}

		def xmlToLyricLanguage(name: String): XmlParser[LyricLanguage] = {
			for {
				number <- attributeId("number").maybe
				n <- attributeId("name").maybe
				lang <- attributeId("{http://www.w3.org/XML/1998/namespace}lang")
				_ <- nodeWithName(name)
			} yield LyricLanguage(number, n, lang)
		}

		def xmlToOpus(name: String): XmlParser[Opus] = {
			xmlToLinkAttributes << nodeWithName(name)
		}

		def xmlToPartGroup(name: String): XmlParser[PartGroup] = {
			for {
				ssType <- startStop("type")(attribute)
				number <- attributeId("number") <|> Parser.from("1")
				partGroup <- branchNode(name) {
					for {
						n <- xmlToGroupName("group-name").maybe
						nameDisplay <- xmlToNameDisplay("group-name-display").maybe
						abbreviation <- xmlToGroupName("group-abbreviation").maybe
						abbreviationDisplay <- xmlToNameDisplay("group-abbreviation-display").maybe
						symbol <- xmlToGroupSymbol("group-symbol").maybe
						barline <- xmlToGroupBarline("group-barline").maybe
						time <- xmlToEmpty("group-time").maybe
						editorial <- xmlToEditorial
					} yield PartGroup(n, nameDisplay, abbreviation, abbreviationDisplay, symbol, barline, time, editorial, ssType, number)
				}
			} yield partGroup
		}

		def xmlToPartList(name: String): XmlParser[PartList] = {
			branchNode(name) {
				for {
					partGroup <- groupScoreParser.xmlToPartGroup.many
					scorePart <- groupScoreParser.xmlToScorePart
					choice <- groupScoreParser.xmlToPartGroup.map(PartGroupChoice)
						.orElse(groupScoreParser.xmlToScorePart.map(ScorePartChoice))
						.many
				} yield PartList(partGroup, scorePart, choice)
			}
		}

		def xmlToPartName(name: String): XmlParser[PartName] = {
			for {
				partNameText <- xmlToPartNameText
				base <- xmlToString(name)
			} yield PartName(base, partNameText)
		}

		def xmlToScoreInstrument(name: String): XmlParser[ScoreInstrument] = {
			for {
				id <- attributeId("id")
				scoreInstrument <- branchNode(name) {
					for {
						n <- xmlToString("instrument-name")
						abbreviation <- xmlToString("instrument-abbreviation").maybe
						sound <- xmlToString("instrument-sound").maybe
						choice <- xmlToEmpty("solo").map(SoloChoice)
							.orElse(positiveIntegerOrEmpty("ensemble")(node).map(EnsembleChoice))
							.maybe
						virtInstr <- xmlToVirtualInstrument("virtual-instrument").maybe
					} yield ScoreInstrument(n, abbreviation, sound, choice, virtInstr, id)
				}
			} yield scoreInstrument
		}

		def xmlToScorePart(name: String): XmlParser[ScorePart] = {
			def toDeviceAndInstrument = {
				for {
					device <- xmlToMidiDevice("midi-device").maybe
					instrument <- xmlToMidiInstrument("midi-instrument").maybe
				} yield (device, instrument)
			}

			for {
				id <- attributeId("id")
				scorePart <- branchNode(name) {
					for {
						ident <- xmlToIdentification("identification").maybe
						n <- xmlToPartName("part-name")
						nameDisplay <- xmlToNameDisplay("part-name-display").maybe
						abbr <- xmlToPartName("part-abbreviation").maybe
						abbrDisplay <- xmlToNameDisplay("part-abbreviation-display").maybe
						group <- xmlToString("group").many
						instr <- xmlToScoreInstrument("score-instrument").many
						midi <- toDeviceAndInstrument.takeUntil { case (dev, ins) => dev.isEmpty && ins.isEmpty }
					} yield ScorePart(ident, n, nameDisplay, abbr, abbrDisplay, group, instr, midi, id)
				}
			} yield scorePart
		}

		def xmlToVirtualInstrument(name: String): XmlParser[VirtualInstrument] = {
			branchNode(name) {
				for {
					lib <- xmlToString("virtual-library").maybe
					n <- xmlToString("virtual-name").maybe
				} yield VirtualInstrument(lib, n)
			}
		}

		def xmlToWork(name: String): XmlParser[Work] = {
			branchNode(name) {
				for {
					number <- xmlToString("work-number").maybe
					title <- xmlToString("work-title").maybe
					opus <- xmlToOpus("opus").maybe
				} yield Work(number, title, opus)
			}
		}
	}
}
