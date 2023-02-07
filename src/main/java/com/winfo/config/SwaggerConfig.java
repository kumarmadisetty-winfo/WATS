package com.winfo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
		@Bean
	    public Docket customDocket(){
	        return new Docket(DocumentationType.SWAGGER_2)
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com.winfo.controller")).build().apiInfo(metaData());

	 }
	 
	 private ApiInfo metaData() {
	        return new ApiInfoBuilder()
	                .title("WinfoTest API Documentation")
	                .description("\"This Documentation provides api body and also get response for an api\"")
	                
	                .contact(new Contact("WinfoSolutions", "https://winfosolutions.com", "info@winfosolutions.com"))
	                .build();
	    } 
	 
}
