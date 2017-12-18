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

REM 查看包结构树
REM call mvn dependency:tree -Dverbose -Dincludes=org.apache.curator:curator-recipes

REM 全部包导出到lib
REM call mvn dependency:copy-dependencies -DoutputDirectory=lib

REM 只导出关联包到lib
REM call mvn dependency:copy-dependencies -DoutputDirectory=lib -DincludeScope=compile

REM 出WAR包
REM call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -Dversion=1.0.0-SNAPSHOT -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeCatalog=internal

REM 出POM
REM call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -DarchetypeArtifactId=maven-archetype-site-simple -DarchetypeCatalog=internal

REM 出JAR包
REM call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=internal 

REM 加入到本地库
REM call mvn install:install-file -DgroupId=kilim -DartifactId=kilim -Dversion=1.0 -Dfile=lib/kilim-1.0.jar -Dpackaging=jar -DgeneratePom=true

pause