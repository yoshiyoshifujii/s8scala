package com.github.yoshiyoshifujii.s8scala.infrastructure.kinesis.user

import java.nio.charset.StandardCharsets

import com.amazonaws.services.kinesis.AmazonKinesisClient
import com.amazonaws.services.kinesis.model.PutRecordRequest
import com.amazonaws.xray.AWSXRay
import com.amazonaws.xray.handlers.TracingHandler
import com.github.yoshiyoshifujii.s8scala.domain.user.UserCreatedEvent
import spray.json._

import scala.util.Try

trait UserPublisherOnKinesis {

  protected val regionName: String
  protected val streamName: String

  private val kinesisClient = AmazonKinesisClient
    .builder()
    .withRegion(regionName)
    .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder))
    .build()

  def publish(userCreatedEvent: UserCreatedEvent): Try[String] =
    Try {
      import UserCreatedEventJsonProtocol._
      val request = new PutRecordRequest()
        .withStreamName(streamName)
        .withPartitionKey(userCreatedEvent.id)
        .withData(java.nio.ByteBuffer
          .wrap(userCreatedEvent.toJson.compactPrint.getBytes(StandardCharsets.UTF_8)))

      val result = kinesisClient.putRecord(request)
      s"${result.getShardId}-${result.getSequenceNumber}"
    }

}
