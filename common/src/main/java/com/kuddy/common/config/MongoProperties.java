package com.kuddy.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {
	private String client;
	private String name;

	// Getter와 Setter는 Lombok의 @Data로 자동 생성됩니다.
}
