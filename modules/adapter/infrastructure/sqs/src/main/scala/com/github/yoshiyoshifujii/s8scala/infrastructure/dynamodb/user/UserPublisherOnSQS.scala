package com.github.yoshiyoshifujii.s8scala.infrastructure.dynamodb.user

import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.amazonaws.xray.AWSXRay
import com.amazonaws.xray.handlers.TracingHandler
import com.github.yoshiyoshifujii.s8scala.domain.user.UserCreatedEvent
import spray.json._

import scala.util.Try

trait UserPublisherOnSQS {

  protected val regionName: String
  protected val queueUrl: String

  private val sqsClient = AmazonSQSClientBuilder
    .standard()
    .withRegion(regionName)
    .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder))
    .build()

  def publish(userCreatedEvent: UserCreatedEvent): Try[String] =
    Try {
      import UserCreatedEventJsonProtocol._
      val request = new SendMessageRequest()
        .withQueueUrl(queueUrl)
        .withMessageBody(userCreatedEvent.toJson.compactPrint)
      sqsClient.sendMessage(request).getMessageId
    }
}
