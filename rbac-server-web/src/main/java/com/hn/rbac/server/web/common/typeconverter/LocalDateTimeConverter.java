package com.hn.rbac.server.web.common.typeconverter;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime类型转换器
 */
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public LocalDateTime convert(String s) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);
        try {
            return LocalDateTime.parse(s, dateTimeFormatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Long timestamp = Long.parseLong(s);
            Instant instant = Instant.ofEpochMilli(timestamp);
            ZoneId zone = ZoneId.systemDefault();
            return LocalDateTime.ofInstant(instant, zone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
