# Build the jar file in docker
FROM maven:3.9-eclipse-temurin-21 as build
WORKDIR /build
COPY . .
RUN mvn package -DskipTests

# Copy jar file
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/server/target/server.jar /app/app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]