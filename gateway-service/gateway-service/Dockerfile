# Stage 1: Build stage (Switch to JDK 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only the pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run stage (Match with JDK 21)
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]