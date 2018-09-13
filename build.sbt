import CompilerSettings._

lazy val projectSettings = Seq(
    organization := "coop.rchain",
    name := "rsong-acquisition",
    scalaVersion := "2.12.6",
    scalafmtOnCompile := true
  )

lazy val commonSettings = projectSettings ++ CompilerSettings.options

lazy val root = (project in file("."))
  .settings(projectSettings)
  .settings(
    libraryDependencies ++= {
      object V {
        val http4s = "0.19.0-M1"
        val specs2 = "4.2.0"
        val logback = "1.2.3"
        val scalalogging = "3.9.0"
        val config = "1.3.3"
        val scalapb= "0.7.4"
        val dropbox="3.0.8"
        val circie="0.9.3"
        val catsEffect="1.0.0-RC3"
      }
      Seq(
        "org.typelevel" %% "cats-effect" % "1.0.0",
       "io.monix" %% "monix" % "3.0.0-RC1" ,
        "io.circe" %% "circe-core" % V.circie,
        "io.circe" %% "circe-generic" % V.circie,
        "io.circe" %% "circe-parser" % V.circie,
        "org.specs2" %% "specs2-core" % V.specs2 % "test",
        "com.typesafe" %  "config" % V.config,
        "com.typesafe.scala-logging" %% "scala-logging" % V.scalalogging, 
        "com.thesamet.scalapb" %% "compilerplugin" % V.scalapb,
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
        "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
        "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
        "ch.qos.logback" % "logback-classic" % V.logback
      )})

scalacOptions in Compile ++= CompilerSettings.options

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value)

enablePlugins(JavaServerAppPackaging)

dockerRepository := Some("kayvank")
dockerUpdateLatest := true
version in Docker := version.value + "-" + scala.sys.env.getOrElse(
  "CIRCLE_BUILD_NUM", default = "local")
