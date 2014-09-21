package org.hackerschool.banditod.commands
import org.jboss.netty.handler.codec.http.{DefaultHttpResponse, HttpVersion}
import com.twitter.finagle.http.Status
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}

class BanditoArguments

class EmptyBanditoArguments extends BanditoArguments

trait BanditoCommand extends Enumeration {
  /** The keys for values for creating a command. */
  val ExperimentIDKey = "expId"
  val ArmIDKey = "armId"
  val RewardsKey = "rewards"
  val OffersKey = "offers"
  val FieldKey = "field"
  val ValueKey = "value"

  type Command = Value
  val INCREMENT, QUERY, UPDATE, DELETE, ERROR = Value
  def execute: DefaultHttpResponse
  def validate[T >: BanditoArguments]: (T, List[String])
}

class BanditoError(val errors: List[String]) extends BanditoCommand {
  val command: this.Command = this.ERROR

  def execute: DefaultHttpResponse = {
    val res: DefaultHttpResponse = new DefaultHttpResponse(
      HttpVersion.HTTP_1_1,
      Status.Ok)

    /** TODO */
    val body = "There was an error; TODO: we'll emit the list of errors"
    val cb: ChannelBuffer = ChannelBuffers.copiedBuffer(body.getBytes)
    res.setContent(cb)
    res
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
          case p if !p.isEmpty && p.head == "increment" => {
            new URLIncrement(qsd)
          }
          case _ => {
            notFoundError
          }
        }
      }
      case _ => {
        notFoundError
      }
    }
  }
  /** TODO: command from JSON, command from socket stream */
}
