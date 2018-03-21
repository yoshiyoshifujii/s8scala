package com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.core

import com.github.yoshiyoshifujii.s8scala.application.Authorizer
import s8scala.api.RequestConverters.AuthorizeConverter
import spray.json.{DefaultJsonProtocol, JsonParser}

object AuthorizerConverters extends DefaultJsonProtocol {

  implicit val AuthorizerFormatter = jsonFormat1(Authorizer)

  implicit object AuthorizeConverterImpl extends AuthorizeConverter[Authorizer] {
    override def convert(t: String): Authorizer = JsonParser(t).convertTo[Authorizer]
  }
}
