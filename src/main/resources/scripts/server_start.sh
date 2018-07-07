#!/usr/bin/env bash

cd /home/ec2-user/server
java -jar *.jar > output.log 2>&1 &
