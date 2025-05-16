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
- `aws.s3.bucket`
- `aws.accessKeyId`
- `aws.secretAccessKey`

## Docker
```bash
./gradlew clean build
docker build -t file-service .
docker run -p 8085:8085 file-service
