# === STAGE 1: Build the JAR ===
# We use a heavy Maven image just to compile the code
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Tell Maven to package the application (skipping tests to speed up deployment)
RUN mvn clean package -DskipTests

# === STAGE 2: Run the App ===
# We throw away the heavy Maven tools and just keep a tiny Java runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the compiled JAR from Stage 1 and rename it to app.jar
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]