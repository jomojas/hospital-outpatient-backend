# =====================================
# Builder Stage: JDK 21 + Maven on Alpine
# =====================================
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Install Maven (requirement: base on eclipse-temurin JDK image)
RUN apk add --no-cache maven

# Copy Maven descriptor first to leverage layer caching for dependencies
COPY pom.xml ./
RUN mvn -B -ntp -f pom.xml dependency:go-offline

# Copy source and build (skip tests per requirement)
COPY src ./src
RUN mvn -B -ntp clean package -DskipTests \
  && JAR_FILE="$(ls target/*.jar | grep -v '\-original' | head -n 1)" \
  && cp "$JAR_FILE" /app/app.jar

# =====================================
# Runtime Stage: JRE 21 on Alpine
# =====================================
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Create a non-root user for better security
RUN adduser -D appuser
USER appuser

# Copy only the packaged application
COPY --from=builder /app/app.jar /app/app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

