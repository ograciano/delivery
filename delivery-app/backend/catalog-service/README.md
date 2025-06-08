# Catalog Service

Service responsible for managing products and categories.

## Running locally

Start the infrastructure from the repository root:

```bash
./start_infra_and_show_urls.sh
```

Then launch the service on port **8081** using the `local` profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Key endpoints

- `GET /api/products` – list all products
- `POST /api/products` – create a product
- `POST /api/products/update-stock` – update stock after an order
- `POST /api/products/{id}/files` – upload images for a product
- `GET /api/categories` – list categories
- `POST /api/categories` – create a category
- `GET /api/products/search` – list indexed products
- `GET /api/products/search/{id}` – get indexed product by id
- `GET /api/products/search/name/{name}` – search product by name

## Redis caching

Products are cached in Redis and invalidated when new products are added or stock changes.
