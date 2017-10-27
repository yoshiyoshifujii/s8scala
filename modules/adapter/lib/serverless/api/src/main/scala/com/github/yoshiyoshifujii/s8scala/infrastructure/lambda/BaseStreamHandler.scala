package com.github.yoshiyoshifujii.s8scala.infrastructure.lambda

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.github.yoshiyoshifujii.s8scala.infrastructure.logging.LambdaLogger
import spray.json._

import scala.util.Try

trait BaseStreamHandler extends RequestStreamHandler {

  protected def handle(requestJson: RequestJson): ResponseJson

  protected val logger = LambdaLogger()

  @throws(classOf[java.io.IOException])
  override def handleRequest(input: InputStream,
                             output: OutputStream,
                             context: Context): Unit = {

    def toByteArray(input: InputStream) = Try {
      Stream.continually(input.read).takeWhile(_ != -1).map(_.toByte).toArray
    }

    def convert(bytes: Array[Byte]): Try[RequestJson] = Try {
      import RequestJsonProtocol._
      JsonParser(bytes).convertTo[RequestJson]
    }

    def toJson(responseJson: ResponseJson): Try[String] = Try {
      import ResponseJsonProtocol._
      responseJson.toJson.compactPrint
    }

    def toBytes(jsonString: String): Try[Array[Byte]] = Try {
      jsonString.getBytes(StandardCharsets.UTF_8)
    }

    def except(cause: Throwable): Unit = {
      logger.error(cause.getMessage, cause)
      (for {
        errorJson <- toJson(InternalServerError())
        errorBytes <- toBytes(errorJson)
      } yield output.write(errorBytes)).get
    }

    (for {
      bytes <- toByteArray(input)
      requestJson <- convert(bytes)
      responseJson <- Try(handle(requestJson))
      responseJsonString <- toJson(responseJson)
      responseBytes <- toBytes(responseJsonString)
    } yield output.write(responseBytes)).fold(
      except,
      identity
    )
  }
}
