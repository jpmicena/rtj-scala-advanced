package lectures.part5ts

object FBoundedPolymorphism extends App {

  //  trait Animal {
  //    def breed: List[Animal]
  //  }
  //
  //  class Cat extends Animal {
  //    override def breed: List[Animal] = ??? // List[Cat] !! (how?)
  //  }
  //
  //  class Dog extends Animal {
  //    override def breed: List[Animal] = ??? // List[Dog] !! (how?)
  //  }

  //  // Solution 1 - naive
  //  trait Animal {
  //    def breed: List[Animal]
  //  }
  //
  //  class Cat extends Animal {
  //    override def breed: List[Cat] = ??? // List[Cat] !! (how?)
  //  }
  //
  //  class Dog extends Animal {
  //    override def breed: List[Cat] = ??? // List[Dog] !! (how?)
  //  }

  //  // Solution 2 - FBP
  //  trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism
  //    def breed: List[Animal[A]]
  //  }
  //
  //  class Cat extends Animal[Cat] {
  //    override def breed: List[Cat] = ??? // List[Cat] !! (how?)
  //  }
  //
  //  class Dog extends Animal[Dog] {
  //    override def breed: List[Dog] = ??? // List[Dog] !! (how?)
  //  }
  //
  //  // uses FBP
  //  trait Entity[E <: Entity[E]]
  //  class Person extends Comparable[Person] {
  //    override def compareTo(o: Person): Int = ???
  //  }
  //
  //  // still prone to errors...
  //  class Crocodile extends Animal[Dog] {
  //    override def breed: List[Animal[Dog]] = ???
  //  }

  // Solution 3 - FBP + self-types
  //  trait Animal[A <: Animal[A]] { self: A =>
  //    def breed: List[Animal[A]]
  //  }
  //
  //  class Cat extends Animal[Cat] {
  //    override def breed: List[Cat] = ??? // List[Cat] !! (how?)
  //  }
  //
  //  class Dog extends Animal[Dog] {
  //    override def breed: List[Dog] = ??? // List[Dog] !! (how?)
  //  }
  //
  //  class Crocodile extends Animal[Crocodile] {
  //    override def breed: List[Animal[Crocodile]] = ???
  //  }
  //
  //  // breaks if you bring the class hierarchy down 1 more level
  //  trait Fish extends Animal[Fish]
  //  class Shark extends Fish {
  //    override def breed: List[Animal[Fish]] = List(new Cod)
  //  }
  //  class Cod extends Fish {
  //    override def breed: List[Animal[Fish]] = ???
  //  }

  //  // Solution 4 - type classes!
  //  trait Animal
  //  trait CanBreed[A] {
  //    def breed(a: A): List[A]
  //  }
  //
  //  class Dog extends Animal
  //  object Dog {
  //    implicit object DogsCanBreed extends CanBreed[Dog] {
  //      override def breed(a: Dog): List[Dog] = List(new Dog)
  //    }
  //  }
  //
  //  implicit class CanBreedOps[A](animal: A) {
  //    def breed(implicit canBreed: CanBreed[A]): List[A] = canBreed.breed(animal)
  //  }
  //
  //  val dog = new Dog
  //  dog.breed
  //
  //  class Cat extends Animal
  //  object Cat {
  //    implicit object CatsCanBreed extends CanBreed[Cat] {
  //      override def breed(a: Cat): List[Cat] = List(new Cat)
  //    }
  //  }
  //
  //  val cat = new Cat
  //  cat.breed

 // Solution 5 - API inside the concept itself
  trait Animal[A] { // pure type classes
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] = animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed
}
