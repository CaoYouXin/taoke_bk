#!/usr/bin/env bash

. ./upload.sh

#ssh root@www.tkmqr.com "bash -s" < nginx.sh &

ssh root@www.tkmqr.com "bash -s" < restart.sh 10001 &

echo "sleeping for 5 seconds ..."
sleep 5

bash ./testurl.sh "http://server.tkmqr.com:10001/admin/ping"

ssh root@www.tkmqr.com "bash -s" < restart.sh 10002 &

echo "sleeping for 5 seconds ..."
sleep 5

bash ./testurl.sh "http://server.tkmqr.com:10002/admin/ping"

echo "all published"
