package com.winfo.Factory;

import org.springframework.context.annotation.Bean;

import com.winfo.interface1.SeleniumKeyWordsInterface;
import com.winfo.scripts.ARLOSeleniumKeywords;
import com.winfo.scripts.UDGSeleniumKeyWords;

public class BeanClass {
@Bean(name = "udg")
public SeleniumKeyWordsInterface interface1() {
	return new UDGSeleniumKeyWords();
}
@Bean(name = "arlo")
public SeleniumKeyWordsInterface interface2() {
	return new ARLOSeleniumKeywords();
}
}
