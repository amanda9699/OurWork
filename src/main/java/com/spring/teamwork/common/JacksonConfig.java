package com.spring.teamwork.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 全局 Jackson 配置（接口文档 1.1 + 12.7）：
 * - 时间格式统一 yyyy-MM-dd HH:mm:ss
 * - 注册 JavaTimeModule 让 Jackson 能正确处理 LocalDateTime / LocalDate
 * - 关闭 unknown-property 报错（前端可能多加字段）
 * - @Primary 确保 BetContract 的 JacksonTypeHandler 也复用这个 ObjectMapper
 */
@Configuration
public class JacksonConfig {
    private static final String DATETIME_PATTERN="yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {

        // 1. 注册 Java 8 时间模块
        JavaTimeModule timeModule = new JavaTimeModule();

        // 2. LocalDateTime 序列化：对象 → "2026-11-25 10:00:00"
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMATTER));
        // 3. LocalDateTime 反序列化：前端传字符串 → LocalDateTime
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMATTER));

        // 4. LocalDate 序列化（有些场景可能只用到日期）
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));

        // 5. 通过 builder 构建，Spring Boot 会把它放进所有 HttpMessageConverter
        ObjectMapper mapper = builder
                .modules(timeModule)
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS   // 禁用时间戳数组格式
                )
                .build();

        // 6. 容错：前端多传字段不报错（MVP 阶段友好）
        mapper.configure(
                com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
        );

        return mapper;
    }


}
