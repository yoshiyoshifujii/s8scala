package com.github.yoshiyoshifujii.s8scala.infrastructure.lambda

import com.github.yoshiyoshifujii.s8scala.infrastructure.lambda.HttpStatusCode.HttpStatusCode

case class ResponseJson(isBase64Encoded: Boolean,
                        statusCode: HttpStatusCode,
                        headers: Map[String, String])
