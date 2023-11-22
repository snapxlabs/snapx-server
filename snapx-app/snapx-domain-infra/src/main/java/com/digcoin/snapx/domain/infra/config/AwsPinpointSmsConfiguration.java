package com.digcoin.snapx.domain.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;

import java.time.Duration;

@Configuration
public class AwsPinpointSmsConfiguration {

    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretAccessKey}")
    private String awsSecretAccessKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public PinpointClient pinpointClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(this.awsAccessKeyId, this.awsSecretAccessKey);
        return PinpointClient.builder()
                .region(Region.of(awsRegion))
                .httpClientBuilder(UrlConnectionHttpClient.builder()
                        .socketTimeout(Duration.ofMinutes(5)))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

}
