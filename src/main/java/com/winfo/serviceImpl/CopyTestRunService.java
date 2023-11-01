package com.winfo.serviceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.winfo.config.MessageUtil;
import com.winfo.dao.CopyTestRunDao;
import com.winfo.exception.WatsEBSException;
import com.winfo.model.ExecuteStatus;
import com.winfo.model.ExecuteStatusPK;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.repository.TestSetRepository;
import com.winfo.utils.Constants;
import com.winfo.vo.ApiValidationVO;
import com.winfo.vo.CopytestrunVo;
import com.winfo.vo.InsertScriptsVO;
import com.winfo.vo.ResponseDto;

@Service
public class CopyTestRunService {
	public static final Logger logger = Logger.getLogger(CopyTestRunService.class);

	private static final String NEW = "New";

	@Autowired
	CopyTestRunDao copyTestrunDao;
	
	@Autowired
	TestSetRepository testSetRepository;
	
	@Autowired
	MessageUtil messageUtil;

	@Transactional
	public int copyTestrun(@Valid CopytestrunVo copyTestrunvo) throws InterruptedException, JsonMappingException, JsonProcessingException {
		TestSet testSetObj = testSetRepository.findByTestRunId(copyTestrunvo.getTestScriptNo());	
		TestSet newTestSetObj = new TestSet();
		newTestSetObj.setTestRunDesc(testSetObj.getTestRunDesc());
		newTestSetObj.setTestRunComments(testSetObj.getTestRunComments());
		newTestSetObj.setEnabled("Y");
		newTestSetObj.setDescription(testSetObj.getDescription());
		newTestSetObj.setEffectiveFrom(testSetObj.getEffectiveFrom());
		newTestSetObj.setEffectiveTo(testSetObj.getEffectiveTo());
		newTestSetObj.setTestRunName(copyTestrunvo.getNewtestrunname());
		newTestSetObj.setConfigurationId(copyTestrunvo.getConfiguration());
		newTestSetObj.setProjectId(copyTestrunvo.getProject());
		newTestSetObj.setCreatedBy(copyTestrunvo.getCreatedBy());
		newTestSetObj.setLastUpdatedBy(null);
		newTestSetObj.setCreationDate(copyTestrunvo.getCreationDate());
		newTestSetObj.setUpdateDate(null);
		newTestSetObj.setTsCompleteFlag("Active");
		newTestSetObj.setPassPath(testSetObj.getPassPath());
		newTestSetObj.setFailPath(testSetObj.getFailPath());
		newTestSetObj.setExceptionPath(testSetObj.getExceptionPath());
		newTestSetObj.setTestRunMode("ACTIVE");
		newTestSetObj.setLastExecutBy(null);
		newTestSetObj.setCopyIncreamentFlag(copyTestrunvo.getIncrementValue());
		newTestSetObj.setReferenceTestRunId(copyTestrunvo.getTestScriptNo());
		
		String productVersion = copyTestrunDao.getProductVersion(newTestSetObj.getProjectId());
		Map<Integer, Integer> mapOfTestRunDependencyOldToNewId = new HashMap<>();

		Map<Integer, TestSetLine> mapOfLinesData = new HashMap<>();

		for (TestSetLine testSetLineObj : testSetObj.getTestRunScriptDatalist()) {// getScriptdata

			ScriptMaster scriptMaster = copyTestrunDao.getScriptMasterInfo(testSetLineObj.getScriptNumber(),
					productVersion);
			TestSetLine testSetLineRecords = new TestSetLine();
			mapOfLinesData.put(testSetLineObj.getTestRunScriptId(), testSetLineRecords);
			if (scriptMaster != null) {
				testSetLineRecords.setScriptId(scriptMaster.getScriptId());
				testSetLineRecords.setCreatedBy(copyTestrunvo.getCreatedBy());
				testSetLineRecords.setCreationDate(copyTestrunvo.getCreationDate());
				testSetLineRecords.setEnabled("Y");
				testSetLineRecords.setScriptNumber(scriptMaster.getScriptNumber());
				testSetLineRecords.setSeqNum(testSetLineObj.getSeqNum());
				mapOfTestRunDependencyOldToNewId.put(testSetLineObj.getTestRunScriptId(), testSetLineObj.getSeqNum());
				testSetLineRecords.setStatus(NEW);
				testSetLineRecords.setLastUpdatedBy(null);
				testSetLineRecords.setScriptUpadated("N");
				testSetLineRecords.setUpdateDate(null);
				testSetLineRecords.setTestRunScriptPath(testSetLineObj.getTestRunScriptPath());
				testSetLineRecords.setExecutedBy(null);
				testSetLineRecords.setExecutionStartTime(null);
				testSetLineRecords.setExecutionEndTime(null);
				newTestSetObj.addTestRunScriptData(testSetLineRecords);
			} else {

				continue;
			}
			Integer newScriptParamSeq = 0;
			Integer oldScriptParamSeq = 0;
			List<ScriptMetaData> metadataList = scriptMaster.getScriptMetaDatalist();
			List<TestSetScriptParam> scriptLineList = testSetLineObj.getTestRunScriptParam();
			Comparator<TestSetScriptParam> scriptLineComparator = (TestSetScriptParam s1,
					TestSetScriptParam s2) -> s1.getLineNumber() - s2.getLineNumber();
			Comparator<ScriptMetaData> metaDataComparator = (ScriptMetaData s1,
					ScriptMetaData s2) -> s1.getLineNumber() - s2.getLineNumber();
			Collections.sort(scriptLineList, scriptLineComparator);
			Collections.sort(metadataList, metaDataComparator);
			ScriptMetaData metadata = null;
			Integer check = null;
			TestSetScriptParam getScriptlinedata = null;
			TestSetScriptParam setScriptlinedata = null;
			while (newScriptParamSeq < metadataList.size() && oldScriptParamSeq < scriptLineList.size()) {
				metadata = metadataList.get(newScriptParamSeq);

				getScriptlinedata = scriptLineList.get(oldScriptParamSeq);

				if (!(newScriptParamSeq.equals(check))) {
					setScriptlinedata = new TestSetScriptParam();
					setScriptlinedata.setInputParameter(metadata.getInputParameter());
					setScriptlinedata.setScriptId(testSetLineRecords.getScriptId());
					setScriptlinedata.setScriptNumber(testSetLineRecords.getScriptNumber());
					setScriptlinedata.setLineNumber(metadata.getLineNumber());
					setScriptlinedata.setAction(metadata.getAction());
					setScriptlinedata.setTestRunParamDesc(metadata.getStepDesc());
					setScriptlinedata.setMetadataId(metadata.getScriptMetaDataId());
					setScriptlinedata.setHint(metadata.getHint());
					setScriptlinedata.setFieldType(metadata.getFieldType());
					setScriptlinedata.setXpathLocation(getScriptlinedata.getXpathLocation());
					setScriptlinedata.setXpathLocation1(getScriptlinedata.getXpathLocation1());
					setScriptlinedata.setCreatedBy(copyTestrunvo.getCreatedBy());
					setScriptlinedata.setCreationDate(copyTestrunvo.getCreationDate());
					setScriptlinedata.setUpdateDate(null);
					setScriptlinedata.setLastUpdatedBy(null);
					setScriptlinedata.setLineExecutionStatus(NEW);
					setScriptlinedata.setLineErrorMessage(null);
					setScriptlinedata.setDataTypes(metadata.getDatatypes());
					setScriptlinedata.setUniqueMandatory(metadata.getUniqueMandatory());
					setScriptlinedata.setValidationType(metadata.getValidationType());
					setScriptlinedata.setValidationName(metadata.getValidationName());
					check = newScriptParamSeq.intValue();
				}
				if (setScriptlinedata.getInputParameter() != null && getScriptlinedata.getInputParameter() != null) {
					if (setScriptlinedata.getAction().equalsIgnoreCase(getScriptlinedata.getAction())
							&& setScriptlinedata.getInputParameter()
									.equalsIgnoreCase(getScriptlinedata.getInputParameter())
							&& setScriptlinedata.getLineNumber().equals(getScriptlinedata.getLineNumber())) {
						addInputvalues(getScriptlinedata, setScriptlinedata, copyTestrunvo, testSetLineRecords);
					} else {
						setScriptlinedata.setInputValue(null);
					}
				} else {
					setScriptlinedata.setInputValue(null);
				}
				if (setScriptlinedata.getLineNumber() >= getScriptlinedata.getLineNumber()) {
					oldScriptParamSeq++;

				}
				if (getScriptlinedata.getLineNumber() >= setScriptlinedata.getLineNumber()) {
					newScriptParamSeq++;

				}

				testSetLineRecords.addTestScriptParam(setScriptlinedata);

			}
			newScriptParamSeq++;
			while (newScriptParamSeq < metadataList.size()) {
				metadata = metadataList.get(newScriptParamSeq);
				setScriptlinedata = new TestSetScriptParam();
				setScriptlinedata.setInputParameter(metadata.getInputParameter());
				setScriptlinedata.setScriptId(testSetLineRecords.getScriptId());
				setScriptlinedata.setScriptNumber(testSetLineRecords.getScriptNumber());
				setScriptlinedata.setLineNumber(metadata.getLineNumber());
				setScriptlinedata.setAction(metadata.getAction());

				setScriptlinedata.setMetadataId(metadata.getScriptMetaDataId());
				setScriptlinedata.setHint(metadata.getHint());
				setScriptlinedata.setFieldType(metadata.getFieldType());
				setScriptlinedata.setCreatedBy(copyTestrunvo.getCreatedBy());
				setScriptlinedata.setCreationDate(copyTestrunvo.getCreationDate());
				setScriptlinedata.setUpdateDate(null);
				setScriptlinedata.setLastUpdatedBy(null);
				setScriptlinedata.setLineExecutionStatus(NEW);
				setScriptlinedata.setLineErrorMessage(null);
				setScriptlinedata.setDataTypes(metadata.getDatatypes());
				setScriptlinedata.setUniqueMandatory(metadata.getUniqueMandatory());

				setScriptlinedata.setInputValue(null);

				testSetLineRecords.addTestScriptParam(setScriptlinedata);

				newScriptParamSeq++;

			}

		}
		logger.info("before saveTestrun");
		for (TestSetLine oldTestSetLine : testSetObj.getTestRunScriptDatalist()) {
			if (oldTestSetLine.getDependencyTr() != null) {
				mapOfLinesData.get(oldTestSetLine.getTestRunScriptId())
						.setDependencyTr(mapOfTestRunDependencyOldToNewId.get(oldTestSetLine.getDependencyTr()));
			}
		}

		TestSet newtestrun = copyTestrunDao.saveTestrun(newTestSetObj);

		Map<Integer, Integer> dependencyLinesIdAndSeqNum = new HashMap<>();
		for (TestSetLine newTestSetLine : newTestSetObj.getTestRunScriptDatalist()) {
			dependencyLinesIdAndSeqNum.put(newTestSetLine.getTestRunScriptId(), newTestSetLine.getSeqNum());
		}

		for (TestSetLine newTestSetLine : newTestSetObj.getTestRunScriptDatalist()) {
			if (newTestSetLine.getDependencyTr() != null) {
				for(Map.Entry<Integer, Integer> entrySet : dependencyLinesIdAndSeqNum.entrySet()) {
					if(entrySet.getValue().equals(newTestSetLine.getDependencyTr())) {
						newTestSetLine.setDependencyTr(entrySet.getKey());
						break;
					}
				}
				copyTestrunDao.updatelinesRecord(newTestSetLine);
			}
		}
		ExecuteStatus executeStatusObj = new ExecuteStatus();
		
		ExecuteStatusPK executeStatusPK = new ExecuteStatusPK();
		
		executeStatusPK.setExecutedBy(copyTestrunvo.getCreatedBy());
		executeStatusPK.setTestSetId(newtestrun.getTestRunId());
		executeStatusObj.setExecuteStatusPK(executeStatusPK);
		executeStatusObj.setExecutionDate(new Date());
		executeStatusObj.setFlag('I');
		executeStatusObj.setTestRunName(copyTestrunvo.getNewtestrunname());
		copyTestrunDao.updateExecuteStatusDtls(executeStatusObj);
		logger.info("New test run id " + newtestrun.getTestRunId());
		return newtestrun.getTestRunId();
	}

