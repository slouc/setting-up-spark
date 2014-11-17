sparkintro
==========

Here’s a quick guide on setting up Spark with Scala in Eclipse IDE.

- First of all, download [sbt](http://github.com/harrah/xsbt/) and [sbteclipse](https://github.com/typesafehub/sbteclipse). Instructions on how to set those up are pretty well explained on their websites. I would recommend getting sbt 0.13+ and adding the following to the global sbt file at *~/.sbt/plugins/plugins.sbt* (instead of editting the project-specific file): 

`addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")`

- Now create a folder which you will use for your Spark project and place the build.sbt found in this repo inside it. Then simply run sbt and use the *eclipse* command to turn it into an Eclipse project (I am assuming that you have Scala set up in your Eclipse IDE):

` > eclipse`

- Note that you will need Scala 2.10 for these dependencies to work. I didn’t manage to get Spark 1.1.0 to work with Scala 2.11 (at the time of writing this, there was a [Jira issue](https://issues.apache.org/jira/browse/SPARK-1812) raised for that problem)

Now simply import the created project as existing project into Eclipse workspace and run it!
