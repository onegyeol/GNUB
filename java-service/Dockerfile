# java-service/Dockerfile
FROM openjdk:21-slim

WORKDIR /app

# 프로젝트 루트의 빌드된 jar 파일을 컨테이너에 복사
# docker-compose.yml 파일에서 Java 서비스의 build context를 프로젝트 루트로 설정해야 합니다.
COPY build/libs/GNUB-0.0.1-SNAPSHOT.jar .

# Spring Boot 애플리케이션이 사용하는 포트 노출 (기본 8080)
EXPOSE 8080

# jar 파일 실행
CMD ["java", "-jar", "GNUB-0.0.1-SNAPSHOT.jar"]
