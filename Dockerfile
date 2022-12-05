FROM openjdk:8-jdk-alpine
MAINTAINER joe
COPY target/example-0.0.1-SNAPSHOT.jar example-0.0.1.jar
EXPOSE 8080-8090
ENTRYPOINT ["java","-jar","/example-0.0.1.jar"]