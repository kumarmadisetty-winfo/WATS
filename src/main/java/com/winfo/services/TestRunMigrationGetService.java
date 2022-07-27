package com.winfo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.dao.CopyTestRunDao;
import com.winfo.dao.TestRunMigrationGetDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.ScriptsData;
import com.winfo.model.ScritplinesData;
import com.winfo.model.Testrundata;
import com.winfo.vo.DomGenericResponseBean2;
import com.winfo.vo.DomGenericResponseBean3;
import com.winfo.vo.ExistsTestRun;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.LookUpVO;
import com.winfo.vo.TestRunExistsVO;
import com.winfo.vo.WatsMasterDataVOListForTestRunMig;
import com.winfo.vo.WatsMasterVO;
import com.winfo.vo.WatsMetaDataVO;
import com.winfo.vo.WatsTestSetParamVO;
import com.winfo.vo.WatsTestSetVO;

@Service
public class TestRunMigrationGetService {

	@Autowired
	TestRunMigrationGetDao dao;

	@Autowired
	CopyTestRunDao copyTestrunDao;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public DomGenericResponseBean3 centralRepoData(List<WatsMasterDataVOListForTestRunMig> mastervo) {
		
		TestRunExistsVO testRunExistsVO = new TestRunExistsVO();
		
		List<ExistsTestRun> listOfExistsTestRun = new ArrayList<>();
		
		List<DomGenericResponseBean2> bean = new ArrayList<>();
		
		DomGenericResponseBean3 domGenericResponseBean3 = new DomGenericResponseBean3();
		
		for (WatsMasterDataVOListForTestRunMig mastervolist : mastervo) {
			Session session = entityManager.unwrap(Session.class);
			
//			ExistsTestRun existsTestRun = new ExistsTestRun();
			
			BigDecimal checkTest = (BigDecimal) session
					.createNativeQuery("select count(*) from WIN_TA_TEST_SET where test_set_name ='"
							+ mastervolist.getTest_set_name() + "'")
					.getSingleResult();

			int checkTestRun = Integer.parseInt(checkTest.toString());
			
			
			if(checkTestRun>0 && !mastervolist.isTestRunExists()) {
				DomGenericResponseBean2 domGenericResponseBean = new DomGenericResponseBean2();
				domGenericResponseBean.setStatus(0);
				domGenericResponseBean.setStatusMessage("Already Exists");
				domGenericResponseBean.setTestRunName(mastervolist.getTest_set_name());
				bean.add(domGenericResponseBean);
				domGenericResponseBean3.setResponse(bean);
				continue;
			}
			
			
			
			int customerId = 0;
			try {
				BigDecimal checkCustomer = (BigDecimal) session
						.createNativeQuery("select customer_id from win_ta_customers where customer_name ='"
								+ mastervolist.getCustomer() + "'")
						.getSingleResult();
				customerId = Integer.parseInt(checkCustomer.toString());
			} catch (Exception e) {
				DomGenericResponseBean2 domGenericResponseBean = new DomGenericResponseBean2();
				domGenericResponseBean.setStatusMessage("Customer Not Found");
				domGenericResponseBean.setTestRunName(mastervolist.getTest_set_name());
				bean.add(domGenericResponseBean);
				domGenericResponseBean3.setResponse(bean);
				return domGenericResponseBean3;
			}

			List<ScriptMaster> listOfScriptMaster = new ArrayList<>();

			Map<Integer, Integer> mapOfScriptIdsOldToNew = new HashMap<>();
			Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew = new HashMap<>();

			for (Map.Entry<String, LookUpVO> entry : mastervolist.getLookUpData().entrySet()) {

				String value = entry.getValue().getLOOKUP_NAME();
				BigDecimal countOfLookups = (BigDecimal) session
						.createNativeQuery("select count(*) from win_ta_lookups where lookup_name = '" + value + "'")
						.getSingleResult();
				Integer data = Integer.parseInt(countOfLookups.toString());

				if (data == 0) {
					String sql = "SELECT LOOKUP_ID_S.nextval FROM DUAL";
					SQLQuery query = session.createSQLQuery(sql);

					List results = query.list();
					if (results.size() > 0) {

						BigDecimal bigDecimal = (BigDecimal) results.get(0);
						Integer id = Integer.parseInt(bigDecimal.toString());

						String query1 = "insert into win_ta_lookups(LOOKUP_ID,LOOKUP_NAME,LOOKUP_DESC,CREATED_BY,LAST_UPDATED_BY) VALUES("
								+ id + ",'" + value + "','" + entry.getValue().getLOOKUP_DESC() + "','"
								+ entry.getValue().getCREATED_BY() + "','" + entry.getValue().getLAST_UPDATED_BY()
								+ "')";
						session.createNativeQuery(query1).executeUpdate();
					}

				}

				BigDecimal lookupId = (BigDecimal) session
						.createNativeQuery("select lookup_id from win_ta_lookups where lookup_name = '" + value + "'")
						.getSingleResult();
				Integer getlookupId = Integer.parseInt(lookupId.toString());

				for (Map.Entry<String, LookUpCodeVO> secondEntry : entry.getValue().getMapOfData().entrySet()) {

					if (secondEntry.getValue().getLOOKUP_NAME().equalsIgnoreCase(entry.getValue().getLOOKUP_NAME())) {

						BigDecimal countOflookupCode = (BigDecimal) session.createNativeQuery(
								"select count(*) from WATS_PROD.win_ta_lookup_codes where lookup_name = '"
										+ secondEntry.getValue().getLOOKUP_NAME() + "' and lookup_code = '"
										+ secondEntry.getValue().getLOOKUP_CODE()+ "'")
								.getSingleResult();

						Integer dataOflookupCode = Integer.parseInt(countOflookupCode.toString());

						if (dataOflookupCode == 0) {
							String sql = "SELECT LOOKUP_CODES_ID_S.nextval FROM DUAL";
							SQLQuery query = session.createSQLQuery(sql);

							List results = query.list();
							if (results.size() > 0) {

								BigDecimal bigDecimal = (BigDecimal) results.get(0);
								Integer id = Integer.parseInt(bigDecimal.toString());

								String query1 = "insert into win_ta_lookup_codes(LOOKUP_CODES_ID,LOOKUP_ID,LOOKUP_NAME,LOOKUP_CODE,TARGET_CODE,MEANING) VALUES("
										+ id + "," + getlookupId + ",'" + value + "','"
										+ secondEntry.getValue().getLookup_code() + "','"
										+ secondEntry.getValue().getTarget_code() + "','"
										+ secondEntry.getValue().getMeaning() + "')";
								session.createNativeQuery(query1).executeUpdate();
							}
						}
					}
				}

			}
			for (WatsMasterVO masterdata : mastervolist.getScriptMasterData()) {
				ScriptMaster master = new ScriptMaster();
				master.setScript_id(masterdata.getScript_id());
				master.setModule(masterdata.getModule());
				master.setScenario_name(masterdata.getScenario_name());
				master.setScenario_description(masterdata.getScenario_description());
				master.setProduct_version(masterdata.getProduct_version());
				master.setPriority(masterdata.getPriority());
				master.setProcess_area(masterdata.getProcess_area());
				master.setRole(masterdata.getRole());
				master.setScript_number(masterdata.getScript_number());
				master.setSub_process_area(masterdata.getSub_process_area());
				master.setStandard_custom(masterdata.getStandard_custom());
				master.setTest_script_status(masterdata.getTest_script_status());
				master.setCustomer_id(masterdata.getCustomer_id());
				master.setDependency(masterdata.getDependency());
				master.setEnd2end_scenario(masterdata.getEnd2end_scenario());
				master.setExpected_result(masterdata.getExpected_result());
				master.setSelenium_test_script_name(masterdata.getSelenium_test_script_name());
				master.setSelenium_test_method(masterdata.getSelenium_test_method());
				master.setAuthor(masterdata.getAuthor());
				master.setCreated_by(masterdata.getCreated_by());
				master.setCreation_date(masterdata.getCreation_date());
				master.setUpdated_by(masterdata.getUpdated_by());
				master.setUpdate_date(masterdata.getUpdate_date());
				master.setCustomisation_reference(masterdata.getCustomisation_reference());
				master.setAttribute1(masterdata.getAttribute1());
				master.setAttribute2(masterdata.getAttribute2());
				master.setAttribute3(masterdata.getAttribute3());
				master.setAttribute4(masterdata.getAttribute4());
				master.setAttribute5(masterdata.getAttribute5());
				master.setAttribute6(masterdata.getAttribute6());
				master.setAttribute7(masterdata.getAttribute7());
				master.setAttribute8(masterdata.getAttribute8());
				master.setAttribute9(masterdata.getAttribute9());
				master.setAttribute10(masterdata.getAttribute10());

				for (WatsMetaDataVO metadatavo : masterdata.getScriptMetaData()) {
					ScriptMetaData metadata = new ScriptMetaData();
					metadata.setAction(metadatavo.getAction());
					metadata.setLine_number(metadatavo.getLine_number());
					metadata.setInput_parameter(metadatavo.getInput_parameter());
					metadata.setScript_number(masterdata.getScript_number());
					metadata.setXpath_location(metadatavo.getXpath_location());
					metadata.setXpath_location1(metadatavo.getXpath_location1());
					metadata.setCreated_by(metadatavo.getCreated_by());
					metadata.setCreation_date(metadatavo.getCreation_date());
					metadata.setUpdated_by(metadatavo.getUpdated_by());
					metadata.setUpdate_date(metadatavo.getUpdate_date());
					metadata.setStep_desc(metadatavo.getStep_desc());
					metadata.setField_type(metadatavo.getField_type());
					metadata.setHint(metadatavo.getHint());
					metadata.setScript_number(metadatavo.getScript_number());
					metadata.setDatatypes(metadatavo.getDatatypes());
					metadata.setUnique_mandatory(metadatavo.getUnique_mandatory());
					metadata.setValidation_type(metadatavo.getValidation_type());
					metadata.setValidation_name(metadatavo.getValidation_name());
					master.addMetadata(metadata);

				}
				listOfScriptMaster.add(master);
			}
			mapOfScriptIdsOldToNew = Independent(listOfScriptMaster, mapOfMetaDataScriptIdsOldToNew);

			BigDecimal checkConfig1 = (BigDecimal) session
					.createNativeQuery("select count(*) from win_ta_config where config_name ='"
							+ mastervolist.getConfigurationName() + "'")
					.getSingleResult();
			Integer checkConfig = Integer.parseInt(checkConfig1.toString());

			if (checkConfig == 0) {
				BigDecimal bigDecimal = (BigDecimal) session.createSQLQuery("SELECT CONFIG_ID_SEQ.nextval FROM DUAL")
						.getSingleResult();
				Integer id = Integer.parseInt(bigDecimal.toString());
				session.createNativeQuery(
						"Insert into WIN_TA_CONFIG (CONFIGURATION_ID,CREATED_BY,LAST_UPDATED_BY,CONFIG_NAME) values ("
								+ id + ",'SUPER_ADMIN','SUPER_ADMIN','" + mastervolist.getConfigurationName() + "')")
						.executeUpdate();
			}
			BigDecimal configuration = (BigDecimal) session
					.createNativeQuery("select configuration_id from win_ta_config where config_name ='"
							+ mastervolist.getConfigurationName() + "'")
					.getSingleResult();
			int configuration_id1 = Integer.parseInt(configuration.toString());

//==========================================
			BigDecimal checkProject = (BigDecimal) session.createNativeQuery(
					"select count(*) from win_ta_projects where project_name ='" + mastervolist.getProjectName() + "'")
					.getSingleResult();

			int checkProjectId = Integer.parseInt(checkProject.toString());

			if (checkProjectId == 0) {
				BigDecimal nextValueProjectNumber = (BigDecimal) session
						.createNativeQuery("SELECT WIN_TA_PROJECT_NUM.nextval FROM DUAL").getSingleResult();
				Integer newNextValueProjectNumber = Integer.parseInt(nextValueProjectNumber.toString());

				BigDecimal nextValueProject = (BigDecimal) session
						.createNativeQuery("SELECT WIN_TA_PROJECT_SEQ.nextval FROM DUAL").getSingleResult();
				Integer newNextValueProject = Integer.parseInt(nextValueProject.toString());

				session.createNativeQuery(
						"insert into win_ta_projects(PROJECT_ID,PROJECT_NUMBER,PROJECT_NAME,CUSTOMER_ID,PRODUCT_VERSION) VALUES("
								+ newNextValueProject + "," + newNextValueProjectNumber + ",'"
								+ mastervolist.getProjectName() + "'," + customerId + ",'"
								+ mastervolist.getScriptMasterData().get(0).getProduct_version() + "')")
						.executeUpdate();

			}

			Testrundata testrundata = new Testrundata();

//			BigDecimal checkTest = (BigDecimal) session
//					.createNativeQuery("select count(*) from WIN_TA_TEST_SET where test_set_name ='"
//							+ mastervolist.getTest_set_name() + "'")
//					.getSingleResult();
//
//			int checkTestRun = Integer.parseInt(checkTest.toString());
			System.out.println("checkTestRun " + checkTestRun);
			int testrunid = copyTestrunDao.getIds();
			if (checkTestRun > 0) {
				testrundata.setTestsetname(mastervolist.getTest_set_name() + "-" + testrunid);
			} else {
				testrundata.setTestsetname(mastervolist.getTest_set_name());
			}
			testrundata.setTestsetid(testrunid);
			testrundata.setConfigurationid(configuration_id1);

			BigDecimal project = (BigDecimal) session
					.createNativeQuery("select project_id from win_ta_projects where project_name ='"
							+ mastervolist.getProjectName() + "'")
					.getSingleResult();
			int projectId = Integer.parseInt(project.toString());

			testrundata.setProjectid(projectId);

			testrundata.setDescription(mastervolist.getDescription());
			testrundata.setTest_set_desc(mastervolist.getTest_set_desc());
			testrundata.setTest_set_comments(mastervolist.getTest_set_comments());
			testrundata.setEnabled(mastervolist.getEnabled());

			testrundata.setPasspath(mastervolist.getPass_path());
			testrundata.setFailpath(mastervolist.getFail_path());
			testrundata.setExceptionpath(mastervolist.getExeception_path());
			testrundata.setTscompleteflag("ACTIVE");

			for (WatsTestSetVO lineVo : mastervolist.getTestSetLinesAndParaData()) {
				ScriptsData testSetLineData = new ScriptsData();
				int sectiptid = copyTestrunDao.getscrtiptIds();
				testSetLineData.setTestsetlineid(sectiptid);
				testSetLineData.setScriptid(mapOfScriptIdsOldToNew.get(Integer.parseInt(lineVo.getScript_id())));
				testSetLineData.setCreatedby(lineVo.getCreatedby());
				testSetLineData.setCreationdate(lineVo.getCreationdate());
				testSetLineData.setEnabled(lineVo.getEnabled());
				testSetLineData.setExecutedby(lineVo.getExecutedby());
				testSetLineData.setExecutionendtime(lineVo.getExecutionendtime());
				testSetLineData.setExecutionstarttime(lineVo.getExecutionstarttime());
				testSetLineData.setLastupdatedby(lineVo.getLastupdatedby());
				testSetLineData.setScriptnumber(lineVo.getScriptnumber());
				testSetLineData.setScriptUpadated(lineVo.getScriptUpadated());
				testSetLineData.setSeqnum(Integer.parseInt(lineVo.getSeqnum()));
				testSetLineData.setStatus(lineVo.getStatus());
				testSetLineData.setTestsstlinescriptpath(lineVo.getTestsstlinescriptpath());
				testSetLineData.setUpdateddate(lineVo.getUpdateddate());

				for (WatsTestSetParamVO paramVo : lineVo.getScriptParam()) {
					ScritplinesData testSetParam = new ScritplinesData();
					int sectiptlineid = copyTestrunDao.getscrtiptlineIds();
					testSetParam.setTestscriptperamid(sectiptlineid);
					testSetParam.setScript_id(mapOfScriptIdsOldToNew.get(Integer.parseInt(lineVo.getScript_id())));
					testSetParam.setAction(paramVo.getAction());
					testSetParam.setLine_number(paramVo.getLine_number());
					testSetParam.setInput_parameter(paramVo.getInput_parameter());
					testSetParam.setField_type(paramVo.getField_type());
					testSetParam.setHint(paramVo.getHint());
					testSetParam.setScript_number(paramVo.getScript_number());
					testSetParam.setDatatypes(paramVo.getDatatypes());
					testSetParam.setCreatedby(paramVo.getCreated_by());
					testSetParam.setCreationdate(paramVo.getCreation_date());
					testSetParam.setInput_value(paramVo.getInput_value());
					testSetParam.setLastupdatedby(paramVo.getLast_updated_by());
					testSetParam.setLineerrormessage(paramVo.getLine_error_message());
					testSetParam.setLineexecutionstatues(paramVo.getLine_execution_status());
//				testSetParam.setMetadata_id(mapOfMetaDataScriptIdsOldToNew.get(Integer.parseInt(paramVo.getScript_meta_data_id())));
//				testSetParam.setScript_id();
					testSetParam.setScript_number(paramVo.getScript_number());
//				testSetParam.setScriptsdata(paramVo.getT);
					testSetParam.setTest_run_param_desc(paramVo.getTest_run_param_desc());
					testSetParam.setTest_run_param_name(paramVo.getTest_run_param_name());
//				testSetParam.setTestscriptperamid(paramVo.test);
					testSetParam.setUniquemandatory(paramVo.getUnique_mandatory());
					testSetParam.setUpdateddate(paramVo.getUpdate_date());
					testSetParam.setXpathlocation(paramVo.getXpath_location());
					testSetParam.setXpathlocation1(paramVo.getXpath_location1());
//				testSetLineData.addMetadata(metadata);
					testSetLineData.addScriptlines(testSetParam);

				}
				testrundata.addScriptsdata(testSetLineData);
			}
			dao.insertTestRun(testrundata);
			DomGenericResponseBean2 domGenericResponseBean = new DomGenericResponseBean2();
			domGenericResponseBean.setStatus(200);
			domGenericResponseBean.setStatusMessage("Migrated Successfully");
			domGenericResponseBean.setTestRunName(testrundata.getTestsetname());
			bean.add(domGenericResponseBean);
			domGenericResponseBean3.setResponse(bean);
//			return bean;
		}
		if(!listOfExistsTestRun.isEmpty()) {
			testRunExistsVO.setTestRunNameList(listOfExistsTestRun);
			for(ExistsTestRun existsTestRun : listOfExistsTestRun) {
				System.out.println("Test Run "+existsTestRun.getTestRunName());
			}
		}
		return domGenericResponseBean3;
	}

