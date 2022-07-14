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
	public void saveXpathParams(String scriptID, String line_number, String xpath) {
		dao.saveXpathParams(scriptID,line_number,xpath);
	}
	@Transactional
	public String getXpathParams(String scriptID, String line_number) {
		return dao.getXpathParams(scriptID,line_number);
	}
	@Transactional
	public String checkValidScript(String scriptID) {
		String status=null;
		Timestamp executionStartDate=dao.executionDate(scriptID);
		Timestamp executionEndDate=dao.executionEndDate(scriptID);
		Timestamp scriptUpdateDate=dao.scriptUpdateDate(scriptID);
		if(executionStartDate!=null&&scriptUpdateDate!=null&&executionEndDate!=null) {
		  long l1 = executionStartDate.getTime();
		    long l2 = scriptUpdateDate.getTime();
		    long l3 = executionEndDate.getTime();
		    if (l1 > l2 && l2==l3)
		    	status= "Yes";
		    else 
		    	status= "No";
		}
		else {
			status= "No";
		}
		System.out.println("executionDate......::"+executionStartDate);
		System.out.println("executionDate......::"+executionEndDate);
		System.out.println("scriptUpdateDate......::"+scriptUpdateDate);
		System.out.println("Status::::::"+status);

		return status;
	}
//	@Transactional
//	public void saveXpathParams(String param1,String param2,String scripNumber,String xpath, String action, String lineNumber) {
//		if(param2.equals("")|| param2==null) {
////			String xpathlocation=dao.checkXpathlocation(param1,scripNumber);
////			if(xpathlocation==null) {
//			
//				dao.saveXpathParams(param1,scripNumber,xpath,action,lineNumber);
////			}
//		}
//		else {
//			String params=param1+">"+param2;
////			String xpathlocation=dao.checkXpathlocation(params,scripNumber);
////			if(xpathlocation==null) {
//	
//				int i=dao.saveXpathParams(params,scripNumber,xpath,action,lineNumber);
//////				if(i==0) {
////					String params1="(*)"+param1+">"+param2;
////					dao.saveXpathParams(params1,scripNumber,xpath);
//////				}
////			}
//			
//		}
//	}

	
//	@Transactional
//	public String getXpathParams(String param1, String param2, String scriptID, String action) {
//		String xpathlocation =null;
//		if(param2.equals("")|| param2==null) {
//			 xpathlocation=dao.checkXpathlocation1(param1,scriptID,action);
//		
//		}
//		else {
//			String params=param1+">"+param2;
//			 xpathlocation=dao.checkXpathlocation1(params,scriptID,action);
//	
//			
//		}
//
//		return xpathlocation;
//		
//		
//	}


	

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