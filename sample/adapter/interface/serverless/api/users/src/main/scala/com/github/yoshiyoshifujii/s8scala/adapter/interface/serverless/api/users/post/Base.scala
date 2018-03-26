package com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.post

import com.github.yoshiyoshifujii.s8scala.application.Authorizer
import com.github.yoshiyoshifujii.s8scala.application.service.user.UserService
import s8scala.api.{BaseStreamHandler, RequestJson, CORS}
import spray.json._

trait Base extends BaseStreamHandler with CORS with UserService {

  object UserCreateIOJsonProtocol extends DefaultJsonProtocol {
    import s8scala.api.RequestConverters._
    implicit val userCreateInputJsonFormatter  = jsonFormat2(UserCreateInput)
    implicit val userCreateOutputJsonFormatter = jsonFormat2(UserCreateOutput)

    implicit object BodyConverterImpl extends BodyConverter[UserCreateInput] {
      override def convert(t: String): UserCreateInput = JsonParser(t).convertTo[UserCreateInput]
    }

    implicit object Ok extends (UserCreateOutput => String) {
      override def apply(output: UserCreateOutput): String =
        output.toJson.compactPrint
    }
  }

  import com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.core.ServerlessApiErrorConverters._
  import com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.core.AuthorizerConverters._
  import s8scala.api.ResponseJsonConverters._
  import UserCreateIOJsonProtocol._

  override protected def handle(requestJson: RequestJson): Either[Invalid, Valid] =
    for {
      env        <- requestJson.stageVariable("env").ifNoneThenInternalServerError
      authorizer <- requestJson.authorizeConvert[Authorizer].ifNoneThenUnauthorized
      input      <- requestJson.bodyConvert[UserCreateInput].ifNoneThenBadRequest("invalid_data")
      output     <- create(authorizer, input).toServerlessError
    } yield output.toOk
}
