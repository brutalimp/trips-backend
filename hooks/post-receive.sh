#!/bin/bash
TARGET="/home/www/trip-backend"
WARFILE="/home/www/trip-backend/target/trips-0.0.1-SNAPSHOT.war"
GIT_DIR="/home/gitrepo/trip-backend.git"
TOMCAT_BIN="/home/tomcat/bin"
TOMCAT_APP="/home/tomcat/webapps/trips.war"
APP_NAME="trip-backend"
BRANCH="master"

echo "post-receive: Triggered."
if [ ! -d "$TARGET" ]; then
  echo "mkdir $TARGET"
  mkdir -p $TARGET
fi
cd $TARGET

echo "post-receive: git check out..."
git --git-dir=$GIT_DIR  --work-tree=$TARGET checkout -f

echo "mvn clean package" \
&& mvn clean package \
&& echo "cd to tomcat bin"\
&& cd $TOMCAT_BIN \
&& echo "stop tomcat" \
&& sh shutdown.sh \
&& echo "delete old files"\
&& rm trips.war \
&& rm -rf trips \
&& echo "copy war file to web apps" \
&& cp $WARFILE $TOMCAT_APP \
&& echo "start tomcat" \
&& sh startup.sh \ 
&& echo "tomcat start succeed!"
