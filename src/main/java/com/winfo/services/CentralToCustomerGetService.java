package com.winfo.services;

import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.JSONObject;

import java.util.List;

import javax.transaction.Transactional;

//import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsMasterDataVOList;
import com.winfo.vo.ScriptAndCustomeridDetails;
import com.winfo.dao.CentralToCustomerGetDao;
import com.winfo.model.FetchData;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.WatsMasterVO;
import com.winfo.vo.WatsMetaDataVO;


import reactor.core.publisher.Mono;


@Service
public class CentralToCustomerGetService {

	@Autowired
	CentralToCustomerGetDao dao;

	 
	
	@Transactional
	public String customerRepoData(ScriptAndCustomeridDetails scriptandcustomeriddetails) throws ParseException {
		return dao.customerRepoData(scriptandcustomeriddetails);
		

	}
	
}

