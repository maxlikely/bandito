package org.hackerschool.banditod

import org.hackerschool.banditod.commands._

import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.Status
import com.twitter.util.{Await, Future}

import org.jboss.netty.handler.codec.http._


object Server extends App {
  val service = new Service[HttpRequest, HttpResponse] {

    def apply(req: HttpRequest): Future[HttpResponse] = {
      Future.value(HttpCommand.commandFromRequest(req).execute)
    }
  }

  /** TODO: configuration file*/
  val server = Http.serve(":8080", service)
  Await.ready(server)
}
