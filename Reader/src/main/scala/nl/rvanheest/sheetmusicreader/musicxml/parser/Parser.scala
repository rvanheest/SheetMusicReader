package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.monadics.{MonadPlus, StateT}

trait Parser {

	object Parser {
		def apply[S, A, M[+_]](parser: S => M[(A, S)])(implicit m: MonadPlus[M]): StateT[S, A, M] = {
			StateT(parser)
		}

		def from[S, A, M[+_]](a: A)(implicit m: MonadPlus[M]): StateT[S, A, M] = {
			Parser(m.create(a, _))
		}

		def failure[S, A, M[+_]](implicit m: MonadPlus[M]): StateT[S, A, M] = {
			Parser(_ => m.empty)
		}
	}
}
