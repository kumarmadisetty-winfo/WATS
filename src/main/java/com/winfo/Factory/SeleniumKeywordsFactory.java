package com.winfo.Factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.winfo.interface1.SeleniumKeyWordsInterface;
import com.winfo.scripts.ARLOSeleniumKeywordsTest;
import com.winfo.scripts.UDGSeleniumKeyWordsTest;
@Configuration
public class SeleniumKeywordsFactory {
	@Bean
	@Primary
public SeleniumKeyWordsInterface getInstanceObj(String instanceName) {
	if(instanceName.equalsIgnoreCase("udg")) {
		
//		return new UDGSeleniumKeyWordsTest();
	} 
	else if(instanceName.equalsIgnoreCase("arlo")) {
//		return new ARLOSeleniumKeywordsTest();
	}
	return null;
}
}
