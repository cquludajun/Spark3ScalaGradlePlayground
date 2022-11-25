package examples.`implicit`

import java.time.LocalDate

object ImplictClassMustBeWithinAnObject {
  val ago = "ago"
  val from_now = "from_now"

  // using AnyVal to avoid create class instance every time an implicit conversion happens
  implicit class DateHelperImplictClass(val offset: Int) extends AnyVal{
    def days(when: String): LocalDate = {
      val today = LocalDate.now()
      when match {
        case "ago" => today.minusDays(offset)
        case "from_now" => today.plusDays(offset)
        case _ => today
      }
    }
  }

}

object ImplicitClass extends App {
  import ImplictClassMustBeWithinAnObject._
  println(2 days ago)
  println(5 days from_now)
}
