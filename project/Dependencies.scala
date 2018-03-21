import sbt._

object Dependencies {

  // Specs2
  val specs2Core  = "org.specs2" %% "specs2-core"  % "3.8.6"
  val specs2Mock  = "org.specs2" %% "specs2-mock"  % "3.8.6"
  val specs2JUnit = "org.specs2" %% "specs2-junit" % "3.8.6"

  // Amazon
  val awsSDKVersion  = "1.11.297"
  val awsSDKDynamoDB = "com.amazonaws" % "aws-java-sdk-dynamodb" % awsSDKVersion

  // XRay
  val awsXRayVersion      = "1.3.1"
  val awsXRayCore         = "com.amazonaws" % "aws-xray-recorder-sdk-core" % awsXRayVersion
  val awsXRaySDK          = "com.amazonaws" % "aws-xray-recorder-sdk-aws-sdk" % awsXRayVersion
  val awsXRayInstrumentor = "com.amazonaws" % "aws-xray-recorder-sdk-aws-sdk-instrumentor" % awsXRayVersion

  // Amazon Lambda
  val lambdaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.2.0"

  // spray-json
  val sprayJson = "io.spray" %% "spray-json" % "1.3.4"

  // Scala Logging
  val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"  % "3.8.0"
  val logBackClassic = "ch.qos.logback"             % "logback-classic" % "1.2.3"

  val infraDynamoDeps = Seq(
    awsSDKDynamoDB,
    awsXRayCore,
    awsXRaySDK,
    awsXRayInstrumentor,
    sprayJson
  )

  val authorizationDeps = Seq(
    lambdaCore,
    sprayJson
  )

}
