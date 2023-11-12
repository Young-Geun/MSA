FROM openjdk:22-ea-17-slim
VOLUME /tmp
COPY target/catalog-service-1.0.jar catalog-service.jar
ENTRYPOINT ["java", "-jar", "catalog-service.jar"]