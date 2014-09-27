package org.hackerschool.banditod

import com.twitter.finatra._


class ExperimentController extends Controller {
  /* STATUS */
  get("/") { request =>
    val status = Map("ok" -> "Bandito!",
                     "algorithms" -> Registry.algorithms,
                     "experiments" -> Registry.experiments)
    render.json(status).toFuture
  }

  /* CREATE */
  put("/:experimentName") { request =>
    val experimentName = request.routeParams("experimentName")
    val arms = request.params.getOrElse("arms", "").split(",").filter(arm => arm != "").toList
    val numArms = request.params.getOrElse("numArms", arms.length) // TODO
    val algorithmName = request.params.getOrElse("algorithm", "EpsilonGreedy")

    val algorithm = Registry.algorithms(algorithmName)() // TODO: Parse some settings...
    algorithm.initialize(arms)

    val experiment = Experiment(experimentName, algorithm)
    if (Registry.register(experiment)) {
      render.json(Map("acknowledged" -> true)).toFuture
    } else {
      val status = Map("error" -> s"ExperimentAlreadyExistsException[$experimentName] already exists.",
                       "status" -> 400)
      render.json(status).status(400).toFuture
    }
  }

  /* SELECT */
  get("/:experimentName") { request =>
    val experimentName = request.routeParams("experimentName")
    val experiment = Registry.experiments().get(experimentName) match {
      case Some(experiment) => experiment
      case None             => Experiment.fromString(experimentName)
    }
    Registry.register(experiment)

    val arm = experiment.algorithm.selectArm
    val status = Map("experiment" -> experimentName,
                     "selectedArm" -> arm)
    render.json(status).toFuture
  }

  /* UPDATE */
  post("/:experimentName/:armName") { request =>
    val experimentName = request.routeParams("experimentName")
    val armName = request.routeParams("armName")

    // Set-up experiment if it doesn't exist yet.
    val experiment = Registry.experiments().get(experimentName) match {
      case Some(experiment) => experiment
      case None             => Experiment.fromString(experimentName)
    }
    Registry.register(experiment)

    val algorithm = experiment.algorithm
    val success = request.params.get("reward") match {
      case Some(reward) => algorithm.updateReward(armName, reward.toDouble)
      case _            => algorithm.updateReward(armName)
    }

    if (success) {
      render.json(Map("acknowledged" -> true)).toFuture
    } else {
      val body = Map("error" -> s"ArmMissingException[$armName] missing.",
                      "status" -> 400)
      render.json(body).status(400).toFuture
    }

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

    if (Registry.experiments().contains(experimentName)) {
      val success = armName match {
        case "*"         => Registry.delete(experimentName)
        case arm: String => Registry.experiments()(experimentName).algorithm.removeArm(arm)
      }
      if (success) {
        render.json(Map("acknowledged" -> true)).toFuture
      } else {
        val body = Map("error" -> s"ArmMissingException[$armName] missing.",
                       "status" -> 400)
        render.json(body).status(400).toFuture
      }
    } else {
      val body = Map("error" -> s"ExperimentMissingException[$experimentName] missing.",
                     "status" -> 400)
      render.json(body).status(400).toFuture
    }
  }
}

object App extends FinatraServer {
  register(new ExperimentController())
}
