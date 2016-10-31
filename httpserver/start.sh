#!/bin/bash
#make sure a process is always running.

export DISPLAY=:0 #needed if you are running a simple gui app.

process="server.MainServer"
makerun="/usr/bin/java -cp .:./lib/*:./classes/ server.MainServer < config"

check_process() {
  echo "$ts: checking $1"
  [ "$1" = "" ]  && return 0
  [ `pgrep -n $1` ] && return 1 || return 0
}

while [ 1 ]; do 
  # timestamp
  ts=`date +%T`

  echo "$ts: begin checking..."
  check_process "$process"
  [ $? -eq 0 ] && echo "$ts: not running, restarting..." && eval $makerun
  sleep 5
done

