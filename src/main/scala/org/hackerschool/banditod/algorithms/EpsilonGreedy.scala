package org.hackerschool.banditod.algorithms

import scala.util.Random


case class EpsilonGreedy(epsilon: Double = 0.10,
                         var names: List[String] = List[String](),
                         var counts: List[Int] = List[Int](),
                         var values: List[Double] = List[Double]()) extends Algorithm {
  def selectArm() = {
    if (names.length == 0) {
      addArm("DEFAULT")
    }

    if (Random.nextDouble() > epsilon) {
      names(values.indexOf(values.max))
    } else {
      names(Random.nextInt(values.length))
    }
  }

  def selectFromSubset(arms: List[String]): String = {
    arms.length match {
      case 0 => selectArm()
      case 1 => arms(0)
      case _ => {
        // Get indexes of the subset of arm ids.
        val idxSubset: List[Int] = arms.map(a => names.indexOf(a)).filter(idx => idx > -1)
        if (idxSubset.length == 0) {
          ""
        } else if (Random.nextDouble() > epsilon) {
          val valuesSubset: List[Double] = idxSubset.map(idx => this.values(idx))
          // Get thee index or a random index of maximum values
          names(ListUtils.randMaxIndex(valuesSubset))
        } else {
          names(idxSubset(Random.nextInt(idxSubset.length)))
        }
      }
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

  def removeArm(name: String): Boolean = {
    val index = names.indexOf(name)
    if (index == -1) {
      return false
    }
    names = names.take(index) ++ names.drop(index + 1)
    counts = counts.take(index) ++ counts.drop(index + 1)
    values = values.take(index) ++ values.drop(index + 1)
    return true
  }

  def updateReward(arm: String, reward: Double = 1): Boolean = {
    var armIndex = names.indexOf(arm)

    if (armIndex == -1) {
      addArm(arm)
      armIndex = names.length - 1
    }

    val value = values(armIndex)
    val nCounts = 1 + counts(armIndex)
    val newValue = (nCounts - 1.0) / nCounts * value + 1.0 / nCounts * reward

    values = values.updated(armIndex, newValue)
    counts = counts.updated(armIndex, nCounts)

    return true
  }
}
