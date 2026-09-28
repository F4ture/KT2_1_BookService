#!/bin/sh
BASE_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
MAVEN_USER_HOME=${MAVEN_USER_HOME:-"$BASE_DIR/work/.m2"}
export MAVEN_USER_HOME
JAVACMD=${JAVA_HOME:+"$JAVA_HOME/bin/java"}
JAVACMD=${JAVACMD:-java}
exec "$JAVACMD" "-Duser.home=$BASE_DIR/work" "-Dmaven.repo.local=$MAVEN_USER_HOME/repository" "-Dmaven.multiModuleProjectDirectory=$BASE_DIR" -classpath "$BASE_DIR/.mvn/wrapper/maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain "$@"
