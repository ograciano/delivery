package com.delivery.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.delivery.order.elasticsearch.repository")
public class ElasticsearchConfig {
}
