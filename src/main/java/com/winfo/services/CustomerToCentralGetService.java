package com.winfo.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.dao.CustomerToCentralGetDao;
import com.winfo.vo.ScriptDtlsDto;
import com.winfo.vo.WatsMasterDataVOList;
import com.winfo.vo.ScriptMasterDto;

import reactor.core.publisher.Mono;

@Service
public class CustomerToCentralGetService {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	CustomerToCentralGetDao dao;

	public String webClientService(WatsMasterDataVOList watsMasterDataVO, String customerUri) {
		String response;
		if (customerUri.equals("")) {
			response = "[{\"status\":500,\"statusMessage\":\"Invalid URL\",\"description\":\"Invalid URL!!\"}]";
		} else {
			String uri = customerUri + "/centralToCustomerScriptMigrate";
			WebClient webClient = WebClient.create(uri);
			Mono<String> result = webClient.post().syncBody(watsMasterDataVO).retrieve().bodyToMono(String.class);
			response = result.block();
			if ("[]".equals(response)) {
				response = "[{\"status\":404,\"statusMessage\":\"PV_ERROR\",\"description\":\"Wrong Product Version\"}]";
			}
		}
		return response;
	}

	@Transactional
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String scriptMetaData(ScriptDtlsDto scriptDtls) {
		List<ScriptMasterDto> watsMasterVOList = dao.fecthMetaDataList(scriptDtls);
		Session session = entityManager.unwrap(Session.class);
		Query query4 = session.createQuery(
				"select valueName from ApplicationProperties where keyName='" + scriptDtls.getCustomerName() + "'");
		List<String> result4 = query4.list();
//		String customerUri = result4.isEmpty() ? "http://localhost:38081/wats" : result4.get(0);
		String customerUri = result4.isEmpty() ? "" : result4.get(0);
		WatsMasterDataVOList watsMasterDataVO = new WatsMasterDataVOList();
		watsMasterDataVO.setData(watsMasterVOList);
		return webClientService(watsMasterDataVO, customerUri);
	}

}
