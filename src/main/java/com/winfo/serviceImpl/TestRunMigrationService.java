package com.winfo.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.winfo.controller.JobController;
import com.winfo.dao.DataBaseEntryDao;
import com.winfo.model.LookUpCode;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.repository.LookUpCodeRepository;
import com.winfo.utils.Constants;
import com.winfo.vo.ExistTestRunDto;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.LookUpVO;
import com.winfo.vo.ScriptMasterDto;
import com.winfo.vo.ScriptMetaDataDto;
import com.winfo.vo.TestRunDetails;
import com.winfo.vo.TestRunMigrationDto;
import com.winfo.vo.TestSetLineDto;
import com.winfo.vo.WatsTestSetParamVO;

import reactor.core.publisher.Mono;

@Service
public class TestRunMigrationService {

	private static final String NA = "NA";
	private static final String ACTION = "ACTION";
	private static final String UNIQUE_MANDATORY = "UNIQUE_MANDATORY";
	private static final String DATATYPES = "DATATYPES";
	public static final Logger logger = Logger.getLogger(TestRunMigrationService.class);
	@Autowired
	private DataBaseEntryDao dataBaseEntryDao;
	
	@Autowired
	private LookUpCodeRepository lookUpCodeJpaRepository;
	
	public String webClientService(List<TestRunMigrationDto> listOfTestRunMigrate, String customerUrl)
			throws JsonMappingException, JsonProcessingException {

		logger.info("TestRun Migrate json data " + listOfTestRunMigrate);
		WebClient webClient = WebClient.create(customerUrl + "/testRunMigrationToCustomer");
		Mono<String> result = webClient.post().syncBody(listOfTestRunMigrate).retrieve().bodyToMono(String.class);
		String response = result.block();
		if ("[]".equals(response)) {
			response = "[{\"status\":404,\"statusMessage\":\"PV_ERROR\",\"description\":\"Wrong Product Version\"}]";
		}
		logger.info("Test Run Migration Response " +response);
		return response;
	}

