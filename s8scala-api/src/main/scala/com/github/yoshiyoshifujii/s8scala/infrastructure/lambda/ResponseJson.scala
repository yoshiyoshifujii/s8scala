package com.github.yoshiyoshifujii.s8scala.infrastructure.lambda

import com.github.yoshiyoshifujii.s8scala.infrastructure.lambda.HttpStatusCode.HttpStatusCode
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class ResponseJson(isBase64Encoded: Boolean,
                        statusCode: HttpStatusCode,
                        headers: Map[String, String],
                        body: Option[String])

object NoContentJson {
  def apply(): ResponseJson = ResponseJson(
    isBase64Encoded = false,
    statusCode = HttpStatusCode.NoContent,
    headers = Map("Content-Type" -> "application/json"),
    body = None
  )
}

object BadRequestJson {
  def apply(message: String): ResponseJson = ResponseJson(
    isBase64Encoded = false,
    statusCode = HttpStatusCode.BadRequest,
    headers = Map("Content-Type" -> "application/json"),
    body = Some(s"""{"message":"$message"}""")
  )
}

object NotFoundJson {
  def apply(): ResponseJson = ResponseJson(
    isBase64Encoded = false,
    statusCode = HttpStatusCode.NotFound,
    headers = Map("Content-Type" -> "application/json"),
    body = None
  )
}

object ConflictJson {
  def apply(): ResponseJson = ResponseJson(
    isBase64Encoded = false,
    statusCode = HttpStatusCode.Conflict,
    headers = Map("Content-Type" -> "application/json"),
    body = None
  )
}

object InternalServerErrorJson {
  def apply(): ResponseJson = ResponseJson(
    isBase64Encoded = false,
    statusCode = HttpStatusCode.InternalServerError,
    headers = Map("Content-Type" -> "application/json"),
    None
  )
}

object ResponseJsonConverters {

  implicit class Option2ResponseJson[E](val o: Either[ResponseJson, Option[E]]) extends AnyVal {
    def ifNoneThenBadRequest(message: String): Either[ResponseJson, E] =
      o.flatMap(_.toRight(BadRequestJson(message)))
  }

  implicit class E2ResponseJson[E](val e: E) extends AnyVal {
    def toOk(implicit converter: E => String): ResponseJson =
      ResponseJson(
        isBase64Encoded = false,
        statusCode = HttpStatusCode.OK,
        headers = Map("Content-Type" -> "application/json"),
        body = Some(converter(e))
      )
  }

}

object ResponseJsonProtocol extends DefaultJsonProtocol {
  implicit val responseJsonFormatter: RootJsonFormat[ResponseJson] =
    jsonFormat4(ResponseJson)
}
