package s8scala.api

import s8scala.api.HttpStatusCode.HttpStatusCode
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class ResponseJson(isBase64Encoded: Boolean,
                        statusCode: HttpStatusCode,
                        headers: Map[String, String],
                        body: Option[String])

object ResponseJson {
  def simple(code: HttpStatusCode): ResponseJson =
    ResponseJson(
      isBase64Encoded = false,
      statusCode = code,
      headers = Map("Content-Type" -> "application/json"),
      body = None
    )
}

object CreatedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.Created)
}

object AcceptedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.Accepted)
}

object Non_AuthoritativeInformationJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.Non_AuthoritativeInformation)
}

object NoContentJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.NoContent)
}

object ResetContentJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.ResetContent)
}

object PartialContentJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.PartialContent)
}

object Multi_StatusJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.Multi_Status)
}

object AlreadyReportedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.AlreadyReported)
}

object IM_UsedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.IM_Used)
}

object BadRequestJson {
  def apply(message: String): ResponseJson = ResponseJson(
    isBase64Encoded = false,
    statusCode = HttpStatusCode.BadRequest,
    headers = Map("Content-Type" -> "application/json"),
    body = Some(s"""{"message":"$message"}""")
  )
}

object UnauthorizedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.Unauthorized)
}

object PaymentRequiredJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.PaymentRequired)
}

object ForbiddenJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.Forbidden)
}

object NotFoundJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.NotFound)
}

object MethodNotAllowedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.MethodNotAllowed)
}

object NotAcceptableJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.NotAcceptable)
}

object ProxyAuthenticationRequiredJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.ProxyAuthenticationRequired)
}

object RequestTimeoutJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.RequestTimeout)
}

object ConflictJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.Conflict)
}

object GoneJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.Gone)
}

object LengthRequiredJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.LengthRequired)
}

object PreconditionFailedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.PreconditionFailed)
}

object RequestEntityTooLargeJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.RequestEntityTooLarge)
}

object RequestURITooLargeJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.RequestURITooLarge)
}

object UnsupportedMediaTypeJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.UnsupportedMediaType)
}

object RequestedRangeNotSatisfiableJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.RequestedRangeNotSatisfiable)
}

object ExpectationFailedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.ExpectationFailed)
}

object ImTeapotJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.ImTeapot)
}

object UnprocessableEntityJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.UnprocessableEntity)
}

object LockedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.Locked)
}

object FailedDependencyJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.FailedDependency)
}

object UnorderedCollectionJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.UnorderedCollection)
}

object UpgradeRequiredJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.UpgradeRequired)
}

object PreconditionRequiredJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.PreconditionRequired)
}

object TooManyRequestsJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.TooManyRequests)
}

object RequestHeaderFieldsTooLargeJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.RequestHeaderFieldsTooLarge)
}

object ReportLegalObstaclesJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.ReportLegalObstacles)
}

object InternalServerErrorJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.InternalServerError)
}

object NotImplementedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.NotImplemented)
}

object BadGatewayJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.BadGateway)
}

object ServiceUnavailableJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.ServiceUnavailable)
}

object GatewayTimeoutJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.GatewayTimeout)
}

object HTTPVersionNotSupportedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.HTTPVersionNotSupported)
}

object VariantAlsoNegotiatesJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.VariantAlsoNegotiates)
}

object InsufficientStorageJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.InsufficientStorage)
}

object BandwidthLimitExceededJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.BandwidthLimitExceeded)
}

object NotExtendedJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.NotExtended)
}

object NetworkAuthenticationRequiredJson {
  def apply(): ResponseJson = ResponseJson.simple(HttpStatusCode.NetworkAuthenticationRequired)
}

object ResponseJsonConverters {

  implicit class Option2ResponseJson[E](val o: Either[ResponseJson, Option[E]]) extends AnyVal {
    def ifNoneThenNoContent: Either[ResponseJson, E] =
      o.flatMap(_.toRight(NoContentJson()))

    def ifNoneThenBadRequest(message: String): Either[ResponseJson, E] =
      o.flatMap(_.toRight(BadRequestJson(message)))

    def ifNoneThenUnauthorized: Either[ResponseJson, E] =
      o.flatMap(_.toRight(UnauthorizedJson()))

    def ifNoneThenForbidden: Either[ResponseJson, E] =
      o.flatMap(_.toRight(ForbiddenJson()))

    def ifNoneThenNotFound: Either[ResponseJson, E] =
      o.flatMap(_.toRight(NotFoundJson()))
  }

  implicit class E2ResponseJson[E](val e: E) extends AnyVal {
    def toOk(implicit converter: E => String): ResponseJson =
      ResponseJson(
        isBase64Encoded = false,
        statusCode = HttpStatusCode.OK,
        headers = Map("Content-Type" -> "application/json"),
        body = Some(converter(e))
      )
  }

}

object ResponseJsonProtocol extends DefaultJsonProtocol {
  implicit val responseJsonFormatter: RootJsonFormat[ResponseJson] =
    jsonFormat4(ResponseJson.apply)
}
