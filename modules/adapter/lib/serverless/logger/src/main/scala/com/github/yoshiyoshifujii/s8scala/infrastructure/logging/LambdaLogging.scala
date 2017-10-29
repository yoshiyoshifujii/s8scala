package com.github.yoshiyoshifujii.s8scala.infrastructure.logging

import com.typesafe.scalalogging.LazyLogging
import spray.json._

trait LambdaLogging extends LazyLogging {
  private val JsonKeysMessage    = "message"
  private val JsonKeysType       = "type"
  private val JsonKeysStackTrace = "stackTrace"

  private def parse(message: String) =
    Option(message) map { m =>
      try {
        JsonParser(m)
      } catch {
        case _: JsonParser.ParsingException =>
          JsString(m)
      }
    } getOrElse {
      JsNull
    }

  protected def formatJson(message: String) =
    JsObject(JsonKeysMessage -> parse(message))

  private lazy val format =
    (message: String) => formatJson(message).compactPrint

  protected def format2Json(message: String, cause: Throwable): JsObject = {

    lazy val cause2Type: Throwable => JsValue =
      Option(_).map(t => parse(t.toString)).getOrElse(JsNull)

    lazy val cause2StackTrace: Throwable => JsValue =
      Option(_).map { t =>
        JsArray(
          t.getStackTrace
            .map(st => parse(st.toString)): _*)
      } getOrElse JsNull

    JsObject(
      JsonKeysMessage    -> parse(message),
      JsonKeysType       -> cause2Type(cause),
      JsonKeysStackTrace -> cause2StackTrace(cause)
    )
  }

  private lazy val format2 =
    (message: String) => (cause: Throwable) => format2Json(message, cause).compactPrint

  def error(message: String): Unit = logger.error(format(message))
  def error(message: String, cause: Throwable): Unit =
    logger.error(format2(message)(cause))
  def warn(message: String): Unit = logger.warn(format(message))
  def warn(message: String, cause: Throwable): Unit =
    logger.warn(format2(message)(cause))
  def info(message: String): Unit = logger.info(format(message))
  def info(message: String, cause: Throwable): Unit =
    logger.info(format2(message)(cause))
  def debug(message: String): Unit = logger.debug(format(message))
  def debug(message: String, cause: Throwable): Unit =
    logger.debug(format2(message)(cause))
  def trace(message: String): Unit = logger.trace(format(message))
  def trace(message: String, cause: Throwable): Unit =
    logger.trace(format2(message)(cause))
}

case class LambdaLogger() extends LambdaLogging
