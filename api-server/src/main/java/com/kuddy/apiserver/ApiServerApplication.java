package com.kuddy.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EntityScan("com.kuddy.common")//@Entity , Spring Data Repository 관련 클래스들은 해당 패키지에 존재해도 인식을 할 수 없는 문제 해결
@EnableJpaRepositories("com.kuddy.common")
@SpringBootApplication(scanBasePackages = "com.kuddy")
@EnableAsync(proxyTargetClass = true)
@EnableScheduling
public class ApiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiServerApplication.class, args);
	}

}
