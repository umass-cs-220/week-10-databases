name := "anorm"

organization := "timdrichards"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalikejdbc" %% "scalikejdbc"       % "2.2.5",
  "com.h2database"  %  "h2"                % "1.4.186",
  "ch.qos.logback"  %  "logback-classic"   % "1.1.2"
)
