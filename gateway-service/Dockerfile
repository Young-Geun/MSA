FROM openjdk:22-ea-17-slim
VOLUME /tmp
COPY target/gateway-service-1.0.jar gateway-service.jar
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]