package org.hackerschool.banditod

import com.twitter.finatra._


class ExperimentController extends Controller {
  /* STATUS */
  get("/") { request =>
    render.json(Map("ok" -> "Bandito!")).toFuture
  }

  /* CREATE */
  put("/:experimentName") { request =>
    val experimentName = request.routeParams("experimentName")
    render.json(Map(experimentName -> "CREATE")).toFuture
  }

  /* SELECT */
  get("/:experimentName") { request =>
    val experimentName = request.routeParams("experimentName")
    request.params.get("arms") match {
      case Some(arms) => render.json(Map(experimentName -> ("SELECT", arms))).toFuture
      case None       => render.json(Map(experimentName -> "SELECT")).toFuture
    }
    
  }

  /* UPDATE */
  post("/:experimentName/:armName") { request =>
    val reward = request.params.get("reward")
    val experimentName = request.routeParams("experimentName")
    val armName = request.routeParams("armName")
    render.json(Map(experimentName -> ("UPDATE", armName, reward))).toFuture
  }

  /* DELETE */
  delete("/:experimentName") { request =>
    val experimentName = request.routeParams("experimentName")
    route.delete(s"/$experimentName/*")
  }

  /* DELETE */
  delete("/:experimentName/:armName") { request =>
    val experimentName = request.routeParams("experimentName")
    val armName = request.routeParams("armName")
    render.json(Map(experimentName -> ("DELETE", armName))).toFuture
  }
}

object App extends FinatraServer {
  register(new ExperimentController())
}
