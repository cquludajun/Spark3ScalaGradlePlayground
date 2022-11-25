import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, concat, lit}
import utils.SparkSessionFactory

import sys.process._

object DataProcessingExamples extends App {

  val spark = SparkSessionFactory.getYarn("DataProcessingExamples")

  import spark.implicits._

  def readExample(): Unit = {
    // data frame orders
    val orders = spark.read
      .schema(
        """
          |order_id INT, order_date TIMESTAMP,
          |order_customer_id INT, order_status STRING
          |""".stripMargin)
      .option("sep", ",")
      .csv("/public/retail_db/orders") // files in hdfs ( retail_db in resources folder has to be copied to hdfs).
    orders.show(false)

    // source https://github.com/itversity/retail_db_json
    // copy to hdfs: hdfs dfs -put retail_db_json /public
    val df = spark.read
      .schema(
        """
          |order_id INT, order_date TIMESTAMP,
          |order_customer_id INT, order_status STRING
          |""".stripMargin)
      .json("/public/retail_db_json/orders") // hdfs folders too,
    df.show(false)

    // print schema
    orders.printSchema()

    // get statistics (count, mean, stddev, min, max)
    orders.describe().show(false)
  }

  def writeExample(spark: SparkSession): Unit = {
    val orders = spark.
      read.
      schema(
        """order_id INT,
                  order_date STRING,
                  order_customer_id INT,
                  order_status STRING
               """
      ).
      csv("/public/retail_db/orders")

    println(spark.conf.get("spark.sql.parquet.compression.codec"))

    orders.coalesce(1).write.mode("overwrite")
      .option("compression", "none")
      .parquet("/tmp/orders_parquet_no_compression")

    orders.coalesce(1).write.mode("overwrite")
      .option("compression", "snappy")
      .parquet("/tmp/orders_parquet_compression_snappy")

    orders.coalesce(1).write.mode("overwrite")
      .option("compression", "gzip")
      .parquet("/tmp/orders_parquet_compression_gzip")

    "hdfs dfs -ls -h /tmp/orders_parquet_no_compression" !

    "hdfs dfs -ls -h /tmp/orders_parquet_compression_snappy" !

    "hdfs dfs -ls -h /tmp/orders_parquet_compression_gzip" !
  }


  def projectionExample(): Unit = {
    val employees = List((1, "Scott", "Tiger", 1000.0, "united states"),
      (2, "Henry", "Ford", 1250.0, "India"),
      (3, "Nick", "Junior", 750.0, "united KINGDOM"),
      (4, "Bill", "Gomes", 1500.0, "AUSTRALIA")
    )
    import spark.implicits._
    // toDF is an implicit function which converts seq to
    val df = employees.toDF("employee_id", "first_name", "last_name",
      "salary", "nationality")
    df.printSchema()
    df.show
    df.select("first_name", "last_name").show()
    df.drop("nationality").show()  // create a new df
    df.show()

    df.select(
      col("employee_id"),
      concat(col("first_name"), lit(", "), col("last_name")).alias("full_name"),
      col("salary"),
      col("nationality")
    ).show

    df.withColumn("full_name", concat(col("first_name"), lit(", "), col("last_name")))
      .drop("first_name", "last_name")
      .show
  }

  def diffExample(): Unit = {

    val df1 = Seq(1, 2, 1).toDF("n")
    val df2 = Seq(1, 2, 2).toDF("n")

    df1.show(false)

    // union and unionAll are the same, unionAll is deprecated since spark 2
    // use distinct() to remove duplicates
    df1.union(df2).show(false) // [1 2 1 1 2 2]
    df1.unionAll(df2).show(false)  // [1 2 1 1 2 2]
    df1.union(df2).distinct().show(false)

    df1.except(df2).show(false)  // empty, resolve duplicates
    df1.exceptAll(df2).show(false) // [1]
    df1.union(df2).exceptAll(df1.intersectAll(df2)).show(false) // not a diff [ 1 1 2 2]
    df1.exceptAll(df2).unionAll(df2.exceptAll(df1)).show(false) // diff [1, 2]
  }

//  readExample()
//  projectionExample()
//    writeExample()
  diffExample()
}
