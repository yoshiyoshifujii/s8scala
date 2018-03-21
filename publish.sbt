publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ =>
  false
}

sonatypeProfileName := "com.github.yoshiyoshifujii"

pomExtra := (<url>https://github.com/yoshiyoshifujii/s8scala</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:yoshiyoshifujii/s8scala.git</url>
    <connection>scm:git:git@github.com:yoshiyoshifujii/s8scala.git</connection>
  </scm>
  <developers>
    <developer>
      <id>yoshiyoshifujii</id>
      <name>Yoshitaka Fujii</name>
      <url>https://github.com/yoshiyoshifujii</url>
    </developer>
  </developers>)
