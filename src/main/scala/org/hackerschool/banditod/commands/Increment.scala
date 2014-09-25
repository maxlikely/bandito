package org.hackerschool.banditod.commands

import org.jboss.netty.handler.codec.http.{QueryStringDecoder, DefaultHttpResponse, HttpResponseStatus}


case class IncrementArguments(
  armId: String,
  expId: String,
  rewards: Int,
  offers: Int) extends BanditoArguments {
  def emptyArguments: IncrementArguments = IncrementArguments("", "", 0, 0)
}


/** The Increment command instantianted from a URL. */
class URLIncrement(qsd: QueryStringDecoder) extends BanditoCommand {
  val command: this.Command = this.INCREMENT
  // Validator
  val v: URLArguments = new URLArguments(qsd.getParameters())

  /** Validate and return the arguments for a command and a list of errors. */
  override def validate[T >: BanditoArguments]: (T, List[String]) = {
    val (expId, expError): (String, List[String]) = this.v.validateString(this.ExperimentIDKey)
    val (armId, armError): (String, List[String]) = this.v.validateString(this.ArmIDKey)
    val (rewards, rError): (Int, List[String]) = this.v.validateInt(this.RewardsKey, 0)
    val (offers, oError): (Int, List[String]) = this.v.validateInt(this.OffersKey, 0)

    /** Check that at least one value was provided. */
    val bothInvalidError: List[String] = {
      if (rewards <= 0 && offers <= 0) {
        List("'rewards' and 'offers' values both <= 0!") ++ rError ++ oError
      } else {
        List()
      }
    }
    val errors: List[String] = bothInvalidError ++ List(expError, armError).flatten
    val args: (String, String, Int, Int) = (expId, armId, rewards, offers)
    (IncrementArguments.tupled(args), errors)
  }

  override def execute: DefaultHttpResponse = {
    val (validated, errors): (BanditoArguments, List[String]) = this.validate
    /**
      Scala recommends using pattern matching to handle casting but remember:
      eschew obfuscation and espouse elucidation.
      */
    val arguments: IncrementArguments = validated.asInstanceOf[IncrementArguments]

    if (!errors.isEmpty) {
      HTTPLib.makeErrorRequest(errors, HttpResponseStatus.BAD_REQUEST)
    } else {
      // Execute valid command.
      val results: IncrementResults = IncrementExecutor(arguments).execute
      if (results.errors.isEmpty) {
        val body = "OK\r\n"
        HTTPLib.makeRequest(body, HttpResponseStatus.OK)
      } else {
        HTTPLib.makeErrorRequest(results.errors, HttpResponseStatus.BAD_REQUEST)
      }
    }
  }
}