	@Transactional
	public String testRunMigration(TestRunDetails testRunDetails) throws ParseException, JsonProcessingException {

		List<TestRunMigrationDto> testRunMigrationDto = new ArrayList<>();
		LookUpCode lookUpCode = lookUpCodeJpaRepository.findByLookUpNameAndLookUpCode(Constants.Look_Up_Name,testRunDetails.getCustomerName());
		logger.info("LookUpCode Data " + lookUpCode);
		for (ExistTestRunDto id : testRunDetails.getListOfTestRun()) {
			int testRunId = id.getTestSetId();

			List<ScriptMasterDto> listOfMasterVO = new ArrayList<>();

			TestSet testRunData = dataBaseEntryDao.getTestSetObjByTestSetId(testRunId);

			TestRunMigrationDto testRunMigrateDto = new TestRunMigrationDto(testRunData);
			Map<String, LookUpVO> lookUpDataMap = new HashMap<>();

			String configurationName = dataBaseEntryDao.getConfiNameByConfigId(testRunData.getConfigurationId());

			List<Object[]> projectNameAndWatsPackage = dataBaseEntryDao.getProjectNameById(testRunData.getProjectId());

			testRunMigrateDto.setCustomer(testRunDetails.getCustomerName());
			testRunMigrateDto.setProjectName(projectNameAndWatsPackage.get(0)[0].toString());
			testRunMigrateDto.setConfigurationName(configurationName);
			testRunMigrateDto.setTestRunExists(id.isForceMigrate());
			testRunMigrateDto.setWatsPackage(projectNameAndWatsPackage.get(0)[1].toString());
			testRunMigrateDto.setCreatedBy(testRunDetails.getCreatedBy());
			List<Integer> testSetLineIDs = dataBaseEntryDao.getListOfLineIdByTestSetId(testRunId);

			List<TestSetLineDto> testSetLinesAndParaData = new ArrayList<>();

			Map<String, LookUpCodeVO> lookUpCodeAction = new HashMap<>();
			Map<String, LookUpCodeVO> lookUpCodeUnique = new HashMap<>();
			Map<String, LookUpCodeVO> lookUpCodeDataTypes = new HashMap<>();

			for (int testSetLineID : testSetLineIDs) {
				TestSetLine scriptsData = dataBaseEntryDao.getScriptDataByLineID(testSetLineID);
				logger.debug("scriptsData " + scriptsData.getTestRunScriptId());
				TestSetLineDto testSetLineDto = new TestSetLineDto(scriptsData);

				List<WatsTestSetParamVO> ScriptParamMetaData = new ArrayList<>();

				List<TestSetScriptParam> listOfTestSetScriptParam = dataBaseEntryDao
						.getScriptParamList(scriptsData.getTestRunScriptId());

				for (TestSetScriptParam testSetScriptParam : listOfTestSetScriptParam) {
					WatsTestSetParamVO watsTestSetParamVO = new WatsTestSetParamVO(testSetScriptParam,
							testSetScriptParam.getTestRunScriptParamId());
					ScriptParamMetaData.add(watsTestSetParamVO);

				}
				testSetLineDto.setScriptParam(ScriptParamMetaData);

				testSetLinesAndParaData.add(testSetLineDto);

				List<ScriptMaster> listOfScriptMaster = dataBaseEntryDao
						.getScriptMasterListByScriptId(scriptsData.getScriptId());

				for (ScriptMaster scriptMaster : listOfScriptMaster) {
					ScriptMasterDto scriptMasterDto = new ScriptMasterDto(scriptMaster);

					List<ScriptMetaData> listOfMetaData = dataBaseEntryDao
							.getScriptMetaDataList(scriptsData.getScriptId());

					List<ScriptMetaDataDto> metaDataList = new ArrayList<>();
					Map<String, LookUpCodeVO> validationMap = new HashMap<>();
					for (ScriptMetaData scriptMetaData : listOfMetaData) {
						ScriptMetaDataDto scriptMetaDataDto = new ScriptMetaDataDto(scriptMetaData);
						metaDataList.add(scriptMetaDataDto);

						if (scriptMetaData.getAction() != null || !NA.equals(scriptMetaData.getAction())) {
							lookUpCodeAction.put(scriptMetaData.getAction(),
									dataBaseEntryDao.getLookupCode(ACTION, scriptMetaData.getAction()));
						}
						if (!(scriptMetaData.getDatatypes() == null || NA.equals(scriptMetaData.getDatatypes()))) {
							lookUpCodeDataTypes.put(scriptMetaData.getDatatypes(),
									dataBaseEntryDao.getLookupCode(DATATYPES, scriptMetaData.getDatatypes()));
						}
						if (!NA.equals(scriptMetaData.getUniqueMandatory())) {
							lookUpCodeUnique.put(scriptMetaData.getUniqueMandatory().toUpperCase(), dataBaseEntryDao
									.getLookupCode(UNIQUE_MANDATORY, scriptMetaData.getUniqueMandatory()));
						}

						if (!NA.equals(scriptMetaData.getValidationName())) {
							validationMap.put(scriptMetaData.getValidationName(),
									dataBaseEntryDao.getLookupCode(scriptMetaData.getValidationType().toUpperCase(),
											scriptMetaData.getValidationName()));
						}

						if (!NA.equals(scriptMetaData.getValidationType())) {
							lookUpDataMap.put(scriptMetaData.getValidationType(), dataBaseEntryDao
									.getLookUp(scriptMetaData.getValidationType().toUpperCase(), validationMap));

						}
					}
					scriptMasterDto.setMetaDataList(metaDataList);
					listOfMasterVO.add(scriptMasterDto);

				}

			}
			testRunMigrateDto.setTestSetLinesAndParaData(testSetLinesAndParaData);
			testRunMigrateDto.setScriptMasterData(listOfMasterVO);
			lookUpDataMap.put(ACTION.toLowerCase(), dataBaseEntryDao.getLookUp(ACTION, lookUpCodeAction));
			lookUpDataMap.put(UNIQUE_MANDATORY.toLowerCase(),
					dataBaseEntryDao.getLookUp(UNIQUE_MANDATORY, lookUpCodeUnique));
			lookUpDataMap.put(DATATYPES.toLowerCase(), dataBaseEntryDao.getLookUp(DATATYPES, lookUpCodeDataTypes));
			testRunMigrateDto.setLookUpData(lookUpDataMap);
			testRunMigrationDto.add(testRunMigrateDto);
			logger.info("Succesfully added migration data");
      }
		return webClientService(testRunMigrationDto, lookUpCode.getTargetCode());


	}

}
