package lectures.part4implicits

object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
//  implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1, 4, 5, 3, 2).sorted)

  // scala.Predef

  /*
  Implicits:
    - val/var
    - object
    - accessor methods = defs with no parentheses
   */

  // Exercise
  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

//  object Person {
//    implicit def alphabeticOrdering: Ordering[Person] =
//      Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
//  }

//  implicit def ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)

  object AlphabeticNameOrdering {
    implicit def alphabeticOrdering: Ordering[Person] =
      Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }
  object AgeOrdering {
    implicit def ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  import AlphabeticNameOrdering._
//  import AgeOrdering._
  println(persons.sorted)

  /*
    Implicit scope
    - normal scope = LOCAL SCOPE
    - imported scope
    - companion of all types involved in the method signature
      - List
      - Ordering
      - all the types involved (A or any supertype)
   */

  /*
    Exercise
    - totalPrice = most used (50%)
    - by unit count = 25%
    - by unit price = 25%
   */

  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit def totalPriceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((a, b) => (a.nUnits * a.unitPrice) < (b.nUnits * b.unitPrice))
  }

  object UnitCountOrdering {
    implicit def unitCountOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((a, b) => a.nUnits < b.nUnits)
  }

  object UnitPriceOrdering {
    implicit def unitPriceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((a, b) => a.unitPrice < b.unitPrice)
  }
}
