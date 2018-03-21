package com.github.yoshiyoshifujii.s8scala.application.service.user

import com.github.yoshiyoshifujii.s8scala.application.{ApplicationError, Authorizer}
import com.github.yoshiyoshifujii.s8scala.domain.user.{User, UserRepository}

trait UserService {
  import com.github.yoshiyoshifujii.s8scala.application.ApplicationErrorConverters._

  protected val userRepository: UserRepository

  case class UserCreateInput(name: String, email: String)
  case class UserCreateOutput(id: String, version: Long)
  def create(authorizer: Authorizer,
             input: UserCreateInput): Either[ApplicationError, UserCreateOutput] =
    for {
      user  <- User.create(input.name, input.email).toApplicationError
      saved <- userRepository.save(user).toApplicationError
    } yield
      UserCreateOutput(
        id = saved.id.value,
        version = saved.version.getOrElse(1L)
      )
}
