FROM ubuntu:latest
LABEL authors="duaaa"

ENTRYPOINT ["top", "-b"]

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/Onsocial_app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]