FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=builder /workspace/target/*.jar ms-usuario.jar
EXPOSE 8091
ENTRYPOINT ["java","-jar","ms-usuario.jar"]