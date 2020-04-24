lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.mrmonksy.kafkarecipe",
      scalaVersion := "2.12.6"
    )
  ),
  name := "RedditRecipeFilter",
  libraryDependencies := Seq(
    library.kafkaClients,
    library.kafkaStreams,
    library.log4jCore,
    library.typesafeConfig,
    library.kafkaTest,
    library.scalaTest,
    library.json4sJackson,
    library.json4sNative,
    library.json4sCore,
    "io.github.azhur" %% "kafka-serde-json4s" % "0.4.0"
  ),
  //  assemblyJarName in assembly := "redditrecipefilter-.jar",
  mainClass in assembly := Some("com.mrmonksy.kafkarecipe.redditrecipefilter.App")
)

lazy val library = new {

  val version = new {
    val kafkaVersion = "2.5.0"
    val scalaTest = "3.1.1"
    val log4jCore = "2.11.1"
    val typesafeConfig = "1.4.0"
    val json4s = "3.6.4"
  }

  val kafkaClients = "org.apache.kafka" % "kafka-clients" % version.kafkaVersion
  val kafkaStreams = "org.apache.kafka" %% "kafka-streams-scala" % version.kafkaVersion
  val log4jCore = "org.apache.logging.log4j" % "log4j-core" % version.log4jCore
  val typesafeConfig = "com.typesafe" % "config" % version.typesafeConfig

  val kafkaTest = "org.apache.kafka" % "kafka-streams-test-utils" % version.kafkaVersion
  val scalaTest = "org.scalatest" %% "scalatest" % version.scalaTest % "test"
  val json4sJackson = "org.json4s" %% "json4s-jackson" % version.json4s
  val json4sNative = "org.json4s" %% "json4s-native" % version.json4s
  val json4sCore = "org.json4s" %% "json4s-core" % version.json4s

}

assemblyMergeStrategy in assembly := {
  case "module-info.class" => MergeStrategy.discard
  case manifest if manifest.contains("MANIFEST.MF") =>
    // We don't need manifest files since sbt-assembly will create
    // one with the given settings
    MergeStrategy.discard
  case referenceOverrides if referenceOverrides.contains("reference-overrides.conf") =>
    // Keep the content for all reference-overrides.conf files
    MergeStrategy.concat
  case x =>
    // For all the other files, use the default sbt-assembly merge strategy
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}