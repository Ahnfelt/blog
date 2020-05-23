ThisBuild / organization := "com.github.ahnfelt"
ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
    .settings(
        name := "site",
        libraryDependencies += "com.vladsch.flexmark" % "flexmark-all" % "0.61.30"
    )
