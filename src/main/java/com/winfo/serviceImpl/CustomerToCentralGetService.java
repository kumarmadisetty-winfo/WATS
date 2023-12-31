package com.winfo.serviceImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.winfo.dao.CustomerToCentralGetDao;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.LookUpCode;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.utils.Constants;
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

	public String webClientService(WatsMasterDataVOList watsMasterDataVO, String customerUrl, String customerName) {

		String response;
		if (customerUrl.equals("")) {
			response = "[{\"status\":500,\"statusMessage\":\"Invalid URL\",\"description\":\"Invalid URL!!\"}]";
			logger.error("Invalid URL " + customerUrl);
		} else {
			try {
				WebClient webClient = WebClient.create(customerUrl + "/centralToCustomerScriptMigrate/" + customerName);
				Mono<String> result = webClient.post().syncBody(watsMasterDataVO).retrieve()
						.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
								clientResponse -> Mono.error(new NotFoundException("Endpoint not found")))
						.bodyToMono(String.class);
				response = result.block();
				if ("[]".equals(response)) {
					response = "[{\"status\":404,\"statusMessage\":\"PV_ERROR\",\"description\":\"Wrong Product Version\"}]";
					logger.error("Invalid Product Version ");
				}
			} catch (NotFoundException ex) {
				logger.error(ex.getMessage());
				throw new WatsEBSException(HttpStatus.NOT_FOUND.value(), ex.getMessage());
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
			}
		}
		return response;
	}

	@Transactional
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String scriptMetaData(ScriptDtlsDto scriptDtls) {
		List<ScriptMasterDto> watsMasterVOList = dao.fecthMetaDataList(scriptDtls);
		LookUpCode lookUpCode = lookUpCodeJpaRepository.findByLookUpNameAndLookUpCode(Constants.Look_Up_Name,scriptDtls.getCustomerName());
		logger.info("LookUpCode Data " + lookUpCode);
		WatsMasterDataVOList watsMasterDataVO = new WatsMasterDataVOList();
		watsMasterDataVO.setData(watsMasterVOList);
		return webClientService(watsMasterDataVO, lookUpCode.getTargetCode(),scriptDtls.getCustomerName());
	}

}
