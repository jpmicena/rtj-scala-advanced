package lectures.part4implicits

object TypeClasses extends App {

//  trait HTMLWritable {
//    def toHTML: String
//  }
//
//  case class User(name: String, age: Int, email: String) extends HTMLWritable {
//    override def toHTML: String = s"<div>$name ($age yo) <a href=$email /></div>"
//  }

  case class User(name: String, age: Int, email: String)

  /*
    1 - only works for the types WE write
    2 - ONE implementation out of quite a number
   */

  /*
    option 2 - pattern matching

    object HTMLSerializerPM {
      def serializationToHtml(value: Any): String = value match {
        case User(n, a, e) => s"<div>$n ($a yo) <a href=$e /></div>"
        case java.util.Date =>
        case _ =>
      }
    }

    1 - lost type safety
    2 - need to modify the code every time
    3 - still one implementation
  */

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email} /></div>"
  }

  val john = User("John", 32, "john@rockthejvm.com")
  println(UserSerializer.serialize(john))

  // 1 - we can define serializer for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  // 2 - we can define MULTIPLE serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  // part 2
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }

  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  // access to the entire type class interface
  println(HTMLSerializer[User].serialize(john))

  // part 3
  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }
  println(john.toHTML)
  /*
    - extend functionality to new types
    - choose implementation (importing serializer to local scope or passing it explicitly)
    - super expressive!
   */
  println(2.toHTML)
  println(john.toHTML(PartialUserSerializer))

  /*
    - type class itself (trait HTMLSerializer[T])
    - type class instances (some of which are implicit, e.g. UserSerializer, IntSerializer)
    - conversion with implicit classes (semantic)
   */

  // context bounds
  def htmlBoilerPlate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body>${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T : HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]
    s"<html><body>${content.toHTML(serializer)}</body></html>"
  }

  // implicitly
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code...
  val standardPerms: Permissions = implicitly[Permissions]

}
