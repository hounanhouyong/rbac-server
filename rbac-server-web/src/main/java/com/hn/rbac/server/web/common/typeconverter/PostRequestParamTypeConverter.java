package com.hn.rbac.server.web.common.typeconverter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * 处理HTTP POST/PUT 请求 JSON序列号 参数装换
 */
@Configuration
public class PostRequestParamTypeConverter {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer customizeLocalDateTimeFormat(){
        return jacksonObjectMapperBuilder -> {
//            jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(FORMATTER));
            jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
        };
    }

}
