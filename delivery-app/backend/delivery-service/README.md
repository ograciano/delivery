# Delivery Service

Tracks drivers and assignments for delivering orders.

## Running locally

Start the infrastructure:

```bash
./start_infra_and_show_urls.sh
```

Run the service on port **8083** with the `local` profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Key endpoints

- `GET /api/drivers` – list drivers
- `POST /api/drivers` – create a driver
- `GET /api/assignments` – list assignments
- `POST /api/assignments?orderId=&driverId=` – assign an order to a driver
- `PATCH /api/assignments/{id}?status=` – update assignment status
- `GET /api/assignments/available-orders` – orders available for assignment
