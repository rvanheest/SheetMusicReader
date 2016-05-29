package nl.rvanheest.sheetmusicreader.test.parser

import java.io.File
import java.net.URI
import javax.xml.parsers.SAXParserFactory

import nl.rvanheest.sheetmusicreader.musicxml.model.Root.ScorePartwise
import nl.rvanheest.sheetmusicreader.parser.MusicXmlParser
import org.junit.Assert.assertEquals
import org.junit.{BeforeClass, Test}

import scala.xml.{Utility, XML}

class MusicXMLParserIntegrationTest {

	import AttributeMusicDataObjects._
	import BackupMusicDataObjects._
	import BarlineMusicDataObjects._
	import BookmarkMusicDataObjects._
	import DirectionMusicDataObjects._
	import FiguredBassMusicDataObject._
	import ForwardMusicDataObjects._
	import GroupingMusicDataObjects._
	import HarmonyMusicDataObjects._
	import HeaderObjects._
	import IdentificationObjects._
	import LinkMusicDataObjects._
	import Measure1Objects._
	import MusicXMLParserIntegrationTest.score
	import NoteMusicDataObjects._
	import PrintMusicDataObjects._
	import ScoreObjects._
	import SoundMusicDataObjects._

	@Test
	def testFullScore() = {
		assertEquals(scorePartwise, score)
	}

	@Test
	def testScoreHeader() = {
		assertEquals(header, score.header)
	}

	@Test
	def testWork() = {
		assertEquals(work, score.header.work.get)
	}

	@Test
	def testMovementNumber() = {
		assertEquals(movementNumber, score.header.movementNumber.get)
	}

	@Test
	def testMovementTitle() = {
		assertEquals(movementTitle, score.header.movementTitle.get)
	}

	@Test
	def testIdentification() = {
		assertEquals(identification, score.header.identification.get)
	}

	@Test
	def testDefaults() = {
		assertEquals(defaults, score.header.defaults.get)
	}

	@Test
	def testCredit() = {
		assertEquals(credits, score.header.credit)
	}

	@Test
	def testPartList() = {
		assertEquals(partList, score.header.partList)
	}

	@Test
	def testParts() = {
		assertEquals(parts, score.part)
	}

	@Test
	def testMeasure1_1() = {
		assertEquals(measure1_1, score.part.head.measure.head)
	}

	@Test
	def testM1_1Print0() = {
		assertEquals(print0, score.part.head.measure.head.musicData.data.head)
	}

	@Test
	def testM1_1Print1() = {
		assertEquals(print1, score.part.head.measure.head.musicData.data(1))
	}

	@Test
	def testM1_1Attr0() = {
		assertEquals(attr0, score.part.head.measure.head.musicData.data(2))
	}

	@Test
	def testM1_1Attr1() = {
		assertEquals(attr1, score.part.head.measure.head.musicData.data(3))
	}

	@Test
	def testM1_1Harm0() = {
		assertEquals(harm0, score.part.head.measure.head.musicData.data(4))
	}

	@Test
	def testM1_1Harm1() = {
		assertEquals(harm1, score.part.head.measure.head.musicData.data(5))
	}

	@Test
	def testM1_1Harm2() = {
		assertEquals(harm2, score.part.head.measure.head.musicData.data(6))
	}

	@Test
	def testM1_1Backup0() = {
		assertEquals(backup0, score.part.head.measure.head.musicData.data(7))
	}

	@Test
	def testM1_1Backup1() = {
		assertEquals(backup1, score.part.head.measure.head.musicData.data(8))
	}

	@Test
	def testM1_1Forward0() = {
		assertEquals(forward0, score.part.head.measure.head.musicData.data(9))
	}

	@Test
	def testM1_1Forward1() = {
		assertEquals(forward1, score.part.head.measure.head.musicData.data(10))
	}

	@Test
	def testM1_1Forward2() = {
		assertEquals(forward2, score.part.head.measure.head.musicData.data(11))
	}

	@Test
	def testM1_1Direction0() = {
		assertEquals(direction0, score.part.head.measure.head.musicData.data(12))
	}

	@Test
	def testM1_1Direction1() = {
		assertEquals(direction1, score.part.head.measure.head.musicData.data(13))
	}

	@Test
	def testM1_1Direction2() = {
		assertEquals(direction2, score.part.head.measure.head.musicData.data(14))
	}

	@Test
	def testM1_1Note0() = {
		assertEquals(note0, score.part.head.measure.head.musicData.data(15))
	}

	@Test
	def testM1_1Note1() = {
		assertEquals(note1, score.part.head.measure.head.musicData.data(16))
	}

	@Test
	def testM1_1Note2() = {
		assertEquals(note2, score.part.head.measure.head.musicData.data(17))
	}

	@Test
	def testM1_1Note3() = {
		assertEquals(note3, score.part.head.measure.head.musicData.data(18))
	}

	@Test
	def testM1_1FiguredBass0(): Unit = {
		assertEquals(figuredBass0, score.part.head.measure.head.musicData.data(19))
	}

	@Test
	def testM1_1FiguredBass1(): Unit = {
		assertEquals(figuredBass1, score.part.head.measure.head.musicData.data(20))
	}

	@Test
	def testM1_1FiguredBass2(): Unit = {
		assertEquals(figuredBass2, score.part.head.measure.head.musicData.data(21))
	}

	@Test
	def testM1_1Sound(): Unit = {
		assertEquals(sound, score.part.head.measure.head.musicData.data(22))
	}

	@Test
	def testM1_1Barline0(): Unit = {
		assertEquals(barline0, score.part.head.measure.head.musicData.data(23))
	}

	@Test
	def testM1_1Barline1(): Unit = {
		assertEquals(barline1, score.part.head.measure.head.musicData.data(24))
	}

	@Test
	def testM1_1Grouping0(): Unit = {
		assertEquals(grouping0, score.part.head.measure.head.musicData.data(25))
	}

	@Test
	def testM1_1Grouping1(): Unit = {
		assertEquals(grouping1, score.part.head.measure.head.musicData.data(26))
	}

	@Test
	def testM1_1Link(): Unit = {
		assertEquals(link, score.part.head.measure.head.musicData.data(27))
	}

	@Test
	def testM1_1Bookmark(): Unit = {
		assertEquals(bookmark, score.part.head.measure.head.musicData.data(28))
	}

	@Test
	def testMeasure1_2(): Unit = {
		assertEquals(measure1_2, score.part.head.measure(1))
	}

	@Test
	def testDocumentAttributes() = {
		assertEquals(documentAttributes, score.documentAttributes)
	}
}

object MusicXMLParserIntegrationTest {

	var score: ScorePartwise = _

	@BeforeClass
	def setUpClass(): Unit = {
		val string = this.getClass.getResource("/Example.xml").toExternalForm
		val file = new File(URI.create(string))

		val parserFactory = SAXParserFactory.newInstance
		parserFactory.setNamespaceAware(false)
		parserFactory.setValidating(false)

		parserFactory.setFeature("http://xml.org/sax/features/validation", false)
		parserFactory.setFeature("http://xml.org/sax/features/validation", false)
		parserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
		parserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false)
		parserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)

		val parser = XML.withSAXParser(parserFactory.newSAXParser())
		val xml = Utility.trim(parser.loadFile(file))
		val p = new MusicXmlParser {}
		score = p.partwise.run(xml)
	}
}
