FROM openjdk:22-ea-17-slim
VOLUME /tmp
COPY target/order-service-1.0.jar order-service.jar
ENTRYPOINT ["java", "-jar", "order-service.jar"]