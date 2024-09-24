# Base image
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod

# 컨테이너 시작 시 실행할 명령어 지정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
