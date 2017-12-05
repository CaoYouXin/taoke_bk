#!/usr/bin/env bash

server_ok=false;
retry_delay=1;
while [[ ${server_ok} == false ]]; do

curl $1 > server.json

if [ $? == 0 ];then
	if [ $(jq .code server.json) == 2000 ];then
		echo "$1 返回json无误";
		server_ok=true;
	else
	cat server.json
	echo "${retry_delay} 秒后重新测试！";
	sleep ${retry_delay};
	let retry_delay++;
	fi
else
	cat server.json;
	echo "$1 服务器异常，请检查！！";
	echo "${retry_delay} 秒后重新测试！";
	sleep ${retry_delay};
	let retry_delay++;
fi
done
