#!/bin/bash
set -euo pipefail

# Check prerequisites
usage() {
    echo "Usage: $0"
}

if ! command -v docker >/dev/null 2>&1; then
    echo "Error: docker is required."
    usage
    exit 1
fi

if ! command -v docker-compose >/dev/null 2>&1; then
    echo "Error: docker-compose is required."
    usage
    exit 1
fi

# Levantar todo con docker-compose
echo "Levantando infraestructura local con Docker Compose..."
docker-compose -f delivery-app/docker-compose.yml up -d

# Verificar si Docker Compose levantó correctamente
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Infraestructura levantada exitosamente."
    echo ""
    echo "🔗 Accesos rápidos:"
    echo ""
    echo "🛢️ PostgreSQL -> puerto: 5432 (usar cliente como DBeaver o psql)"
    echo "🧠 Redis -> puerto: 6379 (usar RedisInsight o CLI)"
    echo "📬 RabbitMQ Management -> http://localhost:15672 (usuario: admin / contraseña: admin)"
    echo "🗄️ MinIO Storage -> http://localhost:9000 (usuario: admin / contraseña: admin123)"
    echo "📈 Prometheus Monitoring -> http://localhost:9090"
    echo "📊 Grafana Dashboard -> http://localhost:3000 (usuario: admin / contraseña: admin)"
    echo "🔍 ElasticSearch -> http://localhost:9200"
    echo "📋 Kibana Logs UI -> http://localhost:5601"
    echo ""
    echo "Puedes verificar los contenedores corriendo con: docker ps"
else
    echo "❌ Hubo un error al levantar la infraestructura. Revisa el docker-compose.yml."
fi

