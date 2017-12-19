@echo on
@echo =============================================================
@echo $                                                           $
@echo $                     Nepxion Coroutine                     $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Technologies All Right Reserved                  $
@echo $  Copyright(C) 2017                                        $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Coroutine
@color 0a

call mvn dependency:tree

rem �鿴���ṹ��
rem call mvn dependency:tree -Dverbose -Dincludes=org.apache.curator:curator-recipes

rem ȫ����������lib
rem call mvn dependency:copy-dependencies -DoutputDirectory=lib

rem ֻ������������lib
rem call mvn dependency:copy-dependencies -DoutputDirectory=lib -DincludeScope=compile

rem ��WAR��
rem call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -Dversion=1.0.0-SNAPSHOT -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeCatalog=internal

rem ��POM
rem call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -DarchetypeArtifactId=maven-archetype-site-simple -DarchetypeCatalog=internal

rem ��JAR��
rem call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=internal 

rem ���뵽���ؿ�
rem call mvn install:install-file -DgroupId=kilim -DartifactId=kilim -Dversion=1.0 -Dfile=lib/kilim-1.0.jar -Dpackaging=jar -DgeneratePom=true

pause