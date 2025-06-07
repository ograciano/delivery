# Viewing Logs

Logs from each microservice are written in JSON format under `./logs/<service>`.
Logstash reads these files and sends events to ElasticSearch. To review them in Grafana:

1. Start the infrastructure using `docker-compose up`.
2. Access Grafana at [http://localhost:3000](http://localhost:3000) with `admin`/`admin`.
3. Add ElasticSearch as a data source pointing to `http://elasticsearch:9200`.
4. Import or create a dashboard using the `delivery-logs-*` index.
5. Use Logs or Table panels to explore log entries.
