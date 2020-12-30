package com.hn.rbac.server.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    private String host;
    private int port;
    private String password;
    private int timeout = 0;
    private int maxIdle = 500;
    private long maxWaitMillis = 500;
    private int database = 0;
}
