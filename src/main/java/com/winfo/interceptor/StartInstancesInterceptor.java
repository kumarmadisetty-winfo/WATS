package com.winfo.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.winfo.model.AuditLogs;
import com.winfo.services.VMDetailesService;

@Component
@Transactional
public class StartInstancesInterceptor extends HandlerInterceptorAdapter {
	static final Logger logger = Logger.getLogger(StartInstancesInterceptor.class);
	@Autowired
	private VMDetailesService vmDetailesService;

	@Override
	@Transactional
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			logger.debug("audit logging for the request " + request.getRequestURL() + " Started at ::"
					+ System.currentTimeMillis());
			
			Enumeration<String> parameterNames = request.getParameterNames();
			String service = request.getRequestURL().toString()
					.substring(request.getRequestURL().toString().lastIndexOf('/') + 1);
			
			StringBuffer jb = new StringBuffer();
			  String line = null;
			
		
			if (service.equalsIgnoreCase("executeTestScript")) {
					    BufferedReader reader = request.getReader();
//					    BufferedReader reader = request.getReader();
					    while ((line = reader.readLine()) != null)
					      jb.append(line);
					 String paramValue= jb.toString().replaceAll("[^0-9]", "");
					
						vmDetailesService.startInstance(paramValue);
						logger.info("vms are started");
				
			}

		} catch (Exception e) {
			System.out.println("Exception" + e);
			logger.error("Exception" + e);
			return true;
		}
		logger.debug("prehandle  " + request.getRequestURL() + " end " + System.currentTimeMillis());
		return true;

	}
}
