import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object Dependencies {

  object Versions {
    val scala = "2.12.13"
    val config = "1.4.2"
    val scalaTest = "5.1.0"
    val playJson = "2.9.3"
    val slick = "5.0.2"
    val postgresql = "42.5.0"
  }

  object Compiles {
    lazy val config = "com.typesafe" % "config" % Versions.config

    lazy val play = Seq(
      "com.typesafe.play" %% "play-slick" % Versions.slick
    )

    lazy val postgresql = "org.postgresql" % "postgresql" % Versions.postgresql

    lazy val scalaTest = "org.scalatestplus.play" %% "scalatestplus-play" % Versions.scalaTest % Test

  }

  import Compiles._

  lazy val dependencies: Setting[Seq[ModuleID]] =
    libraryDependencies ++=  Seq(config, postgresql, guice, scalaTest) ++ play

}
