@echo off
setlocal
set "WRAPPER_JAR=%~dp0.mvn\wrapper\maven-wrapper.jar"
if not defined MAVEN_USER_HOME set "MAVEN_USER_HOME=%~dp0work\.m2"
set "JAVACMD=java"
if defined JAVA_HOME set "JAVACMD=%JAVA_HOME%\bin\java.exe"
if not exist "%WRAPPER_JAR%" (
  echo Maven Wrapper JAR not found: %WRAPPER_JAR%
  exit /b 1
)
"%JAVACMD%" "-Duser.home=%~dp0work" "-Dmaven.repo.local=%MAVEN_USER_HOME%\repository" "-Dmaven.multiModuleProjectDirectory=%~dp0." -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
set "MVNW_EXIT=%ERRORLEVEL%"
endlocal & exit /b %MVNW_EXIT%
