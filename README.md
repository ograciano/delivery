# DeliveryApp Monorepo

This repository contains a set of microservices and the required infrastructure to run a sample delivery platform. The `delivery-app` folder groups all backend services, Docker Compose files and infrastructure definitions used for local development.

## Repository layout

```
/README.md              This file
/delivery-app           Backend microservices and Docker configuration
    ├─ backend/         Source for each Spring Boot service
    ├─ docker-compose.yml       Compose file with all services for local usage
    ├─ docker-compose.override.yml  Extra dependencies between services
    ├─ docker-compose.prod.yml  Reduced set of containers for a prod like mode
    ├─ infrastructure/  Initialization files for Postgres, Prometheus, etc.
    └─ docs/            Project documentation
/imagenes               Sample images for the catalog service
setup_local_infra.sh    Helper script used to create the folder structure
start_infra_and_show_urls.sh  Script that starts Docker Compose and prints URLs
```

## Running the stack with Docker Compose

1. Install Docker and Docker Compose.
2. From the repository root run:

```bash
cd delivery-app
docker-compose up -d
```

This will start PostgreSQL, Redis, RabbitMQ, MinIO, Prometheus, Grafana, the ELK stack and all the backend microservices.

If you want to run the trimmed production configuration you can execute:

```bash
docker-compose -f docker-compose.prod.yml up -d
```

The script `start_infra_and_show_urls.sh` provides the same behaviour and prints the most common service URLs.

## Microservices

All services are implemented using Spring Boot and expose reactive REST APIs.

| Service            | Port | Purpose |
|--------------------|------|---------|
| **gateway-service**| 8080 | Entry point for clients. Routes requests to the remaining microservices and applies authentication via JWT. |
| **auth-service**   | 8084 | Handles user authentication and token issuance. Uses PostgreSQL via R2DBC. |
| **catalog-service**| 8081 | Manages products and categories. Integrates with Elasticsearch for search and with the file-service for product images. |
| **order-service**  | 8082 | Responsible for order creation, status updates and merchant summaries. |
| **delivery-service**| 8083 | Tracks drivers and order assignments. |
| **file-service**   | 8086 | Uploads and retrieves files using MinIO locally or AWS S3 in production. |

Support services like PostgreSQL, Redis, RabbitMQ, MinIO, Prometheus, Grafana and the ELK stack are also started by Docker Compose.

