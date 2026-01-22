#Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

#
COPY src ./src
RUN mvn -B package -DskipTests

#Run
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/Onsocial_app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]