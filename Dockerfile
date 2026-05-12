# 1. Start with a lightweight Linux machine that has Java 17 installed
FROM eclipse-temurin:17-jdk-alpine

# 2. Create a directory inside the container called /app
WORKDIR /app

# 3. Copy your newly built JAR file from your computer into the container
COPY target/*.jar finance-tracker-0.0.1-SNAPSHOT.jar

# 4. Open port 8080 so the outside world can talk to it
EXPOSE 8080

# 5. The command to start your Spring Boot app
ENTRYPOINT ["java", "-jar", "finance-tracker-0.0.1-SNAPSHOT.jar"]