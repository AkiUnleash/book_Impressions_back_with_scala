name := "book_Impressions_back_with_scala"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= {
  val AkkaVersion = "2.6.8"
  val AkkaHttpVersion = "10.2.4"
  val SlickVersion = "3.3.3"
  val MysqlVersion = "8.0.11"
  Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    "com.typesafe.slick" %% "slick" % SlickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
    "mysql" % "mysql-connector-java" % MysqlVersion,
  )
}
