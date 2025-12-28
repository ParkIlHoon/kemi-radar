# Build stage
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Copy gradle files
COPY gradle ./gradle
COPY gradlew ./
COPY gradlew.bat ./
COPY gradle.properties ./
COPY settings.gradle.kts ./
COPY build.gradle.kts ./

# Copy source code
COPY src ./src

# Build the application
RUN ./gradlew build --no-daemon -x test

# Runtime stage
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port
EXPOSE 8080

# Set environment variables (can be overridden)
ENV PORT=8080
ENV ECOS_API_KEY=""
ENV FRED_API_KEY=""

# Run the application
CMD ["java", "-jar", "app.jar"]
