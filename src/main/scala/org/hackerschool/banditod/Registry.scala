package org.hackerschool.banditod

import org.hackerschool.banditod.algorithms.{Algorithm, EpsilonGreedy}


case class Experiment(name: String, algorithm: Algorithm) 

object Experiment {
  def fromString(name: String): Experiment = {
    val algorithm = EpsilonGreedy()
    Experiment(name, algorithm)
  }
}

object Registry {
  private var _experiments: Map[String, Experiment] = Map()
  val algorithms = Map("EpsilonGreedy" -> EpsilonGreedy)

  def register(experiment: Experiment): Boolean = {
    if (_experiments contains experiment.name) {
      false
    } else {
      _experiments = _experiments + (experiment.name -> experiment)
      true
    }
  }

  def delete(experiment: String): Boolean = {
    if (_experiments contains experiment) {
      _experiments = _experiments - experiment
      true
    } else {
      false
    }
  }

  def experiments(): Map[String, Experiment] = {
    _experiments
  }
}
