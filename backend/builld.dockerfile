# Build the jar file in docker
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Copy pom.xml
COPY pom.xml .
COPY server/pom.xml server/
COPY common/pom.xml common/
COPY pojo/pom.xml pojo/

# Build all the dependencies
RUN mvn dependency:go-offline

# Copy the project source code
COPY . .
RUN mvn package -DskipTests

# Copy jar file
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/server/target/server.jar /app/app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]