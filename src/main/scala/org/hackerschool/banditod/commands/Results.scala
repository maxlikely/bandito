package org.hackerschool.banditod.commands

import org.hackerschool.banditod.{RewardArm}

import scala.collection.JavaConverters._

import com.google.gson.{Gson}



abstract class BanditoResults

/** TODO: handle JSON serialization */



case class IncrementResults(errors: List[String]) extends BanditoResults



case class QueryExperimentResults(arms: List[RewardArm], errors: List[String]) extends BanditoResults {
  /** Returns a list of Java maps */
  def armsToMaps = {
    val serialized = this.arms map {
      case m => {
        Map("rewards" -> m.rewards, "offers" -> m.offers).asJava
      }
    }
    serialized
  }

  class jsonObject(
    val errors: java.util.List[String],
    val results: java.util.List[java.util.Map[String, Int]])

  def asJSON: String = {
    val json: Gson = new Gson()
    val jsonObject = new jsonObject(this.errors.asJava, this.armsToMaps.asJava)
    json.toJson(jsonObject)
  }
}
