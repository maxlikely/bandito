package org.hackerschool.banditod.algorithms

import org.scalatest._

class ListUtilsSpec extends FlatSpec with Matchers {
  "ListUtils.maxDoubleIndices" should "return the list of indices of maximum Double values" in {
    val values: List[Double] = List(1.0, 1.0, 2.0, 2.0, 1.0)
    val indexes: List[Int] = ListUtils.maxDoubleIndices(values, 0, Double.NegativeInfinity, List())
    indexes.sorted should be (List(2, 3))
  }

  "ListUtils.maxDoubleIndices" should "return the list of an index for a single max value" in {
    val values: List[Double] = List(1.0, 1.0, 2.0, 0.2, 1.0)
    val indexes: List[Int] = ListUtils.maxDoubleIndices(values, 0, Double.NegativeInfinity, List())
    indexes.sorted should be (List(2))
  }

  "ListUtils.maxDoubleIndices" should "return the list of all indices if all values are equal" in {
    val values: List[Double] = List(0, 0, 0, 0)
    val indexes: List[Int] = ListUtils.maxDoubleIndices(values, 0, Double.NegativeInfinity, List())
    indexes.sorted should be (List(0, 1, 2, 3))
  }


}
