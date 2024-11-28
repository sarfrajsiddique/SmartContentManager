#Stage 1
FROM maven:3.8-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

#Stage 2
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]