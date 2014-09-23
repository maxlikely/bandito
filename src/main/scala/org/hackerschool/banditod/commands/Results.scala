package org.hackerschool.banditod.commands


abstract class BanditoResults


case class IncrementResults(errors: List[String], success: Boolean) extends BanditoResults
