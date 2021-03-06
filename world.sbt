name := "world"

organization := "no.vedaadata"

version := "1.0.6"

scalaVersion := "2.13.4"

crossScalaVersions := Seq("2.11.12", "2.12.10")

resolvers += "My Maven Repo Resolver" at "https://mymavenrepo.com/repo/pINely5F8nmLUayJnPul/"

libraryDependencies ++= Seq(
  "org.shaqal" %% "shaqal-jtds" % "0.4.4",
	"org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "com.github.tototoshi" %% "scala-csv" % "1.3.6"
)

libraryDependencies ++= Seq(
	"com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
)

publishTo := Some("My Maven Repo Publisher" at "https://mymavenrepo.com/repo/j1YxfckeUitD5ZGTAisl")

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)
