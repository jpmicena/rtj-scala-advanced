package lectures.part3cp

import java.util.concurrent.Executors

object Intro extends App {

  // JVM threads
  /*
    interface Runnable {
      public void run()
    }
   */
  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }
  val aThread = new Thread(runnable)

  // create a JVM thread (runs on top of an OS thread)
  aThread.start() // gives the signal to the JVM to start a JVM thread
  runnable.run() // doesn't do anything in parallel!

  aThread.join() // blocks until aThread finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodbye.start()
  // different runs produce different results

  // executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  // throws an exception in the calling thread
  // pool.shutdown()
  // pool.execute(() => println("should not appear"))

  // shuts down the sleeping threads
  // pool.shutdownNow()

  // helpers
  println(pool.isShutdown)
  println(pool.isTerminated)

  def runInParallel = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)
  }

  for (_ <- 1 to 100) runInParallel
  // race condition (two threads trying to access the same memory zone at the same time)

  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
  }
  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    //println("I've bought " + thing)
    //println("my account is now " + account)
  }

  for (_ <- 1 to 1000) {
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buy(account, "shoes", 3000))
    val thread2 = new Thread(() => buy(account, "iPhone12", 4000))

    thread1.start()
    thread2.start()
    Thread.sleep(10)
    if (account.amount != 43000) println("AHA: " + account.amount)
  }

  // option #1: use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int) =
    account.synchronized {
      // no two threads can evaluate this at the same time
      account.amount -= price
      println("I've bought " + thing)
      println("my account is now " + account)
    }

  // option #2: use @volatile (annotating the var: @volatile var amount: Int

  /*
    Exercises

    1) Construct 50 "inception" threads
        Thread1 -> thread2 -> thread3
        println("hello from thread X")
       in REVERSE ORDER

    2) Code below, answer questions:
       - what is the biggest value possible for x? 100
       - what is the smallest value possible for x? 1

    3) sleep fallacy
      - what is the value of the message? almost always "scala is awesome"
      - is it guaranteed? No!
   */

  // Exercise 1
  def inceptionThreads(maxThreads: Int, i: Int): Thread = new Thread(() => {
    if (i < maxThreads) {
      val newThread = inceptionThreads(maxThreads, i + 1)
      newThread.start()
      newThread.join()
    }
    println(s"hello from thread $i")
  })

  inceptionThreads(50, 1).start()

  // Exercise 2
  var x = 0
  val threads = (1 to 100).map(_ => new Thread(() => x += 1))
  threads.foreach(_.start)

  // Exercise 3
  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "scala is awesome"
  })

  message = "scala sux"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)

}
