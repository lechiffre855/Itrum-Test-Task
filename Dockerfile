# Use the official Maven image to build your application
#FROM maven:3.8.1-jdk-11 as build

FROM maven:3.8.4-openjdk-17 as build

# Copy the project files to the container
COPY . /app

# Set the working directory
WORKDIR /app

ENV src/main/resources/application.properties /

# Package your application
RUN mvn clean package

# Use OpenJDK 11 for running the application
#FROM openjdk:11-jre-slim

FROM openjdk:17-slim

# Copy the JAR from the build stage
COPY --from=build /app/target/itrumtask-0.0.1-SNAPSHOT.jar /app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run your app
CMD ["java", "-jar", "/app.jar"]