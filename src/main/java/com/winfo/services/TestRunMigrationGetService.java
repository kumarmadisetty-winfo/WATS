package com.winfo.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.controller.MigrationReceiver;
import com.winfo.dao.CopyTestRunDao;
import com.winfo.dao.TestRunMigrationGetDao;
import com.winfo.model.ExecuteStatus;
import com.winfo.model.ExecuteStatusPK;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
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
	public static final Logger logger = Logger.getLogger(TestRunMigrationGetService.class);
	@Autowired
	TestRunMigrationGetDao dao;

	@Autowired
	CopyTestRunDao copyTestrunDao;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	@SuppressWarnings("unchecked")
	public List<DomGenericResponseBean> centralRepoData(List<TestRunMigrationDto> listOfTestRunDto) {
		
		List<DomGenericResponseBean> listOfResponseBean = new ArrayList<>();
		int count=0;
		DomGenericResponseBean domGenericResponseBean = null;
		String testrunName=null;
		String description=null;
		
		for (TestRunMigrationDto testRunMigrateDto : listOfTestRunDto) {
			Session session = entityManager.unwrap(Session.class);

			BigDecimal checkTest = (BigDecimal) session
					.createNativeQuery("select count(*) from WIN_TA_TEST_SET where test_set_name ='"
							+ testRunMigrateDto.getTestSetName() + "'")
					.getSingleResult();

			int checkTestRun = Integer.parseInt(checkTest.toString());

			if (checkTestRun > 0 && !testRunMigrateDto.isTestRunExists()) {
				domGenericResponseBean = new DomGenericResponseBean();
				domGenericResponseBean.setStatus(0);
				domGenericResponseBean.setStatusMessage("Already Exists");
				domGenericResponseBean.setTestRunName(testRunMigrateDto.getTestSetName());
				listOfResponseBean.add(domGenericResponseBean);
				logger.info("Test Run already exists " + testRunMigrateDto.getTestSetName() );			continue;
			}
			int customerId = 0;
			try {
				BigDecimal checkCustomer = (BigDecimal) session
						.createNativeQuery("select customer_id from win_ta_customers where UPPER(customer_name) ='"
								+ testRunMigrateDto.getCustomer().toUpperCase() + "'")
						.getSingleResult();
				customerId = Integer.parseInt(checkCustomer.toString());
			} catch (Exception e) {
				domGenericResponseBean = new DomGenericResponseBean();
				domGenericResponseBean.setStatusMessage("Customer Not Found");
				domGenericResponseBean.setTestRunName(testRunMigrateDto.getTestSetName());
				listOfResponseBean.add(domGenericResponseBean);
				logger.info("Customer not found " + testRunMigrateDto.getCustomer());
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

					if (secondEntry.getValue() != null && entry.getValue() != null) {
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

			}
			for (ScriptMasterDto masterdata : testRunMigrateDto.getScriptMasterData()) {
				ScriptMaster master = new ScriptMaster();
				master.setScriptId(masterdata.getScriptId());
				master.setModule(masterdata.getModule());
				master.setTargetApplication(masterdata.getTargetApplication());
				master.setScenarioName(masterdata.getScenarioName());
				master.setScenarioDescription(masterdata.getScenarioDescription());
				master.setProductVersion(masterdata.getProductVersion());
				master.setPriority(masterdata.getPriority());
				master.setProcessArea(masterdata.getProcessArea());
				master.setRole(masterdata.getRole());
				master.setScriptNumber(masterdata.getScriptNumber());
				master.setSubProcessArea(masterdata.getSubProcessArea());
				master.setStandardCustom(masterdata.getStandardCustom());
				master.setTestScriptStatus(masterdata.getTestScriptStatus());
				master.setCustomerId(masterdata.getCustomerId());
				master.setDependency(masterdata.getDependency());
				master.setEnd2endScenario(masterdata.getEnd2endScenario());
				master.setExpectedResult(masterdata.getExpectedResult());
				master.setSeleniumTestScriptName(masterdata.getSeleniumTestScriptName());
				master.setSeleniumTestMethod(masterdata.getSeleniumTestMethod());
				master.setAuthor(masterdata.getAuthor());
				master.setCreatedBy(masterdata.getCreatedBy());
				master.setCreationDate(masterdata.getCreationDate());
				master.setUpdatedBy(masterdata.getUpdatedBy());
				master.setUpdateDate(masterdata.getUpdateDate());
				master.setCustomisationReference(masterdata.getCustomisationReference());
				master.setAttribute2(masterdata.getAttribute2());
				master.setAttribute3(masterdata.getAttribute3());
				master.setAttribute4(masterdata.getAttribute4());
				master.setAttribute5(masterdata.getAttribute5());
				master.setAttribute6(masterdata.getAttribute6());
				master.setAttribute7(masterdata.getAttribute7());
				master.setAttribute8(masterdata.getAttribute8());
				master.setAttribute9(masterdata.getAttribute9());
				master.setAttribute10(masterdata.getAttribute10());
				master.setScriptMetaDatalist(new ArrayList<>());
				
				for (ScriptMetaDataDto metadatavo : masterdata.getMetaDataList()) {
					ScriptMetaData metadata = new ScriptMetaData();
					metadata.setAction(metadatavo.getAction());
					metadata.setLineNumber(metadatavo.getLineNumber());
					metadata.setInputParameter(metadatavo.getInputParameter());
					metadata.setScriptNumber(masterdata.getScriptNumber());
					metadata.setCreatedBy(metadatavo.getCreatedBy());
					metadata.setCreationDate(metadatavo.getCreationDate());
					metadata.setUpdatedBy(metadatavo.getUpdatedBy());
					metadata.setUpdateDate(metadatavo.getUpdateDate());
					metadata.setStepDesc(metadatavo.getStepDesc());
					metadata.setFieldType(metadatavo.getFieldType());
					metadata.setHint(metadatavo.getHint());
					metadata.setScriptNumber(metadatavo.getScriptNumber());
					metadata.setDatatypes(metadatavo.getDatatypes());
					metadata.setUniqueMandatory(metadatavo.getUniqueMandatory());
					metadata.setValidationType(metadatavo.getValidationType());
					metadata.setValidationName(metadatavo.getValidationName());
					master.addMetadata(metadata);

				}
				listOfScriptMaster.add(master);
			}
			mapOfScriptIdsOldToNew = independentScript(listOfScriptMaster, mapOfMetaDataScriptIdsOldToNew);

			List<BigDecimal> listOfConfig = session.createNativeQuery("select configuration_id from win_ta_config")
					.getResultList();

			int configurationId = Integer.parseInt(listOfConfig.get(0).toString());

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
						"insert into win_ta_projects(PROJECT_ID,PROJECT_NUMBER,PROJECT_NAME,CUSTOMER_ID,PRODUCT_VERSION, WATS_PACKAGE) VALUES("
								+ newNextValueProject + "," + newNextValueProjectNumber + ",'"
								+ testRunMigrateDto.getProjectName() + "'," + customerId + ",'"
								+ testRunMigrateDto.getScriptMasterData().get(0).getProductVersion() + "','"
								+ testRunMigrateDto.getWatsPackage() + "')")
						.executeUpdate();

			}

			TestSet testrundata = new TestSet();
			System.out.println("checkTestRun " + checkTestRun);
			if (checkTestRun > 0) {
				String testRunName=null;
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
				if(testRunMigrateDto.getTestSetName().length() >= 67 )
				{
					testRunName=testRunMigrateDto.getTestSetName().substring(0,67);
					testRunName=testRunName + " " + sdf.format(new Timestamp(System.currentTimeMillis()));
					testrundata.setTestRunName(testRunName);
				}else
				{
					testRunName=testRunMigrateDto.getTestSetName() + " " + sdf.format(new Timestamp(System.currentTimeMillis()));
					testrundata.setTestRunName(testRunName);
				}
				
			} else {
				testrundata.setTestRunName(testRunMigrateDto.getTestSetName());
			}
			testrundata.setConfigurationId(configurationId);

			BigDecimal project = (BigDecimal) session
					.createNativeQuery("select project_id from win_ta_projects where project_name ='"
							+ testRunMigrateDto.getProjectName() + "'")
					.getSingleResult();
			int projectId = Integer.parseInt(project.toString());

			testrundata.setProjectId(projectId);

			testrundata.setDescription(testRunMigrateDto.getDescription());
			testrundata.setTestRunDesc(testRunMigrateDto.getTestSetDesc());
			testrundata.setTestRunComments(testRunMigrateDto.getTestSetComments());
			testrundata.setEnabled(testRunMigrateDto.getEnabled());

			testrundata.setPassPath(testRunMigrateDto.getPassPath());
			testrundata.setFailPath(testRunMigrateDto.getFailPath());
			testrundata.setExceptionPath(testRunMigrateDto.getExeceptionPath());
			testrundata.setTsCompleteFlag("ACTIVE");

			for (TestSetLineDto lineVo : testRunMigrateDto.getTestSetLinesAndParaData()) {
				TestSetLine testSetLineData = new TestSetLine();
				testSetLineData.setScriptId(mapOfScriptIdsOldToNew.get(lineVo.getScriptId()));
				testSetLineData.setCreatedBy(lineVo.getCreatedby());
				testSetLineData.setCreationDate(lineVo.getCreationdate());
				testSetLineData.setEnabled(lineVo.getEnabled());
				testSetLineData.setExecutedBy(lineVo.getExecutedby());
				testSetLineData.setExecutionEndTime(lineVo.getExecutionendtime());
				testSetLineData.setExecutionStartTime(lineVo.getExecutionstarttime());
				testSetLineData.setLastUpdatedBy(lineVo.getLastupdatedby());
				testSetLineData.setScriptNumber(lineVo.getScriptnumber());
				testSetLineData.setScriptUpadated(lineVo.getScriptUpadated());
				testSetLineData.setSeqNum(lineVo.getSeqnum());
				testSetLineData.setStatus(lineVo.getStatus());
				testSetLineData.setTestRunScriptPath(lineVo.getTestsstlinescriptpath());
				testSetLineData.setUpdateDate(lineVo.getUpdateddate());
				

				for (WatsTestSetParamVO paramVo : lineVo.getScriptParam()) {
					TestSetScriptParam testSetParam = new TestSetScriptParam();
					testSetParam.setScriptId(mapOfScriptIdsOldToNew.get(lineVo.getScriptId()));
					testSetParam.setAction(paramVo.getAction());
					testSetParam.setLineNumber(paramVo.getLineNumber());
					testSetParam.setInputParameter(paramVo.getInputParameter());
					testSetParam.setFieldType(paramVo.getFieldType());
					testSetParam.setHint(paramVo.getHint());
					testSetParam.setScriptNumber(paramVo.getScriptNumber());
					testSetParam.setDataTypes(paramVo.getDatatypes());
					testSetParam.setCreatedBy(paramVo.getCreatedBy());
					testSetParam.setCreationDate(paramVo.getCreationDate());
					testSetParam.setInputValue(paramVo.getInputValue());
					testSetParam.setLastUpdatedBy(paramVo.getLastUpdatedBy());			
					//				testSetParam.setMetadata_id(mapOfMetaDataScriptIdsOldToNew.get(Integer.parseInt(paramVo.getScript_meta_data_id())));
//				testSetParam.setScript_id();
					testSetParam.setScriptNumber(paramVo.getScriptNumber());
//				testSetParam.setScriptsdata(paramVo.getT);
					testSetParam.setTestRunParamDesc(paramVo.getTestRunParamDesc());
					testSetParam.setTestRunParamName(paramVo.getTestRunParamName());
//				testSetParam.setTestscriptperamid(paramVo.test);
					testSetParam.setUniqueMandatory(paramVo.getUniqueMandatory());
					testSetParam.setUpdateDate(paramVo.getUpdateDate());
					testSetParam.setXpathLocation(paramVo.getXpathLocation());
					testSetParam.setXpathLocation1(paramVo.getXpathLocation1());
//				testSetLineData.addMetadata(metadata);
					testSetLineData.addTestScriptParam(testSetParam);
    
				}
				testrundata.addTestRunScriptData(testSetLineData);
			}
			dao.insertTestRun(testrundata);
			count++;
			ExecuteStatus executeStatusObj = new ExecuteStatus();
			ExecuteStatusPK executeStatusPK = new ExecuteStatusPK();
			executeStatusPK.setExecutedBy(testrundata.getCreatedBy());
			executeStatusPK.setTestSetId(testrundata.getTestRunId());
			executeStatusObj.setExecuteStatusPK(executeStatusPK);
			executeStatusObj.setExecutionDate(new Date());
			executeStatusObj.setFlag('I');
			executeStatusObj.setTestRunName(testrundata.getTestRunName());
			copyTestrunDao.updateExecuteStatusDtls(executeStatusObj);
			if(count==1)
			{
				testrunName=testrundata.getTestRunName();
				description=testrunName + " " + "TestRun Migrated Successfully";
			}
			else
			{
				description=count + " " + "TestRuns Migrated Successfully";
				
			}			
		}
		if(count>=1)
		{
			domGenericResponseBean = new DomGenericResponseBean();
			domGenericResponseBean.setStatus(200);
			domGenericResponseBean.setStatusMessage("SUCCESS");
			domGenericResponseBean.setDescription(description);
			listOfResponseBean.add(domGenericResponseBean);
			logger.info("Successfully migrated test run " + description );
		}
		return listOfResponseBean;
	}

	public int dependentScript(Integer id, List<ScriptMaster> listOfScriptMaster, int insertedScriptaId,
			Map<Integer, Integer> mapOfNewToOld, Map<Integer, Integer> mapOfOldToNew,
			Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew) {
		for (ScriptMaster scriptMaster : listOfScriptMaster) {
			int scriptMasterPrsent = dao.checkScriptPresent(scriptMaster.getProductVersion(),
					scriptMaster.getScriptNumber());
			if (scriptMasterPrsent == 0 && !mapOfNewToOld.containsKey(scriptMaster.getScriptId())
					&& !mapOfOldToNew.containsKey(scriptMaster.getScriptId())) {
				if (id.equals(scriptMaster.getScriptId())) {
					if (scriptMaster.getDependency() == null) {
						int originalId = scriptMaster.getScriptId();
						insertedScriptaId = dao.insertScriptMaster(scriptMaster);
						for (ScriptMetaData scriptMetaData : scriptMaster.getScriptMetaDatalist()) {
							int oldMetaDataId = scriptMetaData.getScriptMetaDataId();
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
						int originalId = scriptMaster.getScriptId();
						insertedScriptaId = dao.insertScriptMaster(scriptMaster);
						for (ScriptMetaData scriptMetaData : scriptMaster.getScriptMetaDatalist()) {
							int oldMetaDataId = scriptMetaData.getScriptMetaDataId();
							scriptMetaData.setScriptMaster(scriptMaster);
							int insertedScriptMetaDataObject = dao.insertScriptMetaData(scriptMetaData);
							mapOfMetaDataScriptIdsOldToNew.put(oldMetaDataId, insertedScriptMetaDataObject);
						}
						mapOfNewToOld.put(insertedScriptaId, originalId);
						mapOfOldToNew.put(originalId, insertedScriptaId);
					}

				}
			} else if (scriptMasterPrsent > 0) {
				int originalId = scriptMaster.getScriptId();
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

			int scriptMasterPrsent = dao.checkScriptPresent(scriptMaster.getProductVersion(),
					scriptMaster.getScriptNumber());

			if (scriptMasterPrsent == 0 && !mapOfNewToOld.containsKey(scriptMaster.getScriptId())
					&& !mapOfOldToNew.containsKey(scriptMaster.getScriptId())) {
				if (scriptMaster.getDependency() != null) {
					int insertedScriptaId = 0;
					dependentScript(scriptMaster.getScriptId(), listOfScriptMaster, insertedScriptaId, mapOfNewToOld,
							mapOfOldToNew, mapOfMetaDataScriptIdsOldToNew);
				} else {
					int originalId = scriptMaster.getScriptId();
					scriptMaster.setScriptId(null);
					int id = dao.insertScriptMaster(scriptMaster);
					for (ScriptMetaData scriptMetaData : scriptMaster.getScriptMetaDatalist()) {
						Integer oldMetaDataId = scriptMetaData.getScriptMetaDataId();
						scriptMetaData.setScriptMaster(scriptMaster);
						int insertedScriptMetaDataObject = dao.insertScriptMetaData(scriptMetaData);
						mapOfMetaDataScriptIdsOldToNew.put(oldMetaDataId, insertedScriptMetaDataObject);
					}
					mapOfNewToOld.put(id, originalId);
					mapOfOldToNew.put(originalId, id);
				}
			} else {
				int originalId = scriptMaster.getScriptId();
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
