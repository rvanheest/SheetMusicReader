package nl.rvanheest.sheetmusicreader.test.parser.integration

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexDirection.Sound
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.SoundChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon._

object SoundMusicDataObjects {

	val sound = SoundChoice(Sound(
		tempo = Option(NonNegativeDecimal(2.68)),
		dynamics = Option(NonNegativeDecimal(3.14)),
		dacapo = Option(YN_No),
		segno = Option("mySegno"),
		dalsegno = Option("myDalsegno"),
		coda = Option("myCoda"),
		tocoda = Option("myTocoda"),
		divisions = Option(-1.4142),
		forwardRepeat = Option(YN_Yes),
		fine = Option("myFine"),
		timeOnly = Option(TimeOnly("12569, 686923, 6694")),
		pizzicato = Option(YN_No),
		pan = Option(RotationDegrees(53)),
		elevation = Option(RotationDegrees(35)),
		damperPedal = Option(YNN_Double(4)),
		softPedal = Option(YNN_YesNo(YN_No)),
		sostenutoPedal = Option(YNN_YesNo(YN_Yes))
	))
}
