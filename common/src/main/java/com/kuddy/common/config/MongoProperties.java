package com.kuddy.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "mongodb")
public class MongoProperties {
	private String client;
	private String name;

	public String getClient(){
		return "mongodb://localhost:27017";
	}

	public String getName(){
		return "kuddy";
	}


}
