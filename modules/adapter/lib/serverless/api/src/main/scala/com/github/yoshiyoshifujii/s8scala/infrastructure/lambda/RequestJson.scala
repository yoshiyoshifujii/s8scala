package com.github.yoshiyoshifujii.s8scala.infrastructure.lambda

import spray.json.DefaultJsonProtocol

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
  implicit val authorizerJsonFormat = jsonFormat1(AuthorizerJson)
  implicit val errorJsonFormat = jsonFormat2(ErrorJson)
  implicit val identityJsonFormat = jsonFormat11(IdentityJson)
  implicit val requestContextJsonFormatter = jsonFormat9(RequestContextJson)
  implicit val requestJsonFormatter = jsonFormat10(RequestJson)
}
