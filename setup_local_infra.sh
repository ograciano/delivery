#!/bin/bash

# Crear la raíz del proyecto
mkdir -p delivery-app/infrastructure/postgres
mkdir -p delivery-app/infrastructure/redis
mkdir -p delivery-app/infrastructure/rabbitmq
mkdir -p delivery-app/infrastructure/minio
mkdir -p delivery-app/infrastructure/prometheus
mkdir -p delivery-app/infrastructure/grafana
mkdir -p delivery-app/infrastructure/elk-stack/elasticsearch
mkdir -p delivery-app/infrastructure/elk-stack/logstash
mkdir -p delivery-app/infrastructure/elk-stack/kibana
mkdir -p delivery-app/docs

# Crear archivos vacíos importantes
touch delivery-app/infrastructure/postgres/init.sql
touch delivery-app/infrastructure/prometheus/prometheus.yml
touch delivery-app/docker-compose.yml
touch delivery-app/docs/arquitectura.md

# Agregar contenido inicial a docker-compose.yml
cat > delivery-app/docker-compose.yml <<EOL
version: "3.8"
services:
  postgres:
    image: postgres:15
    container_name: postgres
    restart: always
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
      POSTGRES_DB: delivery_db
    volumes:
      - ./infrastructure/postgres:/docker-entrypoint-initdb.d
      - postgres_data:/var/lib/postgresql/data

  redis:
    image: redis:7
    container_name: redis
    ports:
      - "6379:6379"

  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: admin

  minio:
    image: minio/minio
    container_name: minio
    ports:
      - "9000:9000"
    environment:
      MINIO_ROOT_USER: admin
      MINIO_ROOT_PASSWORD: admin123
    command: server /data
    volumes:
      - minio_data:/data

  prometheus:
    image: prom/prometheus
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./infrastructure/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    container_name: grafana
    ports:
      - "3000:3000"

  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.10.2
    container_name: elasticsearch
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
    ports:
      - "9200:9200"

  logstash:
    image: docker.elastic.co/logstash/logstash:8.10.2
    container_name: logstash
    ports:
      - "5000:5000"

  kibana:
    image: docker.elastic.co/kibana/kibana:8.10.2
    container_name: kibana
    ports:
      - "5601:5601"

volumes:
  postgres_data:
  minio_data:
EOL

# Agregar contenido inicial a prometheus.yml
cat > delivery-app/infrastructure/prometheus/prometheus.yml <<EOL
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']
EOL

# Mensaje final
echo "¡Infraestructura local creada exitosamente en la carpeta delivery-app/!"

