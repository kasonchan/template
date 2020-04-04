val name = "template"

lazy val buildSettings = Seq(
  organization := "com.kasonchan",
  version := "0.0.1",
  scalaVersion := "2.13.1"
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

  coverageEnabled := true,
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
