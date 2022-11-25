package examples.`implicit`

import java.time.LocalDate


class DateHelper(offset: Int)  {
  def days(when: String): LocalDate = {
    val today = LocalDate.now()
    when match {
      case "ago" => today.minusDays(offset)
      case "from_now" => today.plusDays(offset)
      case _ => today
    }
  }
}

object DateHelper {
  val ago = "ago"
  val from_now = "from_now"

  implicit def convertInt2DateHelper(day: Int): DateHelper = new DateHelper(day)
}

// scala's object can extend
// you can think of object as a singleton class
object ImplicitFunction extends App {
  import DateHelper._
  // convertInt2DateHelper from DateHelper has to be imported into current scope
  // "2 days ago": int 2 does not have method days, it will be converted to DateHelper
  println(2 days ago)
  println(5 days from_now)

}
