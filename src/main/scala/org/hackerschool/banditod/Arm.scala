package org.hackerschool.banditod

import net.fwbrasil.activate.storage.memory.TransientMemoryStorage
import BanditodStorageContext._

class BanditArm extends Entity

class RewardArm(
  val armId: String,
  val experimentId: String,
  var rewards: Int,
  var offers: Int) extends BanditArm