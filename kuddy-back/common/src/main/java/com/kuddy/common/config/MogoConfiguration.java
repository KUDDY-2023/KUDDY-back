package com.kuddy.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "com.kuddy.common.chat.repository")
public class MogoConfiguration {
	private final MongoProperties mongoProperties;

	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create(mongoProperties.getClient());
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), mongoProperties.getName());
	}
}
