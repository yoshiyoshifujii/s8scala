package com.github.yoshiyoshifujii.s8scala.application.service.user

import com.github.yoshiyoshifujii.s8scala.application.ApplicationError
import com.github.yoshiyoshifujii.s8scala.domain.user.{User, UserPublisher, UserRepository}

trait UserService {
  import com.github.yoshiyoshifujii.s8scala.application.ApplicationErrorConverters._

  protected val userRepository: UserRepository
  protected val userPublisher: UserPublisher

  case class UserCreateInput(name: String, email: String)
  case class UserCreateOutput(id: String, version: Long)
  def create(input: UserCreateInput): Either[ApplicationError, UserCreateOutput] =
    for {
      user  <- User.create(input.name, input.email).toApplicationError
      saved <- userRepository.save(user).toApplicationError
      _     <- userPublisher.publish(saved.toCreatedEvent).toApplicationError
    } yield
      UserCreateOutput(
        id = saved.id.value,
        version = saved.version.getOrElse(1L)
      )
}
