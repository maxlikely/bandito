package org.hackerschool.banditod.commands


abstract class BanditoResults

/** TODO: handle JSON serialization */

case class IncrementResults(errors: List[String]) extends BanditoResults
