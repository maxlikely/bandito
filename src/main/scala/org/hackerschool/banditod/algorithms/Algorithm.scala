package org.hackerschool.banditod.algorithms


abstract class Algorithm() {
  val algorithmName = this.getClass.getName

  def initialize(_names: List[String]): Unit
  def addArm(name: String): Unit

  def selectArm: String
  def removeArm(name: String): Boolean
  def updateReward(arm: String, reward: Double = 1.0): Boolean
}
