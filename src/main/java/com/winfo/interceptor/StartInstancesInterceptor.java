package com.winfo.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.winfo.services.VMDetailesService;

@Component
@Transactional
public class StartInstancesInterceptor extends HandlerInterceptorAdapter {
	static final Logger logger = Logger.getLogger(StartInstancesInterceptor.class);
	@Autowired
	private VMDetailesService vmDetailesService;
	@Value("${costoptimisationFlag}")
	private boolean flag; 
	
	@Override
	@Transactional
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			logger.debug("audit logging for the request " + request.getRequestURL() + " Started at ::"
					+ System.currentTimeMillis());
			
			logger.info(request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf('/') + 1));
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				logger.info(paramName);
				String[] paramValues = request.getParameterValues(paramName);
				for (int i = 0; i < paramValues.length; i++) {
					String paramValue = paramValues[i];
					String service = request.getRequestURL().toString()
							.substring(request.getRequestURL().toString().lastIndexOf('/') + 1);
					if (service.equalsIgnoreCase("executeTestScript")&&flag) {
						vmDetailesService.startInstance(paramValue);
						logger.info("vms are started");
					}
				}
			}

		} catch (Exception e) {
			logger.error("Exception" + e);
		}
		logger.debug("prehandle  " + request.getRequestURL() + " end " + System.currentTimeMillis());
		return true;

	}
}
