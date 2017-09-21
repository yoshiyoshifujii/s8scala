package com.github.yoshiyoshifujii.s8scala.infrastructure.lambda

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import spray.json.{JsValue, JsonParser}

import scala.util.Try

trait BaseStreamHandler extends RequestStreamHandler {

  private def toByteArray(input: InputStream) =
    Stream.continually(input.read).takeWhile(_ != -1).map(_.toByte).toArray

  private def convert(bytes: Array[Byte]): Try[JsValue] = Try {
    JsonParser(bytes)
  }

  @throws(classOf[java.io.IOException])
  override def handleRequest(input: InputStream,
                             output: OutputStream,
                             context: Context): Unit = {
    val bytes = toByteArray(input)
    ???
//    (for {
//      i <- convert(bytes)
//      res <- handle(i)
//    } yield res).fold(
//      e => fail(bytes, context, e),
//      s => output.write(s.getBytes("utf-8"))
//    )
  }
}
