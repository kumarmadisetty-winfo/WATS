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
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.LookUpVO;
import com.winfo.vo.ScriptMasterDto;
import com.winfo.vo.ScriptMetaDataDto;
import com.winfo.vo.TestRunMigrationDto;
import com.winfo.vo.TestSetLineDto;
import com.winfo.vo.WatsTestSetParamVO;

@Service
public class TestRunMigrationGetService {

	@Autowired
	TestRunMigrationGetDao dao;

	@Autowired
	CopyTestRunDao copyTestrunDao;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public List<DomGenericResponseBean> centralRepoData(List<TestRunMigrationDto> listOfTestRunDto) {

		List<DomGenericResponseBean> listOfResponseBean = new ArrayList<>();

		for (TestRunMigrationDto testRunMigrateDto : listOfTestRunDto) {
			Session session = entityManager.unwrap(Session.class);

			BigDecimal checkTest = (BigDecimal) session
					.createNativeQuery("select count(*) from WIN_TA_TEST_SET where test_set_name ='"
							+ testRunMigrateDto.getTestSetName() + "'")
					.getSingleResult();

			int checkTestRun = Integer.parseInt(checkTest.toString());

			if (checkTestRun > 0 && !testRunMigrateDto.isTestRunExists()) {
				DomGenericResponseBean domGenericResponseBean = new DomGenericResponseBean();
				domGenericResponseBean.setStatus(0);
				domGenericResponseBean.setStatusMessage("Already Exists");
				domGenericResponseBean.setTestRunName(testRunMigrateDto.getTestSetName());
				listOfResponseBean.add(domGenericResponseBean);
				continue;
			}
			int customerId = 0;
			try {
				BigDecimal checkCustomer = (BigDecimal) session
						.createNativeQuery("select customer_id from win_ta_customers where customer_name ='"
								+ testRunMigrateDto.getCustomer() + "'")
						.getSingleResult();
				customerId = Integer.parseInt(checkCustomer.toString());
			} catch (Exception e) {
				DomGenericResponseBean domGenericResponseBean = new DomGenericResponseBean();
				domGenericResponseBean.setStatusMessage("Customer Not Found");
				domGenericResponseBean.setTestRunName(testRunMigrateDto.getTestSetName());
				listOfResponseBean.add(domGenericResponseBean);
				return listOfResponseBean;
			}

			List<ScriptMaster> listOfScriptMaster = new ArrayList<>();

			Map<Integer, Integer> mapOfScriptIdsOldToNew = new HashMap<>();
			Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew = new HashMap<>();

			for (Map.Entry<String, LookUpVO> entry : testRunMigrateDto.getLookUpData().entrySet()) {

				String value = entry.getValue().getLookupName();
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
								+ id + ",'" + value + "','" + entry.getValue().getLookupDesc() + "','"
								+ entry.getValue().getCreatedBy() + "','" + entry.getValue().getLastUpdatedBy() + "')";
						session.createNativeQuery(query1).executeUpdate();
					}

				}

				BigDecimal lookupId = (BigDecimal) session
						.createNativeQuery("select lookup_id from win_ta_lookups where lookup_name = '" + value + "'")
						.getSingleResult();
				Integer getlookupId = Integer.parseInt(lookupId.toString());

				for (Map.Entry<String, LookUpCodeVO> secondEntry : entry.getValue().getMapOfData().entrySet()) {

					if (secondEntry.getValue().getLookUpName().equalsIgnoreCase(entry.getValue().getLookupName())) {

						BigDecimal countOflookupCode = (BigDecimal) session.createNativeQuery(
								"select count(*) from WATS_PROD.win_ta_lookup_codes where lookup_name = '"
										+ secondEntry.getValue().getLookUpName() + "' and lookup_code = '"
										+ secondEntry.getValue().getLookUpCode() + "'")
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
										+ secondEntry.getValue().getLookUpCode() + "','"
										+ secondEntry.getValue().getTargetCode() + "','"
										+ secondEntry.getValue().getMeaning() + "')";
								session.createNativeQuery(query1).executeUpdate();
							}
						}
					}
				}

			}
			for (ScriptMasterDto masterdata : testRunMigrateDto.getScriptMasterData()) {
				ScriptMaster master = new ScriptMaster();
				master.setScript_id(masterdata.getScriptId());
				master.setModule(masterdata.getModule());
				master.setScenario_name(masterdata.getScenarioName());
				master.setScenario_description(masterdata.getScenarioDescription());
				master.setProduct_version(masterdata.getProductVersion());
				master.setPriority(masterdata.getPriority());
				master.setProcess_area(masterdata.getProcessArea());
				master.setRole(masterdata.getRole());
				master.setScript_number(masterdata.getScriptNumber());
				master.setSub_process_area(masterdata.getSubProcessArea());
				master.setStandard_custom(masterdata.getStandardCustom());
				master.setTest_script_status(masterdata.getTestScriptStatus());
				master.setCustomer_id(masterdata.getCustomerId());
				master.setDependency(masterdata.getDependency());
				master.setEnd2end_scenario(masterdata.getEnd2endScenario());
				master.setExpected_result(masterdata.getExpectedResult());
				master.setSelenium_test_script_name(masterdata.getSeleniumTestScriptName());
				master.setSelenium_test_method(masterdata.getSeleniumTestMethod());
				master.setAuthor(masterdata.getAuthor());
				master.setCreated_by(masterdata.getCreatedBy());
				master.setCreation_date(masterdata.getCreationDate());
				master.setUpdated_by(masterdata.getUpdatedBy());
				master.setUpdate_date(masterdata.getUpdateDate());
				master.setCustomisation_reference(masterdata.getCustomisationReference());
//				master.setAttribute1(masterdata.getAttribute1());
				master.setAttribute2(masterdata.getAttribute2());
				master.setAttribute3(masterdata.getAttribute3());
				master.setAttribute4(masterdata.getAttribute4());
				master.setAttribute5(masterdata.getAttribute5());
				master.setAttribute6(masterdata.getAttribute6());
				master.setAttribute7(masterdata.getAttribute7());
				master.setAttribute8(masterdata.getAttribute8());
				master.setAttribute9(masterdata.getAttribute9());
				master.setAttribute10(masterdata.getAttribute10());

				for (ScriptMetaDataDto metadatavo : masterdata.getMetaDataList()) {
					ScriptMetaData metadata = new ScriptMetaData();
					metadata.setAction(metadatavo.getAction());
					metadata.setLine_number(metadatavo.getLineNumber());
					metadata.setInput_parameter(metadatavo.getInputParameter());
					metadata.setScript_number(masterdata.getScriptNumber());
					metadata.setXpath_location(metadatavo.getXpathLocation());
					metadata.setXpath_location1(metadatavo.getXpathLocation1());
					metadata.setCreated_by(metadatavo.getCreatedBy());
					metadata.setCreation_date(metadatavo.getCreationDate());
					metadata.setUpdated_by(metadatavo.getUpdatedBy());
					metadata.setUpdate_date(metadatavo.getUpdateDate());
					metadata.setStep_desc(metadatavo.getStepDesc());
					metadata.setField_type(metadatavo.getFieldType());
					metadata.setHint(metadatavo.getHint());
					metadata.setScript_number(metadatavo.getScriptNumber());
					metadata.setDatatypes(metadatavo.getDatatypes());
					metadata.setUnique_mandatory(metadatavo.getUniqueMandatory());
					metadata.setValidation_type(metadatavo.getValidationType());
					metadata.setValidation_name(metadatavo.getValidationName());
					master.addMetadata(metadata);

				}
				listOfScriptMaster.add(master);
			}
			mapOfScriptIdsOldToNew = independentScript(listOfScriptMaster, mapOfMetaDataScriptIdsOldToNew);

			BigDecimal checkConfig1 = (BigDecimal) session
					.createNativeQuery("select count(*) from win_ta_config where config_name ='"
							+ testRunMigrateDto.getConfigurationName() + "'")
					.getSingleResult();
			Integer checkConfig = Integer.parseInt(checkConfig1.toString());

			if (checkConfig == 0) {
				BigDecimal bigDecimal = (BigDecimal) session.createSQLQuery("SELECT CONFIG_ID_SEQ.nextval FROM DUAL")
						.getSingleResult();
				Integer id = Integer.parseInt(bigDecimal.toString());
				session.createNativeQuery(
						"Insert into WIN_TA_CONFIG (CONFIGURATION_ID,CREATED_BY,LAST_UPDATED_BY,CONFIG_NAME) values ("
								+ id + ",'SUPER_ADMIN','SUPER_ADMIN','" + testRunMigrateDto.getConfigurationName()
								+ "')")
						.executeUpdate();
			}
			BigDecimal configuration = (BigDecimal) session
					.createNativeQuery("select configuration_id from win_ta_config where config_name ='"
							+ testRunMigrateDto.getConfigurationName() + "'")
					.getSingleResult();
			int configuration_id1 = Integer.parseInt(configuration.toString());

