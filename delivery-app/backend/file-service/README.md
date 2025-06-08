# File Service - DeliveryApp

Handles file uploads using MinIO for local development or S3 in production.

## Running locally

Start the infrastructure from the repository root:

```bash
./start_infra_and_show_urls.sh
```

Launch the service on port **8086** with:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Endpoints
- `POST /api/files/upload` – upload a file

## Profiles
- `local` → uses MinIO
- `prod` → prepared for AWS S3

## Important variables
- `minio.url`
- `minio.access-key`
- `minio.secret-key`
- `minio.bucket-name`
- `aws.s3.bucket`
- `aws.s3.access-key`
- `aws.s3.secret-key`
- `aws.s3.region`
- `aws.s3.endpoint`

## Docker
```bash
./gradlew clean build
docker build -t file-service .
docker run -p 8086:8086 file-service
```
