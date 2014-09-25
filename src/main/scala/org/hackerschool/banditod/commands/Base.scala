package org.hackerschool.banditod.commands

import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}
import org.jboss.netty.handler.codec.http.{
  DefaultHttpResponse,
  HttpVersion,
  HttpResponseStatus,
  HttpRequest,
  QueryStringDecoder}


class BanditoArguments

class EmptyBanditoArguments extends BanditoArguments

trait BanditoCommand extends Enumeration {
  /** The keys for values for creating a command. */
  val ExperimentIDKey = "expId"
  val ArmIDKey = "armId"
  val RewardsKey = "rewards"
  val OffersKey = "offers"

  type Command = Value
  val INCREMENT, QUERY, DELETE, ERROR = Value
  def execute: DefaultHttpResponse
  def validate[T >: BanditoArguments]: (T, List[String])
}

class BanditoError(val errors: List[String]) extends BanditoCommand {
  val command: this.Command = this.ERROR

  def execute: DefaultHttpResponse = {
    HTTPLib.makeErrorRequest(this.errors, HttpResponseStatus.BAD_REQUEST)
  }

  def validate[T >: BanditoArguments]: (T, List[String]) = {
    (new EmptyBanditoArguments, this.errors)
  }
}

/** Returns a command instance from a URL. */
object HttpCommand {
  def commandFromRequest[T >: BanditoCommand](req: HttpRequest): T = {
    val qsd: QueryStringDecoder = new QueryStringDecoder(req.getUri())
    val path: String = qsd.getPath()
    val splitPath: Array[String] = path.split("/")
    val notFoundError: BanditoError = new BanditoError(List("command not found"))
    splitPath match {
      case path if path.length == 2 => {
        path match {
          case p if !p.isEmpty && p(1) == "increment" => {
            new URLIncrement(qsd)
          }
          case _ => {
            /** DEBUG. TODO: logging*/
            print("length")
            println(splitPath.length)
            splitPath.foreach(println)
            notFoundError
          }
        }
      }
      case _ => {
        /** DEBUG. TODO: logging*/
        print("length")
        println(splitPath.length)
        splitPath.foreach(println)
        notFoundError
      }
    }
  }
  /** TODO: command from JSON, command from socket stream */
}
