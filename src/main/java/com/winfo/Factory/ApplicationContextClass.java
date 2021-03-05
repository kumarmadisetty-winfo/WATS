package com.winfo.Factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.winfo.interface1.SeleniumKeyWordsInterface;
import com.winfo.scripts.UDGSeleniumKeyWords;
@Component
public class ApplicationContextClass {
	@Autowired
	private ApplicationContext context;
	@Bean
	public SeleniumKeyWordsInterface commandLineRunner() {
		SeleniumKeywordsFactory factory=context.getBean(SeleniumKeywordsFactory.class);
		return factory.getInstanceObj("udg");
	}
}
