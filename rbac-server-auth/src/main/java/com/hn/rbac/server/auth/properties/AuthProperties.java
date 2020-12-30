package com.hn.rbac.server.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "operate.admin")
public class AuthProperties {

    private ShiroProperties shiro = new ShiroProperties();
}
