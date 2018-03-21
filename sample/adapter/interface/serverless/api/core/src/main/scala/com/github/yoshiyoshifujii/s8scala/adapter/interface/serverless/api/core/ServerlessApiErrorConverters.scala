package com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.core

import com.github.yoshiyoshifujii.s8scala.application._
import com.github.yoshiyoshifujii.s8scala.infrastructure.lambda._

object ServerlessApiErrorConverters {

  implicit class ApplicationError2ServerlessError[E](val e: Either[ApplicationError, E])
      extends AnyVal {
    def toServerlessError: Either[ResponseJson, E] =
      e.fold(
        {
          case _: NoContentsError         => Left(NoContentJson())
          case BadRequestError(m)         => Left(BadRequestJson(m))
          case _: NotFoundError           => Left(NotFoundJson())
          case _: ConflictError           => Left(ConflictJson())
          case InternalServerError(cause) => throw cause
        },
        Right(_)
      )
  }

  implicit class Either2ResponseJson(val e: Either[ResponseJson, ResponseJson]) extends AnyVal {
    def get: ResponseJson = e.fold(identity, identity)
  }

}
