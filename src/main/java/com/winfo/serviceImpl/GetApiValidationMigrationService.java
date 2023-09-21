package com.winfo.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winfo.model.LookUpCode;
import com.winfo.utils.Constants;
import com.winfo.vo.ApiValidationDto;
import com.winfo.vo.ApiValidationMigrationDto;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.ResponseDto;

@Service
public class GetApiValidationMigrationService {
	@Autowired
	DataBaseEntry dataBaseEntry;
	public static final Logger logger = Logger.getLogger(GetApiValidationMigrationService.class);
	
	public ResponseDto apiValidationMigration(ApiValidationDto lookUpCodeVOData) {
		try {
			
			int apiValidationId = dataBaseEntry.getApiValidationIdActionId();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			List<LookUpCode> listOfLookUpCodesData = Arrays.asList(objectMapper.convertValue(lookUpCodeVOData.getLookupCodes(), LookUpCode[].class));
			List<String> listOfLookUpCodeName = listOfLookUpCodesData.stream().map(LookUpCode::getLookUpCode).collect(Collectors.toList());
			String lookUpCodes = String.join("','", listOfLookUpCodeName);
			boolean flag =  lookUpCodeVOData.isFlag();
			List<String> lookUpCodeId = new ArrayList();
			String existsLookUpCodeId = null;
			lookUpCodeVOData.getLookupCodes().forEach(ele->{
			List<String> existsLookUpCode = dataBaseEntry.getExistingLookupCodeByValidationId(apiValidationId,String.valueOf(ele.getLookUpCode()));
				if(flag)
				{
					listOfLookUpCodesData.forEach(listOfLookUpCodes -> {
						List<LookUpCode> id = null;
						try {
							id = dataBaseEntry.getExistingLookupListByValidationId(apiValidationId,String.valueOf(ele.getLookUpCode()));
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("Failed to get existing lookup " + e.getMessage());
						}
						LookUpCode lookUpCode = listOfLookUpCodes;
						lookUpCode.setLookUpCodeId(id.get(0).getLookUpCodeId());
						lookUpCode.setLookUpId(id.get(0).getLookUpId());
						dataBaseEntry.updateApiValidation(lookUpCode);
					}); 
					
					
				}
				else {
					
				
				if( existsLookUpCode==null || existsLookUpCode.isEmpty()) {
					listOfLookUpCodesData.forEach(listOfLookUpCodes -> {
						listOfLookUpCodes.setLookUpCodeId(null);
						listOfLookUpCodes.setLookUpId(apiValidationId);
						dataBaseEntry.insertApiValidation(listOfLookUpCodes);
						});
				}
				else {
					
					lookUpCodeId.add(ele.getLookUpCodeId().toString());

				}
				}
			});
			existsLookUpCodeId=String.join(",", lookUpCodeId);

			if(!lookUpCodeId.isEmpty())
			{
			return new ResponseDto(HttpStatus.CONFLICT.value(), Constants.CONFLICT,existsLookUpCodeId);
			}
			
		}catch(Exception e) {
			logger.error("Failed during ApI validation migration " + e.getMessage());
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, "Migration Failed.");
		}
		logger.info("Successfully Migrated APi validation");
		return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS, "Migration Completed.");
	}
}