# Base image for building the application
FROM gradle:8.0.2-jdk17 AS builder
WORKDIR /app

# Gradle Wrapper를 복사하고 실행 권한 부여
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
# YAML 파일 복사
COPY src/main/resources/application-prod.yml /app/config/

# 소스 코드를 복사
COPY src src

# Gradle 빌드 수행
RUN chmod +x gradlew && ./gradlew build --no-daemon

# Base image for running the application
FROM openjdk:17-jdk-slim

# JAR 파일 경로 설정
ARG JAR_FILE=build/libs/meettify-0.0.1-SNAPSHOT.jar

EXPOSE 8080

# 빌드된 JAR 파일을 컨테이너로 복사
COPY --from=builder /app/${JAR_FILE} app.jar

# 컨테이너 시작 시 실행할 명령어 지정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
