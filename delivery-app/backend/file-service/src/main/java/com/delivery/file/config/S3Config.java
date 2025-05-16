package com.delivery.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {

    @Value("${aws.s3.endpoint}")
    private String endpoint;
    @Value("${aws.s3.region}")
    private String region;
    @Value("${aws.s3.access-key}")
    private String accessKey;
    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Bean
    public S3AsyncClient s3AsyncClient() {
        return S3AsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider())
                .endpointOverride(URI.create(endpoint)) // si usas MinIO local o un endpoint custom
                .build();
    }

    @Bean
    public AwsCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }
}
