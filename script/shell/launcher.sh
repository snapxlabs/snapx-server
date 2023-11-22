#! /bin/bash

# APP_NAME=
# JAR_PATH=
# JAR_NAME=
# COMMON_CONFIG=
# SERVER_PORT=
# LOG_CONFIG=
# LOG_PATH=
# PID_FILE=
# JAVA_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m"

print_env() {
  echo "APP_NAME=$APP_NAME"
  echo "JAR_PATH=$JAR_PATH"
  echo "JAR_NAME=$JAR_NAME"
  echo "COMMON_CONFIG=$COMMON_CONFIG"
  echo "SERVER_PORT=$SERVER_PORT"
  echo "LOG_CONFIG=$LOG_CONFIG"
  echo "LOG_PATH=$LOG_PATH"
  echo "PID_FILE=$PID_FILE"
  echo "JAVA_OPTS=$JAVA_OPTS"
}

start() {
  nohup java $JAVA_OPTS -jar $JAR_PATH/$JAR_NAME \
  --spring.config.additional-location=file:$COMMON_CONFIG \
  --server.port=$SERVER_PORT \
  --logging.config=$LOG_CONFIG \
  --logging.path=$LOG_PATH \
  --spring.application.name=$APP_NAME > $LOG_PATH/startup.log 2>&1 &
  echo $! > $PID_FILE
  echo "start $APP_NAME success pid is $! port is $SERVER_PORT"
}

stop() {
  kill `cat $PID_FILE`
  pid=`cat $PID_FILE`
  pid_exist=`ps aux | awk '{print $2}'| grep -w $pid`
  while [ $pid_exist ]; do
      echo "stoping $APP_NAME pid is $pid port is $SERVER_PORT"
      sleep 1
      pid_exist=`ps aux | awk '{print $2}'| grep -w $pid`
  done
  echo "stop $APP_NAME success pid is $pid port is $SERVER_PORT"
  rm -rf $PID_FILE
}

restart() {
  stop
  start
}

print_env
case $1 in
 'start')
    start
    ;;
 'stop')
    stop
    ;;
 'restart')
    restart
    ;;
esac
exit 0