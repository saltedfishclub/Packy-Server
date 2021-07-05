#!/bin/sh

# jar包名称
jar_name=api-1.0.jar

# 根据启动的jar包名称关闭旧的进程实例
pid=`ps -ef | grep ${jar_name} | grep -v grep | awk '{print $2}'`
echo "API接口服务旧应用进程ID：$pid"
if [ -n "$pid" ]
then
  echo "关闭API接口服务旧进程：$pid"
  kill -9 $pid
fi
