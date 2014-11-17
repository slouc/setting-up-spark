package com.slouc.sparkintro

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
 * Basic Spark implementation of estimation of Pi
 * (calculation code available at https://spark.apache.org/examples.html)
 * 
 * @author slouc
 *
 */
object Main {

  def main(args: Array[String]) {

    val numSamples = 1000000
    val conf = new SparkConf().setAppName("sparkintro").setMaster("local")
    val sc = new SparkContext(conf)

    val count = sc.parallelize(1 to numSamples).map { i =>
      val x = Math.random()
      val y = Math.random()
      if (x * x + y * y < 1) 1 else 0
    }.reduce(_ + _)

    println("Pi is roughly " + 4.0 * count / numSamples)
  }
}