//==========================================
			BigDecimal checkProject = (BigDecimal) session
					.createNativeQuery("select count(*) from win_ta_projects where project_name ='"
							+ testRunMigrateDto.getProjectName() + "'")
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
								+ testRunMigrateDto.getProjectName() + "'," + customerId + ",'"
								+ testRunMigrateDto.getScriptMasterData().get(0).getProductVersion() + "')")
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
				testrundata.setTestsetname(testRunMigrateDto.getTestSetName() + "-" + testrunid);
			} else {
				testrundata.setTestsetname(testRunMigrateDto.getTestSetName());
			}
			testrundata.setTestsetid(testrunid);
			testrundata.setConfigurationid(configuration_id1);

			BigDecimal project = (BigDecimal) session
					.createNativeQuery("select project_id from win_ta_projects where project_name ='"
							+ testRunMigrateDto.getProjectName() + "'")
					.getSingleResult();
			int projectId = Integer.parseInt(project.toString());

			testrundata.setProjectid(projectId);

			testrundata.setDescription(testRunMigrateDto.getDescription());
			testrundata.setTest_set_desc(testRunMigrateDto.getTestSetDesc());
			testrundata.setTest_set_comments(testRunMigrateDto.getTestSetComments());
			testrundata.setEnabled(testRunMigrateDto.getEnabled());

			testrundata.setPasspath(testRunMigrateDto.getPassPath());
			testrundata.setFailpath(testRunMigrateDto.getFailPath());
			testrundata.setExceptionpath(testRunMigrateDto.getExeceptionPath());
			testrundata.setTscompleteflag("ACTIVE");

			for (TestSetLineDto lineVo : testRunMigrateDto.getTestSetLinesAndParaData()) {
				ScriptsData testSetLineData = new ScriptsData();
				int sectiptid = copyTestrunDao.getscrtiptIds();
				testSetLineData.setTestsetlineid(sectiptid);
				testSetLineData.setScriptid(mapOfScriptIdsOldToNew.get(lineVo.getScriptId()));
				testSetLineData.setCreatedby(lineVo.getCreatedby());
				testSetLineData.setCreationdate(lineVo.getCreationdate());
				testSetLineData.setEnabled(lineVo.getEnabled());
				testSetLineData.setExecutedby(lineVo.getExecutedby());
				testSetLineData.setExecutionendtime(lineVo.getExecutionendtime());
				testSetLineData.setExecutionstarttime(lineVo.getExecutionstarttime());
				testSetLineData.setLastupdatedby(lineVo.getLastupdatedby());
				testSetLineData.setScriptnumber(lineVo.getScriptnumber());
				testSetLineData.setScriptUpadated(lineVo.getScriptUpadated());
				testSetLineData.setSeqnum(lineVo.getSeqnum());
				testSetLineData.setStatus(lineVo.getStatus());
				testSetLineData.setTestsstlinescriptpath(lineVo.getTestsstlinescriptpath());
				testSetLineData.setUpdateddate(lineVo.getUpdateddate());

				for (WatsTestSetParamVO paramVo : lineVo.getScriptParam()) {
					ScritplinesData testSetParam = new ScritplinesData();
					int sectiptlineid = copyTestrunDao.getscrtiptlineIds();
					testSetParam.setTestscriptperamid(sectiptlineid);
					testSetParam.setScript_id(mapOfScriptIdsOldToNew.get(lineVo.getScriptId()));
					testSetParam.setAction(paramVo.getAction());
					testSetParam.setLine_number(paramVo.getLineNumber());
					testSetParam.setInput_parameter(paramVo.getInputParameter());
					testSetParam.setField_type(paramVo.getFieldType());
					testSetParam.setHint(paramVo.getHint());
					testSetParam.setScript_number(paramVo.getScriptNumber());
					testSetParam.setDatatypes(paramVo.getDatatypes());
					testSetParam.setCreatedby(paramVo.getCreatedBy());
					testSetParam.setCreationdate(paramVo.getCreationDate());
					testSetParam.setInput_value(paramVo.getInputValue());
					testSetParam.setLastupdatedby(paramVo.getLastUpdatedBy());
					testSetParam.setLineerrormessage(paramVo.getLineErrorMessage());
					testSetParam.setLineexecutionstatues(paramVo.getLineExecutionStatus());
//				testSetParam.setMetadata_id(mapOfMetaDataScriptIdsOldToNew.get(Integer.parseInt(paramVo.getScript_meta_data_id())));
//				testSetParam.setScript_id();
					testSetParam.setScript_number(paramVo.getScriptNumber());
//				testSetParam.setScriptsdata(paramVo.getT);
					testSetParam.setTest_run_param_desc(paramVo.getTestRunParamDesc());
					testSetParam.setTest_run_param_name(paramVo.getTestRunParamName());
//				testSetParam.setTestscriptperamid(paramVo.test);
					testSetParam.setUniquemandatory(paramVo.getUniqueMandatory());
					testSetParam.setUpdateddate(paramVo.getUpdateDate());
					testSetParam.setXpathlocation(paramVo.getXpathLocation());
					testSetParam.setXpathlocation1(paramVo.getXpathLocation1());
//				testSetLineData.addMetadata(metadata);
					testSetLineData.addScriptlines(testSetParam);

				}
				testrundata.addScriptsdata(testSetLineData);
			}
			dao.insertTestRun(testrundata);
			DomGenericResponseBean domGenericResponseBean = new DomGenericResponseBean();
			domGenericResponseBean.setStatus(200);
			domGenericResponseBean.setStatusMessage("Migrated Successfully");
			domGenericResponseBean.setTestRunName(testrundata.getTestsetname());
			listOfResponseBean.add(domGenericResponseBean);
		}
		return listOfResponseBean;
	}

	public int dependentScript(Integer id, List<ScriptMaster> listOfScriptMaster, int insertedScriptaId,
			Map<Integer, Integer> mapOfNewToOld, Map<Integer, Integer> mapOfOldToNew,
			Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew) {
		for (ScriptMaster scriptMaster : listOfScriptMaster) {
			int scriptMasterPrsent = dao.checkScriptPresent(scriptMaster.getProduct_version(),
					scriptMaster.getScript_number());
			if (scriptMasterPrsent == 0 && !mapOfNewToOld.containsKey(scriptMaster.getScript_id())
					&& !mapOfOldToNew.containsKey(scriptMaster.getScript_id())) {
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
						insertedScriptaId = dependentScript(scriptMaster.getDependency(), listOfScriptMaster,
								insertedScriptaId, mapOfNewToOld, mapOfOldToNew, mapOfMetaDataScriptIdsOldToNew);
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

	public Map<Integer, Integer> independentScript(List<ScriptMaster> listOfScriptMaster,
			Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew) {
		Map<Integer, Integer> mapOfNewToOld = new HashMap<>();
		Map<Integer, Integer> mapOfOldToNew = new HashMap<>();
		for (ScriptMaster scriptMaster : listOfScriptMaster) {

			int scriptMasterPrsent = dao.checkScriptPresent(scriptMaster.getProduct_version(),
					scriptMaster.getScript_number());

			if (scriptMasterPrsent == 0 && !mapOfNewToOld.containsKey(scriptMaster.getScript_id())
					&& !mapOfOldToNew.containsKey(scriptMaster.getScript_id())) {
				if (scriptMaster.getDependency() != null) {
					int insertedScriptaId = 0;
					dependentScript(scriptMaster.getScript_id(), listOfScriptMaster, insertedScriptaId, mapOfNewToOld,
							mapOfOldToNew, mapOfMetaDataScriptIdsOldToNew);
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
