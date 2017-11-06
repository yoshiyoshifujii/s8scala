addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC11")
addSbtPlugin("com.geirsson"    % "sbt-scalafmt" % "0.6.6")
addSbtPlugin("com.pigumer.sbt.cloud" % "sbt-aws-cloudformation" % "5.0.19")
//addSbtPlugin("com.github.yoshiyoshifujii" % "sbt-aws-serverless" % "3.1.0")

lazy val root       = project.in(file(".")).dependsOn(githubRepo)
lazy val githubRepo = uri("file:///Users/yoshi/workspace/sbt-plugins/sbt-aws-serverless")
