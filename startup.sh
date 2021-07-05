#!/bin/sh

# java -jar archives-management-1.0-SNAPSHOT.jar --spring.profiles.active=test &

# 根据启动的jar包名称关闭旧的进程实例
pid=`ps -ef | grep app.jar | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]
then
  echo "API接口服务还未关闭,旧应用进程PID：$pid"
  echo "关闭API接口服务旧进程：$pid"
  kill -9 $pid
fi

echo "启动API接口服务..."

java -jar target/app.jar --spring.profiles.active=test &
