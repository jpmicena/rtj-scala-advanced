package lectures.part5ts

object SelfTypes extends App {

  // requiring a type to mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist => // whoever implements Singer to implement Instrumentalist [self type]
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  // Illegal because of self type
  //  class Vocalist extends Singer {
  //    override def sing(): Unit = ???
  //  }

  val jamesHetfield = new Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("guitar solo")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // vs inheritance
  class A
  class B extends A // B is an A

  trait T
  trait S { self: T => } // S requires a T


  // Classical DI
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependantComponent(val component: Component)

  // Cake pattern => "dependency injection"
  trait ScalaComponent {
    // API
    def action(x: Int): String
  }
  trait ScalaDependantComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks!"
  }
  trait ScalaApplication { self: ScalaDependantComponent =>
    // API
  }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - composed components
  trait Profile extends ScalaDependantComponent with Picture
  trait Analytics extends ScalaDependantComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics


  // cyclical dependencies
  // class X extends Y
  // class Y extends X

  trait X { self: Y => }
  trait Y { self: X => }
}
