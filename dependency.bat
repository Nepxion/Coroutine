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

rem 查看包结构树
rem call mvn dependency:tree -Dverbose -Dincludes=org.apache.curator:curator-recipes

rem 全部包导出到lib
rem call mvn dependency:copy-dependencies -DoutputDirectory=lib

rem 只导出关联包到lib
rem call mvn dependency:copy-dependencies -DoutputDirectory=lib -DincludeScope=compile

rem 出WAR包
rem call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -Dversion=1.0.0-SNAPSHOT -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeCatalog=internal

rem 出POM
rem call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -DarchetypeArtifactId=maven-archetype-site-simple -DarchetypeCatalog=internal

rem 出JAR包
rem call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=internal 

rem 加入到本地库
rem call mvn install:install-file -DgroupId=kilim -DartifactId=kilim -Dversion=1.0 -Dfile=lib/kilim-1.0.jar -Dpackaging=jar -DgeneratePom=true

pause