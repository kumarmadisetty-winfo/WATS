package com.winfo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.WatsXpathDao;

@Service
public class ScriptXpathService {

	@Autowired
	WatsXpathDao dao;
	
	@Transactional
	public void saveXpathParams(String param1,String param2,String scripNumber,String xpath) {
		if(param2.equals("")|| param2==null) {
			String xpathlocation=dao.checkXpathlocation(param1,scripNumber);
			if(xpathlocation==null) {
				dao.saveXpathParams(param1,scripNumber,xpath);
			}
		}
		else {
			String params=param1+">"+param2;
			String xpathlocation=dao.checkXpathlocation(params,scripNumber);
			if(xpathlocation==null) {
				int i=dao.saveXpathParams(params,scripNumber,xpath);
				if(i==0) {
					String params1="(*)"+param1+">"+param2;
					dao.saveXpathParams(params1,scripNumber,xpath);
				}
			}
			
		}
	}

}
