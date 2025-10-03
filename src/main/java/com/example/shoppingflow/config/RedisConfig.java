package com.example.shoppingflow.config;

import com.example.shoppingflow.property.RedisProperties;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.util.Optional;

@Configuration
public class RedisConfig {
    private RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    Optional<String> password = Optional.ofNullable(redisProperties.password);

    public Jedis connect() {
        try (Jedis jedis = new Jedis(redisProperties.host, redisProperties.port, true)) {
            password.ifPresent(jedis::auth);
            return jedis;
        }
    }

}
