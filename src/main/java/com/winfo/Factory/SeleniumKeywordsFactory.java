package com.winfo.Factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.winfo.interface1.AbstractSeleniumKeywords;
import com.winfo.interface1.SeleniumKeyWordsInterface;

@Configuration
public class SeleniumKeywordsFactory {
	@Autowired
	private ApplicationContext context;

	public SeleniumKeyWordsInterface getInstanceObj(String instanceName) {
		try {

			return context.getBean(instanceName, SeleniumKeyWordsInterface.class);
		} catch (Exception e) {
			System.out.println("instance name not found");
		}
		return null;
	}

	public AbstractSeleniumKeywords getInstanceObjFromAbstractClass(String instanceName) {
		try {

			return context.getBean(instanceName, AbstractSeleniumKeywords.class);
		} catch (Exception e) {
			System.out.println("instance name not found");
		}
		return null;
	}
}
