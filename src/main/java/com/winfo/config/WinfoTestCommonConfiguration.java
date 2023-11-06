package com.winfo.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class WinfoTestCommonConfiguration {

	@Value("${spring.task.execution.pool.core-size}")
	private int corePoolSize;
	
	@Value("${spring.task.execution.pool.max-size}")
	private int maxPoolSize;
	
	@Value("${spring.task.execution.pool.queue-capacity}")
	private int queuePoolSize;
	
	@Bean
	public ModelMapper modelMapper() {
	return new ModelMapper();
	}
	
	 @Bean(name = "customTaskExecutor")
	    public ThreadPoolTaskExecutor customTaskExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(corePoolSize);
	        executor.setMaxPoolSize(maxPoolSize);
	        executor.setQueueCapacity(queuePoolSize);
	        executor.setThreadNamePrefix("CustomTaskExecutor-");
	        executor.initialize();
	        return executor;
	    }
    
}
