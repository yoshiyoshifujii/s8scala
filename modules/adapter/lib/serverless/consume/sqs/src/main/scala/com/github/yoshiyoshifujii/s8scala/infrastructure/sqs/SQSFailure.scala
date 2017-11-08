package com.github.yoshiyoshifujii.s8scala.infrastructure.sqs

import scala.util.Try

sealed trait SQSFailure
case class SkipSQSFailure(message: String) extends SQSFailure
case class RetrySQSFailure(cause: Throwable) extends SQSFailure

object SQSFailureConverters {
  implicit class Try2SQSFailure[E](val t: Try[E]) extends AnyVal {
    def toSkip: Either[SkipSQSFailure, E] =
      t.fold(
        e => Left(SkipSQSFailure(e.getMessage)),
        Right(_)
      )
  }
}
