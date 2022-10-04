package com.winfo.services;

import java.util.ArrayList;
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
import com.winfo.vo.ApiValidationDto;
import com.winfo.vo.ApiValidationMigrationDto;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.ResponseDto;

@Service
public class GetApiValidationMigrationService {
	@Autowired
	DataBaseEntry dataBaseEntry;

	public ResponseDto apiValidationMigration(ApiValidationDto lookUpCodeVOData) {
		try {
			
			int apiValidationId = dataBaseEntry.getApiValidationIdActionId();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			List<LookUpCode> listOfLookUpCodesData = Arrays.asList(objectMapper.convertValue(lookUpCodeVOData.getLookup_codes(), LookUpCode[].class));
			List<String> listOfLookUpCodeName = listOfLookUpCodesData.stream().map(LookUpCode::getLookUpCode).collect(Collectors.toList());
			String lookUpCodes = String.join("','", listOfLookUpCodeName);
			
			boolean flag =  lookUpCodeVOData.isFlag();
			List<String> lookUpCodeId = new ArrayList();
			String existsLookUpCodeId = null;
			lookUpCodeVOData.getLookup_codes().forEach((ele)->{
				List<String> existsLookUpCode = dataBaseEntry.checkIfValidationExists(apiValidationId,String.valueOf(ele.getLookUpCode()));
				System.out.println("existsLookUpCode " +existsLookUpCode);
				if(flag==true)
				{
					listOfLookUpCodesData.forEach(listOfLookUpCodes -> {
						List<LookUpCode> id = null;
						try {
							id = dataBaseEntry.checkIfValidationExists1(apiValidationId,String.valueOf(ele.getLookUpCode()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						LookUpCode lookUpCode = listOfLookUpCodes;
						lookUpCode.setLookUpCodeId(id.get(0).getLookUpCodeId());
						lookUpCode.setLookUpId(id.get(0).getLookUpId());
						dataBaseEntry.updateApiValidation(lookUpCode);
					}); 
					
					
				}
				else {
					
				
				if(existsLookUpCode.size()==0) {
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

			if(lookUpCodeId.size()!=0)
			{
			return new ResponseDto(409, Constants.CONFLICT,existsLookUpCodeId);
			}
			
		}catch(Exception e) {
			return new ResponseDto(500, Constants.ERROR, "Migration Failed.");
		}
		return new ResponseDto(200, Constants.SUCCESS, "Migration Completed.");
	}
}