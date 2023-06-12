package com.winfo.serviceImpl;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.model.LookUpCode;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.utils.Constants;
import com.winfo.vo.ApiValidationDto;
import com.winfo.vo.ApiValidationMigrationDto;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.ResponseDto;

import reactor.core.publisher.Mono;

@Service
public class PostApiValidationMigrationService {
	public static final Logger logger = Logger.getLogger(PostApiValidationMigrationService.class);
	@Autowired
	DataBaseEntry dataBaseEntry;
	
	@Autowired
	private LookUpCodeRepository lookUpCodeJpaRepository;
	public ResponseDto webClientService(ApiValidationDto listOfLookUpCodesData, String customerUrl) throws JsonMappingException, JsonProcessingException {
		if (customerUrl.equals("")) {
			logger.error("Invalid URL " +customerUrl);
			return new ResponseDto(500,"Invalid URL","Invalid URL!!");
		} else {
			String url = customerUrl + "/apiValidationMigrationReceiver";
			WebClient webClient = WebClient.create(url);
			Mono<String> result = webClient.post().syncBody(listOfLookUpCodesData).retrieve().bodyToMono(String.class);
			ObjectMapper objectMapper = new ObjectMapper();
			String finalResult = result.block();
			ResponseDto response = objectMapper.readValue(finalResult, ResponseDto.class);
			return response;
		}
		
	}

	@Transactional
	public ResponseDto apiValidationMigration(ApiValidationMigrationDto apiValidationMigration) {

		try {
			int apiValidationId = dataBaseEntry.getApiValidationIdActionId();
			List<Object> lookUpCodesData = dataBaseEntry.getApiValidationDataFromLookupsCode(apiValidationId,
					apiValidationMigration.getValidationLookupCodes());
			
			ObjectMapper objectMapper = new ObjectMapper();
			LookUpCodeVO[] listOfLookUpCodesData =objectMapper.convertValue(lookUpCodesData, LookUpCodeVO[].class);
			ApiValidationDto apiDto =new ApiValidationDto();
			apiDto.setLookupCodes(Arrays.asList(listOfLookUpCodesData));
			apiDto.setFlag(apiValidationMigration.isFlag());
			String lookUpName="TARGET CLIENT";
			LookUpCode lookUpCode = lookUpCodeJpaRepository.findByLookUpNameAndLookUpCode(lookUpName,apiValidationMigration.getTargetEnvironment());
			logger.info("LookUpCode Data " + lookUpCode);
			return webClientService(apiDto, lookUpCode.getTargetCode());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed during api validation migration " + e.getMessage());
			return new ResponseDto(500, Constants.ERROR, "Migration Failed.");
		}
	}

}
