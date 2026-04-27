FROM maven:latest AS build
WORKDIR /data-generator
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /data-generator
COPY --from=build data-generator/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]