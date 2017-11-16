package com.github.yoshiyoshifujii.s8scala.infrastructure.kinesis.user

import com.github.yoshiyoshifujii.s8scala.domain.user.UserCreatedEvent
import spray.json.DefaultJsonProtocol

object UserCreatedEventJsonProtocol extends DefaultJsonProtocol {
  implicit val userCreatedEventJsonFormatter = jsonFormat4(UserCreatedEvent.apply)
}
