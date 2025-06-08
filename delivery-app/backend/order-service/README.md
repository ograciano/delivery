# Order Service

Handles order creation, status updates and merchant reporting.

## Running locally

Start the required infrastructure:

```bash
./start_infra_and_show_urls.sh
```

Run the service on port **8082** with the `local` profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Key endpoints

- `GET /api/orders` – list all orders
- `GET /api/orders/pending` – list pending orders
- `POST /api/orders` – create a new order
- `PATCH /api/orders/{id}?status=` – update order status
- `GET /api/orders/merchant/{merchantId}/sales` – orders for a merchant
- `GET /api/orders/merchant/{merchantId}/sales-summary` – sales summary for a merchant
- `GET /api/orders/merchant/{merchantId}/pending-payments` – orders awaiting payment
- `PATCH /api/orders/mark-paid/{orderId}` – mark order as paid
- `GET /api/orders/merchant/{merchantId}/financial-summary?startDate=&endDate=` – financial summary
