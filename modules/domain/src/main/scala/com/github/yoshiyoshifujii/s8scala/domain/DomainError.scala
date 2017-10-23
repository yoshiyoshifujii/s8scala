package com.github.yoshiyoshifujii.s8scala.domain

import scala.util.Try

sealed trait DomainError {
  val message: String
}

case class AssertError(message: String)                      extends DomainError
case class AlreadyExists(message: String = "already_exists") extends DomainError

object DomainErrorConverters {

  implicit class Try2DomainError[E](val t: Try[E]) extends AnyVal {
    def toDomainError: Either[DomainError, E] =
      t.fold(
        {
          case e: AssertionError =>
            Left(AssertError(e.getMessage.replaceFirst("assertion failed: ", "")))
        },
        Right(_)
      )
  }

}
