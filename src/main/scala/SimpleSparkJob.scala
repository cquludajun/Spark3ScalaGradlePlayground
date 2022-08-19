import utils.SparkSessionFactory


object SimpleSparkJob {

  def main(args: Array[String]): Unit = {
    val spark = SparkSessionFactory.getYarn("SimpleSparkJob")
//    val spark = SparkSessionFactory.getLocal("SimpleSparkJob")
    spark.sparkContext.getConf.getAll.foreach(println)
  }
}
