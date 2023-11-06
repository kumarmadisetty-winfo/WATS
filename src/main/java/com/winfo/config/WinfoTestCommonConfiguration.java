package com.winfo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class WinfoTestCommonConfiguration {

	@Bean
	public ModelMapper modelMapper() {
	return new ModelMapper();
	}
	
	 @Bean(name = "customTaskExecutor")
	    public ThreadPoolTaskExecutor customTaskExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(5);
	        executor.setMaxPoolSize(10);
	        executor.setQueueCapacity(25);
	        executor.setThreadNamePrefix("CustomTaskExecutor-");
	        executor.initialize();
	        return executor;
	    }
    
}
