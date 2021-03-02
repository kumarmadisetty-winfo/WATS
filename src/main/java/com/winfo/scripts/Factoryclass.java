package com.winfo.scripts;

import org.springframework.context.annotation.Bean;

public class Factoryclass {
@Bean
ARLOSeleniumKeywords arlo() {
	return new ARLOSeleniumKeywords();
}	
@Bean
UDGSeleniumKeyWords udg() {
	return new UDGSeleniumKeyWords();
}
}
