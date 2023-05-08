package com.winfo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.winfo")
@EnableAsync
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.winfo.dao")
@EntityScan("com.winfo.model")
public class WatsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WatsApplication.class, args);

	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(WatsApplication.class);
	}
}