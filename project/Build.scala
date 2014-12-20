import sbt._
import sbt.Keys._
import Process._
import Keys._

object ApplicationBuild extends Build {

  lazy val commonSettings = Seq(
    organization := "com.example",
    version := "0.1.0",
    scalaVersion := "2.11.4"
  )


  lazy val root = (project in file(".")).
    settings(commonSettings: _*).
    settings(
      name := "scalatag-builder",
      resolvers += Resolver.url("JCenter", url("http://jcenter.bintray.com/"))(Resolver.ivyStylePatterns),
      libraryDependencies ++= Seq(
        "org.jsoup" % "jsoup" % "1.8.1",
        "com.scalatags" %% "scalatags" % "0.4.2",
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
      )
    )

}