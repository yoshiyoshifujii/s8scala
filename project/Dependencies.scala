import sbt._

object Dependencies {

  // Specs2
  val specs2Core  = "org.specs2" %% "specs2-core"  % "3.8.6"
  val specs2Mock  = "org.specs2" %% "specs2-mock"  % "3.8.6"
  val specs2JUnit = "org.specs2" %% "specs2-junit" % "3.8.6"

  // Amazon
  val awsSDKVersion  = "1.11.225"
  val awsSDKDynamoDB = "com.amazonaws" % "aws-java-sdk-dynamodb" % awsSDKVersion
  val awsSDKSQS      = "com.amazonaws" % "aws-java-sdk-sqs" % awsSDKVersion

  // XRay
  val awsXRayVersion = "1.2.0"
  val awsXRayCore         = "com.amazonaws" % "aws-xray-recorder-sdk-core" % awsXRayVersion
  val awsXRaySDK          = "com.amazonaws" % "aws-xray-recorder-sdk-aws-sdk" % awsXRayVersion
  val awsXRayInstrumentor = "com.amazonaws" % "aws-xray-recorder-sdk-aws-sdk-instrumentor" % awsXRayVersion

  // Amazon Lambda
  val lambdaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"

  // spray-json
  val sprayJson = "io.spray" %% "spray-json" % "1.3.3"

  // Scala Logging
  val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"  % "3.5.0"
  val logBackClassic = "ch.qos.logback"             % "logback-classic" % "1.1.7"

  val rootDeps = Seq(
    specs2Core  % Test,
    specs2Mock  % Test,
    specs2JUnit % Test
  )

  val serverlessLoggerDeps = Seq(
    sprayJson,
    scalaLogging,
    logBackClassic
  )

  val serverlessApiDeps = Seq(
    lambdaCore
  )

  val serverlessSubscriberSQSDeps = Seq(
    awsSDKSQS,
    awsXRayCore,
    awsXRaySDK,
    awsXRayInstrumentor
  )

  val infraDynamoDeps = Seq(
    awsSDKDynamoDB,
    awsXRayCore,
    awsXRaySDK,
    awsXRayInstrumentor,
    sprayJson
  )

  val infraSQSDeps = Seq(
    awsSDKSQS,
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
