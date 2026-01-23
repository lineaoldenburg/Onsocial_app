#Build(bygger JAR-filen)
#Startar en miljö som både Maven och Java 17, Så att Docker kan bygga vår Spring Boot app
FROM maven:3.9.6-eclipse-temurin-17 AS build
#Sätter arbetsmappen till /app, håller allt organiserat
WORKDIR /app

#Kopierar pom.xml till containern
COPY pom.xml .
#Laddar ner all dependencies för gör bygget snabbare
RUN mvn -B dependency:go-offline

#Kopierar hela källkoden, Maven behöver den för att bygga JAR-filen
COPY src ./src
#Bygger JAR-filen och hoppar över tester. snabbare build och inga tester behövs i Docker
RUN mvn -B package -DskipTests

#Run(Kör appen)
#Startar en liten Java-miljö(JRE, perfekt för att köra Spring Boot i produktion)
FROM eclipse-temurin:17-jre
#Sätter arbetsmappen igen för ordning och reda
WORKDIR /app

#Kopierar JAR-filen från build-steget ovan
COPY --from=build /app/target/Onsocial_app-0.0.1-SNAPSHOT.jar app.jar

#Molntjänster behöver veta vilken port som används, vi använder port 8080 i vår app
EXPOSE 8080
#Startar Spring Boot appen, Containern kör automatiskt vår app
ENTRYPOINT ["java", "-jar", "app.jar"]