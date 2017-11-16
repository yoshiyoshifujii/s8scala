package com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.post

import com.github.yoshiyoshifujii.s8scala.domain.user.{UserPublisher, UserRepository}
import com.github.yoshiyoshifujii.s8scala.infrastructure.dynamodb.user.UserRepositoryOnDynamoDB
import com.github.yoshiyoshifujii.s8scala.infrastructure.kinesis.user.UserPublisherOnKinesis

class App extends Base {
  private val region    = sys.env.getOrElse("region", "us-east-1")
  private val stageName = sys.env.getOrElse("stage", "dev")

  override protected val userRepository =
    new UserRepository with UserRepositoryOnDynamoDB {
      override protected val regionName = region
      override protected val tableName  = s"user_users-$stageName"
    }
  override protected val userPublisher =
    new UserPublisher with UserPublisherOnKinesis {
      override protected val regionName = region
      override protected val streamName = s"user-users-$stageName"
    }
}
