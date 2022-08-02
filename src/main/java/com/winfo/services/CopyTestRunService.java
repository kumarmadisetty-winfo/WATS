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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.CopyTestRunDao2;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.vo.CopytestrunVo;

@Service
public class CopyTestRunService {
	@Autowired
	CopyTestRunDao2 copyTestrunDao;

	@Transactional
	public int copyTestrun(@Valid CopytestrunVo copyTestrunvo) throws InterruptedException {
		TestSet testSetObj = copyTestrunDao.getdata(copyTestrunvo.getTestScriptNo());
		testSetObj.setEnabled("Y");
		testSetObj.setTestRunName(copyTestrunvo.getNewtestrunname());
		testSetObj.setConfigurationId(copyTestrunvo.getConfiguration());
		testSetObj.setProjectId(copyTestrunvo.getProject());
		testSetObj.setCreatedBy(copyTestrunvo.getCreated_by());
		testSetObj.setLastUpdatedBy(null);
		testSetObj.setCreationDate(copyTestrunvo.getCreation_date());
		testSetObj.setUpdateDate(null);
		testSetObj.setTsCompleteFlag("Active");
		testSetObj.setTestRunMode("ACTIVE");
		testSetObj.setLastExecutBy(null);
		String productVersion = copyTestrunDao.getProductVersion(testSetObj.getProjectId());
		Map<Integer, Integer> mapOfTestRunDependencyOldToNewId = new HashMap<Integer, Integer>();
		for (TestSetLine testSetLineObj : testSetObj.getTestRunScriptDatalist()) {// getScriptdata

			ScriptMaster scriptMaster = copyTestrunDao.getScriptMasterInfo(testSetLineObj.getScriptNumber(),
					productVersion);
			TestSetLine testSetLineRecords = new TestSetLine();
			if (scriptMaster != null) {
				// setScriptdata.setTestsetlineid(sectiptid);
				int id = copyTestrunDao.getscrtiptIds();
//				if (getScriptdata.getDependency_tr() != null) {
				mapOfTestRunDependencyOldToNewId.put(testSetLineObj.getTestRunScriptId(), id);
//				}
				testSetLineRecords.setTestRunScriptId(id);
				testSetLineRecords.setScriptId(scriptMaster.getScript_id());
				testSetLineRecords.setCreatedBy(copyTestrunvo.getCreated_by());
				testSetLineRecords.setCreationDate(copyTestrunvo.getCreation_date());
				testSetLineRecords.setEnabled("Y");
				// setScriptdata.setScriptnumber(getScriptdata.getScriptnumber());
				// setScriptdata.setScriptnumber((String)obj[1]);
				testSetLineRecords.setScriptNumber(scriptMaster.getScript_number());
				// setScriptdata.setSeqnum(getScriptdata.getSeqnum());
				testSetLineRecords.setSeqNum(testSetLineObj.getSeqNum());
				testSetLineRecords.setStatus("New");
				testSetLineRecords.setLastUpdatedBy(null);
				testSetLineRecords.setScriptUpadated("N");
				testSetLineRecords.setUpdateDate(null);
				testSetLineRecords.setTestRunScriptPath(testSetLineObj.getTestRunScriptPath());
				testSetLineRecords.setExecutedBy(null);
				testSetLineRecords.setExecutionStartTime(null);
				testSetLineRecords.setExecutionEndTime(null);
				testSetLineRecords.setDependency_tr(testSetLineObj.getDependency_tr());
//				System.out.println("id :" + copyTestrunDao.getIds());
				testSetObj.addTestRunScriptData(testSetLineRecords);
			} else {

				continue;
			}
			Integer newScriptParamSeq = 0;
			Integer oldScriptParamSeq = 0;
			// List<ScriptMetaData>metadataList=copyTestrunDao.getScriptMetadataInfo(setScriptdata.getScriptid());
			List<ScriptMetaData> metadataList = scriptMaster.getScriptMetaDatalist();
			/*
			 * for(ScritplinesData getScriptlinedata:getScriptdata.getScriptslinedata()) {
			 * 
			 * ScritplinesData setScriptlinedata=new ScritplinesData(); int
			 * sectiptlineid=copyTestrunDao.getscrtiptlineIds();
			 * System.out.println("sectiptlineid"+sectiptlineid);
			 * 
			 * setScriptlinedata.setTestscriptperamid(sectiptlineid);
			 * System.out.println(getScriptlinedata.getInput_parameter());
			 * addInputvalues(getScriptlinedata,setScriptlinedata,
			 * copyTestrunvo,setScriptdata);
			 * 
			 * setScriptlinedata.setInput_parameter(getScriptlinedata.getInput_parameter());
			 * setScriptlinedata.setScript_id(getScriptlinedata.getScript_id());
			 * setScriptlinedata.setScript_number(getScriptlinedata.getScript_number());
			 * setScriptlinedata.setLine_number(getScriptlinedata.getLine_number());
			 * setScriptlinedata.setAction(getScriptlinedata.getAction());
			 * setScriptlinedata.setTest_run_param_desc(getScriptlinedata.
			 * getTest_run_param_desc());
			 * setScriptlinedata.setTest_run_param_name(getScriptlinedata.
			 * getTest_run_param_name());
			 * setScriptlinedata.setMetadata_id(getScriptlinedata.getMetadata_id());
			 * setScriptlinedata.setHint(getScriptlinedata.getHint());
			 * setScriptlinedata.setField_type(getScriptlinedata.getField_type());
			 * setScriptlinedata.setXpathlocation(getScriptlinedata.getXpathlocation());
			 * setScriptlinedata.setXpathlocation1(getScriptlinedata.getXpathlocation1());
			 * setScriptlinedata.setCreatedby(copyTestrunvo.getCreated_by());
			 * setScriptlinedata.setCreationdate(copyTestrunvo.getCreation_date());
			 * setScriptlinedata.setUpdateddate(null);
			 * setScriptlinedata.setLastupdatedby(null);
			 * setScriptlinedata.setLineexecutionstatues("New");
			 * setScriptlinedata.setLineerrormessage(null);
			 * setScriptlinedata.setDatatypes(getScriptlinedata.getDatatypes());
			 * setScriptlinedata.setUniquemandatory(getScriptlinedata.getUniquemandatory());
			 * 
			 * setScriptdata.addScriptlines(setScriptlinedata); }
			 */
			List<TestSetScriptParam> scriptLineList = testSetLineObj.getTestRunScriptParam();
			Comparator<TestSetScriptParam> scriptLineComparator = (TestSetScriptParam s1,
					TestSetScriptParam s2) -> s1.getLineNumber() - s2.getLineNumber();
			Comparator<ScriptMetaData> metaDataComparator = (ScriptMetaData s1,
					ScriptMetaData s2) -> s1.getLine_number() - s2.getLine_number();
			Collections.sort(scriptLineList, scriptLineComparator);
			Collections.sort(metadataList, metaDataComparator);
			// int oldScriptSeqHolder=0;
			// int newScriptSeqHolder=0;
			ScriptMetaData metadata = null;
			Integer check = null;
			TestSetScriptParam getScriptlinedata = null;
			TestSetScriptParam setScriptlinedata = null;
			// for (iterate on script of new Product version )
			while (newScriptParamSeq < metadataList.size() && oldScriptParamSeq < scriptLineList.size()) {
				metadata = metadataList.get(newScriptParamSeq);

				getScriptlinedata = scriptLineList.get(oldScriptParamSeq);

				if (!(newScriptParamSeq.equals(check))) {
					setScriptlinedata = new TestSetScriptParam();
					setScriptlinedata.setInputParameter(metadata.getInput_parameter());
					setScriptlinedata.setScriptId(testSetLineRecords.getScriptId());
					setScriptlinedata.setScriptNumber(testSetLineRecords.getScriptNumber());
					// setScriptlinedata.setLine_number((Integer)metadata[3]);
					setScriptlinedata.setLineNumber(metadata.getLine_number());
					// setScriptlinedata.setAction((String)metadata[5]);
					setScriptlinedata.setAction(metadata.getAction());
					setScriptlinedata.setTestRunParamDesc(metadata.getStep_desc());
					// setScriptlinedata.setTest_run_param_name(getScriptlinedata.getTest_run_param_name());
					// setScriptlinedata.setMetadata_id((Integer)metadata[0]);
					// setScriptlinedata.setHint((String)metadata[14]);
					// setScriptlinedata.setField_type((String)metadata[13]);
					// setScriptlinedata.setXpathlocation((String)metadata[6]);
					// setScriptlinedata.setXpathlocation1((String)metadata[7]);
					setScriptlinedata.setMetadataId(metadata.getScript_meta_data_id());
					setScriptlinedata.setHint(metadata.getHint());
					setScriptlinedata.setFieldType(metadata.getField_type());
					setScriptlinedata.setXpathLocation(metadata.getXpath_location());
					setScriptlinedata.setXpathLocation1(metadata.getXpath_location1());
					setScriptlinedata.setCreatedBy(copyTestrunvo.getCreated_by());
					setScriptlinedata.setCreationDate(copyTestrunvo.getCreation_date());
					setScriptlinedata.setUpdateDate(null);
					setScriptlinedata.setLastUpdatedBy(null);
					setScriptlinedata.setLineExecutionStatus("New");
					setScriptlinedata.setLineErrorMessage(null);
					setScriptlinedata.setDataTypes(metadata.getDatatypes());
					setScriptlinedata.setUniqueMandatory(metadata.getUnique_mandatory());
					check = newScriptParamSeq.intValue();
				}
				if (setScriptlinedata.getInputParameter() != null && getScriptlinedata.getInputParameter() != null) {
					if (setScriptlinedata.getAction().equalsIgnoreCase(getScriptlinedata.getAction())
							&& setScriptlinedata.getInputParameter()
									.equalsIgnoreCase(getScriptlinedata.getInputParameter())
							&& setScriptlinedata.getLineNumber() == getScriptlinedata.getLineNumber()) {
						// setScriptlinedata.setInput_value(getScriptlinedata.getInput_value());
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

				// addInputvalues(getScriptlinedata,setScriptlinedata,
				// copyTestrunvo,setScriptdata);

				testSetLineRecords.addTestScriptParam(setScriptlinedata);

			}
			newScriptParamSeq++;
			while (newScriptParamSeq < metadataList.size()) {
				metadata = metadataList.get(newScriptParamSeq);
				setScriptlinedata = new TestSetScriptParam();
				// int sectiptlineid=copyTestrunDao.getscrtiptlineIds();
				// System.out.println("sectiptlineid"+sectiptlineid);
				// setScriptlinedata.setTestscriptperamid(sectiptlineid);
				setScriptlinedata.setInputParameter(metadata.getInput_parameter());
				setScriptlinedata.setScriptId(testSetLineRecords.getScriptId());
				setScriptlinedata.setScriptNumber(testSetLineRecords.getScriptNumber());
				// setScriptlinedata.setLine_number((Integer)metadata[3]);
				setScriptlinedata.setLineNumber(metadata.getLine_number());
				// setScriptlinedata.setAction((String)metadata[5]);
				setScriptlinedata.setAction(metadata.getAction());

				setScriptlinedata.setMetadataId(metadata.getScript_meta_data_id());
				setScriptlinedata.setHint(metadata.getHint());
				setScriptlinedata.setFieldType(metadata.getField_type());
				setScriptlinedata.setXpathLocation(metadata.getXpath_location());
				setScriptlinedata.setXpathLocation1(metadata.getXpath_location1());
				setScriptlinedata.setCreatedBy(copyTestrunvo.getCreated_by());
				setScriptlinedata.setCreationDate(copyTestrunvo.getCreation_date());
				setScriptlinedata.setUpdateDate(null);
				setScriptlinedata.setLastUpdatedBy(null);
				setScriptlinedata.setLineExecutionStatus("New");
				setScriptlinedata.setLineErrorMessage(null);
				setScriptlinedata.setDataTypes(metadata.getDatatypes());
				setScriptlinedata.setUniqueMandatory(metadata.getUnique_mandatory());

				setScriptlinedata.setInputValue(null);

				testSetLineRecords.addTestScriptParam(setScriptlinedata);

				newScriptParamSeq++;

			}

		}

//		setTestrundata.setScriptsdata(listsScriptdata);
		System.out.println("before saveTestrun");
		if (!mapOfTestRunDependencyOldToNewId.isEmpty()) {
			for (TestSetLine testSetLine : testSetObj.getTestRunScriptDatalist()) {
				if (testSetLine.getDependency_tr() != null) {
					testSetLine.setDependency_tr(mapOfTestRunDependencyOldToNewId.get(testSetLine.getDependency_tr()));
				}
			}
		}
		int newtestrun = copyTestrunDao.saveTestrun(testSetObj);
		System.out.println("newtestrun 1:" + newtestrun);
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
		if ("y".equalsIgnoreCase(copyTestrunvo.getIncrement_value())
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
						&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
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
						&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
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
					&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
				setScriptlinedata.setInputValue(
						getInputvalues.replace(getInputvalues.split(">")[0], copyTestrunvo.getNewtestrunname()));
			} else {
				setScriptlinedata.setInputValue(getScriptlinedata.getInputValue());
			}
		} else {
			if (getInputvalues == null || "copynumber".equalsIgnoreCase(setScriptlinedata.getAction())) {
				setScriptlinedata.setInputValue(null);

			} else if ("paste".equalsIgnoreCase(setScriptlinedata.getAction())
					&& "copyTestRun".equalsIgnoreCase(copyTestrunvo.getRequesttype())) {
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
		System.out.println("getTestrun infromation");
		for (TestSetLine getScriptdata : getTestrun.getTestRunScriptDatalist()) {
			String status = getScriptdata.getStatus();
			if (status.equalsIgnoreCase("fail")) {
//				getTestrun.addScriptsdata(getScriptdata);
				for (TestSetScriptParam getScriptlinedata : getScriptdata.getTestRunScriptParam()) {
					TestSetScriptParam setScriptlinedata = new TestSetScriptParam();
					addInputvalues(getScriptlinedata, setScriptlinedata, copyTestrunvo, getScriptdata);
					getScriptlinedata.setInputValue(setScriptlinedata.getInputValue());
//				getScriptdata.addScriptlines(getScriptlinedata);
				}
			}
		}
		System.out.println("before update");
		int newtestrun = copyTestrunDao.update(getTestrun);
		System.out.println("newtestrun 2:" + newtestrun);
		return newtestrun;
	}

}