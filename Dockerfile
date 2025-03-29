# Use an OpenJDK image with JavaFX support (Java 17)
FROM openjdk:17-jdk-slim

# Set the working directory to /game inside the container
WORKDIR /game

# Copy the gradle wrapper and gradle files into the container
COPY game/src /game/src
COPY game/build.gradle /game/build.gradle

# Install dependencies
RUN apt-get update && \
    apt-get install -y wget unzip

# Install dependencies for Gradle and JavaFX
RUN apt-get update && \
    apt-get install -y wget unzip libx11-dev libgl1-mesa-glx libxi6 libxtst6 libpng-dev libfontconfig1

# Download Gradle 7.5.1
WORKDIR /game
RUN wget https://services.gradle.org/distributions/gradle-7.5.1-bin.zip && \
    unzip gradle-7.5.1-bin.zip

# Set environment variables for Gradle
ENV GRADLE_HOME=/game/gradle-7.5.1
ENV PATH=$GRADLE_HOME/bin:$PATH

# Build the project using Gradle
RUN gradle build

# Run Gradle to start the game (instead of building a JAR file)
CMD ["gradle", "run"]

hello