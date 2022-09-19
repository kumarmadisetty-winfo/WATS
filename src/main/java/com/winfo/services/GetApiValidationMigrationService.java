package com.winfo.services;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.model.LookUpCode;
import com.winfo.utils.Constants;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.ResponseDto;

@Service
public class GetApiValidationMigrationService {
	@Autowired
	DataBaseEntry dataBaseEntry;

	public ResponseDto apiValidationMigration(List<LookUpCodeVO> lookUpCodeVOData) {
		try {
			int apiValidationId = dataBaseEntry.getApiValidationIdActionId();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//			lookUpCodeVOData.forEach(lookUpCodeVO -> {
//				lookUpCodeVO.setLookUpCodeId(null);
//				lookUpCodeVO.set
//			});
			List<LookUpCode> listOfLookUpCodesData = Arrays.asList(objectMapper.convertValue(lookUpCodeVOData, LookUpCode[].class));
			List<String> listOfLookUpCodeName = listOfLookUpCodesData.stream().map(LookUpCode::getLookUpCode).collect(Collectors.toList());
			String lookUpCodes = String.join("','", listOfLookUpCodeName);
			List<String> existsLookUpCode = dataBaseEntry.checkIfValidationExists(apiValidationId,lookUpCodes);
			if(!existsLookUpCode.isEmpty()) {
				String existsLookUpCodeName = String.join(",", existsLookUpCode);
				return new ResponseDto(409, Constants.CONFLICT, existsLookUpCodeName);
			}
			listOfLookUpCodesData.forEach(listOfLookUpCodes -> {
				listOfLookUpCodes.setLookUpCodeId(null);
				listOfLookUpCodes.setLookUpId(apiValidationId);
				dataBaseEntry.insertApiValidation(listOfLookUpCodes);
				});
		}catch(Exception e) {
			return new ResponseDto(500, Constants.ERROR, "Migration Failed.");
		}
		return new ResponseDto(200, Constants.SUCCESS, "Migration Completed.");
	}
}