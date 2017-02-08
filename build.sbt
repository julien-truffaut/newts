import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.scalajs.sbtplugin.cross.CrossProject
import sbt.Keys._
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val buildSettings = Seq(
  organization       := "com.github.julien-truffaut",
  scalaVersion       := "2.12.1",
  scalacOptions     ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:implicitConversions", "-language:higherKinds", "-language:postfixOps",
    "-unchecked",
    "-Xfatal-warnings",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard",
    "-Xfuture"
  ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 10)) => Seq("-Yno-generic-signatures") // no generic signatures for scala 2.10.x, see SI-7932, #571 and #828
    case _             => Seq()
  }),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  scmInfo := Some(ScmInfo(url("https://github.com/julien-truffaut/newts"), "scm:git:git@github.com:julien-truffaut/newts.git"))
)

lazy val cats      = Def.setting("org.typelevel"   %%% "cats"       % "0.9.0")
lazy val catsLaws  = Def.setting("org.typelevel"   %%% "cats-laws"  % "0.9.0")

lazy val scalatest = Def.setting("org.scalatest"   %%% "scalatest"  % "3.0.1"  % "test")

lazy val tagName = Def.setting(
 s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}")

lazy val gitRev =
  sys.process.Process("git rev-parse HEAD").lines_!.head

lazy val scalajsSettings = Seq(
  scalacOptions += {
    val s = if (isSnapshot.value) gitRev else tagName.value
    val a = (baseDirectory in LocalRootProject).value.toURI.toString
    val g = "https://raw.githubusercontent.com/julien-truffaut/Monocle"
    s"-P:scalajs:mapSourceURI:$a->$g/$s/"
  },
  requiresDOM := false,
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck,
                           "-maxSize", "8",
                           "-minSuccessfulTests", "50")
)

lazy val newtsSettings    = buildSettings ++ publishSettings
lazy val newtsJvmSettings = newtsSettings
lazy val newtsJsSettings  = newtsSettings ++ scalajsSettings

lazy val newtsCrossSettings = (_: CrossProject)
  .jvmSettings(newtsJvmSettings: _*)
  .jsSettings(newtsJsSettings: _*)

lazy val newts = project.in(file("."))
  .settings(moduleName := "newts")
  .settings(newtsSettings)
  .aggregate(newtsJVM, newtsJS)
  .dependsOn(newtsJVM, newtsJS)

lazy val newtsJVM = project.in(file(".newtsJVM"))
  .settings(newtsJvmSettings)
  .aggregate(coreJVM, testJVM, bench)
  .dependsOn(
    coreJVM, testJVM % "test-internal -> test",
    bench % "compile-internal;test-internal -> test")

lazy val newtsJS = project.in(file(".newtsJS"))
  .settings(newtsJsSettings)
  .aggregate(coreJS, testJS)
  .dependsOn(coreJS, testJS  % "test-internal -> test")

lazy val coreJVM = core.jvm
lazy val coreJS  = core.js
lazy val core    = crossProject
  .settings(moduleName := "newts-core")
  .configureCross(newtsCrossSettings)
  .settings(libraryDependencies += cats.value)

lazy val testJVM = test.jvm
lazy val testJS  = test.js
lazy val test    = crossProject.dependsOn(core)
  .settings(moduleName := "newts-test")
  .configureCross(newtsCrossSettings)
  .settings(noPublishSettings: _*)
  .settings(libraryDependencies ++= Seq(cats.value, catsLaws.value, scalatest.value))

lazy val bench = project.dependsOn(coreJVM)
  .settings(moduleName := "newts-bench")
  .settings(newtsJvmSettings)
  .settings(noPublishSettings)
  .settings(libraryDependencies ++= Seq(cats.value, scalatest.value))
  .enablePlugins(JmhPlugin)


lazy val publishSettings = Seq(
  homepage := Some(url("https://github.com/julien-truffaut/Monocle")),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  autoAPIMappings := true,
  apiURL := Some(url("https://julien-truffaut.github.io/Monocle/api/")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo := version { (v: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  }.value,
  pomExtra :=
    <developers>
      <developer>
        <id>julien-truffaut</id>
        <name>Julien Truffaut</name>
      </developer>
    </developers>
  ,
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

addCommandAlias("validate", ";compile;test;unidoc;tut")

// For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
credentials ++= (for {
  username <- Option(System.getenv().get("SONATYPE_USERNAME"))
  password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
