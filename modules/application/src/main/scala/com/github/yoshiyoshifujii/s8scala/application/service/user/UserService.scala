package com.github.yoshiyoshifujii.s8scala.application.service.user

import com.github.yoshiyoshifujii.s8scala.application.ApplicationError
import com.github.yoshiyoshifujii.s8scala.domain.user.{User, UserRepository}

trait UserService {
  import com.github.yoshiyoshifujii.s8scala.application.ApplicationErrorConverters._

  protected val userRepository: UserRepository

  def create(input: UserCreateInput): Either[ApplicationError, UserCreateOutput] =
    for {
      user <- User.create(input.name, input.email).toApplicationError
      saved <- userRepository.save(user).toApplicationError
    } yield UserCreateOutput(
      id = saved.id.value,
      version = saved.version.getOrElse(1L)
    )
}
