package com.winfo.services;

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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.CopyTestRunDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.utils.Constants;
import com.winfo.vo.CopytestrunVo;
import com.winfo.vo.InsertScriptsVO;
import com.winfo.vo.ResponseDto;

@Service
public class CopyTestRunService {
	Logger log = Logger.getLogger("Logger");
	
	private static final String NEW = "New";

	@Autowired
	CopyTestRunDao copyTestrunDao;

	@Transactional
	public int copyTestrun(@Valid CopytestrunVo copyTestrunvo) throws InterruptedException {
		TestSet testSetObj = copyTestrunDao.getdata(copyTestrunvo.getTestScriptNo());
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

		String productVersion = copyTestrunDao.getProductVersion(testSetObj.getProjectId());
		Map<Integer, Integer> mapOfTestRunDependencyOldToNewId = new HashMap<Integer, Integer>();
		for (TestSetLine testSetLineObj : testSetObj.getTestRunScriptDatalist()) {// getScriptdata

			ScriptMaster scriptMaster = copyTestrunDao.getScriptMasterInfo(testSetLineObj.getScriptNumber(),
					productVersion);
			TestSetLine testSetLineRecords = new TestSetLine();
			if (scriptMaster != null) {
				testSetLineRecords.setScriptId(scriptMaster.getScript_id());
				testSetLineRecords.setCreatedBy(copyTestrunvo.getCreatedBy());
				testSetLineRecords.setCreationDate(copyTestrunvo.getCreationDate());
				testSetLineRecords.setEnabled("Y");
				testSetLineRecords.setScriptNumber(scriptMaster.getScript_number());
				testSetLineRecords.setSeqNum(testSetLineObj.getSeqNum());
				testSetLineRecords.setStatus(NEW);
				testSetLineRecords.setLastUpdatedBy(null);
				testSetLineRecords.setScriptUpadated("N");
				testSetLineRecords.setUpdateDate(null);
				testSetLineRecords.setTestRunScriptPath(testSetLineObj.getTestRunScriptPath());
				testSetLineRecords.setExecutedBy(null);
				testSetLineRecords.setExecutionStartTime(null);
				testSetLineRecords.setExecutionEndTime(null);
//				testSetLineRecords.setDependency_tr(testSetLineObj.getDependency_tr());
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
					ScriptMetaData s2) -> s1.getLine_number() - s2.getLine_number();
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
					setScriptlinedata.setInputParameter(metadata.getInput_parameter());
					setScriptlinedata.setScriptId(testSetLineRecords.getScriptId());
					setScriptlinedata.setScriptNumber(testSetLineRecords.getScriptNumber());
					setScriptlinedata.setLineNumber(metadata.getLine_number());
					setScriptlinedata.setAction(metadata.getAction());
					setScriptlinedata.setTestRunParamDesc(metadata.getStep_desc());
					setScriptlinedata.setMetadataId(metadata.getScript_meta_data_id());
					setScriptlinedata.setHint(metadata.getHint());
					setScriptlinedata.setFieldType(metadata.getField_type());
					setScriptlinedata.setXpathLocation(metadata.getXpath_location());
					setScriptlinedata.setXpathLocation1(metadata.getXpath_location1());
					setScriptlinedata.setCreatedBy(copyTestrunvo.getCreatedBy());
					setScriptlinedata.setCreationDate(copyTestrunvo.getCreationDate());
					setScriptlinedata.setUpdateDate(null);
					setScriptlinedata.setLastUpdatedBy(null);
					setScriptlinedata.setLineExecutionStatus(NEW);
					setScriptlinedata.setLineErrorMessage(null);
					setScriptlinedata.setDataTypes(metadata.getDatatypes());
					setScriptlinedata.setUniqueMandatory(metadata.getUnique_mandatory());
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
				setScriptlinedata.setInputParameter(metadata.getInput_parameter());
				setScriptlinedata.setScriptId(testSetLineRecords.getScriptId());
				setScriptlinedata.setScriptNumber(testSetLineRecords.getScriptNumber());
				setScriptlinedata.setLineNumber(metadata.getLine_number());
				setScriptlinedata.setAction(metadata.getAction());

				setScriptlinedata.setMetadataId(metadata.getScript_meta_data_id());
				setScriptlinedata.setHint(metadata.getHint());
				setScriptlinedata.setFieldType(metadata.getField_type());
				setScriptlinedata.setXpathLocation(metadata.getXpath_location());
				setScriptlinedata.setXpathLocation1(metadata.getXpath_location1());
				setScriptlinedata.setCreatedBy(copyTestrunvo.getCreatedBy());
				setScriptlinedata.setCreationDate(copyTestrunvo.getCreationDate());
				setScriptlinedata.setUpdateDate(null);
				setScriptlinedata.setLastUpdatedBy(null);
				setScriptlinedata.setLineExecutionStatus(NEW);
				setScriptlinedata.setLineErrorMessage(null);
				setScriptlinedata.setDataTypes(metadata.getDatatypes());
				setScriptlinedata.setUniqueMandatory(metadata.getUnique_mandatory());

				setScriptlinedata.setInputValue(null);

				testSetLineRecords.addTestScriptParam(setScriptlinedata);

				newScriptParamSeq++;

			}

		}
		log.info("before saveTestrun");
		if (!mapOfTestRunDependencyOldToNewId.isEmpty()) {
			for (TestSetLine testSetLine : newTestSetObj.getTestRunScriptDatalist()) {
				if (testSetLine.getDependency_tr() != null) {
					testSetLine.setDependency_tr(mapOfTestRunDependencyOldToNewId.get(testSetLine.getDependency_tr()));
				}
			}
		}
		int newtestrun = copyTestrunDao.saveTestrun(newTestSetObj);
		log.info("newtestrun 1:" + newtestrun);
		return newtestrun;
	}

	private void addInputvalues(TestSetScriptParam getScriptlinedata, TestSetScriptParam setScriptlinedata,
			CopytestrunVo copyTestrunvo, TestSetLine setScriptdata) throws InterruptedException {
		String getInputvalues = getScriptlinedata.getInputValue();
		String[] actios = { "clearandtype", "textarea", "selectAValue", "clickCheckbox", "selectByText",
				"clickButton Dropdown", "clickLinkAction", "Table Dropdown Values", "Table SendKeys", "enterIntoTable",
				"SendKeys", "Login into Application", "Dropdown Values", "typeAtPosition", "clickAndTypeAtPosition",
				"clickRadiobutton", "clickCheckbox", "multipleSendKeys", "multiplelinestableSendKeys", "DatePicker",
				"copynumber", "copytext", "paste" };
		List<String> actionsList = new ArrayList<>(Arrays.asList(actios));
		if ("y".equalsIgnoreCase(copyTestrunvo.getIncrementValue())
				&& (setScriptlinedata.getUniqueMandatory() != null && setScriptlinedata.getUniqueMandatory() != "NA")
				&& (setScriptlinedata.getUniqueMandatory().equalsIgnoreCase("Unique")
						|| setScriptlinedata.getUniqueMandatory().equalsIgnoreCase("Both"))) {
			if ((setScriptlinedata.getDataTypes() != null && setScriptlinedata.getDataTypes() != "NA")
					&& setScriptlinedata.getDataTypes().equalsIgnoreCase("Alpha-Numeric")) {
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
				Date dateobj = new Date();
				String covertDateobj = dateformate.format(dateobj);
				Thread.sleep(1);
				covertDateobj = covertDateobj.replaceAll("[^0-9]", "");
				int fistOff = Integer.parseInt(covertDateobj.substring(0, 8));
				int secondHalf = Integer.parseInt(covertDateobj.substring(8, 15));
				String hexaDecimal = Integer.toString(fistOff, 36) + Integer.toString(secondHalf, 36);
				if (getInputvalues == null || "copynumber".equalsIgnoreCase(setScriptlinedata.getAction())) {
					hexaDecimal = getInputvalues;
					if (actionsList.contains(setScriptlinedata.getAction())) {
						setScriptdata.setScriptUpadated("Y");
					}
				} else if ("paste".equalsIgnoreCase(setScriptlinedata.getAction())
						&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequestType())) {
					hexaDecimal = getInputvalues.replace(getInputvalues.split(">")[0],
							copyTestrunvo.getNewtestrunname());
				} else if (getInputvalues.length() > 5) {
					hexaDecimal = getInputvalues.substring(0, 5) + hexaDecimal;
				} else {
					hexaDecimal = getInputvalues + hexaDecimal;
				}
				setScriptlinedata.setInputValue(hexaDecimal);
			} else {
				DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
				Date dateobj = new Date();
				String covertDateobj = dateformate.format(dateobj);
				Thread.sleep(1);
				covertDateobj = covertDateobj.replaceAll("[^0-9]", "");
				if (getInputvalues == null || "copynumber".equalsIgnoreCase(setScriptlinedata.getAction())) {
					setScriptlinedata.setInputValue(getInputvalues);
					if (actionsList.contains(setScriptlinedata.getAction())) {
						setScriptdata.setScriptUpadated("Y");
					}
				} else if ("paste".equalsIgnoreCase(setScriptlinedata.getAction())
						&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequestType())) {
					setScriptlinedata.setInputValue(
							getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname()));
				} else {
					setScriptlinedata.setInputValue(covertDateobj);
				}
			}
		} else if ("Mandatory".equalsIgnoreCase(setScriptlinedata.getUniqueMandatory())) {
			if (getInputvalues == null || "copynumber".equalsIgnoreCase(setScriptlinedata.getAction())) {
				setScriptlinedata.setInputValue(null);
				if (actionsList.contains(setScriptlinedata.getAction())) {
					setScriptdata.setScriptUpadated("Y");
				}

			} else if ("paste".equalsIgnoreCase(setScriptlinedata.getAction())
					&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequestType())) {
				setScriptlinedata.setInputValue(
						getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname()));
			} else {
				setScriptlinedata.setInputValue(getScriptlinedata.getInputValue());
			}
		} else {
			if (getInputvalues == null || "copynumber".equalsIgnoreCase(setScriptlinedata.getAction())) {
				setScriptlinedata.setInputValue(null);

			} else if ("paste".equalsIgnoreCase(setScriptlinedata.getAction())
					&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequestType())) {
				setScriptlinedata.setInputValue(
						getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname()));
			} else {
				setScriptlinedata.setInputValue(getScriptlinedata.getInputValue());
			}
		}

	}

	@Transactional
	public int reRun(@Valid CopytestrunVo copyTestrunvo) throws InterruptedException {
		TestSet getTestrun = copyTestrunDao.getdata(copyTestrunvo.getTestScriptNo());
		log.info("getTestrun infromation");
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
		log.info("before update");
		int newtestrun = copyTestrunDao.updateTestSetRecord(getTestrun);
		log.info("newtestrun 2:" + newtestrun);
		return newtestrun;
	}

	@Transactional
	public ResponseDto addScriptsOnTestRun(InsertScriptsVO scriptVO) {

		ResponseDto response = new ResponseDto();

		TestSet testSetObj = copyTestrunDao.getdata(scriptVO.getTestSetId());

		Integer maxSeqNum = copyTestrunDao.findMaxSeqNumOfTestRun(scriptVO.getTestSetId());

		for (Integer lineId : scriptVO.getListOfLineIds()) {

			TestSetLine existLineObj = copyTestrunDao.getLineDtlByTestSetId(lineId);

			List<TestSetScriptParam> existScriptParamList = existLineObj.getTestRunScriptParam();

			Comparator<TestSetScriptParam> scriptLineComparator = (TestSetScriptParam s1,
					TestSetScriptParam s2) -> s1.getLineNumber() - s2.getLineNumber();
			Collections.sort(existScriptParamList, scriptLineComparator);

			List<TestSetScriptParam> newTestRunScriptParam = new ArrayList<>();

			TestSetLine newLineObj = new TestSetLine();

			for (TestSetScriptParam existScriptParamObj : existLineObj.getTestRunScriptParam()) {

				TestSetScriptParam newScriptParamObj = new TestSetScriptParam();
				newScriptParamObj.setTestRunScripts(newLineObj);
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
				newTestRunScriptParam.add(newScriptParamObj);
			}
			newLineObj.setScriptId(existLineObj.getScriptId());
			newLineObj.setCreatedBy(existLineObj.getCreatedBy());
			newLineObj.setCreationDate(existLineObj.getCreationDate());
			newLineObj.setEnabled(existLineObj.getEnabled());
			newLineObj.setScriptNumber(existLineObj.getScriptNumber());
			newLineObj.setSeqNum(maxSeqNum + scriptVO.getIncrementalValue());
			maxSeqNum++;
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
			copyTestrunDao.updatelinesRecord(newLineObj);
		}
		response.setStatusCode(200);
		response.setStatusDescr(Constants.SCRIPT_UPDATE_MSG);
		response.setStatusMessage(Constants.SUCCESS);
		return response;

	}

}