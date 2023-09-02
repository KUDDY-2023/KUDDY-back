package com.kuddy.notiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.kuddy")
public class NotiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotiServerApplication.class, args);
	}

}