	private void addInputvalues(TestSetScriptParam testSetScriptParamObj, TestSetScriptParam scriptParamObj,
			CopytestrunVo copyTestrunvo, TestSetLine testSetLineObj) throws InterruptedException, JsonProcessingException {
		String inputValues = testSetScriptParamObj.getInputValue();
		String[] actios = { "clearandtype", "textarea", "selectAValue", "clickCheckbox", "selectByText",
				"clickButton Dropdown", "clickLinkAction", "Table Dropdown Values", "Table SendKeys", "enterIntoTable",
				"SendKeys", "Login into Application", "Dropdown Values", "typeAtPosition", "clickAndTypeAtPosition",
				"clickRadiobutton", "clickCheckbox", "multipleSendKeys", "multiplelinestableSendKeys", "DatePicker",
				"copynumber", "copytext", "paste", "apiValidationResponse" };
		List<String> actionsList = new ArrayList<>(Arrays.asList(actios));
		TestSet testSetObj = copyTestrunDao.getdata(copyTestrunvo.getTestScriptNo());
		if ("y".equalsIgnoreCase(copyTestrunvo.getIncrementValue())
				&& (scriptParamObj.getUniqueMandatory() != null && !scriptParamObj.getUniqueMandatory().equals("NA"))
				&& (scriptParamObj.getUniqueMandatory().equalsIgnoreCase("Unique")
						|| scriptParamObj.getUniqueMandatory().equalsIgnoreCase("Both"))) {
			if ((scriptParamObj.getDataTypes() != null && !scriptParamObj.getDataTypes().equals("NA"))
					&& scriptParamObj.getDataTypes().equalsIgnoreCase("Alpha-Numeric")) {
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
				Date dateobj = new Date();
				String covertDateobj = dateformate.format(dateobj);
				Thread.sleep(1);
				covertDateobj = covertDateobj.replaceAll("[^0-9]", "");
				int fistOff = Integer.parseInt(covertDateobj.substring(0, 8));
				int secondHalf = Integer.parseInt(covertDateobj.substring(8, 15));
				String hexaDecimal = Integer.toString(fistOff, 36) + Integer.toString(secondHalf, 36);
				if("apiValidationResponse".equalsIgnoreCase(scriptParamObj.getAction())) {
					String productVersion = copyTestrunDao.getProductVersion(testSetObj.getProjectId());
					ScriptMaster scriptMaster = copyTestrunDao.getScriptMasterInfo(scriptParamObj.getScriptNumber(), productVersion);
					String[] incrementValues = scriptMaster.getAttribute2().split(",");

					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					String inputValue = inputValues.replaceAll("(\")(?=[\\{])|(?<=[\\}])(\")|(\\\\)(?=[\\\"])","");
					ApiValidationVO apiValidationData = objectMapper.readValue(inputValue,ApiValidationVO.class);
					if (apiValidationData != null && apiValidationData.getResponse() != null
							&& !ObjectUtils.isEmpty(apiValidationData.getRequestBody())) {
						Object requestBody = apiValidationData.getRequestBody();
						ObjectWriter ow1 = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String json1 = ow1.writeValueAsString(requestBody);
						Map<String, String> mapping = null;
						mapping = new ObjectMapper().readValue(json1, HashMap.class);
						for (String keys : incrementValues) {
							if (mapping.containsKey(keys)) {
								String value = mapping.get(keys);
								String newValue = incrementValueForApi(value);
								mapping.put(keys, newValue);
							}
						}
						ObjectWriter ow = new ObjectMapper().writer();
						String json = ow.writeValueAsString(mapping);
						apiValidationData.setRequestBody(json);
						apiValidationData.setResponseCode(null);
						apiValidationData.setAccessToken("");
						String finalJson = ow.writeValueAsString(apiValidationData);
						hexaDecimal = finalJson.replaceAll("(\")(?=[\\{])|(?<=[\\}])(\")|(\\\\)(?=[\\\"])", "");
					} else {
						ObjectWriter ow = new ObjectMapper().writer();
						String finalJson = ow.writeValueAsString(apiValidationData);
						hexaDecimal = finalJson.replaceAll("(\")(?=[\\{])|(?<=[\\}])(\")|(\\\\)(?=[\\\"])", "");
					}
				}else if(inputValues!=null && ("Unique".equalsIgnoreCase(scriptParamObj.getUniqueMandatory())
						|| "Both".equalsIgnoreCase(scriptParamObj.getUniqueMandatory()))) {
					int incrementValue = 0;
					String numericValue="";
					String updatedNumericValue="";
					char[] charArray=inputValues.toCharArray();
			        if(Character.isDigit(charArray[charArray.length-1]))
				     {
			        		for(int i=charArray.length-1;i>=0;i--)
				        	{
				        	    if(!Character.isDigit(charArray[i]))
				        	    {
				        	    	break;
				        	    }
				        	    numericValue=String.valueOf(numericValue+(charArray[i]));
			        	        StringBuilder stringbuilder = new StringBuilder(numericValue);
			        	        updatedNumericValue=String.valueOf(stringbuilder.reverse());
			        	       
				        	}
				            incrementValue= Integer.parseInt(updatedNumericValue);
				            int updatedIncrementValue=incrementValue+1;
				            String finalIncrementValue=String.valueOf(updatedIncrementValue);
				            if(String.valueOf(incrementValue).length()!=updatedNumericValue.length())
			            	 {
				            	int differenceDigit=updatedNumericValue.length()-String.valueOf(incrementValue).length();
				            	finalIncrementValue= StringUtils.leftPad(finalIncrementValue, differenceDigit+String.valueOf(incrementValue).length(),"0");
			            	    
			            	 }
				            
				            String updatedInputValue=inputValues.replace(updatedNumericValue,finalIncrementValue);
				            String finalResult=String.join(" ",String.valueOf(updatedInputValue));
				            hexaDecimal=finalResult;
				     }
			         else
			         {
			        		inputValues=inputValues.concat(" 1");
			        		hexaDecimal=inputValues;
			         }
				}
				else if (inputValues == null || "copynumber".equalsIgnoreCase(scriptParamObj.getAction())) {
					hexaDecimal = inputValues;
					if (actionsList.contains(scriptParamObj.getAction())) {
						testSetLineObj.setScriptUpadated("Y");
					}
				} else if ("paste".equalsIgnoreCase(scriptParamObj.getAction())
						&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequestType())) {
					hexaDecimal = inputValues.replace(inputValues.split(">")[0],
							copyTestrunvo.getNewtestrunname());
				} else if (inputValues.length() > 5) {
					hexaDecimal = inputValues.substring(0, 5) + hexaDecimal;
				} else {
					hexaDecimal = inputValues + hexaDecimal;
				}
				scriptParamObj.setInputValue(hexaDecimal);
			} else {
//				DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
//				Date dateobj = new Date();
//				String covertDateobj = dateformate.format(dateobj);
//				Thread.sleep(1);
//				covertDateobj = covertDateobj.replaceAll("[^0-9]", "");
				if (inputValues == null || "copynumber".equalsIgnoreCase(scriptParamObj.getAction())) {
					scriptParamObj.setInputValue(inputValues);
					if (actionsList.contains(scriptParamObj.getAction())) {
						testSetLineObj.setScriptUpadated("Y");
					}
				} else if ("paste".equalsIgnoreCase(scriptParamObj.getAction())
						&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequestType())) {
					scriptParamObj.setInputValue(
							inputValues.replace(inputValues.split(">")[0], copyTestrunvo.getNewtestrunname()));
				} else if (Constants.VALIDATION_DATATYPE_DATE.equalsIgnoreCase(scriptParamObj.getDataTypes())
						&& Constants.VALIDATION_TYPE_REGULAR_EXPR.equalsIgnoreCase(scriptParamObj.getValidationType())) {
					setDateInputValue(scriptParamObj.getValidationName(), scriptParamObj);

				} else if (Constants.VALIDATION_DATATYPE_DATE.equalsIgnoreCase(scriptParamObj.getDataTypes())
						&& (scriptParamObj.getValidationType() == null || "NA".equalsIgnoreCase(scriptParamObj.getValidationType()))) {
					setDateInputValue("",scriptParamObj);
				}else {
//					scriptParamObj.setInputValue(covertDateobj);
					scriptParamObj.setInputValue(testSetScriptParamObj.getInputValue());
				}
			}
		} else if ("Mandatory".equalsIgnoreCase(scriptParamObj.getUniqueMandatory()) || "Unique".equalsIgnoreCase(scriptParamObj.getUniqueMandatory())
				|| "Both".equalsIgnoreCase(scriptParamObj.getUniqueMandatory())) {
			if (inputValues == null || "copynumber".equalsIgnoreCase(scriptParamObj.getAction())) {
				scriptParamObj.setInputValue(null);
				if (actionsList.contains(scriptParamObj.getAction())) {
					testSetLineObj.setScriptUpadated("Y");
				}

			} else if ("paste".equalsIgnoreCase(scriptParamObj.getAction())
					&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequestType())) {
				scriptParamObj.setInputValue(
						inputValues.replace(inputValues.split(">")[0], copyTestrunvo.getNewtestrunname()));
			} else {
				if (Constants.VALIDATION_DATATYPE_DATE.equalsIgnoreCase(scriptParamObj.getDataTypes())
						&& Constants.VALIDATION_TYPE_REGULAR_EXPR
								.equalsIgnoreCase(scriptParamObj.getValidationType())) {
					setDateInputValue(scriptParamObj.getValidationName(),scriptParamObj);

				} else if (Constants.VALIDATION_DATATYPE_DATE.equalsIgnoreCase(scriptParamObj.getDataTypes()) && (scriptParamObj.getValidationType() == null || "NA".equalsIgnoreCase(scriptParamObj.getValidationType()))) {
					setDateInputValue("",scriptParamObj);
				} else {
					scriptParamObj.setInputValue(testSetScriptParamObj.getInputValue());
				}

			}
		} else {
			if (inputValues == null || "copynumber".equalsIgnoreCase(scriptParamObj.getAction())) {
				scriptParamObj.setInputValue(null);

			} else if ("paste".equalsIgnoreCase(scriptParamObj.getAction())
					&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequestType())) {
				String previousTestRunName = testSetObj.getTestRunName();
				int index=inputValues.indexOf(">");
				if(index!=-1)
				{
					scriptParamObj.setInputValue(inputValues.replace(previousTestRunName, copyTestrunvo.getNewtestrunname()));
				}
				else
				{
					scriptParamObj.setInputValue(testSetScriptParamObj.getInputValue());
				}
			} else {
				
				scriptParamObj.setInputValue(testSetScriptParamObj.getInputValue());
			     }
		}

	}

	private void setDateInputValue(String validationName, TestSetScriptParam scriptParamObj) {
		if(!StringUtils.isBlank(validationName)) {
		try {
			String dateFormat = copyTestrunDao
					.getMeaningUsingValidationName(validationName);
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			scriptParamObj.setInputValue(formatter.format(new Date()));
		} catch (Exception e) {
			logger.info("Exception occurred while converting date Format");
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred while converting the Date Format", e);
		}
		}
		else{
			SimpleDateFormat formatter = new SimpleDateFormat(Constants.MM_dd_yy);
			scriptParamObj.setInputValue(formatter.format(new Date()));
		}
	}

	@Transactional
	public int reRun(@Valid CopytestrunVo copyTestrunvo) throws InterruptedException, JsonMappingException, JsonProcessingException {
		try {
			TestSet getTestrun = copyTestrunDao.getdata(copyTestrunvo.getTestScriptNo());
			logger.info("Test Run Name : " + getTestrun.getTestRunName());
			for (TestSetLine getScriptdata : getTestrun.getTestRunScriptDatalist()) {
				String status = getScriptdata.getStatus();
				if (status.equalsIgnoreCase("fail")) {
					for (TestSetScriptParam getScriptlinedata : getScriptdata.getTestRunScriptParam()) {
						TestSetScriptParam setScriptlinedata = new TestSetScriptParam();
						addInputvalues(getScriptlinedata, setScriptlinedata, copyTestrunvo, getScriptdata);
						getScriptlinedata.setInputValue(setScriptlinedata.getInputValue());
					}
				}
			}
			logger.info("before update");
			int newtestrun = copyTestrunDao.updateTestSetRecord(getTestrun);
			logger.info("New test run " + newtestrun);
			return newtestrun;
		} catch (NullPointerException ne) {
			logger.error("GetTestrun object should not be null" + ne.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GetTestrun object should not be null");
		} catch (Exception e) {
			logger.error("Internal Server Error" + e.getMessage());
			throw new WatsEBSException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
		}
	}

	@Transactional
	public ResponseDto addScriptsOnTestRun(InsertScriptsVO scriptVO) {

		List<String> listOfScriptNumberWrongProductVersion = new ArrayList<>();

		TestSet testSetObj = copyTestrunDao.getdata(scriptVO.getTestSetId());

		Integer maxSeqNum = copyTestrunDao.findMaxSeqNumOfTestRun(scriptVO.getTestSetId());

		String existingProductVersion = copyTestrunDao.findProductVersionByTestSetId(scriptVO.getTestSetId());

		List<TestSetLine> listOfTestSetLinesObj = new ArrayList<>();

		for (Integer lineId : scriptVO.getListOfLineIds()) {

			TestSetLine existLineObj = copyTestrunDao.getLineDtlByTestSetId(lineId);
			Integer testRunId = existLineObj.getTestRun().getTestRunId();

			String newProductVersion = copyTestrunDao.findProductVersionByTestSetId(testRunId);
			String invalidTestRunName = null;
			if (existingProductVersion == null || newProductVersion == null) {
				invalidTestRunName = existingProductVersion == null ? testSetObj.getTestRunName()
						: existLineObj.getTestRun().getTestRunName();
				return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
						MessageUtil.getMessage(messageUtil.getCopyTestRunService().getError().getInvalidProductVersion(), invalidTestRunName));
			} else if (!existingProductVersion.equalsIgnoreCase(newProductVersion)) {
				Integer scriptIdFromMaster = copyTestrunDao.getScriptIdFromMaster(existLineObj.getScriptNumber(),
						existingProductVersion);
				if (scriptIdFromMaster != null) {
					List<ScriptMetaData> scriptMetaDataList = copyTestrunDao.getScriptMetadataInfo(scriptIdFromMaster);
					List<TestSetScriptParam> newTestRunScriptParam = new ArrayList<>();
					Map<String, String> inputParamAndInputValueMap = new HashMap<>();
					existLineObj.getTestRunScriptParam().stream().forEach(
							obj -> inputParamAndInputValueMap.put(obj.getInputParameter(), obj.getInputValue()));
					TestSetLine newLineObj = new TestSetLine();
					for (ScriptMetaData scriptMetaDataObj : scriptMetaDataList) {
						TestSetScriptParam newScriptParamObj = new TestSetScriptParam();
						newScriptParamObj.setTestSetLine(newLineObj);
						newScriptParamObj.setInputParameter(scriptMetaDataObj.getInputParameter());
						newScriptParamObj
								.setInputValue(inputParamAndInputValueMap.get(scriptMetaDataObj.getInputParameter()));
						newScriptParamObj.setScriptId(scriptMetaDataObj.getScriptMaster().getScriptId());
						newScriptParamObj.setScriptNumber(scriptMetaDataObj.getScriptNumber());
						newScriptParamObj.setLineNumber(scriptMetaDataObj.getLineNumber());
						newScriptParamObj.setAction(scriptMetaDataObj.getAction());
						newScriptParamObj.setTestRunParamDesc(scriptMetaDataObj.getStepDesc());
						newScriptParamObj.setMetadataId(scriptMetaDataObj.getScriptMetaDataId());
						newScriptParamObj.setHint(scriptMetaDataObj.getHint());
						newScriptParamObj.setFieldType(scriptMetaDataObj.getFieldType());
						newScriptParamObj.setCreatedBy(scriptMetaDataObj.getCreatedBy());
						newScriptParamObj.setCreationDate(scriptMetaDataObj.getCreationDate());
						newScriptParamObj.setUpdateDate(null);
						newScriptParamObj.setLastUpdatedBy(null);
						newScriptParamObj.setLineExecutionStatus(NEW);
						newScriptParamObj.setLineErrorMessage(null);
						newScriptParamObj.setDataTypes(scriptMetaDataObj.getDatatypes());
						newScriptParamObj.setUniqueMandatory(scriptMetaDataObj.getUniqueMandatory());
						newScriptParamObj.setValidationType(scriptMetaDataObj.getValidationType());
						newScriptParamObj.setValidationName(scriptMetaDataObj.getValidationName());
						newTestRunScriptParam.add(newScriptParamObj);
					}
					newLineObj.setScriptId(scriptIdFromMaster);
					newLineObj.setCreatedBy(existLineObj.getCreatedBy());
					newLineObj.setCreationDate(existLineObj.getCreationDate());
					newLineObj.setEnabled(existLineObj.getEnabled());
					newLineObj.setScriptNumber(existLineObj.getScriptNumber());
					maxSeqNum += scriptVO.getIncrementalValue();
					newLineObj.setSeqNum(maxSeqNum);
					newLineObj.setStatus(NEW);
					newLineObj.setLastUpdatedBy(null);
					newLineObj.setScriptUpadated("N");
					newLineObj.setUpdateDate(null);
					newLineObj.setTestRunScriptPath(existLineObj.getTestRunScriptPath());
					newLineObj.setExecutedBy(null);
					newLineObj.setExecutionStartTime(null);
					newLineObj.setExecutionEndTime(null);
					newLineObj.setTestRunScriptParam(newTestRunScriptParam);
					newLineObj.setTestRun(testSetObj);
					listOfTestSetLinesObj.add(newLineObj);
				} else {
					String customType = copyTestrunDao
							.getScriptMasterInfo(existLineObj.getScriptNumber(), newProductVersion).getStandardCustom();
					if ("CUSTOM".equalsIgnoreCase(customType)) {
						listOfScriptNumberWrongProductVersion.add(existLineObj.getScriptNumber());
					}
				}

			} else {
				List<TestSetScriptParam> existScriptParamList = existLineObj.getTestRunScriptParam();
				Comparator<TestSetScriptParam> scriptLineComparator = (TestSetScriptParam s1,
						TestSetScriptParam s2) -> s1.getLineNumber() - s2.getLineNumber();
				Collections.sort(existScriptParamList, scriptLineComparator);
				List<TestSetScriptParam> newTestRunScriptParam = new ArrayList<>();
				TestSetLine newLineObj = new TestSetLine();
				for (TestSetScriptParam existScriptParamObj : existLineObj.getTestRunScriptParam()) {

					TestSetScriptParam newScriptParamObj = new TestSetScriptParam();
					newScriptParamObj.setTestSetLine(newLineObj);
					newScriptParamObj.setInputParameter(existScriptParamObj.getInputParameter());
					newScriptParamObj.setInputValue(existScriptParamObj.getInputValue());
					newScriptParamObj.setScriptId(existScriptParamObj.getScriptId());
					newScriptParamObj.setScriptNumber(existScriptParamObj.getScriptNumber());
					newScriptParamObj.setLineNumber(existScriptParamObj.getLineNumber());
					newScriptParamObj.setAction(existScriptParamObj.getAction());
					newScriptParamObj.setTestRunParamDesc(existScriptParamObj.getTestRunParamDesc());
					newScriptParamObj.setMetadataId(existScriptParamObj.getMetadataId());
					newScriptParamObj.setHint(existScriptParamObj.getHint());
					newScriptParamObj.setFieldType(existScriptParamObj.getFieldType());
					newScriptParamObj.setXpathLocation(existScriptParamObj.getXpathLocation());
					newScriptParamObj.setXpathLocation1(existScriptParamObj.getXpathLocation1());
					newScriptParamObj.setCreatedBy(existScriptParamObj.getCreatedBy());
					newScriptParamObj.setCreationDate(existScriptParamObj.getCreationDate());
					newScriptParamObj.setUpdateDate(null);
					newScriptParamObj.setLastUpdatedBy(null);
					newScriptParamObj.setLineExecutionStatus(NEW);
					newScriptParamObj.setLineErrorMessage(null);
					newScriptParamObj.setDataTypes(existScriptParamObj.getDataTypes());
					newScriptParamObj.setUniqueMandatory(existScriptParamObj.getUniqueMandatory());
					newScriptParamObj.setValidationType(existScriptParamObj.getValidationType());
					newScriptParamObj.setValidationName(existScriptParamObj.getValidationName());
					newTestRunScriptParam.add(newScriptParamObj);
				}
				newLineObj.setScriptId(existLineObj.getScriptId());
				newLineObj.setCreatedBy(existLineObj.getCreatedBy());
				newLineObj.setCreationDate(existLineObj.getCreationDate());
				newLineObj.setEnabled(existLineObj.getEnabled());
				newLineObj.setScriptNumber(existLineObj.getScriptNumber());
				maxSeqNum += scriptVO.getIncrementalValue();
				newLineObj.setSeqNum(maxSeqNum);
				newLineObj.setStatus(NEW);
				newLineObj.setLastUpdatedBy(null);
				newLineObj.setScriptUpadated("N");
				newLineObj.setUpdateDate(null);
				newLineObj.setTestRunScriptPath(existLineObj.getTestRunScriptPath());
				newLineObj.setExecutedBy(null);
				newLineObj.setExecutionStartTime(null);
				newLineObj.setExecutionEndTime(null);
				newLineObj.setTestRunScriptParam(newTestRunScriptParam);
				newLineObj.setTestRun(testSetObj);
				listOfTestSetLinesObj.add(newLineObj);
			}
		}
		for (TestSetLine testSetLineObj : listOfTestSetLinesObj) {
			copyTestrunDao.updatelinesRecord(testSetLineObj);
		}
		if(listOfTestSetLinesObj.isEmpty()) {
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR,
					MessageUtil.getMessage(messageUtil.getCopyTestRunService().getError().getInvalidScript()));
			
		}
		
		if (listOfScriptNumberWrongProductVersion.isEmpty()) {
			return new ResponseDto(HttpStatus.OK.value(), Constants.SUCCESS,
					messageUtil.getCopyTestRunService().getSuccess().getScriptAdded());
		} else {
			String msg = listOfScriptNumberWrongProductVersion.toString().replace("[", "").replace("]", "");
			return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.ERROR, MessageUtil
					.getMessage(messageUtil.getCopyTestRunService().getError().getProductVersionMissing(), existingProductVersion, msg));
		}

	}
	
	public String incrementValueForApi(String str) {
//		String str="123xvcf22";
        String temp=str.substring(str.length()-3);
        String s=str.substring(0,str.length()-3);
        String firstIndex=temp.substring(0,1);
        String middleIndex=temp.substring(1,2);
        String lastIndex=str.substring(str.length()-1);
        if((lastIndex.toLowerCase()).matches(".*[a-z].*") || !(temp.toLowerCase()).matches(".*[0-9].*"))
        {
            str=str.concat("111");
        }
        else if((temp.toLowerCase()).matches(".*[0-9].*") && !(temp.toLowerCase()).matches(".*[a-z].*")) {
            int increment=Integer.parseInt(temp)+1;
            str=s.concat(String.valueOf(increment));
        }
        else if((firstIndex.toLowerCase()).matches(".*[a-z].*") && (temp.toLowerCase()).matches(".*[0-9].*") && (middleIndex.toLowerCase()).matches(".*[0-9].*"))
        {
             str=str.concat("1");
        }
      else if((lastIndex.toLowerCase()).matches(".*[0-9].*") && (firstIndex.toLowerCase()).matches(".*[a-z].*") || (firstIndex.toLowerCase()).matches(".*[0-9].*"))
        {
            str=str.concat("11");
        }
        System.out.println(" \n Final String "+ str);
		return str;
	}

}