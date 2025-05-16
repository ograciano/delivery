# Auth Service

Microservicio de autenticación para la plataforma DeliveryApp.

## Tecnologías

- Java 21
- Spring Boot 3.3.0
- Spring WebFlux
- Spring Security + JWT
- R2DBC (PostgreSQL)
- Swagger OpenAPI para WebFlux

## Correr localmente

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
