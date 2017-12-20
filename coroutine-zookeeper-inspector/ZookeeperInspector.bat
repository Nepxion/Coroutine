@echo on
@echo =============================================================
@echo $                                                           $
@echo $                    Apache ZooInspector                    $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Technologies All Right Reserved                  $
@echo $  Copyright(C) 2017                                        $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title ZooInspector
@color 0a

@set CLASSPATH=./conf/;./icons/*;./lib/*
@set PATH=

call:JAVA_HOME_CHECK

:JAVA_HOME_CHECK
if "%JAVA_HOME%"=="" goto ECHO_JAVA_HOME_CHECK_QUIT
	
@rem echo Found Java Home=%JAVA_HOME%
echo JAVA_HOME=%JAVA_HOME%
goto SET_CLASSPATH_AND_RUN

:ECHO_JAVA_HOME_CHECK_QUIT
echo Please set JAVA_HOME
goto QUIT

:SET_CLASSPATH_AND_RUN
"%JAVA_HOME%\bin\java" -Dfile.encoding=GBK -Ddefault.client.encoding=GBK -Duser.language=zh -Duser.region=CN -Djava.security.policy=java.policy -Djava.library.path=%PATH% -Xms128m -Xmx512m -classpath %CLASSPATH% org.apache.zookeeper.inspector.ZooInspector

:QUIT

pause