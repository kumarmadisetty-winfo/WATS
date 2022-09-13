package com.winfo.config;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.winfo.utils.Constants;

/***
 * @author winfo References :: 1)
 *         https://www.thymeleaf.org/doc/articles/springmail.html, 2)
 *         https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#what-kind-of-templates-can-thymeleaf-process
 */

@Nonnull
@Configuration
public class ThymeleafConfig {
	public final Logger logger = LogManager.getLogger(ThymeleafConfig.class);

	@Autowired
	private Environment env;

	@Bean
	public SpringTemplateEngine dagTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(fileTemplateResolver());
		return templateEngine;
	}

	private ITemplateResolver fileTemplateResolver() {
		try {
			FileTemplateResolver templateResolver = new FileTemplateResolver();
			String pyjabPath = env.getProperty("pyjab.template.path");
			if (pyjabPath != null) {
				templateResolver.setPrefix(
						System.getProperty(Constants.SYS_USER_HOME_PATH) + pyjabPath.replace("/", File.separator));
			}
			templateResolver.setSuffix(".txt");
			templateResolver.setTemplateMode(TemplateMode.TEXT);
			templateResolver.setCharacterEncoding("UTF8");
			templateResolver.setCheckExistence(true);
			templateResolver.setCacheable(false);
			return templateResolver;
		} catch (NullPointerException e) {
			logger.info(e.getMessage());
		}
		return null;
	}

}
