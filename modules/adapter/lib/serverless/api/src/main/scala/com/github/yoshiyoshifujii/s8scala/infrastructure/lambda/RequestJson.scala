package com.github.yoshiyoshifujii.s8scala.infrastructure.lambda

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

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

case class RequestJson(resource: Option[String],
                       path: String,
                       httpMethod: String,
                       headers: Map[String, String],
                       queryStringParameters: Option[Map[String, String]],
                       pathParameters: Option[Map[String, String]],
                       stageVariables: Option[Map[String, String]],
                       requestContext: RequestContextJson,
                       body: Option[String],
                       isBase64Encoded: Boolean)

object RequestJsonProtocol extends DefaultJsonProtocol {
  implicit val authorizerJsonFormat: RootJsonFormat[AuthorizerJson] =
    jsonFormat1(AuthorizerJson)
  implicit val errorJsonFormat: RootJsonFormat[ErrorJson] = jsonFormat2(
    ErrorJson)
  implicit val identityJsonFormat: RootJsonFormat[IdentityJson] = jsonFormat11(
    IdentityJson)
  implicit val requestContextJsonFormatter
    : RootJsonFormat[RequestContextJson] = jsonFormat9(RequestContextJson)
  implicit val requestJsonFormatter: RootJsonFormat[RequestJson] =
    jsonFormat10(RequestJson)
}
