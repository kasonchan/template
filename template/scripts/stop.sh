#!/bin/bash

PID=`cat RUNNING_PID`
echo ${PID}
kill -9 ${PID}
echo "kill -9 ${PID}"
rm RUNNING_PID
