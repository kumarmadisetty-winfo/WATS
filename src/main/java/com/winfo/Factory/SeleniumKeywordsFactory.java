package com.winfo.Factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.winfo.interface1.SeleniumKeyWordsInterface;
import com.winfo.scripts.ARLOSeleniumKeywords;
import com.winfo.scripts.UDGSeleniumKeyWords;

import net.bytebuddy.implementation.bytecode.Throw;

@Configuration
public class SeleniumKeywordsFactory {
//	@Bean
	@Autowired
	private  ApplicationContext context;
public  SeleniumKeyWordsInterface getInstanceObj(String instanceName) {
	try {
	if("udg".equalsIgnoreCase(instanceName)) {
		return context.getBean(UDGSeleniumKeyWords.class);
		} 
	else if("arlo".equalsIgnoreCase(instanceName)) {
		return context.getBean(ARLOSeleniumKeywords.class);
	}
}catch (Exception e) {
	System.out.println("instance name not found");
}
	return null;
	}
}
