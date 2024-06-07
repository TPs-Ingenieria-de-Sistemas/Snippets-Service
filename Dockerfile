FROM gradle:8.7.0-jdk17-jammy AS build
COPY . /app
WORKDIR /app
RUN chmod +x gradlew
RUN ./gradlew bootJar

FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
RUN mkdir /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/snippet-service-0.0.1-SNAPSHOT.jar /app/snippet-service.jar

# Copy the start.sh script
COPY /start.sh /app/start.sh

# Make the start.sh script executable
RUN chmod +x /app/start.sh

# Set the entry point to the start.sh script
ENTRYPOINT ["/app/start.sh"]
