import Dependencies._
import sbt._

name := """lingxi_scala"""
organization := "com.lingxi"
version := "1.0.0-SNAPSHOT"
scalaVersion := Versions.scala

lazy val root = (project in file(".")).enablePlugins(PlayScala).settings(dependencies)
