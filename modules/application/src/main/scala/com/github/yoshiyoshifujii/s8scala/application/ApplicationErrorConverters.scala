package com.github.yoshiyoshifujii.s8scala.application

import com.github.yoshiyoshifujii.s8scala.domain._

import scala.util.Try

object ApplicationErrorConverters {

  implicit class DomainError2ApplicationError[E](val e: Either[DomainError, E]) extends AnyVal {
    def toApplicationError: Either[ApplicationError, E] =
      e.fold(
        l => Left(BadRequestError(l.message)),
        Right(_)
      )
  }

  implicit class RepositoryError2ApplicationError[E](val e: Either[RepositoryError, E])
      extends AnyVal {
    def toApplicationError: Either[ApplicationError, E] =
      e.fold(
        {
          case _: RepositoryOptimisticError => Left(ConflictError)
          case _: RepositoryNotFoundError   => Left(NotFoundError)
          case _: RepositoryAlreadyExistsError =>
            Left(BadRequestError("already_exists"))
          case RepositorySystemError(t) => Left(InternalServerError(t))
        },
        Right(_)
      )
  }

  implicit class Try2ApplicationError[E](val e: Try[E]) extends AnyVal {
    def toApplicationError: Either[ApplicationError, E] =
      e.fold(
        e => Left(InternalServerError(e)),
        Right(_)
      )
  }
}
