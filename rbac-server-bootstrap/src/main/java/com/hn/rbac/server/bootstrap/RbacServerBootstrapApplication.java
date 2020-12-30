package com.hn.rbac.server.bootstrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@MapperScan("com.hn.rbac.server.service.dao")
@SpringBootApplication(scanBasePackages = {"com.hn.rbac.server"}, exclude = {HibernateJpaAutoConfiguration.class})
public class RbacServerBootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbacServerBootstrapApplication.class, args);
    }

}
