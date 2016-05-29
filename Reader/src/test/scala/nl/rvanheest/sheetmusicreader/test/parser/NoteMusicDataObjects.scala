package nl.rvanheest.sheetmusicreader.test.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.AttributeGroups.AttributeGroupsCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexNote._
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupCommon.{Editorial, EditorialVoice, Staff}
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupNote._
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.NoteChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeNote._

object NoteMusicDataObjects {
	val note0 = NoteChoice(Note(
		noteChoice = GraceNoteChoice(
			grace = Grace(
				stealTimePrevious = Option(Percent(10)),
				stealTimeFollowing = Option(Percent(20)),
				makeTime = Option(30),
				slash = Option(YN_No)),
			fullNote = FullNote(
				chord = Option(()),
				choice = PitchChoice(Pitch(step = Step_A, octave = Octave(1)))
			),
			tie = List(Tie(SS_Start, Option(TimeOnly("6"))), Tie(SS_Stop))),
		instrument = Option("P1-I1"),
		editorialVoice = EditorialVoice(
			footnote = Option(FormattedText("footnote 1")),
			level = Option(Level("level 1", Option(YN_No))),
			voice = Option("voice 1")),
		noteType = Option(NoteType(NoteTypeValue_Eighth, Option(SymbolSize_Full))),
		dot = List(EmptyPlacement(placement = Placement(Option(AB_Above))), EmptyPlacement()),
		accidental = Option(Accidental(AccidentalValue_SlashFlat, Option(YN_No), Option(YN_Yes))),
		timeModification = Option(TimeModification(1, 2,
			Option((NoteTypeValue_Quarter, List((), ()))))),
		stem = Option(Stem(StemValue_Down, YPosition(Option(10)))),
		notehead = Option(NoteHead(NoteHeadValue_Cross, Option(YN_Yes), Option(YN_No))),
		noteheadText = Option(NoteHeadText(List(
			DisplayTextChoice(FormattedText("display text 1")),
			AccidentalTextChoice(AccidentalText(AccidentalValue_Sharp)),
			AccidentalTextChoice(AccidentalText(AccidentalValue_Flat)),
			DisplayTextChoice(FormattedText("display text 2"))))),
		staff = Option(Staff(12)),
		beam = List(Beam(BeamValue_Continue, fan = Option(Fan_Rit)), Beam(BeamValue_Begin)),
		notations = List(
			Notations(
				Editorial(Option(FormattedText("notation footnote"))),
				List(
					TiedNotation(Tied(
						SSC_Continue,
						Option(NumberLevel(2)),
						LineType(Option(LineType_Dotted)))),
					SlurNotation(Slur(SSC_Start, dashedFormatting = DashedFormatting(Option(3)))),
					TupletNotation(Tuplet(
						Option(TupletPortion(
							Option(TupletNumber(4)),
							Option(TupletType(NoteTypeValue_nd32)),
							List(TupletDot(), TupletDot(), TupletDot()))),
						Option(TupletPortion(tupletType = Option(TupletType(NoteTypeValue_th16)))),
						SS_Start,
						bracket = Option(YN_Yes),
						showType = Option(ShowTuplet_Both))),
					TupletNotation(Tuplet(ssType = SS_Stop, showNumber = Option(ShowTuplet_Actual))),
					GlissandoNotation(Glissando("gliss.", SS_Start)),
					SlideNotation(Slide(
						"slide.",
						SS_Stop,
						NumberLevel(4),
						bendSound = BendSound(beats = Option(TrillBeats(3.14))))),
					OrnamentsNotation(Ornaments(List(
						OrnamentsContent(TrillMarkOrnamentsChoice(
							EmptyTrillSound(placement = Placement(Option(AB_Above))))),
						OrnamentsContent(TurnOrnamentsChoice(HorizontalTurn(slash = Option(YN_No)))),
						OrnamentsContent(DelayedTurnOrnamentsChoice()),
						OrnamentsContent(InvertedTurnOrnamentsChoice()),
						OrnamentsContent(DelayedInvertedTurnedOrnamentsChoice()),
						OrnamentsContent(VerticalTurnOrnamentsChoice(EmptyTrillSound(
							trillSound = TrillSound(
								startNote = Option(StartNote_Main),
								twoNoteTurn = Option(TwoNoteTurn_Whole),
								accelerate = Option(YN_Yes))))),
						OrnamentsContent(ShakeOrnamentsChoice()),
						OrnamentsContent(WavyLineOrnamentsChoice(WavyLine(
							SSC_Stop,
							Option(NumberLevel(5)),
							placement = Placement(Option(AB_Above))))),
						OrnamentsContent(MordentOrnamentsChoice(Mordent(
							EmptyTrillSound(placement = Placement(Option(AB_Above))),
							Option(YN_No),
							Option(AB_Above),
							Option(AB_Below)))),
						OrnamentsContent(
							InvertedMordentOrnamentsChoice(Mordent(long = Option(YN_Yes))),
							List(AccidentalMark(AccidentalValue_Natural), AccidentalMark(AccidentalValue_Sharp))),
						OrnamentsContent(SchleiferOrnamentsChoice()),
						OrnamentsContent(TremoloOrnamentsChoice(Tremolo(TremoloMarks(8), SSS_Start))),
						OrnamentsContent(TremoloOrnamentsChoice(Tremolo(TremoloMarks(7)))),
						OrnamentsContent(
							OtherOrnamentOrnamentsChoice(PlacementText("first other ornament")),
							List(AccidentalMark(AccidentalValue_Flat))),
						OrnamentsContent(OtherOrnamentOrnamentsChoice(PlacementText("second other ornament")))))),
					OrnamentsNotation(),
					TechnicalNotation(Technical(List(
						UpBowTechnicalChoice(EmptyPlacement(placement = Placement(Option(AB_Below)))),
						UpBowTechnicalChoice(EmptyPlacement(PrintStyle(Position(Option(5))))),
						DownBowTechnicalChoice(),
						HarmonicTechnicalChoice(Harmonic(Option(Natural(())), Option(BasePitch(())))),
						HarmonicTechnicalChoice(Harmonic(
							Option(Artificial(())),
							Option(TouchingPitch(())))),
						HarmonicTechnicalChoice(Harmonic(harmonicPitchChoice = Option(SoundingPitch(())))),
						HarmonicTechnicalChoice(),
						OpenStringTechnicalChoice(),
						ThumbPositionTechnicalChoice(),
						FingeringTechnicalChoice(Fingering(
							"finger ring",
							Option(YN_Yes),
							Option(YN_No),
							placement = Placement(Option(AB_Below)))),
						PluckTechnicalChoice(PlacementText("plukjes")),
						DoubleTongueTechnicalChoice(),
						TripleTongueTechnicalChoice(),
						StoppedTechnicalChoice(),
						SnapPizzicatoTechnicalChoice(),
						FretTechnicalChoice(Fret(21)),
						StringTechnicalChoice(StringClass(
							StringNumber(3),
							placement = Placement(Option(AB_Above)))),
						HammerOnTechnicalChoice(HammerOnPullOff("hammer 1", SS_Start)),
						PullOffTechnicalChoice(HammerOnPullOff("hammer 2", SS_Stop, NumberLevel(4))),
						BendTechnicalChoice(Bend(
							3.14,
							Option(PreBand(())),
							Option(PlacementText("met bar")),
							bendSound = BendSound(beats = Option(TrillBeats(2))))),
						BendTechnicalChoice(Bend(
							1.4142,
							Option(Release(())),
							bendSound = BendSound(beats = Option(TrillBeats(5))))),
						TapTechnicalChoice(PlacementText("tapjes")),
						HeelTechnicalChoice(HeelToe(substitution = Option(YN_No))),
						ToeTechnicalChoice(),
						FingernailsTechnicalChoice(),
						HoleTechnicalChoice(Hole(
							Option("type"),
							HoleClosed(HoleClosedValue_Half, Option(HoleClosedLocation_Bottom)),
							Option("shape"))),
						HoleTechnicalChoice(Hole(holeClosed = HoleClosed(HoleClosedValue_No))),
						ArrowTechnicalChoice(Arrow(StraightArrowChoice(
							ArrowDirection_Right,
							Option(ArrowStyle_Filled)))),
						ArrowTechnicalChoice(Arrow(CircleArrowChoice(CircularArrow_Anticlockwise))),
						HandbellTechnicalChoice(Handbell(HandbellValue_Echo)),
						OtherTechnicalChoice(PlacementText("other"))))),
					TechnicalNotation(),
					ArticulationsNotation(Articulations(List(
						AccentChoice(),
						StrongAccentChoice(StrongAccent(udType = UD_Down)),
						StrongAccentChoice(),
						StaccatoChoice(),
						TenutoChoice(),
						DetachedLegatoChoice(),
						StaccatissimoChoice(),
						SpiccatoChoice(),
						ScoopChoice(EmptyLine(
							LineShape(Option(LineShape_Curved)),
							LineType(Option(LineType_Dashed)))),
						PlopChoice(EmptyLine(dashedFormatting = DashedFormatting(
							Option(5),
							Option(4)))),
						DoitChoice(EmptyLine(dashedFormatting = DashedFormatting(
							spaceLength = Option(3)))),
						FalloffChoice(),
						BreathMarkChoice(BreathMark(BreathMarkValue_Comma)),
						CaesuraChoice(),
						StressChoice(),
						UnstressChoice(),
						OtherArticulationChoice(PlacementText("other articulation"))))),
					ArticulationsNotation(),
					DynamicsNotation(Dynamics(List(
						DynamicSymbolChoice(DynamicSymbols.p),
						DynamicSymbolChoice(DynamicSymbols.pp),
						DynamicSymbolChoice(DynamicSymbols.ppp),
						DynamicSymbolChoice(DynamicSymbols.pppp),
						DynamicSymbolChoice(DynamicSymbols.ppppp),
						DynamicSymbolChoice(DynamicSymbols.pppppp),
						DynamicSymbolChoice(DynamicSymbols.f),
						DynamicSymbolChoice(DynamicSymbols.ff),
						DynamicSymbolChoice(DynamicSymbols.fff),
						DynamicSymbolChoice(DynamicSymbols.ffff),
						DynamicSymbolChoice(DynamicSymbols.fffff),
						DynamicSymbolChoice(DynamicSymbols.ffffff),
						DynamicSymbolChoice(DynamicSymbols.mp),
						DynamicSymbolChoice(DynamicSymbols.mf),
						DynamicSymbolChoice(DynamicSymbols.sf),
						DynamicSymbolChoice(DynamicSymbols.sfp),
						DynamicSymbolChoice(DynamicSymbols.sfpp),
						DynamicSymbolChoice(DynamicSymbols.fp),
						DynamicSymbolChoice(DynamicSymbols.rf),
						DynamicSymbolChoice(DynamicSymbols.rfz),
						DynamicSymbolChoice(DynamicSymbols.sfz),
						DynamicSymbolChoice(DynamicSymbols.sffz),
						DynamicSymbolChoice(DynamicSymbols.fz),
						DynamicStringChoice("other dynamic")),
						textDecoration = TextDecoration(
							Option(NumberOfLines(2)),
							Option(NumberOfLines(1)),
							Option(NumberOfLines(3))),
						enclosure = Enclosure(Option(EnclosureShape_Bracket)))),
					DynamicsNotation(),
					FermataNotation(Fermata(FermataShape_Normal, Option(UI_Inverted))),
					FermataNotation(Fermata(FermataShape_Empty)),
					ArpeggiateNotation(Arpeggiate(
						Option(NumberLevel(2)),
						Option(UD_Up),
						placement = Placement(Option(AB_Above)))),
					ArpeggiateNotation(),
					NonArpeggiateNotation(NonArpeggiate(TB_Bottom, Option(NumberLevel(6)))),
					NonArpeggiateNotation(NonArpeggiate(TB_Top)),
					AccidentalMarkNotation(AccidentalMark(AccidentalValue_Flat)),
					OtherNotationNotation(OtherNotation(
						"other notation 1",
						SSS_Stop,
						NumberLevel(3))),
					OtherNotationNotation(OtherNotation("other notation 2", SSS_Single))
				)),
			Notations(notations = List(TiedNotation(Tied(SSC_Start)))),
			Notations(Editorial(level = Option(Level("notation level"))))),
		lyric = List(
			Lyric(
				choice = TextLyricsChoice(
					syllabic = Option(Syllabic_Single),
					text = TextElementData(
						"lyric 1",
						textDecoration = TextDecoration(Option(NumberOfLines(1))),
						textRotation = TextRotation(Option(RotationDegrees(30)))),
					est = List(
						ElisionSyllabicText(
							Option(ElisionSyllabic(TextFontColor("elision 1"), Option(Syllabic_Begin))),
							TextElementData("lyric 2")),
						ElisionSyllabicText(
							Option(ElisionSyllabic(TextFontColor("elision 2"))),
							TextElementData("lyric 3")),
						ElisionSyllabicText(text = TextElementData("lyric 4"))
					),
					extend = Option(Extend(Option(SSC_Start)))
				),
				endLine = Option(()),
				endParagraph = Option(()),
				editorial = Editorial(
					Option(FormattedText("lyric footnote")),
					Option(Level(
						"lyric level",
						Option(YN_No),
						LevelDisplay(Option(YN_No), Option(YN_No), Option(SymbolSize_Cue)))))
			),
			Lyric(TextLyricsChoice(
				text = TextElementData("lyric 5"),
				extend = Option(Extend(Option(SSC_Continue))))),
			Lyric(ExtendLyricsChoice(Extend(Option(SSC_Stop)))),
			Lyric(
				LaughingLyricsChoice(()),
				number = Option("11"),
				name = Option("laughing lyric"),
				justify = Justify(Option(LCR_Left)),
				placement = Placement(Option(AB_Below)),
				printObject = PrintObject(Option(YN_No))),
			Lyric(HummingLyricsChoice(()))
		),
		play = Option(Play(id = Option("P1-I1")))
	))

