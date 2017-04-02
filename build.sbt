lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "bike_sharing",
    version := "1.1.0",
    scalaVersion := "2.11.8"
  )
