package org.hackerschool.banditod.algorithms

import scala.util.Random


case class EpsilonGreedy(epsilon: Double,
                         var names: List[String] = List[String](),
                         var counts: List[Int] = List[Int](),
                         var values: List[Double] = List[Double]()) extends Algorithm {
  def selectArm() = {
    if (Random.nextDouble() > epsilon) {
      names(values.indexOf(values.max))
    } else {
      names(Random.nextInt(values.length))
    }
  }

  def initialize(_names: List[String]) = {
    names = _names
    val numArms = names.length

    counts = List.range(0, numArms).map(_ => 0)
    values = List.range(0, numArms).map(_ => 0.0)
  }

  def addArm(name: String) = {
    names ::= name
    counts ::= 0
    values ::= 0.0
  }
    
  def updateReward(arm: String, reward: Double = 1) = {
    val armIndex = names.indexOf(arm)

    val value = values(armIndex)
    val nCounts = 1 + counts(armIndex)
    val newValue = (nCounts - 1.0) / nCounts * value + 1.0 / nCounts * reward

    values = values.updated(armIndex, newValue)
    counts = counts.updated(armIndex, nCounts)
  }
}
