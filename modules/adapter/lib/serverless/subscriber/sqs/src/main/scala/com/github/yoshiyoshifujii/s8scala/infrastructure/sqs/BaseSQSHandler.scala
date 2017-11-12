package com.github.yoshiyoshifujii.s8scala.infrastructure.sqs

import com.amazonaws.services.sqs.model.Message
import com.github.yoshiyoshifujii.s8scala.infrastructure.logging.LambdaLogger
import spray.json._

import scala.util.Try

trait BaseSQSHandler extends SQSWrapper {

  private val logger = LambdaLogger()
  protected type DataType

  protected def convertJsonReader: JsonReader[DataType]
  protected def handle(data: DataType): Either[SQSFailure, Unit]

  def handler(event: Object): Unit = {
    import SQSFailureConverters._

    def convert(m: Message): Either[SkipSQSFailure, DataType] =
      Try {
        JsonParser(m.getBody).convertTo[DataType](convertJsonReader)
      }.toSkip

    def fail(e: Throwable): Unit = e match {
      case io: java.io.IOException => throw io
      case other: Throwable => logger.error(other.getMessage, other)
    }

    def convertFail(skip: SkipSQSFailure): Unit =
      logger.error(skip.message)

    def doSkip(m: Message, skipSQSFailure: SkipSQSFailure): Unit = {
      logger.error(skipSQSFailure.message)
      deleteMessage(m.getReceiptHandle).get
    }

    def doRetry(retrySQSFailure: RetrySQSFailure): Unit =
      throw new java.io.IOException(retrySQSFailure.cause)

    def success(m: Message): Unit =
      deleteMessage(m.getReceiptHandle).get

    (for {
      s <- approximateReceiveMessages
      _ <- Try {
        s.foreach { m =>
          convert(m).fold(
            convertFail,
            handle(_).fold(
              {
                case s: SkipSQSFailure => doSkip(m, s)
                case r: RetrySQSFailure => doRetry(r)
              },
              _ => success(m)
            ))
        }
      }
    } yield ()).fold(
      fail,
      identity
    )
  }

}

