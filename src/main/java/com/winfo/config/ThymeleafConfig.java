package com.winfo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/*** 
 *  @author winfo
 *  References :: 
 *  1) https://www.thymeleaf.org/doc/articles/springmail.html,
 *  2) https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#what-kind-of-templates-can-thymeleaf-process
 */

@Configuration
public class ThymeleafConfig {
	@Autowired
	Environment env;

	 @Bean
	    public TemplateEngine dagTemplateEngine() {
	        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	        // Resolver for TEXT input
	        templateEngine.addTemplateResolver(textTemplateResolver());
	        return templateEngine;
	    }

    private ITemplateResolver textTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding("UTF8");
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    

}
