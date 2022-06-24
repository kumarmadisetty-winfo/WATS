package com.winfo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/***
 * @author winfo References :: 1)
 *         https://www.thymeleaf.org/doc/articles/springmail.html, 2)
 *         https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#what-kind-of-templates-can-thymeleaf-process
 */

@Configuration
public class ThymeleafConfig {
	@Autowired
	Environment env;

	@Bean
	public SpringTemplateEngine dagTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(fileTemplateResolver());
		return templateEngine;
	}

	private ITemplateResolver fileTemplateResolver() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();
		templateResolver.setPrefix(env.getProperty("pyjab.template.path"));
		templateResolver.setSuffix(".txt");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding("UTF8");
		templateResolver.setCheckExistence(true);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

}