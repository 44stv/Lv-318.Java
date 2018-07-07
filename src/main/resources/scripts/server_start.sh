#!/usr/bin/env bash

cd /home/ec2-user/server
touch spring.log
java -jar *.jar > spring.log 2>&1 &
