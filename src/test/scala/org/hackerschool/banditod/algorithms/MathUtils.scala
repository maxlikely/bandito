package org.hackerschool.banditod.algorithms

import org.hackerschool.banditod.algorithms.{MathUtils}
import org.scalatest._

class MathUtilsSpec extends FlatSpec with Matchers {

  "Util.maxDoubleIndexes" should "return the list of indexes of maximum Double values" in {
    val values: List[Double] = List(1.0, 1.0, 2.0, 2.0, 1.0)
    val indexes: List[Int] = MathUtils.maxDoubleIndexes(values, 0, Double.NegativeInfinity, List())
    indexes.sorted should be (List(2, 3))
  }
}
