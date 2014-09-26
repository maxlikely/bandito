package org.hackerschool.banditod.commands

import org.jboss.netty.handler.codec.http.{QueryStringDecoder, DefaultHttpResponse, HttpResponseStatus}

case class QueryExperimentArguments(expId: String) extends BanditoArguments


/** Called to get stats on an experiment. */
class URLQuery(qsd: QueryStringDecoder) extends BanditoCommand {
  val command: this.Command = this.QUERY_EXPERIMENT
  val v: URLArguments = new URLArguments(qsd.getParameters())

  /** Validate and return the arguments for a command and a list of errors. */
  override def validate[T >: BanditoArguments]: (T, List[String]) = {
    val (expId, expError): (String, List[String]) = this.v.validateString(this.ExperimentIDKey)
    if (!expError.isEmpty) {
      (QueryExperimentArguments(""), expError)
    } else {
      (QueryExperimentArguments(expId), expError)
    }
  }
    override def execute: DefaultHttpResponse = {
      val (validated, errors): (BanditoArguments, List[String]) = this.validate
      val arguments: QueryExperimentArguments = validated.asInstanceOf[QueryExperimentArguments]

      if (!errors.isEmpty) {
        HTTPLib.makeErrorRequest(errors, HttpResponseStatus.BAD_REQUEST)
      } else {
        val results: QueryExperimentResults = QueryExperimentExecutor(arguments).execute
        if (results.errors.isEmpty) {
          val jsonBody: String = results.asJSON + "\r\n"
          HTTPLib.makeRequest(jsonBody, HttpResponseStatus.OK)
        } else {
          HTTPLib.makeErrorRequest(results.errors, HttpResponseStatus.BAD_REQUEST)
        }
      }

    }
}
