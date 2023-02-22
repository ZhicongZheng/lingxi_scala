import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

import scala.language.postfixOps

object Dependencies {

  object Versions {
    val scala      = "2.13.8"
    val config     = "1.4.2"
    val scalaTest  = "5.1.0"
    val playJson   = "2.9.3"
    val slick      = "5.1.0"
    val slickCodeGen = "3.4.1"
    val postgresql = "42.5.4"
    val bcrypt     = "0.4"
    val tapir      = "1.2.9"
    val aliyunOss  = "3.16.1"
    val kaptcha    = "2.3.2"
    val guice      = "5.1.0"
    val mailer     = "8.0.1"
    val slick_pg   = "0.21.1"
  }

  object Compiles {
    val config = "com.typesafe" % "config" % Versions.config

    val bcrypt = "org.mindrot" % "jbcrypt" % Versions.bcrypt

    val slick: Seq[ModuleID] = Seq(
      "com.typesafe.play"  %% "play-slick"    % Versions.slick,
      "com.typesafe.slick" %% "slick-codegen" % Versions.slickCodeGen
    )

    val tapir: Seq[ModuleID] = Seq(
      "com.softwaremill.sttp.tapir"   %% "tapir-core"         % Versions.tapir,
      "com.softwaremill.sttp.tapir"   %% "tapir-play-server"  % Versions.tapir,
      "com.softwaremill.sttp.tapir"   %% "tapir-json-play"    % Versions.tapir,
      "com.softwaremill.sttp.tapir"   %% "tapir-openapi-docs" % Versions.tapir,
      "com.softwaremill.sttp.tapir"   %% "tapir-swagger-ui"   % Versions.tapir,
      "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.3.2",
    )

    val slick_pg: Seq[ModuleID] = Seq(
      "com.github.tminglei" %% "slick-pg"           % Versions.slick_pg,
      "com.github.tminglei" %% "slick-pg_play-json" % Versions.slick_pg
    )

    val postgresql = "org.postgresql" % "postgresql" % Versions.postgresql

    val aliyunOss = "com.aliyun.oss" % "aliyun-sdk-oss" % Versions.aliyunOss

    val guice_5_1_0: Seq[ModuleID] = Seq(
      "com.google.inject"            % "guice"                % Versions.guice,
      "com.google.inject.extensions" % "guice-assistedinject" % Versions.guice
    )

    val mailer: Seq[ModuleID] = Seq(
      "com.typesafe.play" %% "play-mailer"       % Versions.mailer,
      "com.typesafe.play" %% "play-mailer-guice" % Versions.mailer
    )

    val kaptcha = "com.github.penggle" % "kaptcha" % Versions.kaptcha

    val scalaTest = "org.scalatestplus.play" %% "scalatestplus-play" % Versions.scalaTest % Test

  }

  import Compiles._

  lazy val dependencies: Setting[Seq[ModuleID]] =
    libraryDependencies ++= Seq(
      config,
      ws,
      guice,
      caffeine,
      postgresql,
      bcrypt,
      kaptcha,
      aliyunOss,
      scalaTest
    ) ++ slick ++ slick_pg ++ tapir ++ guice_5_1_0 ++ mailer

}
