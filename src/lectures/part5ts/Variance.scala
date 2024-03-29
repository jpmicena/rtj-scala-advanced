package lectures.part5ts

object Variance extends App {
  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance?
  // "inheritance" - type substitution of generics

  class Cage[T]

  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
  // val icage: ICage[Animal] = new ICage[Cat] // doesn't work

  // hell no - opposite = contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  // ------------------------------------------------------------------------------------------

  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T) // types of vals are COVARIANT POSITION

  // class ContravariantCage[-T](val animal: T)
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  // class CovariantVariableCage[+T](var animal: T) // types of vars are CONTRAVARIANT POSITION
  /*
    val ccage: CCage[Animal] = new CCAge[Cat](new Cat)
    ccage.animal = new Crocodile
   */
  //  class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION
  //  class InvariantVariableCage[-T](var animal: T) // ok

  // trait AnotherCovariantCage[+T] {
  //   def addAnimal(animal: T) // CONTRAVARIANT POSITION
  // }
  /*
    val ccage: CCage[Animal] = new CCage[Dog]
    ccage.add(new Cat)
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  // acc.addAnimal(new Dog) // doesn't work
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }
  val emptyList                       = new MyList[Kitty]
  val animals: MyList[Kitty]          = emptyList.add(new Kitty)
  val moreAnimals: MyList[Cat]        = animals.add(new Cat)
  val evenMoreAnimals: MyList[Animal] = moreAnimals.add(new Dog)

  // METHOD ARGUMENTS ARE CONTRAVARIANT POSITION

  // return types
  class PetShop[-T] {
    // def get(isItaPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    /*
      val catShop = new PetShop[Animal] {
        def get(isItAPuppy: Boolean): Animal = new Cat
      }

      val dogShop: Petshop[Dog] = catShop
      dogShop.get(true) // EVIL CAT!
     */
    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }
  val shop: PetShop[Dog] = new PetShop[Animal]
  //  val evilCat = shop.get(true, new Cat)
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
    Big rule
      - method arguments are in CONTRAVARIANT position
      - return types are in COVARIANT position
   */

  /**
   * 1. Invariant, covariant, contravariant Parking[T](things: List[T]) {
   *   park(vehicle: T)
   *   impound(vehicles: List[T])
   *   checkVehicles(conditions: String): List[T]
   * }
   *
   * 2. used someone else's API: IList[T]
   * 3. Parking = monad!
   *    - flatMap
   */
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IList[T]

  /// 1

  class InvariantParking[T](things: List[T]) {
    def park(vehicle: T): InvariantParking[T] = ???
    def impound(vehicle: List[T]): InvariantParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => InvariantParking[S]): InvariantParking[S] = ???
  }

  class CovariantParking[+T](things: List[T]) {
    def park[S >: T](vehicle: S): ContravariantParking[S] = ???
    def impound[S >: T](vehicle: List[S]): ContravariantParking[S] = ???
    def checkVehicles(conditions: String): List[T] = things

    def flatMap[S](f: T => CovariantParking[S]): CovariantParking[S] = ???
  }

  class ContravariantParking[-T](things: List[T]) {
    def park(vehicle: T): ContravariantParking[T] = ???
    def impound(vehicle: List[T]): ContravariantParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: R => ContravariantParking[S]): ContravariantParking[S] = ???
  }

  /*
   Rule of thumb
   - use covariance = COLLECTION OF THINGS
   - use contravariance = GROUP OF ACTIONS
   */

  // 2

  class ICovariantParking[+T](things: IList[T]) {
    def park[S >: T](vehicle: S): ICovariantParking[S] = ???
    def impound[S >: T](vehicle: IList[S]): ICovariantParking[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class IContravariantParking[-T](things: IList[T]) {
    def park(vehicle: T): IContravariantParking[T] = ???
    def impound[S <: T](vehicle: IList[S]): ICovariantParking[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

  // 3
}
