package com.github.yoshiyoshifujii.s8scala.infrastructure.dynamodb.user

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.amazonaws.services.dynamodbv2.document.utils.{NameMap, ValueMap}
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import com.amazonaws.xray.AWSXRay
import com.amazonaws.xray.handlers.TracingHandler
import com.github.yoshiyoshifujii.s8scala.domain.RepositoryError
import com.github.yoshiyoshifujii.s8scala.domain.common.Email
import com.github.yoshiyoshifujii.s8scala.domain.user.{User, UserId, UserName}

import scala.collection.JavaConverters._
import scala.util.Try

trait UserRepositoryOnDynamoDB {
  import com.github.yoshiyoshifujii.s8scala.infrastructure.dynamodb.RepositoryErrorConverters._

  protected val regionName: String
  protected val tableName: String

  private lazy val dynamoDBClient = AmazonDynamoDBClientBuilder
    .standard()
    .withRegion(regionName)
    .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder))
    .build()

  private lazy val dynamoDB = new DynamoDB(dynamoDBClient)
  private lazy val table    = dynamoDB.getTable(tableName)
  protected val AttrId      = "id"
  protected val AttrVersion = "version"
  protected val AttrEmail   = "email"
  protected val AttrName    = "name"
  protected val IndexId     = "id-index"

  private def generateItem(entity: User, version: Long): Item =
    new Item()
      .withPrimaryKey(AttrEmail, entity.email.value)
      .withString(AttrId, entity.id.value)
      .withNumber(AttrVersion, version)
      .withString(AttrName, entity.name.value)

  protected def insertInternal(entity: User): Either[RepositoryError, User] =
    Try {
      val version = 1L
      table.putItem(generateItem(entity, version))
      entity.copy(version = Some(version))
    }.toRepositoryError

  protected def updateInternal(entity: User, version: Long): Either[RepositoryError, User] =
    Try {
      val newVersion          = version + 1L
      val conditionExpression = "#v = :version"
      val nameMap             = new NameMap().`with`("#v", AttrVersion)
      val valueMap            = new ValueMap().withLong(":version", version)
      table.putItem(generateItem(entity, newVersion), conditionExpression, nameMap, valueMap)
      entity.copy(version = Some(newVersion))
    }.toRepositoryError

  def findBy(id: UserId): Either[RepositoryError, Option[User]] =
    Try {
      val spec = new QuerySpec()
        .withHashKey(AttrId, id.value)
      table
        .getIndex(IndexId)
        .query(spec)
        .iterator()
        .asScala
        .toSeq
        .headOption
        .map { item =>
          User(
            id = UserId(item.getString(AttrId)),
            version = Option(item.getNumber(AttrVersion).longValue()),
            email = Email(item.getString(AttrEmail)),
            name = UserName(item.getString(AttrName))
          )
        }
    }.toRepositoryError

  def get(id: UserId): Either[RepositoryError, User] = ???
}
