package com.github.yoshiyoshifujii.s8scala.infrastructure.kinesis.account

import com.github.yoshiyoshifujii.s8scala.domain.DomainError
import com.github.yoshiyoshifujii.s8scala.domain.account.AccountModified

trait AccountEventPublisherOnKinesis {

  def publish(modified: AccountModified): Either[DomainError, AccountModified] = ???
}
