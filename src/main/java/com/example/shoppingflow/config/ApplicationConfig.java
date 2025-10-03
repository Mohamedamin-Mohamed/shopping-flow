package com.example.shoppingflow.config;

import com.example.shoppingflow.property.AwsProperties;
import com.example.shoppingflow.property.RedisProperties;
import com.example.shoppingflow.property.UtilProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableConfigurationProperties({RedisProperties.class, UtilProperties.class, AwsProperties.class})
public class ApplicationConfig {

    UtilProperties utilProperties = new UtilProperties();
    UtilConfig utilConfig = new UtilConfig(utilProperties);

    @Bean
    UtilConfig utilConfig(){
        return new UtilConfig(utilProperties);
    }

    @Bean
    public RedisConfig redisConfig(RedisProperties redisProperties) {
        return new RedisConfig(redisProperties);
    }

    @Bean
    AwsProperties awsProperties() {
        return new AwsProperties();
    }

    @Bean
    AwsCredentialsProvider awsCredentialsProvider() {
        return () -> AwsBasicCredentials.create(awsProperties().accessKeyId, awsProperties().secretAccessKey);
    }

    @Bean
    DynamoDbAsyncClient dynamodbClient() throws URISyntaxException {
        if (utilConfig.endpointOverride() != null) {
            return DynamoDbAsyncClient.builder()
                    .endpointOverride(new URI(utilConfig.endpointOverride()))
                    .credentialsProvider(awsCredentialsProvider())
                    .build();
        }
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(awsCredentialsProvider())
                .region(Region.US_EAST_1)
                .build();
    }

    @Bean
    DynamoDbEnhancedAsyncClient dynamoDbEnhancedClient() throws URISyntaxException {
        return DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(dynamodbClient()).build();
    }
}
