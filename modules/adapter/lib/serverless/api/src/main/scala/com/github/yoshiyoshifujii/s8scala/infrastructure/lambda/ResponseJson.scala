package com.github.yoshiyoshifujii.s8scala.infrastructure.lambda

import com.github.yoshiyoshifujii.s8scala.infrastructure.lambda.HttpStatusCode.HttpStatusCode
import spray.json.DefaultJsonProtocol

case class ResponseJson(isBase64Encoded: Boolean,
                        statusCode: HttpStatusCode,
                        headers: Map[String, String],
                        body: Option[String])

object ResponseJsonProtocol extends DefaultJsonProtocol {
  implicit val responseJsonFormatter = jsonFormat4(ResponseJson)
}
