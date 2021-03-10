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
	@Autowired
	private  ApplicationContext context;
public  SeleniumKeyWordsInterface getInstanceObj(String instanceName) {
	try {
	
		return context.getBean(instanceName,SeleniumKeyWordsInterface.class);	
}catch (Exception e) {
	System.out.println("instance name not found");
}
	return null;
	}
}
