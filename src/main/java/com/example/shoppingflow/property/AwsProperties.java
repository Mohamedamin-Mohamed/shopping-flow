package com.example.shoppingflow.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
    public String accessKeyId;
    public String secretAccessKey;
}
