echo off
for /L %%A in (1,1,%1) do (
	set /a "port=%%A+8080"
	call echo creating container joe%%A with port %%port%% ......
	call docker run -d --name joe%%A -e spring.datasource.url=jdbc:postgresql://172.17.0.1/example -e callback.url=http://localhost:8080/callback -e message.broker.url=tcp://192.168.0.49:61616 -p %%port%%:8080 example:0.0.1 --env-file src\main\resources\application.properties
)