package org.hackerschool.banditod

import scala.collection.JavaConverters._
import scala.collection.mutable.Map

/**
  TODO: Use covariance types declarations for passing different objects around.
  - something like BanditoUpdateCommand which has methods for validation and
    attributes specific to that command.

  */

class BanditodCommand(val command: Commands.Command, val data: Map[String, Any]) {
  /** JANK: use covariant classes. */
  val armId: String = this.getString(data, "armId")
  val expId: String = this.getString(data, "expId")
  val rewards: Int = this.getInt(data, "rewards")
  val offers: Int = this.getInt(data, "offers")
  val field: String = this.getString(data, "field")
  val value: Int = this.getInt(data, "value")

  var error: String = ""

  /** These 3 methods are place holders until we can use covariant classes to pass data. */
  def getInt(m: Map[String, Any], key: String): Int = {
    val res: Any = m.getOrElse(key, 0)
    res.asInstanceOf[Int]
  }
  def getString(m: Map[String, Any], key: String): String = {
    val res: Any = m.getOrElse(key, "")
    res.asInstanceOf[String]
  }

  def getBool(m: Map[String, Any], key: String): Boolean = {
    val res: Any = m.getOrElse(key, false)
    res.asInstanceOf[Boolean]
  }

  def isValid(): Boolean = {
    this.error == ""
  }

  /** Insures required arguments are not missing. */
  def validateUpdate(): Boolean = {
    this match {
      case x if x.armId == "" => {
        error = "Missing Arm ID"
        false
      }
      case x if x.expId == "" => {
        error = "Missing Experiment ID"
        false
      }
      case x if x.rewards == 0 && x.offers == 0 => {
        error = "Both Rewards and Offers are 0"
        false
      }
      case _ => true
    }
  }

  def validateIncrement(): Boolean = {
    this match {
      case x if x.armId == "" => {
        this.error = "Missing Arm ID"
        false
      }
      case x if x.expId == "" => {
        this.error = "Missing Experiment ID"
        false
      }
      case x if x.field != "rewards" && x.field != "offers" => {
        this.error = "Invalid Increment field: " + x.field
        false
      }
      case x if x.value <= 0 => {
        this.error = "Zero Increment found"
        false
      }
      case _ => true
    }
  }
  this.command match {
    case Commands.UPDATE => {
      this.validateUpdate()
    }
    case Commands.INCREMENT => {
      this.validateIncrement()
    }
    case _ => {
      this.error = "Unknown Command found"
    }
  }
}

/** Validates and returns a BanditodCommand argument instance. */
object CommandArgValidation {
  // TODO: Is this the first time this method has been written in Scala? If not, replace.
  def atoi(s: String): Int = {
    try {
      s.toInt
    } catch {
        case e: Exception => 0
    }
  }

  // Helper class for getting missing URL arguments.
  def safeGet(m: java.util.Map[String, java.util.List[String]], key: String): String = {
    val li: java.util.List[String] = m.get(key)
    var s: String = ""
    try {
      if (li == null || li.isEmpty) {
        // TODO: replace with logging
        println("list is empty for key" + key)
        ""
      } else {
        s = li.get(0)
      }
    } catch {
      case e: Exception => {
        // TODO: replace with logging
        println(m)
        println(key)
        println(li)
        ""
      }
    }
    s
  }

  def argsForCommand(m: java.util.Map[String, java.util.List[String]], command: Commands.Command): BanditodCommand = {
    if (command == Commands.UNKNOWN) new BanditodCommand(Commands.UNKNOWN, Map())
    // At the moment most commands require at least these arguments.
    val expId: String = this.safeGet(m, "expId")
    val armId: String = this.safeGet(m, "armId")
    var d: Map[String, Any] = Map("armId" -> armId, "expId" -> expId)

    // Check the command and acquire the command's arguments from the Map.
    command match {
      case Commands.UPDATE => {
        // Attempt to get the values for offers and rewards
        val srewards: String = this.safeGet(m, "rewards")
        val rewards: Int = this.atoi(srewards)

        val soffers: String = this.safeGet(m, "offers")
        val offers: Int = this.atoi(soffers)

        // Add these values to the previously acquired map.
        d ++= Map("rewards" -> rewards, "offers" -> offers)

        new BanditodCommand(Commands.UPDATE, d)
      }
      case Commands.INCREMENT => {
        val field: String = this.safeGet(m, "field")

        val svalue: String = this.safeGet(m, "value")
        val value: Int = this.atoi(svalue)

        // Add these values to the previously acquired map.
        d ++= Map("field" -> field, "value" -> value)

        new BanditodCommand(Commands.INCREMENT, d)
      }
      case _ => {
        var e: BanditodCommand = new BanditodCommand(Commands.UNKNOWN, Map())
        e.error = "ERROR: unknown command discovered during request validation."
        e
      }
    }
  }
}
