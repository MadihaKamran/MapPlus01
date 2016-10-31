#!/bin/bash
javac -cp \*  *.java
java -cp classes:jetty-all-uber.jar org.eclipse.jetty.embedded.MainServer  < config

