#!/bin/bash
javac -d classes -cp *.jar *.java
java -cp classes:jetty-all-uber.jar org.eclipse.jetty.embedded.MainServer

