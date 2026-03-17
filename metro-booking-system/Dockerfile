FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Support builds from repo root or from the backend subdir.
RUN if [ -f metro-booking-system/pom.xml ]; then mvn -f metro-booking-system/pom.xml package -DskipTests; else mvn package -DskipTests; fi \
    && if ls metro-booking-system/target/*.jar 1> /dev/null 2>&1; then cp metro-booking-system/target/*.jar /app/app.jar; else cp target/*.jar /app/app.jar; fi

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
