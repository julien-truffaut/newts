import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.scalajs.sbtplugin.cross.CrossProject
import sbt.Keys._
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
import sbtunidoc.Plugin.UnidocKeys._

lazy val buildSettings = Seq(
  organization       := "com.github.julien-truffaut",
  scalaVersion       := "2.12.3",
  crossScalaVersions := Seq("2.12.3", "2.11.11"),
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
  addCompilerPlugin(kindProjector),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  scmInfo := Some(ScmInfo(url("https://github.com/julien-truffaut/newts"), "scm:git:git@github.com:julien-truffaut/newts.git"))
)

lazy val catsVersion = "1.0.0-MF"
lazy val cats      = Def.setting("org.typelevel"   %%% "cats-core"  % catsVersion)
lazy val catsLaws  = Def.setting("org.typelevel"   %%% "cats-laws"  % catsVersion)

lazy val scalatest = Def.setting("org.scalatest"                %%% "scalatest"     % "3.0.1"  % "test")

lazy val kindProjector  = "org.spire-math"  % "kind-projector" % "0.9.3" cross CrossVersion.binary

lazy val tagName = Def.setting(
 s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}")

lazy val gitRev =
  sys.process.Process("git rev-parse HEAD").lines_!.head

lazy val scalajsSettings = Seq(
  scalacOptions += {
    val s = if (isSnapshot.value) gitRev else tagName.value
    val a = (baseDirectory in LocalRootProject).value.toURI.toString
    val g = "https://raw.githubusercontent.com/julien-truffaut/newts"
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
  .aggregate(coreJVM, testJVM, bench, docs)
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

lazy val docs = project.dependsOn(coreJVM)
  .enablePlugins(MicrositesPlugin)
  .settings(moduleName := "newts-docs")
  .settings(newtsSettings)
  .settings(noPublishSettings)
  .settings(unidocSettings)
  .settings(ghpages.settings)
  .settings(docSettings)
  .settings(tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))))
  .settings(
    libraryDependencies ++= Seq(cats.value)
  )

lazy val docsMappingsAPIDir = settingKey[String]("Name of subdirectory in site target directory for api docs")

lazy val docSettings = Seq(
  micrositeName := "newts",
  micrositeDescription := "Newtypes for cats",
  micrositeHighlightTheme := "atom-one-light",
  micrositeHomepage := "http://julien-truffaut.github.io/newts",
  micrositeBaseUrl := "/newts",
  micrositeDocumentationUrl := "/newts/api",
  micrositeGithubOwner := "julien-truffaut",
  micrositeGithubRepo := "newts",
  micrositePalette := Map(
    "brand-primary"   -> "#5B5988",
    "brand-secondary" -> "#292E53",
    "brand-tertiary"  -> "#222749",
    "gray-dark"       -> "#49494B",
    "gray"            -> "#7B7B7E",
    "gray-light"      -> "#E5E5E6",
    "gray-lighter"    -> "#F4F3F4",
    "white-color"     -> "#FFFFFF"),
  autoAPIMappings := true,
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(coreJVM),
  docsMappingsAPIDir := "api",
  addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), docsMappingsAPIDir),
  ghpagesNoJekyll := false,
  fork in tut := true,
  fork in (ScalaUnidoc, unidoc) := true,
  scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
    "-Xfatal-warnings",
    "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath,
    "-diagrams"
  ),
  git.remoteRepo := "git@github.com:julien-truffaut/newts.git",
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md"
)

lazy val publishSettings = Seq(
  homepage := Some(url("https://github.com/julien-truffaut/newts")),
  licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  autoAPIMappings := true,
  apiURL := Some(url("https://julien-truffaut.github.io/newts/api/")),
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
