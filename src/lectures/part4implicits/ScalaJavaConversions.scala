package lectures.part4implicits

import java.{util => ju}

import scala.collection.mutable
import scala.collection.mutable._

object ScalaJavaConversions extends App {

  import collection.JavaConverters._

  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()

  (1 to 5).foreach(javaSet.add)
  println(javaSet)

  val scalaSet: mutable.Set[Int] = javaSet.asScala

  val numbersBuffer = ArrayBuffer[Int](1, 2, 3)
  val juNumbersBuffer = numbersBuffer.asJava

  println(juNumbersBuffer.asScala eq numbersBuffer)

  val numbers: List[Int] = List(1, 2, 3)
  val juNumbers: ju.List[Int] = numbers.asJava
  val backToScala: mutable.Seq[Int] = juNumbers.asScala

  println(backToScala eq numbers) // false
  println(backToScala == numbers) // true
}
