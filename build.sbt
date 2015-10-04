name := """TaxiTube_Chat"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  ws, // Play's web services module
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.4",
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.webjars" % "bootbox" % "4.4.0",
  "org.webjars.npm" % "bootstrap-growl" % "3.1.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test"
)
