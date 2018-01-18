@echo on
@echo =============================================================
@echo $                                                           $
@echo $                     Nepxion Coroutine                     $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Studio All Right Reserved                        $
@echo $  Copyright (C) 2017-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Coroutine
@color 0a

call mvn install:install-file -DgroupId=kilim -DartifactId=kilim -Dversion=1.0.1 -Dfile=repo/kilim/kilim/1.0.1/kilim-1.0.1.jar -Dpackaging=jar -DgeneratePom=true

pause