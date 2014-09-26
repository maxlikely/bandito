package org.hackerschool.banditod.commands

import org.hackerschool.banditod.{RewardArm,BanditodStorageContext}
import org.hackerschool.banditod.commands._
import BanditodStorageContext._


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

  /** NOTE: must be called within a transaction. */
  def getArmsForExpID(expId: String): List[RewardArm] = {
    select[RewardArm] where (_.experimentId :== expId)
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

case class QueryExperimentExecutor(args: QueryExperimentArguments) extends BanditoExecutor {
  def execute: QueryExperimentResults = {
    /** TODO: Catch db errors */
    val arms: List[RewardArm] = transactional {
      this.getArmsForExpID(args.expId)
    }
    QueryExperimentResults(arms, List())
  }
}
