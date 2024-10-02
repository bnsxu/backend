# Build stage
FROM gradle:8.0.2-jdk17 AS builder
WORKDIR /app

# 소스 코드 및 Gradle 설정 파일 복사
COPY . .

# Gradle 빌드 수행
RUN ./gradlew clean build --no-daemon

# Run stage
FROM openjdk:17
ARG JAR_FILE_PATH=build/libs/*.jar
COPY --from=builder ${JAR_FILE_PATH} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
