# 멀티스테이지 빌드를 사용하여 빌드와 런타임 환경을 분리
FROM gradle:8.5-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 설정 파일들을 먼저 복사 (캐시 최적화)
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle ./gradle

# application 모듈의 build.gradle만 복사
COPY application/build.gradle ./application/

# 의존성 다운로드 (캐시 최적화)
RUN gradle :application:dependencies --no-daemon

# application 소스 코드와 리소스 파일 복사
COPY application/src ./application/src

# application-prod.yml이 확실히 포함되었는지 확인
RUN ls -la application/src/main/resources/application*.yml

# 애플리케이션 빌드 (application 모듈만)
RUN gradle :application:bootJar --no-daemon

# 런타임 스테이지 (Alpine Linux 기반으로 더 작은 이미지)
FROM eclipse-temurin:17-jre-alpine

# 애플리케이션 실행을 위한 유저 생성
RUN addgroup -S spring && adduser -S spring -G spring

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/application/build/libs/*.jar app.jar

# 애플리케이션 소유권 변경
RUN chown spring:spring app.jar

# 포트 노출
EXPOSE 8080

# 비root 유저로 실행
USER spring:spring

# 애플리케이션 실행 (prod 프로파일 활성화)
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]