package com.winfo.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.WatsPluginDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.TestSet;
import com.winfo.model.TestSetLine;
import com.winfo.model.TestSetScriptParam;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsPluginMasterVO;
import com.winfo.vo.WatsPluginMetaDataVO;

@Service
public class PluginTestrunService {
	
	Logger log = Logger.getLogger("Logger");

	@Autowired
	WatsPluginDao dao;

	@Transactional
	public DomGenericResponseBean updateTestrun(WatsPluginMasterVO mastervo) {
		TestSetLine setScriptdata = new TestSetLine();

		String module = mastervo.getModule();
		String processArea = mastervo.getProcessArea();
		List<String> scriptNumbers = dao.getScriptNumber(processArea, module);

		String newmodule = mastervo.getModuleSrt();

		String newScriptNumber = null;
		ArrayList<Integer> slist = new ArrayList<Integer>();
		if (scriptNumbers != null) {
			for (String snumber : scriptNumbers) {
				Integer i = Integer.parseInt(snumber.replaceAll("[\\D]", ""));

				slist.add(i);
			}
			int max = Collections.max(slist);
			int snum = max + 1;
			newScriptNumber = processArea + "." + newmodule + "." + snum;
			System.out.println(newScriptNumber);

		} else {
			newScriptNumber = processArea + "." + newmodule + "." + "1";
		}

		ScriptMaster master = new ScriptMaster();
		int masterScriptId = dao.getMasterScriptId();
		master.setScriptId(masterScriptId);
		master.setModule(mastervo.getModule());
		master.setScenarioName(mastervo.getScenarioName());
		master.setScenarioDescription(mastervo.getScenarioDescription());
		master.setProductVersion(mastervo.getProductVersion());
		master.setPriority(mastervo.getPriority());
		master.setProcessArea(mastervo.getProcessArea());
		master.setRole(mastervo.getRole());
		master.setScriptNumber(newScriptNumber);
		master.setSubProcessArea(mastervo.getSubProcessArea());
		master.setStandardCustom(mastervo.getStandardCustom());
		master.setTestScriptStatus(mastervo.getTestScriptStatus());
		master.setCreatedBy(mastervo.getCreatedBy());
		master.setCreationDate(java.sql.Date.valueOf(mastervo.getCreationDate()));

		for (WatsPluginMetaDataVO metadatavo : mastervo.getMetaDataList()) {
			ScriptMetaData metadata = new ScriptMetaData();
			int metaDataId = dao.getMetaDataId();
			metadata.setScriptMetaDataId(metaDataId);
			metadata.setAction(metadatavo.getAction());
			metadata.setLineNumber(metadatavo.getLineNumber());
			metadata.setInputParameter(metadatavo.getInputParameter());
			metadata.setStepDesc(metadatavo.getStepDesc());
			metadata.setScriptNumber(newScriptNumber);
			metadata.setValidationType("NA");
			metadata.setValidationName("NA");
			metadata.setUniqueMandatory("NA");
			metadata.setDatatypes("NA");
			metadata.setCreatedBy(mastervo.getCreatedBy());
			metadata.setCreationDate(java.sql.Date.valueOf(mastervo.getCreationDate()));
			master.addMetadata(metadata);

			TestSetScriptParam setScriptlinedata = new TestSetScriptParam();
			int sectiptlineid = dao.getParamId();
			System.out.println("sectiptlineid" + sectiptlineid);

			setScriptlinedata.setTestRunScriptParamId(sectiptlineid);
			setScriptlinedata.setInputParameter(metadatavo.getInputParameter());
			setScriptlinedata.setScriptId(master.getScriptId());
			setScriptlinedata.setScriptNumber(master.getScriptNumber());
			setScriptlinedata.setLineNumber(metadatavo.getLineNumber());
			setScriptlinedata.setInputValue(metadatavo.getInputValue());
			setScriptlinedata.setAction(metadatavo.getAction());
			setScriptlinedata.setTestRunParamDesc(metadatavo.getStepDesc());
			setScriptlinedata.setMetadataId(metaDataId);

			setScriptlinedata.setLastUpdatedBy(null);
			setScriptlinedata.setLineExecutionStatus("New");
			setScriptlinedata.setLineErrorMessage(null);
			setScriptdata.addTestScriptParam(setScriptlinedata);

		}
		String scriptnumber = master.getScriptNumber();
		dao.pluginData(master, scriptnumber);
		String testsetName = mastervo.getTestRunName();
		int testSetId = dao.getTestsetIde(testsetName);
		int seqNum = dao.getseqNum(testSetId);
		int newSeqNum = seqNum + 1;
		TestSet getTestrun = dao.getTestrunData(testSetId);

		int Testsetlineid = dao.getTestSetLineId();
		setScriptdata.setTestRunScriptId(Testsetlineid);
		setScriptdata.setScriptId(master.getScriptId());

		setScriptdata.setEnabled("Y");
		setScriptdata.setScriptNumber(master.getScriptNumber());
		setScriptdata.setSeqNum(newSeqNum);
		setScriptdata.setStatus("New");

		getTestrun.addTestRunScriptData(setScriptdata);

		dao.updateTestrun(getTestrun);

		DomGenericResponseBean res = new DomGenericResponseBean();
		res.setStatus(200);
		res.setStatusMessage("New ScriptNumber::" + scriptnumber);

		return res;
	}

}
