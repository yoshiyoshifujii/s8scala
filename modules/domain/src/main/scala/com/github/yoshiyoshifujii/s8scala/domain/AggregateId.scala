package com.github.yoshiyoshifujii.s8scala.domain

import java.util.UUID

trait AggregateId {
  type IdValueType
  type AggregateType <: Aggregate
  val value: IdValueType
}

trait AggregateIdGenerator {

  protected def generateUUId: String =
    UUID.randomUUID().toString.split("-").mkString("").toUpperCase

  def generate: AggregateId

}

