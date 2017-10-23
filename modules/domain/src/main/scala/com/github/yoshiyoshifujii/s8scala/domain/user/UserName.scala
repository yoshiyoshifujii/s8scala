package com.github.yoshiyoshifujii.s8scala.domain.user

case class UserName(value: String) {
  assert(value.length <= 80, s"${this.getClass.getName}:over_max")
}
