lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """play2.9_api""",
    organization := "com.example",
    javaOptions ++= Seq(
      "-Dquill.query.tooLong=0",
      "-Dquill.binds.log=true"
    ),
    version := "1.0-SNAPSHOT",
    scalaVersion := "3.3.1",
    libraryDependencies ++= Seq(
      guice,
      ws,
      "org.scalatestplus.play" %% "scalatestplus-play" % "6.0.0" % Test,
      "com.oracle.database.jdbc" % "ojdbc8" % "18.3.0.0",
      "mysql" % "mysql-connector-java" % "8.0.33",
      "io.getquill" %% "quill-jdbc" % "4.8.0" exclude("org.scala-lang.modules" , "scala-java8-compat_3"),
      jdbc % Test
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-Werror"
    ),
  )