	val note1 = NoteChoice(Note(
		noteChoice = CueNoteChoice(
			(),
			FullNote(choice = UnpitchedChoice(Unpitched(Option(DisplayStepOctave(Step_A, Octave(4)))))),
			PositiveDivisions(3)),
		dot = List(EmptyPlacement()),
		accidental = Option(Accidental(AccidentalValue_Flat)),
		timeModification = Option(TimeModification(3, 4, Option((NoteTypeValue_Half, List())))),
		stem = Option(Stem(StemValue_Double)),
		notehead = Option(NoteHead(NoteHeadValue_X)),
		noteheadText = Option(NoteHeadText(List(
			AccidentalTextChoice(AccidentalText(AccidentalValue_Flat))))),
		beam = List(Beam(BeamValue_End, BeamLevel(2)))))

	val note2 = NoteChoice(Note(
		noteChoice = FullNoteChoice(
			fullNote = FullNote(choice = RestChoice(Rest(Option(DisplayStepOctave(Step_B, Octave(1)))))),
			duration = PositiveDivisions(2)),
		timeModification = Option(TimeModification(5, 6))))

	val note3 = NoteChoice(Note(
		noteChoice = FullNoteChoice(
			FullNote(choice = RestChoice(Rest(measure = Option(YN_Yes)))),
			PositiveDivisions(1.0)),
		dynamics = Option(NonNegativeDecimal(3.14)),
		endDynamics = Option(NonNegativeDecimal(1.4142)),
		attack = Option(5),
		release = Option(4),
		timeOnly = Option(TimeOnly("1")),
		pizzicato = Option(YN_Yes)))
}
