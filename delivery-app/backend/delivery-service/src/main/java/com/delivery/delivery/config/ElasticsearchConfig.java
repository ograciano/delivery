package com.delivery.delivery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.delivery.delivery.elasticsearch.repository")
public class ElasticsearchConfig {
    // Additional configuration if needed
}
