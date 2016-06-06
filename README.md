#Quick guide on setting up Spark (using Scala)#

This guide doesn't explain stuff like:
- how and why Spark works
- what are RDDs, actions or transformations
- how to set up JDK on your machine or how to install Scala

It is focused on getting Spark up and running under 10-15 minutes, with as little digressions as possible. There are more detailed tutorials out there, in case that's what you need (you can start from [this one](https://github.com/mbonaci/mbo-spark) or [this one](https://github.com/deanwampler/spark-scala-tutorial)).

##Setting up the project##

First of all, download [sbt](https://github.com/sbt/sbt). Now you can import the project into your favourite IDE.

####Eclipse####

You will need to "eclipsify" the project. Download [sbteclipse](https://github.com/typesafehub/sbteclipse). I would recommend getting sbt 0.13+ and adding the following to the global sbt file at `~/.sbt/plugins/plugins.sbt` (instead of editting the project-specific file): 

`addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")`

Now create a folder which you will use for your Spark project and place the build.sbt found in this repo inside it. Then simply run sbt and use the *eclipse* command to turn it into an Eclipse project (I am assuming that you have Scala set up in your Eclipse IDE):

` > eclipse`

You can now simply import the created project as existing project into Eclipse workspace.

####IntelliJ####
If you're using IntelliJ then all you need to do is to import the project. Select `File -> New -> Project from existing sources` and import it as SBT project.

Note: You will need Scala 2.10 for these dependencies to work. At the time of writing this, Scala 2.11 was still not supported by Spark.

##Just run the code##

Spark is meant to be run on clusters. But to get you going, you can simply run it on your local machine with some ad-hoc configuration. Make sure to change spark master address to "local" in `Main.scala`:  

    val conf = new SparkConf().setAppName("sparkintro").setMaster("local")
    val sc = new SparkContext(conf)

Also remove the `% "provided"` part from the `build.sbt` if you want `sbt` to fetch the library for you. Later on, when we will have our own binary distribution of Spark for running master and slaves, we will need the "provided" part because we will want to run a JAR on the Spark cluster (where Spark dependency is already available).

Code that performs the calculation in the `Main.scala` example is taken from an [official examples page](https://spark.apache.org/examples.html).

##Setting up the local cluster##
Of course, there's no fun in simply running a Spark job without a special dedicated cluster. We will now see how to run the cluster (master + 4 slaves). It's still not the full power of Spark since they're all running on a local machine, but once you get the hang of this, it should be fairly easy to scale out into the cloud or wherever you want.

First of all, you will need a binary distribution of Spark for this; you can get it [here](http://spark.apache.org/downloads.html) (make sure to select the "pre-built" version). Note that the version you choose doesn't have to be the same as defined in `build.sbt`, but I'm sure you're aware of possible issues that could arise if you code against one version and then run against another one. 
  
Next step is to prepare the Spark environment. You can use the template config file provided with the distribution. We will also set the default number of workers to four:

    cp ./conf/spark-env.sh.template ./conf/spark-env.sh 
    echo "export SPARK_WORKER_INSTANCES=4" >> ./conf/spark-env.sh
  
OK, we can start the master and slaves. Note that you will need an SSH deamon running on your system. Linux users can see if it's running by issuing:

    service ssh status
    
while for OS-X it's:

    launchctl list | grep ssh
    
If the service is not running, make sure to install it and run it. For Mac users it's probably enough to enable System Preferences -> Sharing -> Remote Login. Anyways, setting up the SSH deamon is not the point of this text, I'm sure you'll figure it out.

Once the SSH deamon is all good, we can run our master and slave scripts:

    ./sbin/start-master.sh
    ./sbin/start-slaves.sh
    
There's a change you will be asked for the password since you're attempting to SSH to yourself. 

Once that's done you can navigate to http://localhost:8080 to check out the state of your brand new Spark cluster. You should see something like:

![Screenshot](./images/screenshot.png)

There are some other convinient script for working with daemons, such as `start-all.sh` and `stop-all.sh` which will start/stop all daemons (both master and slaves).

##Running the app on cluster##

Now we need to package our app and feed it to running Spark cluster. 

First you need to set the address of your master node in `Main.scala`: 

    .setMaster("[SPARK_ADDRESS]")
    
(you can see your `SPARK_ADDRESS` when you navigate to Spark console on http://localhost:8080; in my case it was `spark://sinisas-mbp-2:7077`, as seen in the screenshot. 

Also, if you removed the "provided" part in the `build.sbt`, now is the good time to bring it back, otherwise you will see tons of errors talking about duplicate versions.

Alright. You can now create the JAR. Easiest way to do it is using [sbt-assembly](https://github.com/sbt/sbt-assembly). You just need to add the `assembly.sbt` file with needed dependency to the `project` folder (this is already done for you in my repo) and run `sbt assembly` task and your shiny app will be created somewhere in `target` folder (if you didn't change anything, it should be something like `target/scala-2.10/sparkintro-assembly-1.0.jar`).

**Just one more step left**. We need to feed the app to the Spark machinery.

In the Spark binary directory, issue the following command:

    PATH-TO-SPARK/bin/spark-submit --class CLASSFILE PATH-TO-JAR
    
Using default values from this repo, it would be something like (given it's run from project folder):

    ./bin/spark-submit --class com.slouc.sparkintro.Main ./target/scala-2.10/sparkintro-assembly-1.0.jar 

And voila! Keep an eye on that console on port 8080 and you'll notice your hard-working slaves calculating the value of Pi for you.


