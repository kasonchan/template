import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

val name = "template"

lazy val buildSettings = Seq(
  organization := "com.kasonchan",
  version := "0.0.1",
  scalaVersion := "2.13.1"
) ++ rpmBuildSettings ++ dockerBuildSettings

lazy val rpmBuildSettings = Seq(
  maintainer in Linux := "Kason Chan <kasonl.chan@gmail.com>",
  packageSummary in Linux := "Template",
  packageDescription in Rpm := "Template",
  rpmBrpJavaRepackJars := true,
  rpmRelease := "1",
  rpmVendor := "kasonchan",
  rpmGroup := Some("template-rpm"),
  rpmUrl := Some("https://github.com/kasonchan/template"),
  rpmLicense := Some("Apache v2"),
  rpmChangelogFile := None
)

lazy val dockerBuildSettings = Seq(
  mappings in Docker += file(s"target/rpm/RPMS/noarch/$name-${version.value}-${rpmRelease.value}.noarch.rpm") ->
    s"${name}-${version.value}-${rpmRelease.value}.noarch.rpm",
  mappings in Docker += file("scripts/start.sh") -> "start.sh",
  mappings in Docker += file("scripts/stop.sh") -> "stop.sh",
  dockerExposedPorts := Seq(11010),
  dockerCommands := Seq(
    Cmd("FROM", "centos:latest"),
    Cmd("EXPOSE", "11010"),
    ExecCmd("RUN", "cat", "/etc/centos-release"),
    Cmd("WORKDIR", "/workspace"),
    Cmd("ADD", s"$name-${version.value}-${rpmRelease.value}.noarch.rpm", "/workspace"),
    Cmd("ADD", "start.sh", "/workspace"),
    Cmd("ADD", "stop.sh", "/workspace"),
    Cmd("RUN", "yum", "install", "-y", "java-1.8.0-openjdk"),
    Cmd("RUN", "rpm2cpio", s"$name-${version.value}-${rpmRelease.value}.noarch.rpm", "|", "cpio", "-idmv"),
    ExecCmd("RUN", "ls", "-laRth", ".")
  )
)

lazy val akkaHttpVersion = "10.1.11"
lazy val akkaVersion    = "2.6.4"

lazy val compilerOptions = Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-language:postfixOps"
)

lazy val buildDependencies = Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,

  "ch.qos.logback"    % "logback-classic" % "1.2.3",
)

val testDependencies = Seq(
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "it,test",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % "it,test",
  "org.scalatest"     %% "scalatest" % "3.1.1" % "it,test"
)

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

val baseSettings = Seq(
  scalafmtOnCompile := true,

  scalastyleFailOnError := true,
  scalastyleFailOnWarning := true,

  coverageEnabled in(Test, compile) := true,
  coverageEnabled in(Compile, compile) := false,
  coverageMinimum := 100,
  coverageFailOnMinimum := true,

  libraryDependencies ++= buildDependencies ++ testDependencies,
  scalacOptions in(Compile, console) := compilerOptions,

  compileScalastyle := scalastyle.in(Compile).toTask("").value,
  (compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value,

  parallelExecution in ThisBuild := false
)

lazy val allSettings = baseSettings ++ buildSettings

lazy val template = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    allSettings
  )
  .enablePlugins(JavaServerAppPackaging, SystemdPlugin, RpmPlugin, DockerPlugin)
