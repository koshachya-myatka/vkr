FROM maven:latest AS build
WORKDIR /data-mart-backend
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /data-mart-backend
COPY --from=build data-mart-backend/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]