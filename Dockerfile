FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle ./gradle
COPY application/build.gradle ./application/
RUN gradle :application:dependencies --no-daemon
COPY application/src ./application/src
RUN ls -la application/src/main/resources/application*.yml
RUN gradle :application:bootJar --no-daemon
FROM eclipse-temurin:17-jre-jammy
RUN groupadd -r spring && useradd -r -g spring spring
WORKDIR /app
COPY --from=build /app/application/build/libs/*.jar app.jar
RUN chown spring:spring app.jar
EXPOSE 8080
USER spring:spring
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]