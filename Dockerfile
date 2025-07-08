# Stage 1: Build Application
FROM maven:3.9.10-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Dspring.profiles.active=build

# Stage 2: Runtime Environment
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 3001

# Run the application
ENTRYPOINT [ "java", "-jar", "app.jar"]