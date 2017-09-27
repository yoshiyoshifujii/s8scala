import sbt._

object Dependencies {

  // Specs2
  val specs2Core = "org.specs2" %% "specs2-core" % "3.8.6"
  val specs2Mock = "org.specs2" %% "specs2-mock" % "3.8.6"
  val specs2JUnit = "org.specs2" %% "specs2-junit" % "3.8.6"

  // Amazon
  val awsSDKVersion = "1.11.199"
  val awsSDKDynamoDB = "com.amazonaws" % "aws-java-sdk-dynamodb" % awsSDKVersion

  // Amazon Lambda
  val lambdaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"

  // spray-json
  val sprayJson = "io.spray" %%  "spray-json" % "1.3.3"

  val rootDeps = Seq(
    specs2Core % Test,
    specs2Mock % Test,
    specs2JUnit % Test
  )

  val infrastructureDeps = Seq(
    sprayJson
  )

  val serverlessApiDeps = Seq(
    lambdaCore,
    sprayJson
  )

  val infraDynamoDeps = Seq(
    awsSDKDynamoDB
  )

  val authorizationDeps = Seq(
    lambdaCore
  )

  val serverlessApiUsersPostDeps = Seq()

}
