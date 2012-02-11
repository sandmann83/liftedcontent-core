import sbt._
import Keys._
    
object CoreBuild extends Build {
  val liftVersion = "2.4"

  override lazy val settings = super.settings ++ Seq(
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.10" % "test",
      "nl.bitwalker" % "UserAgentUtils" % "1.2.4" % "compile->default" withSources,
      "org.specs2" %% "specs2" % "1.7.1" % "test",
      "org.slf4j" % "slf4j-log4j12" % "1.6.4",
      "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-util" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-common" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-wizard" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-widgets" % liftVersion % "compile->default" withSources,
      "eu.sbradl" %% "liftedcontent-util" % "1.0.0" % "compile",
      "eu.sbradl" %% "liftedcontent-rte" % "1.0.0" % "compile",
      "eu.sbradl" %% "liftedcontent-repository" % "1.0.0" % "compile"
    )
  )


  lazy val root = Project("Core", file(".")) settings(Project.defaultSettings:_*)
} 
