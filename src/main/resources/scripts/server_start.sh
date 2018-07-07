#!/usr/bin/env bash

cd /home/ec2-user/server
java -jar *.jar 2>&1 >> logfile.log &
