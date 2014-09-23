package org.hackerschool.banditod.commands

import BanditodStorageContext._
import org.hackerschool.banditod.commands._

import scala.collection.JavaConverters._

import java.util.List
// import java.util.Map


class BanditoExecutor {
 /** Gets or Creates RewardArm NOTE: must be called within a transaction. */
  def getOrCreateArm(armId: String, expId: String): RewardArm = {
    val result: RewardArm = select[RewardArm] where (_.armId :== armId, _.experimentId :== expId)

    /** If Arm not found, create */
    if (result.isEmpty) {
      new List(RewardArm(armId, expId, 0, 0))
    } else {
      result.head
    }
  }
}


case class IncrementExecutor(args: IncrementArguments) extends BanditoExecutor {
  def execute: IncrementResults = {
    args.field match {
      case args.RewardsKey {
        val success = transactional {
          val arm: RewardArm = this.getOrCreateArm(args.armId, args.expId)
          arm.rewards += args.rewards
        }
        println("success:")
        println(success)
        /** TODO: return correct result*/
        IncrementResults(List(), true)
      }

      case args.OffersKey {
        transactional {
          val arm: RewardArm = this.getOrCreateArm(args.armId, args.expId)
          arm.offers += args.offers
        }
      }

      case _ => IncrementResults(List(
    }
  }
}

class CommandExecutor() {

  /** Returns an HttpResponse. */
  def execute[T >: BanditoArguments](command: BanditoCommand): BanditoResult = {

    command match {
      case UpdateCommand => {
        val success = transactional {
          var arm: RewardArm = this.getOrCreateArm(command.armId, command.expId)
          arm.rewards = command.rewards
          arm.offers = command.offers
        }
        command
      }

      case _ => scala.collection.mutable.Map("success" -> false)
    }
  }
}
