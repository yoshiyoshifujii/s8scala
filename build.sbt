import Dependencies._

lazy val root = (project in file("."))
  .aggregate(`s8scala-logger`, `s8scala-api`)
  .settings(
    name := "s8scala",
    organization := "com.github.yoshiyoshifujii",
    publishArtifact := false
  )

lazy val `s8scala-logger` = (project in file("s8scala-logger"))
  .settings(
    scalaVersion := "2.12.4",
    name := "s8scala-logger",
    organization := "com.github.yoshiyoshifujii",
    libraryDependencies ++= Seq(
      sprayJson,
      scalaLogging,
      logBackClassic
    )
  )

lazy val `s8scala-api` = (project in file("s8scala-api"))
  .dependsOn(`s8scala-logger`)
  .settings(
    scalaVersion := "2.12.4",
    name := "s8scala-api",
    organization := "com.github.yoshiyoshifujii",
    libraryDependencies ++= Seq(
      lambdaCore
    )
  )

import jp.pigumer.sbt.cloud.aws.cloudformation._
import serverless._

lazy val EnvName    = sys.env.getOrElse("ENV_NAME", "dev")
lazy val accountId  = sys.props.getOrElse("AWS_ACCOUNT_ID", "")
lazy val region     = sys.props.getOrElse("AWS_REGION", "")
lazy val roleArn    = sys.props.getOrElse("AWS_ROLE_ARN", "")
lazy val bucketName = sys.props.getOrElse("AWS_BUCKET_NAME", "")

val baseName = "s8scala-sample"

val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.4",
  organization := "com.github.yoshiyoshifujii.s8scala",
  libraryDependencies ++= Seq(
    specs2Core  % Test,
    specs2Mock  % Test,
    specs2JUnit % Test
  )
)

val assemblySettings = Seq(
  assemblyMergeStrategy in assembly := {
    case PathList(ps @ _ *) if ps.last endsWith ".properties" => MergeStrategy.first
    case "application.conf"                                   => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  assemblyJarName in assembly := s"${name.value}-${version.value}.jar",
  publishArtifact in (Compile, packageBin) := false,
  publishArtifact in (Compile, packageSrc) := false,
  publishArtifact in (Compile, packageDoc) := false
)

lazy val sample = (project in file("./sample"))
  .enablePlugins(ServerlessPlugin, CloudformationPlugin)
  .aggregate(
    authorization,
    serverlessApiUsers
  )
  .settings(commonSettings: _*)
  .settings(
    name := baseName,
    publishArtifact := false,
    serverlessOption := {
      ServerlessOption(
        Provider(
          awsAccount = accountId,
          deploymentBucket = bucketName,
          region = region
        ),
        ApiGateway(
          swagger = file("./swagger.yaml")
        ),
        Functions(
          Function(
            filePath = (assemblyOutputPath in assembly in authorization).value,
            name = (name in authorization).value,
            description = Some(baseName),
            handler =
              "com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.authorization.App::handleRequest",
            role = roleArn,
            tags = Map("CONTEXT" -> baseName),
            timeout = 30,
            memorySize = 1024,
            tracing = Some(Tracing.Active),
            events = Events(
              AuthorizeEvent(
                name = (name in authorization).value
              )
            )
          ),
          Function(
            filePath = (assemblyOutputPath in assembly in serverlessApiUsers).value,
            name = s"${(name in serverlessApiUsers).value}-post",
            description = Some(baseName),
            handler =
              "com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.post.App::handleRequest",
            role = roleArn,
            tags = Map("CONTEXT" -> baseName),
            timeout = 30,
            memorySize = 1024,
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users",
                method = "POST",
                cors = true,
                authorizerName = Some((name in authorization).value)
              )
            )
          ),
          Function(
            filePath = (assemblyOutputPath in assembly in serverlessApiUsers).value,
            name = s"${(name in serverlessApiUsers).value}-gets",
            description = Some(baseName),
            handler =
              "com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.gets.App::handleRequest",
            role = roleArn,
            tags = Map("CONTEXT" -> baseName),
            timeout = 30,
            memorySize = 1024,
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users",
                method = "GET",
                cors = true,
                authorizerName = Some((name in authorization).value)
              )
            )
          ),
          Function(
            filePath = (assemblyOutputPath in assembly in serverlessApiUsers).value,
            name = s"${(name in serverlessApiUsers).value}-get",
            description = Some(baseName),
            handler =
              "com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.get.App::handleRequest",
            role = roleArn,
            tags = Map("CONTEXT" -> baseName),
            timeout = 30,
            memorySize = 1024,
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users/{id}",
                method = "GET",
                cors = true,
                authorizerName = Some((name in authorization).value)
              )
            )
          ),
          Function(
            filePath = (assemblyOutputPath in assembly in serverlessApiUsers).value,
            name = s"${(name in serverlessApiUsers).value}-put",
            description = Some(baseName),
            handler =
              "com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.put.App::handleRequest",
            role = roleArn,
            tags = Map("CONTEXT" -> baseName),
            timeout = 30,
            memorySize = 1024,
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users/{id}",
                method = "PUT",
                cors = true,
                authorizerName = Some((name in authorization).value)
              )
            )
          ),
          Function(
            filePath = (assemblyOutputPath in assembly in serverlessApiUsers).value,
            name = s"${(name in serverlessApiUsers).value}-delete",
            description = Some(baseName),
            handler =
              "com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.delete.App::handleRequest",
            role = roleArn,
            tags = Map("CONTEXT" -> baseName),
            timeout = 30,
            memorySize = 1024,
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users/{id}",
                method = "DELETE",
                cors = true,
                authorizerName = Some((name in authorization).value)
              )
            )
          )
        )
      )
    },
    awscfSettings := AwscfSettings(
      projectName = s"$baseName/$EnvName",
      region = region,
      bucketName = Some(bucketName),
      templates = Some(file("cloudformation")),
      roleARN = None
    ),
    awscfStacks := Stacks(
      Alias("dynamodb") -> CloudformationStack(
        stackName = s"$baseName-dynamodb-$EnvName",
        template = "dynamodb.yaml",
        parameters = Map("EnvName" -> EnvName)
      )
    )
  )

lazy val domain = (project in file("./sample/domain"))
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-domain"
  )

lazy val application = (project in file("./sample/application"))
  .dependsOn(domain)
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-application"
  )

lazy val infrastructure = (project in file("./sample/adapter/infrastructure/core"))
  .dependsOn(domain)
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-infrastructure"
  )

lazy val infraDynamo = (project in file("./sample/adapter/infrastructure/dynamodb"))
  .dependsOn(infrastructure)
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-infrastructure-dynamodb",
    libraryDependencies ++= infraDynamoDeps
  )

lazy val authorization = (project in file("./sample/adapter/interface/serverless/authorization"))
  .settings(commonSettings: _*)
  .settings(assemblySettings: _*)
  .settings(
    name := s"$baseName-serverless-authorization",
    libraryDependencies ++= authorizationDeps
  )

lazy val serverlessApiCore = (project in file("./sample/adapter/interface/serverless/api/core"))
  .dependsOn(`s8scala-api`, domain, application, infrastructure)
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-serverless-api-core"
  )

lazy val serverlessApiUsers = (project in file("./sample/adapter/interface/serverless/api/users"))
  .dependsOn(serverlessApiCore, infraDynamo)
  .settings(commonSettings: _*)
  .settings(assemblySettings: _*)
  .settings(
    name := s"$baseName-serverless-api-users"
  )
