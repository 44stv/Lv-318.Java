#!/usr/bin/env bash

cd /home/ec2-user/server
nohup java -jar *.jar > /dev/null &
