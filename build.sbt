import Dependencies._
import serverless._

lazy val accountId = sys.props.getOrElse("AWS_ACCOUNT_ID", "")
lazy val roleArn = sys.props.getOrElse("AWS_ROLE_ARN", "")
lazy val bucketName = sys.props.getOrElse("AWS_BUCKET_NAME", "")
lazy val authKey = sys.props.getOrElse("AUTH_KEY", "")

val baseName = ""

val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.3",
  organization := "com.github.yoshiyoshifujii.s8scala",
  libraryDependencies ++= rootDeps
)

val assemblySettings = Seq(
  assemblyMergeStrategy in assembly := {
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

lazy val root = (project in file(".")).
  enablePlugins(ServerlessPlugin).
  aggregate(authorization, serverlessApiUsersPost).
  settings(commonSettings: _*).
  settings(
    name := baseName,
    serverlessOption := {
      ServerlessOption(
        Provider(
          awsAccount = accountId,
          deploymentBucket = bucketName
        ),
        ApiGateway(
          swagger = file("./swagger.yaml")
        ),
        Functions(
          Function(
            filePath = (assemblyOutputPath in assembly in authorization).value,
            name = (name in authorization).value,
            description = Some(baseName),
            handler = "com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.authorization.App::handleRequest",
            role = roleArn,
            tracing = Some(Tracing.Active),
            tags = Map("CONTEXT" -> baseName),
            events = Events(
              AuthorizeEvent(
                name = (name in authorization).value
              )
            )
          ),
          Function(
            filePath = (assemblyOutputPath in assembly in serverlessApiUsersPost).value,
            name = (name in serverlessApiUsersPost).value,
            description = Some(baseName),
            handler = "com.github.yoshiyoshifujii.s8scala.adapter.interface.serverless.api.users.post.App::handleRequest",
            role = roleArn,
            tracing = Some(Tracing.Active),
            tags = Map("CONTEXT" -> baseName),
            events = Events(
              HttpEvent(
                path = "/users",
                method = "POST",
                cors = true,
                authorizerName = (name in authorization).value,
                invokeInput = HttpInvokeInput(
                  headers = Seq("Authorization" -> authKey)
                )
              )
            )
          )
        )
      )
    }
  )

lazy val infrastructure = (project in file("./modules/adapter/lib/infrastructure")).
  settings(commonSettings: _*).
  settings(
    name := s"$baseName-infrastructure",
    libraryDependencies ++= infrastructureDeps
  )

lazy val serverlessApi = (project in file("./modules/adapter/lib/serverless/api")).
  settings(commonSettings: _*).
  settings(
    name := s"$baseName-serverless-api",
    libraryDependencies ++= serverlessApiDeps
  )

lazy val domain = (project in file("./modules/domain")).
  settings(commonSettings: _*).
  settings(
    name := s"$baseName-domain"
  )

lazy val application = (project in file("./modules/application")).
  dependsOn(domain).
  settings(commonSettings: _*).
  settings(
    name := s"$baseName-application"
  )

lazy val infraDynamo = (project in file("./modules/adapter/infrastructure/dynamodb")).
  dependsOn(infrastructure, domain).
  settings(commonSettings: _*).
  settings(
    name := s"$baseName-infrastructure-dynamodb",
    libraryDependencies ++= infraDynamoDeps
  )

lazy val authorization = (project in file("./modules/adapter/interface/serverless/authorization")).
  dependsOn(domain).
  settings(commonSettings: _*).
  settings(assemblySettings: _*).
  settings(
    name := s"$baseName-serverless-authorization",
    libraryDependencies ++= authorizationDeps
  )

lazy val serverlessApiUsersPost = (project in file("./modules/adapter/interface/serverless/api/users/post")).
  dependsOn(infraDynamo).
  settings(commonSettings: _*).
  settings(assemblySettings: _*).
  settings(
    name := s"$baseName-serverless-api-users-post",
    libraryDependencies ++= serverlessApiUsersPostDeps
  )

