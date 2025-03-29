# Use an OpenJDK image with JavaFX support (Java 17)
FROM openjdk:17-jdk-slim

# Set the working directory to /game inside the container
WORKDIR /game

# Copy the gradle wrapper and gradle files into the container
COPY game/src /game/src
COPY game/build.gradle /game/build.gradle


# Give execute permissions to gradlew
RUN chmod +x gradlew

# Build the project using Gradle
RUN gradle build

# Run Gradle to start the game (instead of building a JAR file)
CMD ["gradle", "run"]
