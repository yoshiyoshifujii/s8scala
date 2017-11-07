package com.github.yoshiyoshifujii.s8scala.domain.user

import scala.util.Try

trait UserPublisher {
  def publish(userCreatedEvent: UserCreatedEvent): Try[String]
}
