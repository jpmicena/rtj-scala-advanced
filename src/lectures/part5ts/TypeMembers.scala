package lectures.part5ts

object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat // Alias
  }

  val ac = new AnimalCollection
  //  val dog: ac.AnimalType = ???

  //  val cat: ac.BoundedAnimal = new Cat
  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  // Alternative to generics
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int
    override def add(element: Int): MyList = ???
  }

  // .type
  type CatsType = cat.type

  /*
    Exercise - enforce a type to be applicable to SOME TYPES only
     // Only numbers
   */

  // LOCKED
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }

  // DOESN'T COMPILE!
  //  class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
  //    type A = String
  //    override def head: String = hd
  //    override def tail: MList = tl
  //  }

//  class IntList(hd: Int, tl: IntList) extends MList with ApplicableToNumbers {
//    type A = Int
//    override def head: Int = hd
//    override def tail: MList = tl
//  }
}
