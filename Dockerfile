# Build the Spring Boot application.
FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

# Keep the runtime image smaller by excluding Maven and build tools.
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /workspace/target/*.jar /app/app.jar

ENV PORT=9000
EXPOSE 9000

# Run the application as a non-root user (UID:GID 1000:1000).
USER 1000:1000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
