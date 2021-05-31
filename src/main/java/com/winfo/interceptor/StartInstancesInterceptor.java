package com.winfo.interceptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.winfo.model.AuditLogs;
import com.winfo.services.VMDetailesService;
import com.winfo.vo.TestScriptDto;

@Component
@Transactional
public class StartInstancesInterceptor extends HandlerInterceptorAdapter {
	static final Logger logger = Logger.getLogger(StartInstancesInterceptor.class);
	@Autowired
	private VMDetailesService vmDetailesService;
	
//	@Autowired
//	private TestScriptDto testScriptDto;

	@Override
	@Transactional
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			logger.debug("audit logging for the request " + request.getRequestURL() + " Started at ::"
					+ System.currentTimeMillis());
			
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				System.out.println(paramName);
				System.out.println("n");
				String[] paramValues = request.getParameterValues(paramName);
				for (int i = 0; i < paramValues.length; i++) {
					String paramValue = paramValues[i];
					String service = request.getRequestURL().toString()
							.substring(request.getRequestURL().toString().lastIndexOf('/') + 1);
					if (service.equalsIgnoreCase("executeTestScript")) {
						vmDetailesService.startInstance(paramValue);
						logger.info("vms are started");
					}
				}
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
