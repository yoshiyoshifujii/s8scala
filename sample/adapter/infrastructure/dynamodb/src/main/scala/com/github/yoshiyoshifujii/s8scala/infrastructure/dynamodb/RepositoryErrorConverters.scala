package com.github.yoshiyoshifujii.s8scala.infrastructure.dynamodb

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException
import com.github.yoshiyoshifujii.s8scala.domain.{
  RepositoryError,
  RepositoryNotFoundError,
  RepositoryOptimisticError,
  RepositorySystemError
}

import scala.util.Try

object RepositoryErrorConverters {

  implicit class Try2RepositoryError[E](val t: Try[E]) extends AnyVal {
    def toRepositoryError: Either[RepositoryError, E] =
      t.fold(
        {
          case _: ConditionalCheckFailedException => Left(RepositoryOptimisticError())
          case e                                  => Left(RepositorySystemError(e))
        },
        Right(_)
      )

  }
}
