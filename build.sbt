import Dependencies._
import sbt._

name         := "lingxi_scala"
organization := "com.zhengzhicong"
maintainer   := "1064275075@qq.com"
version      := "1.0.0-SNAPSHOT"
scalaVersion := Versions.scala

//libraryDependencySchemes := Seq(
//  "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
//)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, ScalafmtPlugin, GraalVMNativeImagePlugin, DockerPlugin, JavaServerAppPackaging)
  .settings(
    dependencies,
    scalafmtOnCompile := true,
    Compile / unmanagedResourceDirectories += baseDirectory.value / "conf",
    graalVMNativeImageOptions := Seq(
      "--no-fallback",
      "-H:ResourceConfigurationFiles=../../graal-conf/resource-config.json",
      "-H:ReflectionConfigurationFiles=../../graal-conf/reflect-config.json",
      "-H:JNIConfigurationFiles=../../graal-conf/jni-config.json",
      "-H:DynamicProxyConfigurationFiles=../../graal-conf/proxy-config.json",
      "-H:Log=registerResource:3"
    ),
//    assembly / mainClass := Some("play.core.server.ProdServerStart"),
//    assembly / fullClasspath += Attributed.blank(PlayKeys.playPackageAssets.value),
//    assembly / assemblyMergeStrategy := {
//      case PathList("javax", "servlet", xs @ _*)                                  => MergeStrategy.first
//      case PathList("javax", "xml", xs @ _*)                                      => MergeStrategy.first
//      case PathList("javax", "activation", xs @ _*)                               => MergeStrategy.first
//      case PathList("org", "apache", xs @ _*)                                     => MergeStrategy.first
//      case PathList("module-info.class", xs @ _*)                                 => MergeStrategy.discard
//      case "META-INF/maven/org.webjars/swagger-ui/pom.properties"                 => MergeStrategy.first
//      case "/META-INF/resources/webjars/swagger-ui/4.15.5/swagger-initializer.js" => MergeStrategy.first
//      case PathList("META-INF", xs @ _*)                                          => MergeStrategy.discard
//      case "logback.xml"                                                          => MergeStrategy.concat
//      case "application.conf"                                                     => MergeStrategy.concat
//      case PathList("play", "reference-overrides.conf")                           => MergeStrategy.concat
//      case x =>
//        val oldStrategy = (assembly / assemblyMergeStrategy).value
//        oldStrategy(x)
//    }
  )

