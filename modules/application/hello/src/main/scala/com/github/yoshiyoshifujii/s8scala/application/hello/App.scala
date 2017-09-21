package com.github.yoshiyoshifujii.s8scala.application.hello

import com.github.yoshiyoshifujii.s8scala.domain.account.{AccountEventPublisher, AccountRepository}
import com.github.yoshiyoshifujii.s8scala.infrastructure.dynamodb.account.AccountRepositoryOnDynamoDB
import com.github.yoshiyoshifujii.s8scala.infrastructure.kinesis.account.AccountEventPublisherOnKinesis

class App extends Base {

  override val accountRepository = new AccountRepository with AccountRepositoryOnDynamoDB

  override val accountEventPublisher = new AccountEventPublisher with AccountEventPublisherOnKinesis

}
