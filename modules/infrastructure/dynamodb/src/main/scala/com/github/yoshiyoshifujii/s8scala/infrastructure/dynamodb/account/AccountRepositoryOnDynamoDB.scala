package com.github.yoshiyoshifujii.s8scala.infrastructure.dynamodb.account

import com.github.yoshiyoshifujii.s8scala.domain.DomainError
import com.github.yoshiyoshifujii.s8scala.domain.account.{Account, AccountId}

trait AccountRepositoryOnDynamoDB {

  def save(account: Account): Either[DomainError, Account] = ???

  def get(id: AccountId): Either[DomainError, Option[Account]] = ???

  def findBy(email: String): Either[DomainError, Option[Account]] = ???

  def findAll: Either[DomainError, Seq[Account]] = ???
}
