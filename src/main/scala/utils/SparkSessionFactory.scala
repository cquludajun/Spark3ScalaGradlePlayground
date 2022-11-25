package utils

import org.apache.spark.sql.SparkSession

object SparkSessionFactory {

  // spark cluster with yarn running locally
  // following udemy course: Spark SQL and Spark 3 using Scala Hands-On with Labs
  def getYarn(appName: String = "spark default application"): SparkSession = {
    SparkSession.
      builder.
      appName(appName).
      master("yarn").
      config("spark.ui.enabled", "false").   // errors with spark ui enabled, related to java.lang.ClassNotFoundException: org.apache.hadoop.yarn.server.webproxy.amfilter.AmIpFilter
      config("spark.hadoop.fs.defaultFS", "hdfs://localhost:9000").
      config("spark.yarn.jars", "hdfs:///spark3-jars/*.jar").
      config("spark.hadoop.yarn.resourcemanager.address", "localhost:8032").
      getOrCreate
  }

  def getLocal(appName: String = "spark default application"): SparkSession = {
    val sc = SparkSession.
      builder.
      appName(appName).
      master("local[*]").
      config("spark.ui.enabled", "false").
      getOrCreate
    sc.sparkContext.setLogLevel("WARN")
    sc

  }

}
