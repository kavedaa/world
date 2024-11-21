name := "world"

organization := "no.vedaadata"

version := "1.0.16.5"

scalaVersion := "3.3.3"

resolvers += "Vedaa Data Public" at "https://mymavenrepo.com/repo/UulFGWFKTwklJGmfuD8D/"

libraryDependencies ++= Seq(
  "org.shaqal" %% "shaqal-jtds" % "0.4.5",
	"org.scala-lang.modules" %% "scala-xml" % "2.0.1",
  "com.github.tototoshi" %% "scala-csv" % "1.3.10"
)

libraryDependencies ++= Seq(
	"com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
)

publishTo := Some("Vedaa Data Public publisher" at "https://mymavenrepo.com/repo/zPAvi2SoOMk6Bj2jtxNA/")

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)
