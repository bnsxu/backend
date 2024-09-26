# Base image for building the application
FROM gradle:7.0-jdk17 AS builder
WORKDIR /app

# Gradle Wrapper를 사용하여 빌드
RUN chmod +x gradlew && ./gradlew build --no-daemon

# Base image for running the application
FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/meettify-0.0.1-SNAPSHOT.jar

EXPOSE 8080

# 빌드된 JAR 파일을 컨테이너로 복사
COPY ${JAR_FILE} app.jar

# 컨테이너 시작 시 실행할 명령어 지정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]