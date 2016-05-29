package nl.rvanheest.sheetmusicreader.test.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.TypedText
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexIdentity._
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeCommon.{Date, YN_No, YN_Yes}

object IdentificationObjects {
	val encoding = Encoding(List(
		EncodingSoftware("Software 1"),
		EncodingSoftware("Software 2"),
		EncodingDate(Date("2011-08-08")),
		EncodingDate(Date("30 juli 1992")),
		EncodingEncoder(TypedText("encoder text 1", Option("foo"))),
		EncodingEncoder(TypedText("encoder text 2", Option("bar"))),
		EncodingDescription("description 1"),
		EncodingDescription("description 2"),
		EncodingSupports(Supports(YN_Yes, "print", Option("attribute1"), Option("test"))),
		EncodingSupports(Supports(YN_No, "print test"))))

	val miscellaneous = Miscellaneous(List(
		MiscellaneousField("field1", "test1"),
		MiscellaneousField("field2", "test2"),
		MiscellaneousField("", "test3")))

	val creator = List(TypedText("general creator", Option.empty),
		TypedText("bewerker", Option("bewerking")),
		TypedText("tekstschrijver", Option("tekst")),
		TypedText(""))

	val rights = List(TypedText("Copyright © 2010", Option("words")),
		TypedText("Some more copyright", Option("music")))

	val source = Option("sourceText")

	val relation = List(TypedText("foo", Option("relation1")),
		TypedText("bar", Option("relation2")),
		TypedText("", Option("relation3")))

	val identification = Identification(creator, rights, Option(encoding),
		source, relation, Option(miscellaneous))
}
