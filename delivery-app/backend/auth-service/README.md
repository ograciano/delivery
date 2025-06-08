# Auth Service

Authentication microservice responsible for user registration, login and JWT token issuance.

## Running locally

Start the infrastructure from the repository root:

```bash
./start_infra_and_show_urls.sh
```

Then launch the service with the `local` profile (port **8084**):

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Key endpoints

- `POST /api/auth/register` – register a new user
- `POST /api/auth/login` – obtain access and refresh tokens
- `POST /api/auth/refresh` – refresh the access token
