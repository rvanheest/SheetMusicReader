/**
 * Copyright (C) 2016 Richard van Heest (richard.v.heest@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.rvanheest.sheetmusicreader.musicxml.parser

import nl.rvanheest.sheetmusicreader.monadics.{MonadPlus, StateT}

trait ParserComponent {

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
