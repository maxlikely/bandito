
package org.hackerschool.banditod.commands

import com.twitter.finagle.http.Status
import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}

import org.jboss.netty.handler.codec.http.{
  QueryStringDecoder,
  HttpVersion,
  DefaultHttpResponse}

case class IncrementArguments(
  armId: String,
  expId: String,
  field: String,
  value: Int) extends BanditoArguments {
  def emptyArguments: IncrementArguments = IncrementArguments("", "", "", 0)
}


// import java.util.Map
// import java.util.List


/** The Increment command instantianted from a URL. */
class URLIncrement(qsd: QueryStringDecoder) extends BanditoCommand {
  val command: this.Command = this.INCREMENT
  // Validator
  val v: URLArguments = new URLArguments(qsd.getParameters())

  /** Validate and return the arguments for a command and a list of errors. */
  override def validate[T >: BanditoArguments]: (T, List[String]) = {
    val eidAndError: (String, List[String]) = this.v.validateString(this.ExperimentIDKey, true)
    val aidAndError: (String, List[String]) = this.v.validateString(this.ArmIDKey, true)
    val fieldAndError: (String, List[String]) = this.v.validateString(this.FieldKey, true)
    val valueAndError: (Int, List[String]) = this.v.validateInt(this.ValueKey, true)

    val args: (String, String, String, Int) = (
      eidAndError._1,
      aidAndError._1,
      fieldAndError._1,
      valueAndError._1)

    val errors: List[String] = List(
      eidAndError._2,
      aidAndError._2,
      fieldAndError._2,
      valueAndError._2).flatten

    (IncrementArguments.tupled(args), errors)
  }

  override def execute: DefaultHttpResponse = {
    val validated: (BanditoArguments, List[String]) = this.validate
    // JANK???
    val arguments: IncrementArguments = validated._1.asInstanceOf[IncrementArguments]
    val errors: List[String] = validated._2

    if (!errors.isEmpty) {
      val body = "Called Increment with errors \r\n"
      val res: DefaultHttpResponse = new DefaultHttpResponse(
        HttpVersion.HTTP_1_1,
        Status.BadRequest)

      val cb: ChannelBuffer = ChannelBuffers.copiedBuffer(body.getBytes)
      res.setContent(cb)
      res
    } else {
      val body = "Called Increment without errors \r\n"
      val res: DefaultHttpResponse = new DefaultHttpResponse(
        HttpVersion.HTTP_1_1,
        Status.Ok)
      val cb: ChannelBuffer = ChannelBuffers.copiedBuffer(body.getBytes)
      res.setContent(cb)
      res
    }
  }
}
