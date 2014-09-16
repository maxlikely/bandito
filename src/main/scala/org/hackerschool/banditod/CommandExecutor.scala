package org.hackerschool.banditod

import BanditodStorageContext._
import Commands._

import scala.collection.JavaConverters._

import java.util.List
// import java.util.Map
/**
  TODO: use covariance to return results from subclasses of ExecutionResult.
  - example: BanditoUpdateResult will contain methods for serialization
  */


/** Base abstract class for execution result */
abstract class ExecutionResult {
  var data: Map[String, Any]
  val error: String
}


class CommandExecutor() {
  /** Gets or Creates RewardArm NOTE: must be called within a transaction. */
  def getOrCreateArm(armId: String, expId: String): RewardArm = {
      /** Query for arm. */
      var result: scala.collection.immutable.List[RewardArm] = select[RewardArm] where
      (_.armId :== armId, _.experimentId :== expId)

      /** If Arm not found, create */
      if (result.isEmpty) {
        // Create new object.
        new RewardArm(armId, expId, 0, 0)
      } else {
        result.head
      }
    }

  /** These 3 methods are place holders until we can use covariant classes to pass data. */
  def getInt(m: java.util.Map[String, Any], key: String): Int = {
    val res: Any = m.asScala.getOrElse(key, 0)
    res.asInstanceOf[Int]
  }
  def getString(m: java.util.Map[String, Any], key: String): String = {
    val res: Any = m.asScala.getOrElse(key, "")
    res.asInstanceOf[String]
  }

  def getBool(m: java.util.Map[String, Any], key: String): Boolean = {
    val res: Any = m.asScala.getOrElse(key, false)
    res.asInstanceOf[Boolean]
  }

  /** Returns true on success, false on error or failure. It is assumed that
    the argument is a valid command, checked beforehand with isValid().*/
  def execute(command: Commands.Command, data: java.util.Map[String, Any]): scala.collection.mutable.Map[String, Any] = {
    command match {
      case Commands.UPDATE => {
        var armId: String = this.getString(data, "armId")
        var expId: String = this.getString(data, "expId")
        transactional {
          var arm: RewardArm = this.getOrCreateArm(armId, expId)
          arm.rewards = this.getInt(data, "rewards")
          arm.offers = this.getInt(data, "offers")
        }
        scala.collection.mutable.Map("success" -> true)
      }
      case Commands.INCREMENT => {
        this.getString(data, "field") match {
          case x if x == "rewards" => {
            var armId: String = this.getString(data, "armId")
            var expId: String = this.getString(data, "expId")
            transactional {
              var arm: RewardArm = this.getOrCreateArm(armId, expId)
              arm.rewards += this.getInt(data, x)
            }
            scala.collection.mutable.Map("success" -> true)
          }
          case x if x == "offers" => {
            var armId: String = this.getString(data, "armId")
            var expId: String = this.getString(data, "expId")

            transactional {
              var arm: RewardArm = this.getOrCreateArm(armId, expId)
              arm.offers += this.getInt(data, x)
            }
            scala.collection.mutable.Map("success" -> true)
          }
          case _ => scala.collection.mutable.Map("success" -> false)
        }
      }
      case _ => scala.collection.mutable.Map("success" -> false)
    }
  }
}
