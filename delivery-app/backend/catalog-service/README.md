# Catalog Service

This microservice exposes the product catalog API.

## Running Locally

Use Docker Compose to start the infrastructure, including PostgreSQL and Redis:

```bash
./start_infra_and_show_urls.sh
```

Run the service with the `local` profile (already configured in the compose file). It connects to Redis on `redis:6379`.

## Redis Caching

The service uses Spring Cache backed by Redis to cache the list of products. Cached data is refreshed when products are created or stock is updated.
