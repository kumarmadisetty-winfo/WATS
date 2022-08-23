package com.winfo.services;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.utils.Constants;
import com.winfo.vo.ApiValidationMigrationDto;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.ResponseDto;

import reactor.core.publisher.Mono;

@Service
public class PostApiValidationMigrationService {

	@Autowired
	DataBaseEntry dataBaseEntry;

	public ResponseDto webClientService(List<LookUpCodeVO> listOfLookUpCodesData, String customerUrl) throws JsonMappingException, JsonProcessingException {
		if (customerUrl.equals("")) {
			return new ResponseDto(500,"Invalid URL","Invalid URL!!");
		} else {
			String uri = customerUrl + "/apiValidationMigrationReceiver";
			WebClient webClient = WebClient.create(uri);
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
					apiValidationMigration.getLookUpCodeIds());
			ObjectMapper objectMapper = new ObjectMapper();
			List<LookUpCodeVO> listOfLookUpCodesData = Arrays
					.asList(objectMapper.convertValue(lookUpCodesData, LookUpCodeVO[].class));
			String customerUrl = dataBaseEntry.getCentralRepoUrl(apiValidationMigration.getCustomerName());
			return webClientService(listOfLookUpCodesData, "http://localhost:38080/wats");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseDto(500, Constants.ERROR, "Migration Failed.");
		}
	}

}
