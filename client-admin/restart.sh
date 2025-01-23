#!/bin/sh

# jar文件名
JAR=client-admin.jar


echo '开始重启' $JAR '环境' $profile

pid=`ps -ef | grep $JAR | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ];then
kill -15 $pid
echo kill -15 $pid
sleep 1
fi

pid=`ps -ef | grep $JAR | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ];then
sleep 2
kill -9 $pid
echo kill -9 $pid
fi

echo starting ...
nohup java -jar $JAR > /dev/null 2>&1 &
echo start success!
echo -e "\n"