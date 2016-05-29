package nl.rvanheest.sheetmusicreader.test.parser

import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexCommon.{AccidentalText, AccidentalTextNameDisplayChoice, NameDisplay}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexDirection.{MeasureNumbering, Print}
import nl.rvanheest.sheetmusicreader.musicxml.model.Complex.ComplexLayout.MeasureLayout
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupLayout.Layout
import nl.rvanheest.sheetmusicreader.musicxml.model.Group.GroupScore.PrintChoice
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeDirection.MNV_None
import nl.rvanheest.sheetmusicreader.musicxml.model.Primatives.PrimativeNote.{AccidentalValue_Flat, AccidentalValue_Sharp}

object PrintMusicDataObjects {
	val print0 = PrintChoice(Print(
		Layout(),
		Option(MeasureLayout(Option(12))),
		Option(MeasureNumbering(MNV_None)),
		Option(NameDisplay(List(AccidentalTextNameDisplayChoice(AccidentalText(AccidentalValue_Sharp))))),
		Option(NameDisplay(List(AccidentalTextNameDisplayChoice(AccidentalText(AccidentalValue_Flat)))))))

	val print1 = PrintChoice()
}
