package com.github.yoshiyoshifujii.s8scala.domain

import scala.util.Try

case class DomainError(message: String)

object DomainErrorConverters {

  implicit class Try2DomainError[E](val t: Try[E]) extends AnyVal {
    def toDomainError: Either[DomainError, E] =
      t.fold(
        {
          case e: AssertionError =>
            Left(DomainError(e.getMessage.replaceFirst("assertion failed: ", "")))
          case e: Throwable =>
            Left(DomainError(Option(e).map(_.getMessage).getOrElse(s"${e.getClass.toString}")))
        },
        Right(_)
      )
  }

}
