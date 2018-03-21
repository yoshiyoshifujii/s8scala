package com.github.yoshiyoshifujii.s8scala.domain

sealed trait RepositoryError

case class RepositorySystemError(cause: Throwable) extends RepositoryError
case class RepositoryOptimisticError()             extends RepositoryError
case class RepositoryNotFoundError()               extends RepositoryError
case class RepositoryAlreadyExistsError()          extends RepositoryError
