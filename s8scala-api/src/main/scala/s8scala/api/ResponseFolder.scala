package s8scala.api

trait ResponseFolder {

  private[api] def foldResponseJson(
      eitherResponseJson: Either[ResponseJson, ResponseJson]): ResponseJson =
    eitherResponseJson.fold(identity, identity)

}
