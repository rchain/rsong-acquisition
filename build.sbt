import CompilerSettings._

lazy val projectSettings = Seq(
    organization := "coop.rchain",
    name := "rsong-acquisition",
    scalaVersion := "2.12.6"
  )

lazy val root = (project in file("."))
  .settings(projectSettings: _*)
  .settings(
    libraryDependencies ++= {
      object V {
        val specs2 = "4.2.0"
        val logback = "1.2.3"
        val scalalogging = "3.9.0"
        val config = "1.3.3"
        val scalapb= "0.7.4"
        val circie="0.9.3"
        val catsEffect="1.0.0"
        val monix="3.0.0-RC1"
      }
      Seq(
        "org.specs2" %% "specs2-core" % V.specs2 % "test",
        "org.typelevel" %% "cats-effect" % V.catsEffect,
       "io.monix" %% "monix" % V.monix ,
        "io.circe" %% "circe-core" % V.circie,
        "io.circe" %% "circe-generic" % V.circie,
        "io.circe" %% "circe-parser" % V.circie,
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
