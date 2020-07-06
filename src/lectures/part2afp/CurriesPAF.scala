package lectures.part2afp

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  println(superAdder(3)(5))

  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  // behind the scenes what happens is `lifting` or `ETA-expansion`
  val add4: Int => Int = curriedAdder(4)

  // functions != methods (JVM limitation)
  def inc(x: Int): Int = x + 1
  List(1, 2, 3).map(inc) // ETA-expansion, rewrites as lambda function x => inc(x)

  // Partial function applications (forcing a ETA-expansion)
  val add5 = curriedAdder(5) _ // converts into Int => Int

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int): Int = x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above

  val add7a: Int => Int = simpleAddFunction(7, _)
  val add7b: Int => Int = simpleAddMethod(7, _)
  val add7c: Int => Int = curriedAddMethod(7)

  def concatenator(a: String, b: String, c: String): String = a + b + c
  val insertName = concatenator("Hello I'm ", _, " how are you?")
  println(insertName("Joao"))

  val fillInTheBlanks = concatenator("Hello, ", _, _)
  println(fillInTheBlanks("Daniel", " Scala is awesome!"))

  // EXERCISES
  /*
    1. Process a list of numbers and return their string representation with different formats
    Use the %4.2f, %8.6f and %14.12f with a curried formatter function

    2. Difference between
        - functions vs methods
        - parameters: by-name vs 0-lambda
       Calling byName and byFunction using:
        - int
        - method
        - parenMethod
        - lambda
        - PAF
   */

  def byName(n: => Int): Int = n + 1
  def byFunction(f: () => Int): Int = f() + 1
  def method: Int = 42
  def parenMethod(): Int = 42

  // Exercise 1

  def curriedFormatter(format: String)(n: Double): String =
    format.format(n)

  val fmt1 = curriedFormatter("%4.2f") _
  val fmt2 = curriedFormatter("%8.6f") _
  val fmt3 = curriedFormatter("%14.12f") _

  val nList: Seq[Double] = for (_ <- 0 to 3) yield scala.util.Random.nextDouble

  println(nList.map(fmt1))
  println(nList.map(fmt2))
  println(nList.map(fmt3))

  // Exercise 2

  byName(42) // ok
  byName(method) // ok
  byName(parenMethod()) // ok
  byName(parenMethod) // ok, but beware ==> byName(parenMethod()), uses the value not the function
  // byName(() => 42) // not ok, expects value not function
  byName((() => 42)()) // ok because creates function and then calls it
  // byName(parenMethod _) // not ok

  // byFunction(45) // not ok
  // byFunction(method) // not ok, parameter-less method evaluates to value directly
  byFunction(parenMethod) // compiler does ETA-expansion (uses the function)
  // byFunction(parenMethod()) // not ok, passes value not function
  byFunction(() => 46) // ok
  byFunction(parenMethod _) // also works, but unnecessary (compiler knows that we need to do ETA-expansion)




}
