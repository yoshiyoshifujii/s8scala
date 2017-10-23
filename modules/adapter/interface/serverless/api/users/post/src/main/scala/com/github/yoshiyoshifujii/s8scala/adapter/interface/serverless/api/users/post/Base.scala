package com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.post

import com.github.yoshiyoshifujii.s8scala.infrastructure.lambda.{
  BaseStreamHandler,
  HttpStatusCode,
  RequestJson,
  ResponseJson
}

trait Base extends BaseStreamHandler {
  override protected def handle(requestJson: RequestJson): ResponseJson = {
    println(requestJson)
    ResponseJson(
      isBase64Encoded = false,
      statusCode = HttpStatusCode.OK,
      headers = Map("Content-Type" -> "application/json"),
      body = Some("{}")
    )
  }
}
