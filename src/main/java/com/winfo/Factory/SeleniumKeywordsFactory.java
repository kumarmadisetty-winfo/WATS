package com.winfo.Factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.winfo.interface1.SeleniumKeyWordsInterface;
import com.winfo.scripts.ARLOSeleniumKeywords;
import com.winfo.scripts.UDGSeleniumKeyWords;

@Configuration
public class SeleniumKeywordsFactory {
	@Bean
//	@Primary
public SeleniumKeyWordsInterface getInstanceObj(String instanceName) {
	if(instanceName.equalsIgnoreCase("udg")) {
		
		return new UDGSeleniumKeyWords();
	} 
	else if(instanceName.equalsIgnoreCase("arlo")) {
		return new ARLOSeleniumKeywords();
	}
	return null;
}
}
