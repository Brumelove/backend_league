FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
