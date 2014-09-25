package org.hackerschool.banditod.commands

import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}
import org.jboss.netty.handler.codec.http.{HttpVersion, DefaultHttpResponse, HttpResponseStatus}


object HTTPLib {
  def makeRequest(body: String, status: HttpResponseStatus): DefaultHttpResponse = {
    val res: DefaultHttpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status)
    val cb: ChannelBuffer = ChannelBuffers.copiedBuffer(body.getBytes)
    res.setContent(cb)
    res
  }

  def makeErrorRequest(errors: List[String], status: HttpResponseStatus): DefaultHttpResponse = {
    val body = "ERROR(S): " + errors.mkString("\n") + "\r\n"
    makeRequest(body, status)
  }
}
