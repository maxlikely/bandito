package org.hackerschool.banditod.algorithms


abstract class Algorithm() {
  def initialize(_names: List[String]): Unit
  def addArm(name: String): Unit

  def selectArm: String
  def updateReward(arm: String, reward: Double): Unit
}
