package com.winfo.interceptor;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.winfo.dao.AuditLogsDao;
import com.winfo.model.AuditLogs;
import com.winfo.vo.TestScriptDto;



@Component
@Transactional
public class AuditInterceptor extends HandlerInterceptorAdapter {
	static final Logger logger = Logger.getLogger(AuditInterceptor.class);
     
      @Autowired
  	   private AuditLogsDao auditLogsDao;
      
	 @Override
	 @Transactional
	   public boolean preHandle(
	      HttpServletRequest request, HttpServletResponse response, Object handler)  {
		 try {
			 logger.debug("audit logging for the request "  + request.getRequestURL()+" Started at ::"+  System.currentTimeMillis());	 
		 String service = request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf('/')+1);
		 if(!service.equalsIgnoreCase("error")) {
			 Date date= new Date();		
			 Timestamp endDate = new Timestamp(date.getTime());
			 AuditLogs auditLogs = new AuditLogs();
			 auditLogs.setId(1);
			 auditLogs.setDate(endDate);
			 auditLogsDao.updateTimeAndDate(auditLogs);

		 }
	 }catch (Exception e) {
		 System.out.println("Exception"+e);
		 logger.error("Exception"+e);
 	}
		 logger.debug("prehandle  " +request.getRequestURL()+" end "+  System.currentTimeMillis());
	     return true;
	 }
	 
	 @Override
	 @Transactional
	   public void postHandle(HttpServletRequest request,
				HttpServletResponse response, Object handler,
				ModelAndView modelAndView) {
		 try {
		 logger.debug("request is finished by user ,posthandle start " +  System.currentTimeMillis());
		 String service = request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf('/')+1);
		 if(!service.equalsIgnoreCase("error")) {
			 Date date= new Date();		
			 Timestamp endDate = new Timestamp(date.getTime());
			 AuditLogs auditLogs = new AuditLogs();
			 auditLogs.setId(1);
			 auditLogs.setDate(endDate);
			 auditLogsDao.updateTimeAndDate(auditLogs);
		 }
		}catch (Exception e) {
			 System.out.println("Exception"+e);
			 logger.error("Exception"+e);
		}

	 }

}
