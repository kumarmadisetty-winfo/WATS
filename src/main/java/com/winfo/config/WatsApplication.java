package com.winfo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.winfo.Factory.SeleniumKeywordsFactory;
import com.winfo.interface1.SeleniumKeyWordsInterface;
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableJpaRepositories(basePackages ="com.winfo.dao")
@EntityScan("com.winfo.model")
@ComponentScan("com.winfo.controller;com.winfo.config;com.winfo.scripts;com.winfo.services;com.winfo.dao;com.winfo.Factory;com.winfo.interceptor;com.winfo.exception;")

public class WatsApplication extends SpringBootServletInitializer{
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(WatsApplication.class, args);
		
	}

}