package com.winfo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.winfo")
@EntityScan("com.winfo.model")
@ComponentScan("com.winfo.controller;com.winfo.config;com.winfo.scripts;com.winfo.services;com.winfo.dao;com.winfo.Factory;com.winfo.interceptor;com.winfo.exception;com.winfo.utils;com.winfo.repository")

public class WatsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WatsApplication.class, args);

	}

}