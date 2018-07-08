#!/usr/bin/env bash

cd /home/ec2-user/server
nohup java -jar *.jar > spring.log 2>&1 &
