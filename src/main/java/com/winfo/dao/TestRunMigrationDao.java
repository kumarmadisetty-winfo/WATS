package com.winfo.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.winfo.model.LookUp;
import com.winfo.model.LookUpCode;
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

@Repository
public class TestRunMigrationDao {

	@Autowired
	private EntityManager entityManager;

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

	@SuppressWarnings("unchecked")
	public LookUpCodeVO lookupCode(String lookUpName, String lookupCode, Session session) {

		List<LookUpCode> listOfLookUpCode = session
				.createQuery(
						"from LookUpCode where lookUpName = '" + lookUpName + "' and lookUpCode = '" + lookupCode + "'")
				.getResultList();
		LookUpCodeVO lookUpCodeVo = listOfLookUpCode.isEmpty() ? null : new LookUpCodeVO(listOfLookUpCode.get(0));
		return lookUpCodeVo;
	}

	@SuppressWarnings("unchecked")
	public LookUpVO lookups(String lookUpName, Map<String, LookUpCodeVO> mapOfData, Session session) {
		List<LookUp> listOfLookUp = session.createQuery("from LookUp where lookUpName = '" + lookUpName + "'")
				.getResultList();
		LookUp lookUpObj = listOfLookUp.isEmpty() ? null : listOfLookUp.get(0);
		LookUpVO lookupVo = new LookUpVO(lookUpObj, mapOfData);
		return lookupVo;
	}

	@SuppressWarnings("unchecked")
	public String testRunMigration(TestRunDetails testRunDetails) throws ParseException, JsonProcessingException {

		List<TestRunMigrationDto> testRunMigrationDto = new ArrayList<>();

		Session session = entityManager.unwrap(Session.class);

		String customerURI = dataBaseEntryDao.getCentralRepoUrl(testRunDetails.getCustomerName());

		for (ExistTestRunDto id : testRunDetails.getListOfTestRun()) {
			int testRunId = id.getTestSetId();

			List<ScriptMasterDto> listOfMasterVO = new ArrayList<>();

			Testrundata testRunData = dataBaseEntryDao.getTestSetObjByTestSetId(testRunId);

			TestRunMigrationDto watsMasterDataVOForTestRunMig = new TestRunMigrationDto(
					testRunData);
			Map<String, LookUpVO> lookUpDataMap = new HashMap<>();

			String configurationName = dataBaseEntryDao.getConfiNameByConfigId(testRunData.getConfigurationid());

			String projectName = dataBaseEntryDao.getProjectNameById(testRunData.getProjectid());

			watsMasterDataVOForTestRunMig.setCustomer(testRunDetails.getCustomerName());
			watsMasterDataVOForTestRunMig.setProjectName(projectName);
			watsMasterDataVOForTestRunMig.setConfigurationName(configurationName);
			watsMasterDataVOForTestRunMig.setTestRunExists(id.isFlag());

			List<Integer> testSetLineIDs = session
					.createQuery("select testsetlineid from ScriptsData where Testrundata.testsetid = " + testRunId)
					.getResultList();
			List<TestSetLineDto> testSetLinesAndParaData = new ArrayList<>();

			Map<String, LookUpCodeVO> lookUpCodeAction = new HashMap<>();
			Map<String, LookUpCodeVO> lookUpCodeUnique = new HashMap<>();
			Map<String, LookUpCodeVO> lookUpCodeDataTypes = new HashMap<>();

			for (int testSetLineID : testSetLineIDs) {
				ScriptsData scriptsData = session.find(ScriptsData.class, testSetLineID);
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

					JSONObject jsonScriptMetadata = new JSONObject();
					List<ScriptMetaDataDto> metaDataList = new ArrayList<>();
					Map<String, LookUpCodeVO> validationMap = new HashMap<>();
					for (ScriptMetaData scriptMetaData : listOfMetaData) {
						ScriptMetaDataDto scriptMetaDataDto = new ScriptMetaDataDto(scriptMetaData);
						metaDataList.add(scriptMetaDataDto);

						if (scriptMetaData.getAction() != null || !"NA".equals(scriptMetaData.getAction())) {
							lookUpCodeAction.put(scriptMetaData.getAction(),
									lookupCode("ACTION", scriptMetaData.getAction(), session));
						}
						if (!(scriptMetaData.getDatatypes() == null || "NA".equals(scriptMetaData.getDatatypes()))) {
							lookUpCodeDataTypes.put(scriptMetaData.getDatatypes(),
									lookupCode("DATATYPES", scriptMetaData.getDatatypes(), session));
						}
						jsonScriptMetadata.put("unique_mandatory", scriptMetaData.getUnique_mandatory());
						if (!"NA".equals(scriptMetaData.getUnique_mandatory())) {
							lookUpCodeUnique.put(scriptMetaData.getUnique_mandatory().toUpperCase(),
									lookupCode("UNIQUE_MANDATORY", scriptMetaData.getUnique_mandatory(), session));
						}

						if (!"NA".equals(scriptMetaData.getValidation_name())) {
							validationMap.put(scriptMetaData.getValidation_name(),
									lookupCode(scriptMetaData.getValidation_type().toUpperCase(),
											scriptMetaData.getValidation_name(), session));
						}

						if (!"NA".equals(scriptMetaData.getValidation_type())) {
							lookUpDataMap.put(scriptMetaData.getValidation_type(),
									lookups(scriptMetaData.getValidation_type().toUpperCase(), validationMap, session));

						}
					}
					scriptMasterDto.setMetaDataList(metaDataList);
					listOfMasterVO.add(scriptMasterDto);

				}

			}
			watsMasterDataVOForTestRunMig.setTestSetLinesAndParaData(testSetLinesAndParaData);
			watsMasterDataVOForTestRunMig.setScriptMasterData(listOfMasterVO);
			lookUpDataMap.put("action", lookups("ACTION", lookUpCodeAction, session));
			lookUpDataMap.put("unique_mandatory", lookups("UNIQUE_MANDATORY", lookUpCodeUnique, session));
			lookUpDataMap.put("datatypes", lookups("DATATYPES", lookUpCodeDataTypes, session));
			watsMasterDataVOForTestRunMig.setLookUpData(lookUpDataMap);
			testRunMigrationDto.add(watsMasterDataVOForTestRunMig);
		}
//		return webClientService(watsMasterDataVOListForTestRunMig, "http://localhost:38083/wats");
		return webClientService(testRunMigrationDto, customerURI);
	}

}