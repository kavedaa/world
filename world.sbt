name := "world"

organization := "no.vedaadata"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
"org.shaqal" %% "shaqal-jtds" % "0.4-SNAPSHOT",
	"org.scala-lang.modules" %% "scala-xml" % "1.0.2",
	"com.bizo" %% "mighty-csv" % "0.2"
)

libraryDependencies ++= Seq(
	"com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
)
