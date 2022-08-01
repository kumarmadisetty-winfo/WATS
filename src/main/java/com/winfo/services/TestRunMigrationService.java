package com.winfo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.winfo.dao.DataBaseEntryDao;
import com.winfo.dao.TestRunMigrationDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.ScriptsData;
import com.winfo.model.TestSetScriptParam;
import com.winfo.model.Testrundata;
import com.winfo.vo.ExistTestRunDto;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.LookUpVO;
import com.winfo.vo.TestRunDetails;
import com.winfo.vo.TestRunMigrationDto;
import com.winfo.vo.ScriptMasterDto;
import com.winfo.vo.ScriptMetaDataDto;
import com.winfo.vo.WatsTestSetParamVO;
import com.winfo.vo.TestSetLineDto;

import reactor.core.publisher.Mono;

@Service
public class TestRunMigrationService {
	
	private static final String NA = "NA";
	private static final String ACTION = "ACTION";
	private static final String UNIQUE_MANDATORY = "UNIQUE_MANDATORY";
	private static final String DATATYPES = "DATATYPES";
	

	@Autowired
	TestRunMigrationDao dao;

	@Autowired
	private DataBaseEntryDao dataBaseEntryDao;

	public String webClientService(List<TestRunMigrationDto> listOfTestRunMigrate, String customer_uri)
			throws JsonMappingException, JsonProcessingException {

		System.out.println("json data**" + listOfTestRunMigrate);

		String uri = customer_uri + "/testRunMigrationToCustomer";
		WebClient webClient = WebClient.create(uri);
		Mono<String> result = webClient.post().syncBody(listOfTestRunMigrate).retrieve().bodyToMono(String.class);
		String response = result.block();
		if ("[]".equals(response)) {
			response = "[{\"status\":404,\"statusMessage\":\"PV_ERROR\",\"description\":\"Wrong Product Version\"}]";
		}
		System.out.println(response);
		return response;
	}

