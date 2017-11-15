#!/usr/bin/env bash

rsync -avz -e "ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null" --progress /Users/cls/Dev/Git/taoke/taoke_bk/target/miquaner-0.0.1-SNAPSHOT.jar root@www.tkmqr.com:/var/taoke/

#rsync -avz -e "ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null" --progress /usr/local/etc/nginx/servers/* root@www.tkmqr.com:/etc/nginx/sites-enabled/
