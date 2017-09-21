package com.github.yoshiyoshifujii.s8scala.domain.user

import com.github.yoshiyoshifujii.s8scala.domain.Aggregate
import com.github.yoshiyoshifujii.s8scala.domain.common.Email

import scala.reflect.{classTag, ClassTag}

case class User(id: UserId, version: Option[Long], name: UserName, email: Email) extends Aggregate {
  override type AggregateType = User
  override type IdType = UserId
  override protected val tag: ClassTag[User] = classTag[User]
}
