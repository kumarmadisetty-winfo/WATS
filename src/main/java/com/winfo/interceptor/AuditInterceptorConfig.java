package com.winfo.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class AuditInterceptorConfig  extends WebMvcConfigurerAdapter {
	
@Autowired
AuditInterceptor auditInterceptor;
	
@Override
   public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(auditInterceptor);
   }

}
