package com.github.yoshiyoshifujii.s8scala.infrastructure.lambda

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import spray.json._

import scala.util.Try

trait BaseStreamHandler extends RequestStreamHandler {

  protected def handle(requestJson: RequestJson): ResponseJson

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

    def fail(cause: Throwable): Unit = {
      (for {
        errorJson <- toJson(InternalServerError())
        errorBytes <- toBytes(errorJson)
      } yield output.write(errorBytes)).get
      // log.error(cause)
    }

    (for {
      bytes <- toByteArray(input)
      requestJson <- convert(bytes)
      responseJsonString <- toJson(handle(requestJson))
      responseBytes <- toBytes(responseJsonString)
    } yield output.write(responseBytes)).fold(
      fail,
      identity
    )
  }
}
