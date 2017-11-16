#!/usr/bin/env bash

bash ./upload.sh && ssh root@www.tkmqr.com "bash -s" < restart.sh &