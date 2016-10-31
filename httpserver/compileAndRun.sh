# #!/bin/bash
javac -cp .:./lib/* *.java -d classes
java -cp .:./lib/*:./classes/  server.MainServer  < config