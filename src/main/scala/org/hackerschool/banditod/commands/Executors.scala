package org.hackerschool.banditod.commands
import org.hackerschool.banditod.{RewardArm,BanditodStorageContext}
import BanditodStorageContext._
import org.hackerschool.banditod.commands._

import scala.collection.JavaConverters._

// import java.util.List
// import java.util.Map


class BanditoExecutor {
 /** Gets or Creates RewardArm NOTE: must be called within a transaction. */
  def getOrCreateArm(armId: String, expId: String): RewardArm = {
    val result: List[RewardArm] = select[RewardArm] where (_.armId :== armId, _.experimentId :== expId)

    /** If Arm not found, create */
    if (result.isEmpty) {
      new RewardArm(armId, expId, 0, 0)
    } else {
      result.head
    }
  }
}


case class IncrementExecutor(args: IncrementArguments) extends BanditoExecutor {
  def execute: IncrementResults = {
    /** TODO: Catch db errors */
    transactional {
      val arm: RewardArm = this.getOrCreateArm(args.armId, args.expId)
      arm.rewards += args.rewards
      arm.offers += args.offers
    }
    IncrementResults(List())
  }
}
