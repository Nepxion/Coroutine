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

call mvn install:install-file -DgroupId=kilim -DartifactId=kilim -Dversion=1.0.0 -Dfile=repo/kilim-1.0.0-jdk-1.7.0.jar -Dpackaging=jar -DgeneratePom=true

pause