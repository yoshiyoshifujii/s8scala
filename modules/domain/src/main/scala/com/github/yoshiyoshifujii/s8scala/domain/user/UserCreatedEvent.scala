package com.github.yoshiyoshifujii.s8scala.domain.user

case class UserCreatedEvent(id: String, version: Long, name: String, email: String)
