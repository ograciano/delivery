# File Service - DeliveryApp

Microservicio para gestión de archivos en MinIO (desarrollo) o AWS S3 (producción).

## Endpoints
- POST `/api/files/upload` : Subir archivo
- GET `/api/files/download/{filename}` : Descargar archivo

## Perfiles
- `local` → usa MinIO
- `prod` → usa AWS S3 (preparado para futura integración)

## Variables importantes
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
