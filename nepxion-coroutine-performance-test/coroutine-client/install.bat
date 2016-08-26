copy ..\coroutine-server\target\nepxion-coroutine-server-performance-test-1.0.0.jar lib\

call mvn clean install

copy target\nepxion-coroutine-client-performance-test-1.0.0.jar lib\

pause