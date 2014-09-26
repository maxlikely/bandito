package org.hackerschool.banditod

import org.hackerschool.banditod.algorithms.Algorithm


case class Experiment(algorithm: Algorithm, name: String)

object Registry {
  private var experiments: Map[String, Experiment] = Map()

  def register(experiment: Experiment) = {
    experiments = experiments + (experiment.name -> experiment)
  }

  def listExperiments() = {
    for ((name, experiment) <- experiments) println(name + " -- " + experiment.algorithm)
  }
}
