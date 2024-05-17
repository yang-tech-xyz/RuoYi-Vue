# Use the official OpenJDK 21 image as the base image
FROM karluto/jdk21-apline3.18

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/client-admin-1.0-SNAPSHOT.jar /app/client-admin-1.0-SNAPSHOT.jar

# Expose the port that the application will run on
EXPOSE 8082

# Command to run the application when the container starts
CMD ["java", "-jar", "client-admin-1.0-SNAPSHOT.jar"]
