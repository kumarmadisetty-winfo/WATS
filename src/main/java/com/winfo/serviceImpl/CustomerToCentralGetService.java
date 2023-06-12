package com.winfo.serviceImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.dao.CustomerToCentralGetDao;
import com.winfo.model.LookUpCode;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.vo.ScriptDtlsDto;
import com.winfo.vo.WatsMasterDataVOList;
import com.winfo.vo.ScriptMasterDto;

import reactor.core.publisher.Mono;

@Service
public class CustomerToCentralGetService {
	public static final Logger logger = Logger.getLogger(GetApiValidationMigrationService.class);
	
	@Autowired
	private EntityManager entityManager;

	@Autowired
	CustomerToCentralGetDao dao;
	
	@Autowired
	private LookUpCodeRepository lookUpCodeJpaRepository;

	public String webClientService(WatsMasterDataVOList watsMasterDataVO, String customerUrl) {
		String response;
		if (customerUrl.equals("")) {
			response = "[{\"status\":500,\"statusMessage\":\"Invalid URL\",\"description\":\"Invalid URL!!\"}]";
			logger.error("Invalid URL " + customerUrl);
		} else {
			String url = customerUrl + "/centralToCustomerScriptMigrate";
			WebClient webClient = WebClient.create(url);
			Mono<String> result = webClient.post().syncBody(watsMasterDataVO).retrieve().bodyToMono(String.class);
			response = result.block();
			if ("[]".equals(response)) {
				response = "[{\"status\":404,\"statusMessage\":\"PV_ERROR\",\"description\":\"Wrong Product Version\"}]";
				logger.error("Invalid Product Version ");
			}
		}
		return response;
	}

	@Transactional
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String scriptMetaData(ScriptDtlsDto scriptDtls) {
		List<ScriptMasterDto> watsMasterVOList = dao.fecthMetaDataList(scriptDtls);
		String lookUpName="TARGET CLIENT";
		LookUpCode lookUpCode = lookUpCodeJpaRepository.findBylookUpNameAndLookUpCode(lookUpName,scriptDtls.getCustomerName());
		logger.info("LookUpCode Data " + lookUpCode);
		WatsMasterDataVOList watsMasterDataVO = new WatsMasterDataVOList();
		watsMasterDataVO.setData(watsMasterVOList);
		return webClientService(watsMasterDataVO, lookUpCode.getTargetCode());
	}

}
