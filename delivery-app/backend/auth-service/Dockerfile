FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/auth-service-1.0.0.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
