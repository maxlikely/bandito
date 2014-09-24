package org.hackerschool.banditod.commands


abstract class BanditoResults

/** TODO: handle JSON serialization */
case class IncrementResults(errors: List[String]) extends BanditoResults


// abstract class Letter

// case class A() extends Letter {
//   val msg = "I'm a A"
// }

// case class B() extends Letter {
//   val msg = "I'm a B"
// }

// case class C() extends Letter {
//   val msg = "I'm a C"
//   def get[T >: Letter](cls: T): T = {
//     cls match {
//       case x: B => {
//         new B
//       }
//       case x: C => {
//         new C
//       }
//       case x: A => {
//         new A
//       }
//     }
//   }
// }
