package org.hackerschool.banditod

import BanditodStorageContext._


class BanditArm extends Entity

class RewardArm(
  val armId: String,
  val experimentId: String,
  var rewards: Int,
  var offers: Int) extends BanditArm
