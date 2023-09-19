package com.kuddy.chatserver;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EntityScan("com.kuddy.common")//@Entity , Spring Data Repository 관련 클래스들은 해당 패키지에 존재해도 인식을 할 수 없는 문제 해결
@EnableJpaRepositories("com.kuddy.common")
//@EnableRedisRepositories("com.kuddy.common")
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = "com.kuddy")
public class ChatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatServerApplication.class, args);
	}

}
