package s8scala.api

import org.specs2.mutable.Specification
import spray.json._

class ResponseJsonProtocolSpec extends Specification {

  "ResponseJsonProtocol" should {
    "success" in {
      import s8scala.api.ResponseJsonProtocol._
      ResponseJson(
        isBase64Encoded = false,
        statusCode = HttpStatusCode.OK,
        headers = Map(
          "Content-Type" -> "application/json"
        ),
        body = Some("{}")
      ).toJson must_== JsObject(
        "isBase64Encoded" -> JsFalse,
        "statusCode"      -> JsNumber(200),
        "headers" -> JsObject(
          "Content-Type" -> JsString("application/json")
        ),
        "body" -> JsString("{}")
      )
    }
  }
}
