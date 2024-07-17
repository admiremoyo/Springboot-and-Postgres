# Use a base image with Java runtime
FROM openjdk:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar file to the Docker image
COPY target/rimewwn-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
