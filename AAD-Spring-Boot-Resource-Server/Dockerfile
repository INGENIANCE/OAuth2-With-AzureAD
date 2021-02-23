## Stage 0: Buld env.
FROM registry.gitlab.com/ingeniance/infrastucture/docker-images/build_java_services/java_build:11.0.1 AS BUILD
LABEL maintainer="Cyrille PERROT"
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
FROM registry.gitlab.com/ingeniance/infrastucture/docker-images/build_java_services/java_services:11.0.1
USER root
# copy over the built artifact from the maven image
COPY --from=BUILD /src/main/resources/application.yml ./
COPY --from=BUILD /target/*.jar ./web-api.jar
EXPOSE 9091
ENTRYPOINT ["java","-jar","/web-api.jar"]