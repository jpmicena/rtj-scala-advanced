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

}
