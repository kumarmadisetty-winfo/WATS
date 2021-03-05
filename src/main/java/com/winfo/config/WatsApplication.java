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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.winfo.Factory.SeleniumKeywordsFactory;
import com.winfo.interface1.SeleniumKeyWordsInterface;
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan("com.winfo.model")
@ComponentScan("com.winfo.controller;com.winfo.scripts;com.winfo.services;com.winfo.dao;com.winfo.Factory")
public class WatsApplication extends SpringBootServletInitializer{
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(WatsApplication.class, args);
		
	}

}