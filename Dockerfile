#Build(bygger JAR-filen)
#Startar en miljö som har både Maven och Java 17, så att Docker kan bygga min Spring Boot appen
FROM maven:3.9.6-eclipse-temurin-21 AS build
#sätter arbetsmappen till /app,  håller allt organiserat
WORKDIR /app

#Kopierar från pom.xml till container, Maven kan alltså ladda ner dependencies
COPY pom.xml .
#Laddar ner alla dependencies, det gör så att bygget blir snabbare
RUN mvn -B dependency:go-offline

#kopierar hela källkoden, Maven behöver den för att bygga JAR-filen
COPY src ./src
#Bygger JAR-filen och hoppar över tester. snabbare build, inga tester behövs i Docker
RUN mvn -B package -DskipTests

#Run(Kör appen)
#Startar en liten Java-miljö(JRE), kör Spring Boot i produktion
FROM eclipse-temurin:17-jre
#sätter arbetsmappen igen för ordning och reda
WORKDIR /app

#Kopierar JAR-filen från build-steget ovan, detta gör att man får en ren och liten image
COPY --from=build /app/target/Onsocial_app-0.0.1-SNAPSHOT.jar app.jar

#Molntjänster behöver veta vilken port som används.vår app använder port 8080
EXPOSE 8080
#Startar Spring Boot appen. Containern kör automatiskt min app
ENTRYPOINT ["java", "-jar", "app.jar"]