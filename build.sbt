name := "banditod"

version := "0.1"

resolvers += "Twitter" at "http://maven.twttr.com"

libraryDependencies += "com.twitter" %% "finatra" % "1.5.3"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test"
