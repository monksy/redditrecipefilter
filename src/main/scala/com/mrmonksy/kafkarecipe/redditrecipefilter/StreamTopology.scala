package com.mrmonksy.kafkarecipe.redditrecipefilter

import com.mrmonksy.kafkarecipe.redditrecipefilter.models.RedditPost
import io.github.azhur.kafkaserdejson4s.Json4sSupport._
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.json4s.{DefaultFormats, jackson}

object StreamTopology {

  private implicit val formats = DefaultFormats
  implicit val serialization = jackson.Serialization
  implicit val readPostSerdes: Serde[RedditPost] = toSerde

  def topology(): StreamsBuilder = {
    val builder: StreamsBuilder = new StreamsBuilder
    val textLines =
      builder.stream[String, RedditPost](StreamSettings.inputTopic)

    val allIds = textLines.mapValues(_.id)

    allIds.to("convertedIds")

    /*

    val wordCount: KTable[String, Long] = textLines
      .flatMapValues(words => words.split("\\W+"))
      .groupBy((_, word) => word)
      .count()
*/

    /*
        wordCount.toStream.print(Printed.toSysOut[String, Long])
        wordCount.toStream.to(StreamSettings.outputTopic)
    */

    builder
  }
}