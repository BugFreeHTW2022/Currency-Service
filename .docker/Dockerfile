# Multistage Docker: https://docs.docker.com/develop/develop-images/multistage-build/

# Pull official gradle docker image with JDK 17
FROM gradle:7.4.2-jdk17 AS builder

# Define working directory
WORKDIR /app

# Copy project
COPY ../gradle gradle
COPY ../build.gradle ../settings.gradle ../gradlew ./
COPY ../src src

# Build microservice
RUN ./gradlew build
RUN ls -la

FROM openjdk:17

# Copy JAR - file to container
COPY --from=builder /app/build/libs/*.jar app.jar

# Start
ENTRYPOINT ["java","-jar","/app.jar"]
