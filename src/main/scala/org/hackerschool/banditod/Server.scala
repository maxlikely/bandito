package org.hackerschool.banditod

import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.Status
import com.twitter.util.{Await, Future}

import org.jboss.netty.handler.codec.http._
import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}

// Convert a java.util.Map to a Scala
import scala.collection.JavaConverters._

import java.util.Map
import java.util.List


object Server extends App {
  val service = new Service[HttpRequest, HttpResponse] {

    def apply(req: HttpRequest): Future[HttpResponse] = {
      var status = Status.Ok
      var body: String = ""

      val uri: String = req.getUri()
      val qse: QueryStringDecoder = new QueryStringDecoder(uri)
      val queryParams: Map[String, java.util.List[String]] = qse.getParameters()

      // Route the URL prefix to a command.
      val command: Commands.Command = Commands.commandFromURI(qse.getPath())

      // TODO: could move this to the Executor file.
      val banditodCommandArg: BanditodCommand = command match {
        case Commands.UPDATE => {
          CommandArgValidation.argsForCommand(queryParams, command)
        }
        case Commands.INCREMENT => {
          CommandArgValidation.argsForCommand(queryParams, command)
        }
        case Commands.QUERY => {
          CommandArgValidation.argsForCommand(queryParams, command)
        }
        case Commands.DELETE => {
          CommandArgValidation.argsForCommand(queryParams, command)
        }
        case _ => {
          CommandArgValidation.argsForCommand(queryParams, Commands.UNKNOWN)
        }
      }

      var isValidCommand: Boolean = banditodCommandArg.isValid()
      var isKnownCommand: Boolean = banditodCommandArg.command != Commands.UNKNOWN
      if (isValidCommand && isKnownCommand) {
        // Run Executor
        val executor: CommandExecutor = new CommandExecutor
        var result: scala.collection.mutable.Map[String, Any] = executor.execute(banditodCommandArg.command, banditodCommandArg.data.asJava)
        var success: Any = result.getOrElse("success", false)

        if (success.asInstanceOf[Boolean]) {
          status = Status.Ok
        } else {
          status = Status.BadRequest
          body = "Command was not successful" // TODO: add reason
        }
      } else {
        // Handle Error
        status = Status.BadRequest
        body = "Bad Request"
        if (!isValidCommand) {
          body += ": Command is invalid -> error: " + banditodCommandArg.error
        }
        if (!isKnownCommand) {
          body += "Also, an unknown command was parsed from the URI: " + uri
        }
      }
      body += "\r\n"
      var res: DefaultHttpResponse = new DefaultHttpResponse(req.getProtocolVersion, status)
      val cb: ChannelBuffer = ChannelBuffers.copiedBuffer(body.getBytes)
      res.setContent(cb)

      Future.value(res)
    }
  }
  // TODO: config file for address/port
  val server = Http.serve(":8080", service)
  Await.ready(server)
}
