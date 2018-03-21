package com.github.yoshiyoshifujii.s8scala.domain

trait Repository {
  type ID <: AggregateId
  type ENTITY <: Aggregate

  protected def insertInternal(entity: ENTITY): Either[RepositoryError, ENTITY]
  protected def updateInternal(entity: ENTITY, version: Long): Either[RepositoryError, ENTITY]

  def findBy(id: ID): Either[RepositoryError, Option[ENTITY]]

  def get(id: ID): Either[RepositoryError, ENTITY]

  def save(entity: ENTITY): Either[RepositoryError, ENTITY] =
    entity.version map { v =>
      updateInternal(entity, v)
    } getOrElse {
      insertInternal(entity)
    }
}
