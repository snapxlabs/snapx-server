package com.digcoin.snapx.domain.infra.config;

import com.digcoin.snapx.domain.infra.component.AwsS3FileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.Duration;

@ConditionalOnBean(AwsS3FileStorage.class)
@Configuration
public class AwsS3FileStorageConfiguration {

    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretAccessKey}")
    private String awsSecretAccessKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(this.awsAccessKeyId, this.awsSecretAccessKey);
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .httpClientBuilder(UrlConnectionHttpClient.builder()
                        .socketTimeout(Duration.ofMinutes(5)))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

}
