name := """moa"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.python" % "jython-standalone" % "2.5.3",
  "org.pygments" % "pygments" % "1.6"
)

javaOptions ++= Seq("-Xmx1G", "-Xms512M", "-XX:+UseG1GC", "-XX:MaxPermSize=512M", "-XX:+CMSClassUnloadingEnabled")
