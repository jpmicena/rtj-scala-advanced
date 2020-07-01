package lectures.part2afp

object PartialFunctions extends App {

  val aFunction: Int => Int = (x: Int) => x + 1 // Function1[Int, Int]

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 3 => 999
  }

  // {1,2,5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 3 => 999
  } // Partial Function value

  println(aPartialFunction(2))
  // println(aPartialFunction(3432432)) // This breaks, partial function is based on pattern matching

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  // lift
  val lifted: Int => Option[Int] = aPartialFunction.lift
  println(lifted(2))
  println(lifted(534321))

  // orElse
  val pfChain = aPartialFunction.orElse[Int, Int]{
    case 45 => 67
  }
  println(pfChain(2))
  println(pfChain(45))

  // PF extends normal functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
    Note: Unlike functions that have multiple parameters, partial functions can only have one parameter type
   */

  /*
   * Exercises:
   * 1 - construct a PF instance yourself (anonymous class)
   * 2 - dumb chat bot (as a PF)
   */

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }
    override def isDefinedAt(x: Int): Boolean = x == 1 || x == 2 | x == 5
  }

  val chatBot: PartialFunction[String, String] = {
    case "hello" => "Hi my name is HAL9000"
    case "goodbye" => "Once you start talking to me, there is no return, human!"
    case "call mom" => "Unable to find your phone"
  }

  scala.io.Source.stdin.getLines().map(chatBot).foreach(println)



}
