package com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.subscriber.sqs.users

class App extends Base {
  override protected val regionName = sys.env.getOrElse("region", "us-east-1")
  private val stageName             = sys.env.getOrElse("stage", "dev")
  override protected val queueName  = s"user-users-$stageName"
}
