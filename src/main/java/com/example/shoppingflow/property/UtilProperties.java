package com.example.shoppingflow.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config")
public class UtilProperties {
    public String endpointOverride;
    public String tableName;
}
