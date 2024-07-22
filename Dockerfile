FROM gradle:8.7.0-jdk17-jammy AS build
COPY  . /app
WORKDIR /app

ENV USERNAME
ENV TOKEN

RUN chmod +x gradlew
RUN ./gradlew bootJar

FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
RUN mkdir /app
COPY --from=build /app/build/libs/snippet-service-*.jar /app/snippet-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production","/app/snippet-service.jar"]
