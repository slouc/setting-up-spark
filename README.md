#Scala Spark Quick Guide#

Here’s a quick guide on setting up Spark with Scala.

##Setting up the project##

First of all, download [sbt](https://github.com/sbt/sbt). Now you can import the project into your favourite IDE:

####Eclipse####

Download [sbteclipse](https://github.com/typesafehub/sbteclipse). I would recommend getting sbt 0.13+ and adding the following to the global sbt file at *~/.sbt/plugins/plugins.sbt* (instead of editting the project-specific file): 

`addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")`

Now create a folder which you will use for your Spark project and place the build.sbt found in this repo inside it. Then simply run sbt and use the *eclipse* command to turn it into an Eclipse project (I am assuming that you have Scala set up in your Eclipse IDE):

` > eclipse`

You can now simply import the created project as existing project into Eclipse workspace.

####IntelliJ####
If you're using IntelliJ then all you need to do is to import the project. Select File -> New -> Project from existing sources and import it as SBT project.

Note: You will need Scala 2.10 for these dependencies to work. At the time of writing this, Scala 2.11 was still not supported.

##Working with Spark##

Spark is meant to be run on clusters. However, to get you going you can simply run it on your local machine. Make sure to add `.setMaster("local")` whenever you are setting up a configuration for your Spark context:  

`val conf = new SparkConf().setAppName("sparkintro").setMaster("local")`  
`val sc = new SparkContext(conf)`  

Code that performs the calculation in the `Main.scala` example is taken from an [official examples page](https://spark.apache.org/examples.html).

Of course, there's no fun in simply running it without a cluster, even if the whole cluster is located on your machine. You will need a pre-built distribution of Spark for this; you can get it [here](http://spark.apache.org/downloads.html) (make sure to get the pre-built version). Unless you changed the build.sbt provided in this repo, version you need is 1.2.0.
  
Next step is to prepare the environment. You can use the template config file provided with the distribution:

    cp ./conf/spark-env.sh.template ./conf/spark-env.sh 
    echo "export SPARK_WORKER_INSTANCES=4" >> ./conf/spark-env.sh
  
Now that we have set up our environment to use four Spark workers, all that's left to do is to start the master and the slaves. Note that you will need an SSH deamon running on your system. Linux users can see if it's running by issuing:

    service ssh status
    
while for OS-X it's:

    launchctl list | grep ssh
    
If the service is not running, make sure to install it and run it. For Mac users it's probably enough to enable System Preferences -> Sharing -> Remote Login. Anyways, setting up the SSH deamon is not the point of this text, I'm sure you'll figure it out.

Once the SSH deamon is all good, we can run our master and slave scripts:

    ./sbin/start-master.sh
    ./sbin/start-slaves.sh
    
Navigate to http://localhost:8080, you should see something like:

![Screenshot](./images/screenshot.png)
