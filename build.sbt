name := "book_Impressions_back_with_scala"

version := "0.1"

scalaVersion := "2.13.6"


  val AkkaVersion = "2.6.8"
  val AkkaHttpVersion = "10.2.4"
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
  )
