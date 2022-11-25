package examples.`implicit`

object ImplicitParameters {

  implicit val name: String = "default"

  log("init")

  def log(message: String)(implicit xyz: String): Unit = {
    println(s"[$xyz] $message")
  }

  def process(): Unit = {
    // implicit variable has to be called name to override the one defined at the top level
    // otherwise there would be two implicit string variables; scala would not know which one to pick
    implicit val name: String = "process"
    log("doing something")
  }

  def main(args: Array[String]): Unit = {
    implicit val name = "main"
    log("start")
    process()
    log("end")("custom name")
  }

}
