import Dependencies._
import jp.pigumer.sbt.cloud.aws.cloudformation._
import serverless._

lazy val EnvName    = sys.env.getOrElse("ENV_NAME", "dev")
lazy val accountId  = sys.props.getOrElse("AWS_ACCOUNT_ID", "")
lazy val region     = sys.props.getOrElse("AWS_REGION", "")
lazy val roleArn    = sys.props.getOrElse("AWS_ROLE_ARN", "")
lazy val bucketName = sys.props.getOrElse("AWS_BUCKET_NAME", "")
lazy val authKey    = sys.props.getOrElse("AUTH_KEY", "")

val baseName = "s8scala"

val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.3",
  organization := "com.github.yoshiyoshifujii.s8scala",
  libraryDependencies ++= rootDeps
)

val assemblySettings = Seq(
  assemblyMergeStrategy in assembly := {
    case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
    case "application.conf" => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  assemblyJarName in assembly := s"${name.value}-${version.value}.jar",
  publishArtifact in (Compile, packageBin) := false,
  publishArtifact in (Compile, packageSrc) := false,
  publishArtifact in (Compile, packageDoc) := false
)

lazy val root = (project in file("."))
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
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users",
                method = "POST",
                cors = true,
                authorizerName = (name in authorization).value,
                invokeInput = HttpInvokeInput(
                  headers = Seq("Authorization" -> authKey),
                  body = Some("""{"name":"hoge","email":"hoge@hoge.com"}""".getBytes("utf-8"))
                )
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
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users",
                method = "GET",
                cors = true,
                authorizerName = (name in authorization).value,
                invokeInput = HttpInvokeInput(
                  headers = Seq("Authorization" -> authKey)
                )
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
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users/{id}",
                method = "GET",
                cors = true,
                authorizerName = (name in authorization).value,
                invokeInput = HttpInvokeInput(
                  headers = Seq("Authorization" -> authKey),
                  pathWithQuerys = Seq("id"     -> "XXX")
                )
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
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users/{id}",
                method = "PUT",
                cors = true,
                authorizerName = (name in authorization).value,
                invokeInput = HttpInvokeInput(
                  headers = Seq("Authorization" -> authKey),
                  pathWithQuerys = Seq("id"     -> "XXX")
                )
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
            tracing = Some(Tracing.Active),
            environment = Map("region" -> region),
            events = Events(
              HttpEvent(
                path = "/users/{id}",
                method = "DELETE",
                cors = true,
                authorizerName = (name in authorization).value,
                invokeInput = HttpInvokeInput(
                  headers = Seq("Authorization" -> authKey),
                  pathWithQuerys = Seq("id"     -> "XXX")
                )
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
      ),
      Alias("sqs") -> CloudformationStack(
        stackName = s"$baseName-sqs-$EnvName",
        template = "sqs.yaml",
        parameters = Map("EnvName" -> EnvName)
      )
    )
  )

lazy val serverlessLogger = (project in file("./modules/adapter/lib/serverless/logger"))
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-serverless-logger",
    libraryDependencies ++= serverlessLoggerDeps
  )

lazy val serverlessApi = (project in file("./modules/adapter/lib/serverless/api"))
  .dependsOn(serverlessLogger)
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-serverless-api",
    libraryDependencies ++= serverlessApiDeps
  )

lazy val domain = (project in file("./modules/domain"))
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-domain"
  )

lazy val application = (project in file("./modules/application"))
  .dependsOn(domain)
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-application"
  )

lazy val infrastructure = (project in file("./modules/infrastructure"))
  .dependsOn(domain)
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-infrastructure"
  )

lazy val infraDynamo = (project in file("./modules/adapter/infrastructure/dynamodb"))
  .dependsOn(infrastructure)
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-infrastructure-dynamodb",
    libraryDependencies ++= infraDynamoDeps
  )

lazy val infraSQS = (project in file("./modules/adapter/infrastructure/sqs"))
  .dependsOn(infrastructure)
  .settings(commonSettings: _*)
  .settings(
    name := s"$baseName-infrastructure-sqs",
    libraryDependencies ++= infraSQSDeps
  )

lazy val authorization = (project in file("./modules/adapter/interface/serverless/authorization"))
  .settings(commonSettings: _*)
  .settings(assemblySettings: _*)
  .settings(
    name := s"$baseName-serverless-authorization",
    libraryDependencies ++= authorizationDeps
  )

lazy val serverlessApiUsers = (project in file("./modules/adapter/interface/serverless/api/users"))
  .dependsOn(serverlessApi, domain, application, infraDynamo, infraSQS)
  .settings(commonSettings: _*)
  .settings(assemblySettings: _*)
  .settings(
    name := s"$baseName-serverless-api-users"
  )
