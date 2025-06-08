# Gateway Service

Spring Cloud Gateway acting as the single entry point for all backend services. It applies JWT authentication and routes requests to the underlying microservices.

## Running locally

Start the supporting infrastructure:

```bash
./start_infra_and_show_urls.sh
```

Run the gateway on port **8080**:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Key endpoints

The gateway forwards requests to each service using these base paths:

- `/api/auth/**` → auth-service
- `/api/products/**` and `/api/categories/**` → catalog-service
- `/api/orders/**` → order-service
- `/api/drivers/**` and `/api/assignments/**` → delivery-service
- `/api/files/**` → file-service

OpenAPI documentation for all services is available at:
`http://localhost:8080/swagger-ui.html`.
