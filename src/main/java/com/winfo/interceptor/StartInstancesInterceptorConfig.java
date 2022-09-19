package com.winfo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StartInstancesInterceptorConfig implements WebMvcConfigurer {
	@Autowired
	StartInstancesInterceptor startInstancesInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(startInstancesInterceptor);
	}
}
