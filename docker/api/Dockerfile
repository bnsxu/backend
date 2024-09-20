# Base image
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/meettify-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너 시작 시 실행할 명령어 지정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
