package lectures.part5ts

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner): Unit = println(i)
    def printGeneral(i: Outer#Inner): Unit = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // defined per-instance
  val o = new Outer
  val inner = new o.Inner

  val oo = new Outer
  val otherInner: oo.Inner = new oo.Inner // can't do new o.Inner

  o.print(inner)
  //  oo.print(inner) // not okay

  // Outer#Inner
  o.printGeneral(inner)
  o.printGeneral(otherInner)

  /*
    Exercise
    DB Keyed by Int or String, but maybe others
   */

  //  def get[ItemType](key: Key): ItemType
  //  get[IntItem](key = 42) // ok
  //  get[StringItem](key = "home") // ok
  //  get[IntItem](key = "scala") // not okay

  trait ItemLike {
    type Key
  }
  trait Item[K] extends ItemLike {
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): Unit = println("oi")

  get[IntItem](key = 42)
  get[StringItem](key = "home")
}
