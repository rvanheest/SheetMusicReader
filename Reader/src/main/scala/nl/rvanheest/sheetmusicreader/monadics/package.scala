package nl.rvanheest.sheetmusicreader

import scala.util.{Failure, Success, Try}

package object monadics {

	implicit object OptionIsMonadPlus extends MonadPlus[Option] {
		def empty[A]: Option[A] = None

		def fail[A](e: Throwable): Option[A] = None

		def create[A](a: A): Option[A] = Some(a)

		def orElse[A, B >: A](alt1: Option[A], alt2: => Option[B]): Option[B] = {
			alt1.orElse(alt2)
		}

		def map[A, B](option: Option[A])(f: A => B): Option[B] = {
			option.map(f)
		}

		def flatMap[A, B](option: Option[A])(f: A => Option[B]): Option[B] = {
			option.flatMap(f)
		}
	}

	implicit object TryIsMonadPlus extends MonadPlus[Try] {
		def empty[A]: Try[A] = Failure(new NoSuchElementException("empty"))

		def fail[A](e: Throwable): Try[A] = Failure(e)

		def create[A](a: A): Try[A] = Success(a)

		def orElse[A, B >: A](self: Try[A], other: => Try[B]): Try[B] = {
			self.orElse(other)
		}

		def map[A, B](self: Try[A])(f: A => B): Try[B] = {
			self.map(f)
		}

		def flatMap[A, B](self: Try[A])(f: A => Try[B]): Try[B] = {
			self.flatMap(f)
		}
	}
}
