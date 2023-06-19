package com.winfo.serviceImpl;

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
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winfo.controller.MigrationReceiver;
import com.winfo.dao.CopyTestRunDao;
import com.winfo.dao.DataBaseEntryDao;
import com.winfo.dao.TestRunMigrationGetDao;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.ConfigTable;
import com.winfo.model.ExecuteStatus;
import com.winfo.model.ExecuteStatusPK;
import com.winfo.model.Project;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.repository.ConfigurationRepository;
import com.winfo.repository.ProjectRepository;
import com.winfo.repository.ScriptMasterRepository;
import com.winfo.utils.Constants;
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
	
	@Autowired
	DataBaseEntryDao dataBaseEntryDao;
	
	@Autowired
	ScriptMasterRepository scriptMasterRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	DomGenericResponseBean domGenericResponseBean;

	@Autowired
	ConfigurationRepository configurationRepository;
	
	@Transactional
	@SuppressWarnings("unchecked")
	public List<DomGenericResponseBean> centralRepoData(List<TestRunMigrationDto> listOfTestRunDto) {
		
		List<DomGenericResponseBean> listOfResponseBean = new ArrayList<>();
		int count=0;
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
				domGenericResponseBean.setStatus(0);
				domGenericResponseBean.setStatusMessage("Already Exists");
				domGenericResponseBean.setTestRunName(testRunMigrateDto.getTestSetName());
				listOfResponseBean.add(domGenericResponseBean);
				logger.info("Test Run already exists " + testRunMigrateDto.getTestSetName());
				continue;
			}
			int customerId = 0;
			try {
				BigDecimal checkCustomer = (BigDecimal) session
						.createNativeQuery("select customer_id from win_ta_customers where UPPER(customer_name) ='"
								+ testRunMigrateDto.getCustomer().toUpperCase() + "'")
						.getSingleResult();
				customerId = Integer.parseInt(checkCustomer.toString());
			} catch (Exception e) {
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
			mapOfScriptIdsOldToNew = getOldToNewScriptIds(listOfScriptMaster, mapOfMetaDataScriptIdsOldToNew, customerId,testRunMigrateDto);
			int configurationId;
			try {
				ConfigTable config=configurationRepository.findFirstByCustomerId(customerId);
				configurationId=config.getConfigurationId();		
			}catch (Exception e) {
				logger.error(Constants.CONFFIG_ERROR);
				throw new WatsEBSException(HttpStatus.NOT_FOUND.value(),Constants.CONFFIG_ERROR);
			}
			
			BigDecimal checkProject = (BigDecimal) session
					.createNativeQuery("select count(*) from win_ta_projects where project_name ='"
							+ testRunMigrateDto.getProjectName() + "' and customer_id="+customerId)
					.getSingleResult();

			int checkProjectId = Integer.parseInt(checkProject.toString());

			if (checkProjectId == 0) {
				BigDecimal nextValueProjectNumber = (BigDecimal) session
						.createNativeQuery("SELECT WIN_TA_PROJECT_NUM.nextval FROM DUAL").getSingleResult();
				Integer newNextValueProjectNumber = Integer.parseInt(nextValueProjectNumber.toString());

				BigDecimal nextValueProject = (BigDecimal) session
						.createNativeQuery("SELECT WIN_TA_PROJECT_SEQ.nextval FROM DUAL").getSingleResult();
				Integer newNextValueProject = Integer.parseInt(nextValueProject.toString());
				
				long checkProjectIdInInstance = projectRepository.countByProjectName(testRunMigrateDto.getProjectName());				
				if (checkProjectIdInInstance > 0) {	
					String maxProjectName=projectRepository.getMaxProjectName(testRunMigrateDto.getProjectName()).trim();
					String newProjectName=maxProjectName+"-1";
					testRunMigrateDto.setProjectName(newProjectName);
				}

				session.createNativeQuery(
						"insert into win_ta_projects(PROJECT_ID,PROJECT_NUMBER,PROJECT_NAME,START_DATE,CUSTOMER_ID,PRODUCT_VERSION, WATS_PACKAGE) VALUES("
								+ newNextValueProject + "," + newNextValueProjectNumber + ",'"
								+ testRunMigrateDto.getProjectName() +"',SYSDATE," + customerId + ",'"
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
			
			String query = "select project_id from win_ta_projects where project_name ='"+testRunMigrateDto.getProjectName()+"' and customer_id="+customerId;
			BigDecimal project = null;
			try {
			project = (BigDecimal) session
					.createNativeQuery(query)
					.getSingleResult();
			}catch (Exception e) {			
				logger.error(Constants.PROJECT_ERROR);
				throw new WatsEBSException(HttpStatus.NOT_FOUND.value(),Constants.PROJECT_ERROR);
			}
			Project projectObject=projectRepository.findByProjectNameAndCustomerId(testRunMigrateDto.getProjectName(),customerId);
			int projectId =  projectObject.getProjectId();

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
			Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew,int customerId, TestRunMigrationDto testRunMigrateDto) {
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
								insertedScriptaId, mapOfNewToOld, mapOfOldToNew, mapOfMetaDataScriptIdsOldToNew,customerId,testRunMigrateDto);
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
				createScriptIfExistWithDiffCustomer(listOfScriptMaster,mapOfMetaDataScriptIdsOldToNew,customerId,testRunMigrateDto,
						scriptMaster,scriptMasterPrsent,mapOfOldToNew,mapOfNewToOld);
			}
		}
		return insertedScriptaId;
	}

	public Map<Integer, Integer> getOldToNewScriptIds(List<ScriptMaster> listOfScriptMaster,
			Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew, int customerId, TestRunMigrationDto testRunMigrateDto) {
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
							mapOfOldToNew, mapOfMetaDataScriptIdsOldToNew,customerId,testRunMigrateDto);
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
				createScriptIfExistWithDiffCustomer(listOfScriptMaster,mapOfMetaDataScriptIdsOldToNew,customerId,testRunMigrateDto,
						scriptMaster,scriptMasterPrsent,mapOfOldToNew,mapOfNewToOld);
			}
		}
		return mapOfOldToNew;
	}

	public void createScriptIfExistWithDiffCustomer(List<ScriptMaster> listOfScriptMaster,
			Map<Integer, Integer> mapOfMetaDataScriptIdsOldToNew, int customerId, TestRunMigrationDto testRunMigrateDto,
			ScriptMaster scriptMaster,int scriptMasterPrsent,Map<Integer, Integer> mapOfOldToNew,Map<Integer, Integer> mapOfNewToOld) {
		String newCustomScriptNumber="";
		//Get customer id of existing script
		ScriptMaster oldScriptCustomerId = scriptMasterRepository.findByScriptNumberAndProductVersion(scriptMaster.getScriptNumber(),
				scriptMaster.getProductVersion());
		//check existing script is mapped with target customer or not 
		if(oldScriptCustomerId.getCustomerId()!=customerId) {
			newCustomScriptNumber=scriptMaster.getScriptNumber().contains(".C.")?scriptMaster.getScriptNumber():
				scriptMaster.getScriptNumber()+".C.";
			int maxScriptId=scriptMasterRepository.getMaxScriptNumber(newCustomScriptNumber.substring(0,newCustomScriptNumber.indexOf(".C.")+3)
					,scriptMaster.getProductVersion());
			ScriptMaster maxScriptObject=scriptMasterRepository.findByScriptId(maxScriptId);
			String maxScriptNumber=maxScriptObject.getScriptNumber();
			if("".equals(maxScriptNumber) || maxScriptNumber==null) {
				newCustomScriptNumber=newCustomScriptNumber+"1";
			}
			else {
				try {
					newCustomScriptNumber=maxScriptNumber.substring(0,maxScriptNumber.indexOf(".C.")+3) 
							+ (Integer.parseInt(maxScriptNumber.substring(maxScriptNumber.indexOf(".C.")+3))+1);
					}catch(Exception e) {
						logger.error("Exception Occured while creating new script for Test Run");
						throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception Occured while creating new script for Test Run", e);
					}				
			}
			String setNewCustomScriptNumber=newCustomScriptNumber;
			//updating new script number in test_set_line and test_set_script_param
			List<TestSetLineDto> updatedLineData =
			testRunMigrateDto.getTestSetLinesAndParaData().stream()
			.map((testSetLine) -> {
				if(scriptMaster.getScriptNumber().equals(testSetLine.getScriptnumber())) {
					testSetLine.setScriptnumber(setNewCustomScriptNumber);
					List<WatsTestSetParamVO> updatedParamData =testSetLine.getScriptParam().stream().map((testSetLineParam) -> {
						testSetLineParam.setScriptNumber(setNewCustomScriptNumber);
						return testSetLineParam;
					}).collect(Collectors.toList());
					testSetLine.setScriptParam(updatedParamData);
				}
				return testSetLine;
			}).collect(Collectors.toList());
			testRunMigrateDto.setTestSetLinesAndParaData(updatedLineData);	
			scriptMaster.setScriptNumber(newCustomScriptNumber);
			scriptMaster.setStandardCustom(Constants.CUSTOM_SCRIPT);
			int originalId = scriptMaster.getScriptId();
			scriptMaster.setScriptId(null);
			int id = dao.insertScriptMaster(scriptMaster);
			List<ScriptMetaData> updatedScriptMetaData=
				scriptMaster.getScriptMetaDatalist().stream().map((scriptMetaData) -> {
				Integer oldMetaDataId = scriptMetaData.getScriptMetaDataId();
				if(!"".equals(setNewCustomScriptNumber)) scriptMetaData.setScriptNumber(setNewCustomScriptNumber);
				int insertedScriptMetaDataObject = dao.insertScriptMetaData(scriptMetaData);
				mapOfMetaDataScriptIdsOldToNew.put(oldMetaDataId, insertedScriptMetaDataObject);
				return scriptMetaData;
			}).collect(Collectors.toList());
			scriptMaster.setScriptMetaDatalist(updatedScriptMetaData);
			mapOfNewToOld.put(id, originalId);
			mapOfOldToNew.put(originalId, id);
		}
		else {
			int originalId = scriptMaster.getScriptId();
				mapOfNewToOld.put(scriptMasterPrsent, originalId);
				mapOfOldToNew.put(originalId, scriptMasterPrsent);				
		}
	}
}
