FROM openjdk:22-ea-17-slim
VOLUME /tmp
COPY target/discoveryservice-1.0.jar discovery-service.jar
ENTRYPOINT ["java", "-jar", "discovery-service.jar"]