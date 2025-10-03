package com.example.shoppingflow.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    public String host = "";
    public String password = "";
    public int port = 6379;
    public String verificationKey = "";

}
