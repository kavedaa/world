name := "world"

organization := "no.vedaadata"

version := "1.0.14"

scalaVersion := "2.13.10"

crossScalaVersions := Seq("2.13.10", "3.2.2")

resolvers += "My Maven Repo Resolver" at "https://mymavenrepo.com/repo/pINely5F8nmLUayJnPul/"

libraryDependencies ++= Seq(
  "org.shaqal" %% "shaqal-jtds" % "0.4.5",
	"org.scala-lang.modules" %% "scala-xml" % "2.0.1",
  "com.github.tototoshi" %% "scala-csv" % "1.3.10"
)

libraryDependencies ++= Seq(
	"com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
)

publishTo := Some("My Maven Repo Publisher" at "https://mymavenrepo.com/repo/j1YxfckeUitD5ZGTAisl")

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)
