#!/usr/bin/env bash

#nginx -s reload

kill -9 $(lsof -t -i:10001) \
    && nohup java -jar /var/taoke/miquaner-0.0.1-SNAPSHOT.jar --server.port=10001 >/dev/null 2>&1 </dev/null &

bash ./testurl.sh "http://server.tkmqr.com:10001/admin/ping"

kill -9 $(lsof -t -i:10002) \
    && nohup java -jar /var/taoke/miquaner-0.0.1-SNAPSHOT.jar --server.port=10002 >/dev/null 2>&1 </dev/null &

bash ./testurl.sh "http://server.tkmqr.com:10002/admin/ping"

echo "all published"
