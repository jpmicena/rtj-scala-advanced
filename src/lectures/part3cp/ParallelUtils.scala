package lectures.part3cp

import java.util.concurrent.atomic.AtomicReference

import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}
import scala.collection.parallel.immutable.ParVector
import scala.concurrent.forkjoin.ForkJoinPool

object ParallelUtils extends App {

  // 1 - parallel collections
  val parList = List(1, 2, 3).par

  val aParVector = ParVector[Int](1, 2, 3)

  /*
    Seq
    Vector
    Array
    Map - Hash, Trie
    Set - Hash, Trie
   */

  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis - time
  }

  val list = (1 to 10000).toList
  val serialTime = measure {
    list.map(_ + 1)
  }
  val parallelTime = measure {
    list.par.map(_ + 1)
  }

  println("Serial Time: " + serialTime)
  println("Parallel Time: " + parallelTime)

  /*
    Map-reduce model
      - split the elements into chunks - Splitter
      - operation
      - recombine - Combiner
   */

  // map, flatMap, filter, foreach, reduce, fold
  
  // fold, reduce with non-associative operators
  println(List(1, 2, 3).reduce(_ - _))
  println(List(1, 2, 3).par.reduce(_ - _))

  // synchronization (race conditions)
  var sum = 0
  List(1, 2, 3).par.foreach(sum += _)
  println(sum) // should be six, but not guaranteed

  // configuring
  aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))
  /*
    Alternatives:
      - ThreadPoolTaskSupport - deprecated
      - ExecutionContextTaskSupport(EC)
      - Very special cases, create your own TaskSupport
   */

  aParVector.tasksupport = new TaskSupport {
    // environment that manages the threads
    override val environment: AnyRef = _
    // schedules a thread to run in parallel
    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???
    // same thing, but block until the result is available
    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???
    // number of threads
    override def parallelismLevel: Int = ???
  }

  // 2 - atomic ops and references
  val atomic = new AtomicReference[Int](2)
  val currentValue = atomic.get() // thread-safe read
  atomic.set(4) // thread-safe write
  atomic.getAndSet(5) // thread-safe combo

  atomic.compareAndSet(38, 56)
  // if the value is 38, then set to 56
  // reference equality

  atomic.updateAndGet(_ + 1) // thread-safe function run
  atomic.getAndUpdate(_ + 1)

  atomic.accumulateAndGet(12, _ + _) // thread-safe accumulation
  atomic.getAndAccumulate(12, _ + _)

}
