package com.github.yoshiyoshifujii.s8scala.infrastructure.sqs

import com.amazonaws.services.sqs.model.Message
import com.github.yoshiyoshifujii.s8scala.infrastructure.logging.LambdaLogger
import spray.json._

import scala.util.{Failure, Success, Try}

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

    def success(m: Message, input: DataType): Unit = {
      logger.debug(s"success. $input")
      deleteMessage(m.getReceiptHandle).get
    }

    (for {
      s <- approximateReceiveMessages
      _ <- Try {
        s.foreach { m =>
          convert(m).fold(
            convertFail,
            r => handle(r).fold(
              {
                case skip: SkipSQSFailure =>
                  logger.error(skip.message)
                case retry: RetrySQSFailure =>
                  throw new java.io.IOException(retry.cause)
              },
              _ => success(m, r)
            ))
        }
      }
    } yield ()) match {
      case Failure(e) => fail(e)
      case Success(_) => ()
    }
  }

}

