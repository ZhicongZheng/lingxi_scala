import Dependencies._
import sbt._

name         := "lingxi_scala"
organization := "com.zhengzhicong"
maintainer   := "1064275075@qq.com"
version      := "1.0.0-SNAPSHOT"
scalaVersion := Versions.scala

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, ScalafmtPlugin)
  .settings(dependencies, scalafmtOnCompile := true)
