#!/usr/bin/env bash

. ./upload.sh

ssh root@www.tkmqr.com "bash -s" < restart.sh &