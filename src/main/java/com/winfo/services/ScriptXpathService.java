package com.winfo.services;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.WatsXpathDao;

@Service
public class ScriptXpathService {

	@Autowired
	WatsXpathDao dao;
	
	@Transactional
	public void saveXpathParams(String param1,String param2,String scripNumber,String xpath, String action, String lineNumber) {
		if(param2.equals("")|| param2==null) {
//			String xpathlocation=dao.checkXpathlocation(param1,scripNumber);
//			if(xpathlocation==null) {
			
				dao.saveXpathParams(param1,scripNumber,xpath,action,lineNumber);
//			}
		}
		else {
			String params=param1+">"+param2;
//			String xpathlocation=dao.checkXpathlocation(params,scripNumber);
//			if(xpathlocation==null) {
	
				int i=dao.saveXpathParams(params,scripNumber,xpath,action,lineNumber);
////				if(i==0) {
//					String params1="(*)"+param1+">"+param2;
//					dao.saveXpathParams(params1,scripNumber,xpath);
////				}
//			}
			
		}
	}

	@Transactional
	public String getXpathParams(String param1, String param2, String scriptID, String action) {
		String xpathlocation =null;
		if(param2.equals("")|| param2==null) {
			 xpathlocation=dao.checkXpathlocation1(param1,scriptID,action);
		
		}
		else {
			String params=param1+">"+param2;
			 xpathlocation=dao.checkXpathlocation1(params,scriptID,action);
	
			
		}

		return xpathlocation;
		
		
	}
	@Transactional
	public String checkValidScript(String scripNumber) {
		String status=null;
		Timestamp executionDate=dao.executionDate(scripNumber);
		Timestamp scriptUpdateDate=dao.scriptUpdateDate(scripNumber);
		if(executionDate!=null&&scriptUpdateDate!=null) {
		  long l1 = executionDate.getTime();
		    long l2 = scriptUpdateDate.getTime();
		    if (l1 > l2)
		    	status= "Yes";
		    else 
		    	status= "No";
		}
		else {
			status= "No";
		}
		System.out.println("executionDate......::"+executionDate);

		System.out.println("scriptUpdateDate......::"+scriptUpdateDate);
		System.out.println("Status::::::"+status);

		return status;
	}

}

//package com.winfo.services;
//
//import javax.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.winfo.dao.WatsXpathDao;
//
//@Service
//public class ScriptXpathService {
//
//	@Autowired
//	WatsXpathDao dao;
//	
//	@Transactional
//	public void saveXpathParams(String param1,String param2,String scripNumber,String xpath) {
//		if(param2.equals("")|| param2==null) {
//			String xpathlocation=dao.checkXpathlocation(param1,scripNumber);
//			if(xpathlocation==null) {
//			
//				dao.saveXpathParams(param1,scripNumber,xpath);
//			}
//		}
//		else {
//			String params=param1+">"+param2;
//			String xpathlocation=dao.checkXpathlocation(params,scripNumber);
//			if(xpathlocation==null) {
//	
//				int i=dao.saveXpathParams(params,scripNumber,xpath);
//////				if(i==0) {
////					String params1="(*)"+param1+">"+param2;
////					dao.saveXpathParams(params1,scripNumber,xpath);
//////				}
//			}
//			
//		}
//	}
//
//}