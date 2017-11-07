package com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.post

import com.github.yoshiyoshifujii.s8scala.application.service.user.UserService
import com.github.yoshiyoshifujii.s8scala.infrastructure.lambda._
import spray.json._

trait Base extends BaseStreamHandler with UserService {

  object UserCreateIOJsonProtocol extends DefaultJsonProtocol {
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

  override protected def handle(requestJson: RequestJson): ResponseJson = {
    import com.github.yoshiyoshifujii.s8scala.infrastructure.lambda.ResponseJsonConverters._
    import UserCreateIOJsonProtocol._
    (for {
      input <- requestJson.bodyConvert[UserCreateInput].ifNoneThenBadRequest("invalid_data")
      output <- create(input).fold(
        _ => Left(InternalServerErrorJson()),
        Right(_)
      )
    } yield output.toOk).fold(
      l => l,
      r => r
    )
  }
}
