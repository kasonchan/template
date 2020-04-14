#!/bin/bash

xmsValue=$1
xmxValue=$2

if [ ! $# == 2 ];then
    echo "Usage: start.sh [Xms value] [Xmx value]"
    echo "You must specify the minimum heap size and maximum heap size."
    exit 1
fi

CURRENT_PATH=$(pwd)
FILE=TEST_RUNNING_PID
if [[ -f "$FILE" ]]; then
  echo "This application is already running (or delete ${CURRENT_PATH}/${FILE} file)."
else
  echo "This application is starting."
  mkdir -p "${CURRENT_PATH}"/logs

  # https://www.oracle.com/technetwork/articles/java/vmoptions-jsp-140102.html
  nohup ./usr/share/template/bin/template -J-Xms"${xmsValue}" -J-Xmx"${xmxValue}" \
    -J-XX:+UseParallelGC -J-XX:+UseStringDeduplication -J-verbose:gc -J-XX:-PrintGCDetails \
    -J-XX:+PrintGCDateStamps -J-Xloggc:./logs/gc.log -J-XX:+HeapDumpOnOutOfMemoryError \
    -J-XX:HeapDumpPath=./logs/dumps/hs_err_%p_`date`.hprof -XX:ErrorFile=./logs/hs_err_%p.log \
    -Dad.service.host=0.0.0.0 > logs/std.out 2>&1 &
  PID=$!
  echo "$PID" >> RUNNING_PID
fi

tail -f /dev/null
