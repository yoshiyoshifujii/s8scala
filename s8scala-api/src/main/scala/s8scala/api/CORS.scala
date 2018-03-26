package s8scala.api

trait CORS extends ResponseFolder {

  protected def cors(responseJson: ResponseJson): ResponseJson =
    responseJson.copy(headers = responseJson.headers + ("Access-Control-Allow-Origin" -> "*"))

  override private[api] def foldResponseJson(
      eitherResponseJson: Either[ResponseJson, ResponseJson]): ResponseJson =
    eitherResponseJson.fold(cors, cors)

}
