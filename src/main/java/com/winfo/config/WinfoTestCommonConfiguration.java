package com.winfo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WinfoTestCommonConfiguration {

	@Bean
	public ModelMapper modelMapper() {
	return new ModelMapper();
	}
    
}
