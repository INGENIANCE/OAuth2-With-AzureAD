## Stage 0: Build env.
FROM maven:3.6.0-jdk-11-slim AS BUILD
LABEL maintainer="Cyrille P."
ENV MAVEN_HOME .
# Copy the project files
COPY ./pom.xml ./pom.xml
# Build all dependencies for offline use
RUN mvn dependency:go-offline -B
# Copy your other files
COPY ./src ./src
# Build for release
RUN mvn clean package spring-boot:repackage -B -DskipTests

## Stage 1: Run env.
FROM openjdk:11.0.1-jre-slim
USER root
# copy over the built artifact from the maven image
COPY --from=BUILD /src/main/resources/application.yml ./
COPY --from=BUILD /target/*.jar ./web-api.jar
EXPOSE 9091
ENTRYPOINT ["java","-jar","/web-api.jar"]