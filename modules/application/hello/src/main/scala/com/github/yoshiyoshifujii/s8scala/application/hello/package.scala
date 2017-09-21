package com.github.yoshiyoshifujii.s8scala.application

import com.github.yoshiyoshifujii.s8scala.domain.account.AccountId

package object hello {

  case class StageVariables(env: String,
                            region: String)

  case class Authorizer(accountId: AccountId)

  case class Request(stageVariables: StageVariables,
                     authorizer: Authorizer)
}
