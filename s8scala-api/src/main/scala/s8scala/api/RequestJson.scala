package s8scala.api

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.util.Try

case class AuthorizerJson(principalId: String)

case class ErrorJson(message: String, responseType: String)

case class IdentityJson(accountId: Option[String],
                        apiKey: Option[String],
                        caller: Option[String],
                        cognitoAuthenticationProvider: Option[String],
                        cognitoAuthenticationType: Option[String],
                        cognitoIdentityId: Option[String],
                        cognitoIdentityPoolId: Option[String],
                        sourceIp: Option[String],
                        user: Option[String],
                        userAgent: Option[String],
                        userArn: Option[String])

case class RequestContextJson(apiId: String,
                              authorizer: Option[AuthorizerJson],
                              httpMethod: String,
                              error: Option[ErrorJson],
                              identity: IdentityJson,
                              requestId: String,
                              resourceId: String,
                              resourcePath: String,
                              stage: String)

object RequestConverters {
  sealed trait Converter[C] {
    def convert(t: String): C
  }
  trait BodyConverter[J]      extends Converter[J]
  trait AuthorizeConverter[A] extends Converter[A]
}

case class RequestJson(resource: Option[String],
                       path: String,
                       httpMethod: String,
                       headers: Map[String, String],
                       queryStringParameters: Option[Map[String, String]],
                       pathParameters: Option[Map[String, String]],
                       stageVariables: Option[Map[String, String]],
                       requestContext: RequestContextJson,
                       body: Option[String],
                       isBase64Encoded: Boolean) {
  import RequestConverters._

  private def convert[A](f: => Option[String])(
      implicit converter: Converter[A]): Either[ResponseJson, Option[A]] =
    Try(f.map(converter.convert)).fold(
      cause => Left(BadRequestJson(cause.getMessage)),
      Right(_)
    )

  def bodyConvert[A](implicit bodyConverter: BodyConverter[A]): Either[ResponseJson, Option[A]] =
    convert(body)

  def authorizeConvert[A](
      implicit authorizeConverter: AuthorizeConverter[A]): Either[ResponseJson, Option[A]] =
    convert(requestContext.authorizer.map(_.principalId))

  def queryStringParameter(key: String): Option[String] = queryStringParameters.flatMap(_.get(key))
  def pathParameter(key: String): Option[String]        = pathParameters.flatMap(_.get(key))
  def stageVariable(key: String): Option[String]        = stageVariables.flatMap(_.get(key))
}

object RequestJsonProtocol extends DefaultJsonProtocol {
  implicit val authorizerJsonFormat: RootJsonFormat[AuthorizerJson] =
    jsonFormat1(AuthorizerJson)
  implicit val errorJsonFormat: RootJsonFormat[ErrorJson]       = jsonFormat2(ErrorJson)
  implicit val identityJsonFormat: RootJsonFormat[IdentityJson] = jsonFormat11(IdentityJson)
  implicit val requestContextJsonFormatter: RootJsonFormat[RequestContextJson] = jsonFormat9(
    RequestContextJson)
  implicit val requestJsonFormatter: RootJsonFormat[RequestJson] =
    jsonFormat10(RequestJson)
}
