package com.github.yoshiyoshifujii.s8scala.domain.user

import com.github.yoshiyoshifujii.s8scala.domain.{AggregateId, AggregateIdGenerator}

case class UserId(value: String) extends AggregateId {
  override type IdValueType   = String
  override type AggregateType = User
}

object UserId extends AggregateIdGenerator {
  override def generate = UserId(generateUUId)
}
