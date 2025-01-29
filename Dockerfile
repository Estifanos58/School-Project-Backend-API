# Build stage
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app

# Copy the POM file and download dependencies
COPY pom.xml .
RUN mvn -X dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/ChatAPP-0.0.1-SNAPSHOT.jar .

# Expose port 8080 for the application
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "/app/ChatAPP-0.0.1-SNAPSHOT.jar"]
