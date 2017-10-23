package com.github.yoshiyoshifujii.s8scala.domain.user

import com.github.yoshiyoshifujii.s8scala.domain.Repository

trait UserRepository extends Repository {
  override type ID = UserId
  override type ENTITY = User
}
