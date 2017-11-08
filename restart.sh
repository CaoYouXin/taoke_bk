#!/usr/bin/env bash

kill -9 $(lsof -t -i:10001) && nohup java -jar /var/taoke/miquaner-0.0.1-SNAPSHOT.jar --server.port=10001 >/dev/null 2>&1 </dev/null &