	public int dependent(Integer id, List<ScriptMaster> listOfScriptMaster, int insertedScriptaId,Map<Integer, Integer> mapOfNewToOld, Map<Integer, Integer> mapOfOldToNew, Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew) {
		for (ScriptMaster scriptMaster : listOfScriptMaster) {
			int scriptMasterPrsent = dao.checkScriptPresent(scriptMaster.getProduct_version(),scriptMaster.getScript_number());
			if (scriptMasterPrsent == 0 && !mapOfNewToOld.containsKey(scriptMaster.getScript_id()) && !mapOfOldToNew.containsKey(scriptMaster.getScript_id())) {
				if (id.equals(scriptMaster.getScript_id())) {
					if (scriptMaster.getDependency() == null) {
						int originalId = scriptMaster.getScript_id();
						insertedScriptaId = dao.insertScriptMaster(scriptMaster);
						for (ScriptMetaData scriptMetaData : scriptMaster.getScriptMetaDatalist()) {
							int oldMetaDataId = scriptMetaData.getScript_meta_data_id();
							scriptMetaData.setScriptMaster(scriptMaster);
							int insertedScriptMetaDataObject = dao.insertScriptMetaData(scriptMetaData);
							mapOfMetaDataScriptIdsOldToNew.put(oldMetaDataId, insertedScriptMetaDataObject);
						}
						mapOfNewToOld.put(insertedScriptaId, originalId);
						mapOfOldToNew.put(originalId, insertedScriptaId);
						return insertedScriptaId;
					} else {
						insertedScriptaId = dependent(scriptMaster.getDependency(), listOfScriptMaster, insertedScriptaId, mapOfNewToOld, mapOfOldToNew, mapOfMetaDataScriptIdsOldToNew);
						scriptMaster.setDependency(insertedScriptaId);
						int originalId = scriptMaster.getScript_id();
						insertedScriptaId = dao.insertScriptMaster(scriptMaster);
						for (ScriptMetaData scriptMetaData : scriptMaster.getScriptMetaDatalist()) {
							int oldMetaDataId = scriptMetaData.getScript_meta_data_id();
							scriptMetaData.setScriptMaster(scriptMaster);
							int insertedScriptMetaDataObject = dao.insertScriptMetaData(scriptMetaData);
							mapOfMetaDataScriptIdsOldToNew.put(oldMetaDataId, insertedScriptMetaDataObject);
						}
						mapOfNewToOld.put(insertedScriptaId, originalId);
						mapOfOldToNew.put(originalId, insertedScriptaId);
					}

				}
			} else if (scriptMasterPrsent > 0) {
				int originalId = scriptMaster.getScript_id();
				mapOfNewToOld.put(scriptMasterPrsent, originalId);
				mapOfOldToNew.put(originalId, scriptMasterPrsent);
			}
		}
		return insertedScriptaId;
	}



