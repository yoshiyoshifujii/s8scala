addSbtPlugin("io.get-coursier"            % "sbt-coursier"       % "1.0.0-RC10")
addSbtPlugin("com.geirsson"               % "sbt-scalafmt"       % "0.6.6")
//addSbtPlugin("com.github.yoshiyoshifujii" % "sbt-aws-serverless" % "3.1.0")
lazy val root = project.in(file(".")).dependsOn(githubRepo)
lazy val githubRepo = uri("file:///Users/yoshitaka.fujii/workspace/sbt-plugins/sbt-aws-serverless")

