FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/order-service-1.0.0.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]