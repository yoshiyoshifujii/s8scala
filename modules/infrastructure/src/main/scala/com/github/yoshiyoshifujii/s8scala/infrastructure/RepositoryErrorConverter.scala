package com.github.yoshiyoshifujii.s8scala.infrastructure

import com.github.yoshiyoshifujii.s8scala.domain.{
  RepositoryError,
  RepositorySystemError
}

import scala.util.Try

object RepositoryErrorConverter {

  implicit class Try2RepositoryError[E](val t: Try[E]) extends AnyVal {
    def toRepositoryError: Either[RepositoryError, E] =
      t.fold(
        e => Left(RepositorySystemError(e)),
        Right(_)
      )
  }

}
