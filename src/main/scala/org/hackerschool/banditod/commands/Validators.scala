package org.hackerschool.banditod.commands

// Convert a java.util.Map to a Scala
import scala.collection.JavaConverters._


/** Validates the arguments and handles type conversion for a URL command. */
class URLArguments(arguments: java.util.Map[String, java.util.List[String]]) {

  /** Returns the string and a list of errors; if there are errors the string is empty. */
  def validateString(key: String, required: Boolean): (String, List[String]) = {
    val list: java.util.List[String] = this.arguments.get(key)
    val error: List[String] = list match {
      case null => {
        if (required) {
          List(key + " key is missing")
        } else {
          List()
        }
      }
      case li if li.isEmpty => {
        if (required) {
          List(key + " key contains no value")
        } else {
          List()
        }
      }
      case _ => List()
    }

    if (error.isEmpty) {
      (list.asScala.head, error)
    } else {
      ("", error)
    }
  }

  /** Returns the string and a list of errors; if there are errors the string is empty. */
  def validateInt(key: String, required: Boolean): (Int, List[String]) = {
    // Errors can be is missing, or empty.
    val list: java.util.List[String] = this.arguments.get(key)
    val error: List[String] = list match {
      case null => List(key + " key is missing")
        if (required) {
          List(key + " key is missing")
        } else {
          List()
        }
      case li if li.isEmpty => {
        if (required) {
          List(key + " key contains no value")
        } else {
          List()
        }
      }
      case _ => List()
    }

    if (error.isEmpty) {
      // Attempt to convert string to Integer.
      val option: Option[Int] = this.atoi(list.asScala.head)

      option match {
        case o if o.isEmpty => (0, List("Unable to coerce " + key + " to Int"))
        case _ => (option.getOrElse(0), error)
      }
    } else {
      (0, error)
    }
  }
  def atoi(s: String): Option[Int] = {
    try {
      Option(s.toInt)
    } catch {
        case e: Exception => None
    }
  }
}
