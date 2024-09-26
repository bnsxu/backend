# Base image for building the application
FROM gradle:7.0-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build

# Base image
FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar

# 빌드된 JAR 파일을 컨테이너로 복사
COPY ${JAR_FILE} app.jar

# 컨테이너 시작 시 실행할 명령어 지정
# prod를 application-prod.yml을 사용하려고 한다.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
