Sparkintro
==========

Here’s a quick guide on setting up Spark with Scala in Eclipse IDE.

Setting up sbt
==============
- First of all, download [sbt](https://github.com/sbt/sbt) and [sbteclipse](https://github.com/typesafehub/sbteclipse). Instructions on how to set those up are pretty well explained on their websites. I would recommend getting sbt 0.13+ and adding the following to the global sbt file at *~/.sbt/plugins/plugins.sbt* (instead of editting the project-specific file): 

`addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")`

- Now create a folder which you will use for your Spark project and place the build.sbt found in this repo inside it. Then simply run sbt and use the *eclipse* command to turn it into an Eclipse project (I am assuming that you have Scala set up in your Eclipse IDE):

` > eclipse`

- Note that you will need Scala 2.10 for these dependencies to work. I didn’t manage to get Spark 1.1.0 to work with Scala 2.11 (at the time of writing this, there was a [Jira issue](https://issues.apache.org/jira/browse/SPARK-1812) raised for that problem)

You can now simply import the created project as existing project into Eclipse workspace.

Working with Spark
==================
- Spark is meant to be run on clusters. However, to get you going you can simply run it on your local machine. Make sure to add `.setMaster("local")` whenever you are setting up a configuration for your Spark context:
`val conf = new SparkConf().setAppName("sparktest").setMaster("local")`
`val sc = new SparkContext(conf)`
- Code that performs the calculation in the `Main.scala` example is taken from an [official examples page](https://spark.apache.org/examples.html)

This is it! Hopefully you now have your project up and running, all dependencies resolved and estimated value of Pi printing out on your console.
