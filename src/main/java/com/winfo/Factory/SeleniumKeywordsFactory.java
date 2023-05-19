package com.winfo.Factory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.winfo.service.SeleniumKeyWordsInterface;
import com.winfo.serviceImpl.AbstractSeleniumKeywords;

@Configuration
public class SeleniumKeywordsFactory {
	Logger log = Logger.getLogger("Logger");
	@Autowired
	private ApplicationContext context;

	public SeleniumKeyWordsInterface getInstanceObj(String instanceName) {
		try {

			return context.getBean(instanceName, SeleniumKeyWordsInterface.class);
		} catch (Exception e) {
			log.info("instance name not found");
		}
		return null;
	}

	public AbstractSeleniumKeywords getInstanceObjFromAbstractClass(String instanceName) {
		try {

			return context.getBean(instanceName, AbstractSeleniumKeywords.class);
		} catch (Exception e) {
			log.info("instance name not found");
		}
		return null;
	}
}
