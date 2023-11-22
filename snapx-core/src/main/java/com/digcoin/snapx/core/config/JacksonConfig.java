package com.digcoin.snapx.core.config;

import com.digcoin.snapx.core.common.constant.Format;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfig {

	@Bean
	@ConditionalOnMissingBean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> builder
				.failOnUnknownProperties(false)
				.deserializerByType(LocalDateTime.class,
						new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(Format.DATETIME)))
				.deserializerByType(LocalDate.class,
						new LocalDateDeserializer(DateTimeFormatter.ofPattern(Format.DATE)))
				.serializerByType(LocalDateTime.class,
						new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(Format.DATETIME)))
				.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(Format.DATE)))
				.serializerByType(Long.class, ToStringSerializer.instance)
				.serializerByType(BigDecimal.class, ToStringSerializer.instance);
	}

}
