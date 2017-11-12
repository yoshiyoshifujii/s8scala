package com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.subscriber.sqs.users

import com.github.yoshiyoshifujii.s8scala.domain.user.UserCreatedEvent
import com.github.yoshiyoshifujii.s8scala.infrastructure.dynamodb.user.UserCreatedEventJsonProtocol
import com.github.yoshiyoshifujii.s8scala.infrastructure.sqs.{BaseSQSHandler, SQSFailure}
import spray.json.JsonReader

trait Base extends BaseSQSHandler {

  override protected type DataType = UserCreatedEvent

  protected def convertJsonReader: JsonReader[DataType] =
    UserCreatedEventJsonProtocol.userCreatedEventJsonFormatter

  protected def handle(data: DataType): Either[SQSFailure, Unit] = {
    Right(println(data))
  }

}