	@Transactional
	public String testRunMigration(TestRunDetails testRunDetails) throws ParseException, JsonProcessingException {

		List<TestRunMigrationDto> testRunMigrationDto = new ArrayList<>();

		String customerURI = dataBaseEntryDao.getCentralRepoUrl(testRunDetails.getCustomerName());

		for (ExistTestRunDto id : testRunDetails.getListOfTestRun()) {
			int testRunId = id.getTestSetId();

			List<ScriptMasterDto> listOfMasterVO = new ArrayList<>();

			Testrundata testRunData = dataBaseEntryDao.getTestSetObjByTestSetId(testRunId);

			TestRunMigrationDto watsMasterDataVOForTestRunMig = new TestRunMigrationDto(testRunData);
			Map<String, LookUpVO> lookUpDataMap = new HashMap<>();

			String configurationName = dataBaseEntryDao.getConfiNameByConfigId(testRunData.getConfigurationid());

			String projectName = dataBaseEntryDao.getProjectNameById(testRunData.getProjectid());

			watsMasterDataVOForTestRunMig.setCustomer(testRunDetails.getCustomerName());
			watsMasterDataVOForTestRunMig.setProjectName(projectName);
			watsMasterDataVOForTestRunMig.setConfigurationName(configurationName);
			watsMasterDataVOForTestRunMig.setTestRunExists(id.isFlag());

			List<Integer> testSetLineIDs = dataBaseEntryDao.getListOfLineIdByTestSetId(testRunId);

			List<TestSetLineDto> testSetLinesAndParaData = new ArrayList<>();

			Map<String, LookUpCodeVO> lookUpCodeAction = new HashMap<>();
			Map<String, LookUpCodeVO> lookUpCodeUnique = new HashMap<>();
			Map<String, LookUpCodeVO> lookUpCodeDataTypes = new HashMap<>();

			for (int testSetLineID : testSetLineIDs) {
				ScriptsData scriptsData = dataBaseEntryDao.getScriptDataByLineID(testSetLineID);
				System.out.println("scriptsData " + scriptsData.getTestsetlineid());
				TestSetLineDto testSetLineDto = new TestSetLineDto(scriptsData);

				List<WatsTestSetParamVO> ScriptParamMetaData = new ArrayList<>();

				List<TestSetScriptParam> listOfTestSetScriptParam = dataBaseEntryDao
						.getScriptParamList(scriptsData.getTestsetlineid());

				for (TestSetScriptParam testSetScriptParam : listOfTestSetScriptParam) {
					WatsTestSetParamVO watsTestSetParamVO = new WatsTestSetParamVO(testSetScriptParam,
							testSetScriptParam.getTestRunScriptParamId());
					ScriptParamMetaData.add(watsTestSetParamVO);

				}
				testSetLineDto.setScriptParam(ScriptParamMetaData);

				testSetLinesAndParaData.add(testSetLineDto);

				List<ScriptMaster> listOfScriptMaster = dataBaseEntryDao
						.getScriptMasterListByScriptId(scriptsData.getScriptid());

				for (ScriptMaster scriptMaster : listOfScriptMaster) {
					ScriptMasterDto scriptMasterDto = new ScriptMasterDto(scriptMaster);

					List<ScriptMetaData> listOfMetaData = dataBaseEntryDao
							.getScriptMetaDataList(scriptsData.getScriptid());
					
					List<ScriptMetaDataDto> metaDataList = new ArrayList<>();
					Map<String, LookUpCodeVO> validationMap = new HashMap<>();
					for (ScriptMetaData scriptMetaData : listOfMetaData) {
						ScriptMetaDataDto scriptMetaDataDto = new ScriptMetaDataDto(scriptMetaData);
						metaDataList.add(scriptMetaDataDto);

						if (scriptMetaData.getAction() != null || !NA.equals(scriptMetaData.getAction())) {
							lookUpCodeAction.put(scriptMetaData.getAction(),
									dataBaseEntryDao.lookupCode(ACTION, scriptMetaData.getAction()));
						}
						if (!(scriptMetaData.getDatatypes() == null || NA.equals(scriptMetaData.getDatatypes()))) {
							lookUpCodeDataTypes.put(scriptMetaData.getDatatypes(),
									dataBaseEntryDao.lookupCode(DATATYPES, scriptMetaData.getDatatypes()));
						}
						if (!NA.equals(scriptMetaData.getUnique_mandatory())) {
							lookUpCodeUnique.put(scriptMetaData.getUnique_mandatory().toUpperCase(), dataBaseEntryDao
									.lookupCode(UNIQUE_MANDATORY, scriptMetaData.getUnique_mandatory()));
						}

						if (!NA.equals(scriptMetaData.getValidation_name())) {
							validationMap.put(scriptMetaData.getValidation_name(),
									dataBaseEntryDao.lookupCode(scriptMetaData.getValidation_type().toUpperCase(),
											scriptMetaData.getValidation_name()));
						}

						if (!NA.equals(scriptMetaData.getValidation_type())) {
							lookUpDataMap.put(scriptMetaData.getValidation_type(), dataBaseEntryDao
									.lookups(scriptMetaData.getValidation_type().toUpperCase(), validationMap));

						}
					}
					scriptMasterDto.setMetaDataList(metaDataList);
					listOfMasterVO.add(scriptMasterDto);

				}

			}
			watsMasterDataVOForTestRunMig.setTestSetLinesAndParaData(testSetLinesAndParaData);
			watsMasterDataVOForTestRunMig.setScriptMasterData(listOfMasterVO);
			lookUpDataMap.put(ACTION.toLowerCase(), dataBaseEntryDao.lookups(ACTION, lookUpCodeAction));
			lookUpDataMap.put(UNIQUE_MANDATORY.toLowerCase(), dataBaseEntryDao.lookups(UNIQUE_MANDATORY, lookUpCodeUnique));
			lookUpDataMap.put(DATATYPES.toLowerCase(), dataBaseEntryDao.lookups(DATATYPES, lookUpCodeDataTypes));
			watsMasterDataVOForTestRunMig.setLookUpData(lookUpDataMap);
			testRunMigrationDto.add(watsMasterDataVOForTestRunMig);
		}
		return webClientService(testRunMigrationDto, "http://localhost:38080/wats");
//		return webClientService(testRunMigrationDto, customerURI);

		// return dao.testRunMigration(testRunDetails);

	}

}
