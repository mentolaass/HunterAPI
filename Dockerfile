FROM maven:3.9.9-eclipse-temurin-17 AS build
COPY . /app
WORKDIR /app
RUN mvn clean resources:resources compiler:compile surefire:test jar:jar spring-boot:repackage

FROM eclipse-temurin:17-jdk
COPY --from=build /app/target/*.jar /app.jar
CMD ["java", "-jar", "/app.jar"]