	public Map<Integer, Integer> Independent(List<ScriptMaster> listOfScriptMaster,Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew) {
		Map<Integer, Integer> mapOfNewToOld = new HashMap<>();
		Map<Integer, Integer> mapOfOldToNew = new HashMap<>();
		for (ScriptMaster scriptMaster : listOfScriptMaster) {

			int scriptMasterPrsent = dao.checkScriptPresent(scriptMaster.getProduct_version(),scriptMaster.getScript_number());

			if (scriptMasterPrsent == 0 && !mapOfNewToOld.containsKey(scriptMaster.getScript_id()) && !mapOfOldToNew.containsKey(scriptMaster.getScript_id())) {
				if (scriptMaster.getDependency() != null) {
					int insertedScriptaId = 0;
					dependent(scriptMaster.getScript_id(), listOfScriptMaster, insertedScriptaId, mapOfNewToOld, mapOfOldToNew, mapOfMetaDataScriptIdsOldToNew);
				} else {
					int originalId = scriptMaster.getScript_id();
					scriptMaster.setScript_id(null);
					int id = dao.insertScriptMaster(scriptMaster);
					for (ScriptMetaData scriptMetaData : scriptMaster.getScriptMetaDatalist()) {
						int oldMetaDataId = scriptMetaData.getScript_meta_data_id();
						scriptMetaData.setScriptMaster(scriptMaster);
						int insertedScriptMetaDataObject = dao.insertScriptMetaData(scriptMetaData);
						mapOfMetaDataScriptIdsOldToNew.put(oldMetaDataId, insertedScriptMetaDataObject);
					}
					mapOfNewToOld.put(id, originalId);
					mapOfOldToNew.put(originalId, id);
				}
			} else {
				int originalId = scriptMaster.getScript_id();
				int id = scriptMasterPrsent;
				if (id > 0) {
					mapOfNewToOld.put(id, originalId);
					mapOfOldToNew.put(originalId, id);
				}

			}
		}
		return mapOfOldToNew;
	}

}
