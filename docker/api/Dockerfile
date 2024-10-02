# Base image for building the application
FROM gradle:8.0.2-jdk17 AS builder
WORKDIR /app


# Gradle 빌드 수행
RUN chmod +x gradlew && ./gradlew build --no-daemon --parallel

# Base image for running the application
FROM openjdk:17-jdk-slim

# JAR 파일 경로 설정
ARG JAR_FILE=build/libs/meettify-0.0.1-SNAPSHOT.jar

EXPOSE 8080

# 빌드된 JAR 파일을 컨테이너로 복사
COPY --from=builder /app/${JAR_FILE} app.jar

# 컨테이너 시작 시 실행할 명령어 지정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
