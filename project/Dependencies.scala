import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object Dependencies {

  object Versions {
    val scala = "2.13.8"
    val config = "1.4.2"
    val playMongodb = "0.20.13-play28"
    val scalaTest = "5.1.0"
    val playJson = "2.9.3"
    val play = "play28"
    val jsonCompat = "1.0.1-play28"
    val besnCompat = "0.20.13"

  }

  object Compiles {
    lazy val config: ModuleID = "com.typesafe" % "config" % Versions.config

    lazy val play: Seq[ModuleID] = Seq(
      // Enable reactive mongo for Play 2.8
      "org.reactivemongo" %% "play2-reactivemongo" % Versions.playMongodb,
      // Provide JSON serialization for reactive mongo
      "org.reactivemongo" %% "reactivemongo-play-json-compat" % Versions.jsonCompat,
      // Provide BSON serialization for reactive mongo
      "org.reactivemongo" %% "reactivemongo-bson-compat" % Versions.besnCompat,
      // Provide JSON serialization for Joda-Time
      "com.typesafe.play" %% "play-json-joda" % Versions.playJson,
    )

    lazy val scalaTest: ModuleID = "org.scalatestplus.play" %% "scalatestplus-play" % Versions.scalaTest % Test

  }

  import Compiles._

  lazy val dependencies: Setting[Seq[ModuleID]] =
    libraryDependencies ++=  Seq(config) ++  Seq(guice, scalaTest) ++ play

}
