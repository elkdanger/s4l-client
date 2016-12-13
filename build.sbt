scalaVersion := "2.11.7"

resolvers := Seq(
    Resolver.bintrayRepo("hmrc", "releases")
)

libraryDependencies += "uk.gov.hmrc" %% "crypto" % "4.1.0"
libraryDependencies += "uk.gov.hmrc" % "json-encryption_2.11" % "3.1.0"
libraryDependencies += "org.mongodb.scala" % "mongo-scala-driver_2.11" % "1.2.1"
