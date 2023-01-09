import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

import scala.language.postfixOps

object Dependencies {

  object Versions {
    val scala = "2.13.8"
    val config = "1.4.2"
    val scalaTest = "5.1.0"
    val playJson = "2.9.3"
    val slick = "5.1.0"
    val postgresql = "42.5.0"
    val bcrypt = "0.4"
    val tapir = "1.1.2"
    val aliyunOss = "3.15.2"
    val kaptcha = "2.3.2"
    val guice = "5.1.0"
  }

  object Compiles {
    lazy val config = "com.typesafe" % "config" % Versions.config

    lazy val bcrypt = "org.mindrot" % "jbcrypt" % Versions.bcrypt

    lazy val slick: Seq[ModuleID] = Seq(
      "com.typesafe.play" %% "play-slick" % Versions.slick
    )

    lazy val tapir: Seq[ModuleID] = Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % Versions.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-play-server" % Versions.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-json-play" % Versions.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % Versions.tapir,
      "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.2.1",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % Versions.tapir
    )

    lazy val postgresql = "org.postgresql" % "postgresql" % Versions.postgresql

    val aliyunOss = "com.aliyun.oss" % "aliyun-sdk-oss" % Versions.aliyunOss

    val guice_5_1_0: Seq[ModuleID] = Seq(
      "com.google.inject" % "guice" % Versions.guice,
      "com.google.inject.extensions" % "guice-assistedinject" % Versions.guice
    )

    val kaptcha = "com.github.penggle" % "kaptcha" % Versions.kaptcha

    lazy val scalaTest = "org.scalatestplus.play" %% "scalatestplus-play" % Versions.scalaTest % Test

  }

  import Compiles._

  lazy val dependencies: Setting[Seq[ModuleID]] =
    libraryDependencies ++= Seq(config, ws, guice ,caffeine, postgresql, bcrypt, kaptcha, aliyunOss, scalaTest) ++ slick ++ tapir ++ guice_5_1_0

}
