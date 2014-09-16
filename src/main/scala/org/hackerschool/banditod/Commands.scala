package org.hackerschool.banditod


object Commands extends Enumeration {
  type Command = Value
  val INCREMENT, QUERY, UPDATE, DELETE, UNKNOWN = Value

  /** Returns the command from a URI */
  def commandFromURI(uri: String): Command = {
    var path: Array[String] = uri.split("/")
    if (path.length >= 2) {
      var p: String = path(1)
      p match {
        case "increment" => Commands.INCREMENT
        case "query" => Commands.QUERY
        case "update" => Commands.UPDATE
        case "delete" => Commands.DELETE
        case _ => {
          println("unknown command: " + p)
          Commands.UNKNOWN
        }
      }
    } else {
      Commands.UNKNOWN
    }
  }
  // TODO: command from stream method.
}
