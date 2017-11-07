package com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.post

import com.github.yoshiyoshifujii.s8scala.domain.user.{UserPublisher, UserRepository}
import com.github.yoshiyoshifujii.s8scala.infrastructure.dynamodb.user.{
  UserPublisherOnSQS,
  UserRepositoryOnDynamoDB
}

class App extends Base {
  private val region    = sys.env.getOrElse("region", "us-east-1")
  private val stageName = sys.env.getOrElse("stage", "dev")

  override protected val userRepository =
    new UserRepository with UserRepositoryOnDynamoDB {
      override protected val regionName = region
      override protected val tableName  = s"user_users-$stageName"
    }
  override protected val userPublisher =
    new UserPublisher with UserPublisherOnSQS {
      override protected val regionName = region
      override protected val queueUrl   = s"user-users-$stageName"
    }
}
