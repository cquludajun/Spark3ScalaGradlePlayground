import org.apache.spark.sql.functions.current_date
import utils.SparkSessionFactory

object TimeFunctions extends App {

  val spark = SparkSessionFactory.getLocal("TimeFunctions")
  import spark.implicits._

  def showCurrentDate(): Unit = {
    val df = List("X").toDF()
    df.select(current_date).show(false)
  }

  def dateTimeOperations(): Unit = {
    val datetimes = List(
      ("2014-02-28", "2014-02-28 10:00:00.123"),
      ("2016-02-29", "2016-02-29 08:08:08.999"),
      ("2017-10-31", "2017-12-31 11:59:59.123"),
      ("2019-11-30", "2019-08-31 00:00:00.000")
    )
    val datetimeDF = datetimes.toDF("date", "time")
    datetimeDF.show(false)

    // 1. add 10 days to both date and time values
  }

//  showCurrentDate()
  dateTimeOperations()



}
