FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Demo-1.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]