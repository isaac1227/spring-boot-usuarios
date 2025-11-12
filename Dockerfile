# Multi-stage Dockerfile: build with Maven, run with a lightweight JRE
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy only what we need to cache dependencies better
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

# Build the application (skip tests to speed up local builds)
RUN mvn -B -DskipTests package

# Runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
