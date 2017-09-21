package com.github.yoshiyoshifujii.s8scala.infrastructure.lambda

import org.specs2.mutable.Specification
import spray.json.JsonParser

class RequestJsonProtocolSpec extends Specification {

  "RequestJsonProtocol" should {
    "success" in {
      val input =
        """
          |{
          |  "resource": "/hellos",
          |  "path": "/hellos",
          |  "httpMethod": "GET",
          |  "headers": {
          |    "Accept-Encoding": "gzip,deflate",
          |    "Authorization": "hoge",
          |    "CloudFront-Forwarded-Proto": "https",
          |    "CloudFront-Is-Desktop-Viewer": "true",
          |    "CloudFront-Is-Mobile-Viewer": "false",
          |    "CloudFront-Is-SmartTV-Viewer": "false",
          |    "CloudFront-Is-Tablet-Viewer": "false",
          |    "CloudFront-Viewer-Country": "JP",
          |    "Content-Type": "application/json",
          |    "Host": "2249zareg7.execute-api.us-east-1.amazonaws.com",
          |    "User-Agent": "Apache-HttpClient/4.5.2 (Java/1.8.0_25)",
          |    "Via": "1.1 b9a9dc9b687f664e04b835f7a2f61559.cloudfront.net (CloudFront)",
          |    "X-Amz-Cf-Id": "sZMJCvvSDOlnK-t7R6CoQ7L_ziETfKHF-3OK7UgaoydeM56P75xjXg==",
          |    "x-amzn-ssl-client-hello": "AQABAgMDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEbAI8AnADzAJcApAGcAQMAJwBMAL8AEwA4AMwAywCvALwCcwC3AMQCeAKLACMASAArAA8ANABYAE8AHwBEABcACwAwABAD/AQAAkwAKADQAMgAXAAEAAwATABUABgAHAAkACgAYAAsADAAZAA0ADgAPABAAEQACABIABAAFABQACAAWAAsAAgEAAA0AGgAYBgMGAQUDBQEEAwQBAwMDAQIDAgECAgEBAAAAMwAxAAAuMjI0OXphcmVnNy5leGVjdXRlLWFwaS51cy1lYXN0LTEuYW1hem9uYXdzLmNvbQ==",
          |    "X-Amzn-Trace-Id": "Root=1-59be2d25-09bca0ce3abea03f10e88a6c",
          |    "X-Forwarded-For": "180.9.217.134, 205.251.211.81",
          |    "X-Forwarded-Port": "443",
          |    "X-Forwarded-Proto": "https"
          |  },
          |  "queryStringParameters": null,
          |  "pathParameters": null,
          |  "stageVariables": {
          |    "env": "dev",
          |    "region": "us-east-1"
          |  },
          |  "requestContext": {
          |    "path": "/dev/hellos",
          |    "accountId": "468260865666",
          |    "resourceId": "r6cker",
          |    "stage": "dev",
          |    "authorizer": {
          |      "principalId": "{\"account_id\":\"xxx\"}"
          |    },
          |    "requestId": "2f9c1753-9b7f-11e7-ad3a-3dd7bba23121",
          |    "identity": {
          |      "cognitoIdentityPoolId": null,
          |      "accountId": null,
          |      "cognitoIdentityId": null,
          |      "caller": null,
          |      "apiKey": "",
          |      "sourceIp": "180.9.217.134",
          |      "accessKey": null,
          |      "cognitoAuthenticationType": null,
          |      "cognitoAuthenticationProvider": null,
          |      "userArn": null,
          |      "userAgent": "Apache-HttpClient/4.5.2 (Java/1.8.0_25)",
          |      "user": null
          |    },
          |    "resourcePath": "/hellos",
          |    "httpMethod": "GET",
          |    "apiId": "2249zareg7"
          |  },
          |  "body": null,
          |  "isBase64Encoded": false
          |}
        """.stripMargin

      import RequestJsonProtocol._
      val result = JsonParser(input).convertTo[RequestJson]

      result.body must beNone
    }
  }

}
