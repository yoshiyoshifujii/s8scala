package com.github.yoshiyoshifujii.s8scala.api.users.post

import com.github.yoshiyoshifujii.s8scala.infrastructure.lambda.{BaseStreamHandler, RequestJson, ResponseJson}

trait Base extends BaseStreamHandler {
  override protected def handle(requestJson: RequestJson): ResponseJson = {
    ???
  }
}
