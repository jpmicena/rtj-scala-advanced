package exercises

import lectures.part4implicits.TypeClasses.User

object EqualityPlayGround extends App {
  /*
    Equality
   */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }
  object Equal {
    def apply[T](a: T, b: T)(implicit equality: Equal[T]): Boolean = equality(a, b)
  }
  object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }
  implicit object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  val john = User("John", 32, "john@rockthejvm.com")
  val anotherJohn = User("John", 32, "anotheremail@rockthejvm.com")

  /*
    Implement the TC pattern for the Equality tc
   */

  // AD-HOC polymorphism
  println(Equal(john, anotherJohn))

  /*
    Exercise - improve the EQUAL TC with an implicit conversion class
    ===(anotherValue: T)
    !==(anotherValue: T)
   */

  implicit class EqualEnrichment[T](value: T) {
    def ===(anotherValue: T)(implicit equality: Equal[T]): Boolean = equality(value, anotherValue)
    def !==(anotherValue: T)(implicit equality: Equal[T]): Boolean = !equality(value, anotherValue)
  }

  john === anotherJohn
}
