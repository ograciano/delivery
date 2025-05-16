package com.delivery.catalog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.delivery.catalog.elasticsearch.repository")
public class ElasticsearchConfig {
    // Configuración adicional si es necesaria
}
