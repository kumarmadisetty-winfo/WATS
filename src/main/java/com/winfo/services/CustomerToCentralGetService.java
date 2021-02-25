package com.winfo.services;

import java.util.ArrayList;
import java.util.Collections;
import com.winfo.vo.ScriptId;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.json.simple.JSONObject;

import java.util.List;

import javax.persistence.EntityManager;
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
import com.winfo.dao.CustomerToCentralGetDao;
import com.winfo.model.FetchData;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.WatsMasterVO;
import com.winfo.vo.WatsMetaDataVO;


import reactor.core.publisher.Mono;


@Service
public class CustomerToCentralGetService {

	@Autowired
	CustomerToCentralGetDao dao;
	@Autowired
	private EntityManager entityManager;
	public String webClientService(JSONObject json2,String customer_uri) {
		String uri=customer_uri+"/centralTocustomer_customer";
		WebClient webClient = WebClient.create(uri);
	//  WebClient webClient = WebClient.create("http://localhost:8081/customerTocentral_central");
			Mono<String> result = webClient.post().syncBody(json2).retrieve().bodyToMono(String.class);
			String response = result.block();
			if(response.equals("[]"))
			{
				response="[{\"status\":404,\"statusMessage\":\"PV_ERROR\",\"description\":\"Wrong Product Version\"}]";
			}
			
			return response;
			}
	 
	
	@Transactional
	public String customerRepoData(ScriptId scriptID) throws ParseException {
		Session session = entityManager.unwrap(Session.class);
		JSONObject json2=new JSONObject();
		json2=dao.customerRepoData(scriptID);
		CustomerToCentralGetService service=new CustomerToCentralGetService();
		Query query4=session.createQuery("select customer_uri from CustomerTable where customer_name='WATS_CENTRAL'");
		List<String> result4 = (List<String>) query4.list();
		String customer_uri=result4.get(0);
		return service.webClientService(json2,customer_uri);
		

	}
	
}

