package com.github.yoshiyoshifujii.s8scala.domain.user

import com.github.yoshiyoshifujii.s8scala.domain.AggregateId

case class UserId(value: String) extends AggregateId {
  override type IdValueType = String
  override type AggregateType = User
}
