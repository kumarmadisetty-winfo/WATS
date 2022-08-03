package com.winfo.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

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
		master.setScript_id(masterScriptId);
		master.setModule(mastervo.getModule());
		master.setScenario_name(mastervo.getScenarioName());
		master.setScenario_description(mastervo.getScenarioDescription());
		master.setProduct_version(mastervo.getProductVersion());
		master.setPriority(mastervo.getPriority());
		master.setProcess_area(mastervo.getProcessArea());
		master.setRole(mastervo.getRole());
		master.setScript_number(newScriptNumber);
		master.setSub_process_area(mastervo.getSubProcessArea());
		master.setStandard_custom(mastervo.getStandardCustom());
		master.setTest_script_status(mastervo.getTestScriptStatus());
		master.setCreated_by(mastervo.getCreatedBy());
		master.setCreation_date(java.sql.Date.valueOf(mastervo.getCreationDate()));

		for (WatsPluginMetaDataVO metadatavo : mastervo.getMetaDataList()) {
			ScriptMetaData metadata = new ScriptMetaData();
			int metaDataId = dao.getMetaDataId();
			metadata.setScript_meta_data_id(metaDataId);
			metadata.setAction(metadatavo.getAction());
			metadata.setLine_number(metadatavo.getLine_number());
			metadata.setInput_parameter(metadatavo.getInput_parameter());
			metadata.setStep_desc(metadatavo.getStep_desc());
			metadata.setScript_number(newScriptNumber);
			metadata.setValidation_type("NA");
			metadata.setValidation_name("NA");
			metadata.setUnique_mandatory("NA");
			metadata.setDatatypes("NA");
			metadata.setCreated_by(mastervo.getCreatedBy());
			metadata.setCreation_date(java.sql.Date.valueOf(mastervo.getCreationDate()));
			master.addMetadata(metadata);

			TestSetScriptParam setScriptlinedata = new TestSetScriptParam();
			int sectiptlineid = dao.getParam_id();
			System.out.println("sectiptlineid" + sectiptlineid);

			setScriptlinedata.setTestRunScriptParamId(sectiptlineid);
			setScriptlinedata.setInputParameter(metadatavo.getInput_parameter());
			setScriptlinedata.setScriptId(master.getScript_id());
			setScriptlinedata.setScriptNumber(master.getScript_number());
			setScriptlinedata.setLineNumber(metadatavo.getLine_number());
			setScriptlinedata.setInputValue(metadatavo.getInput_value());
			setScriptlinedata.setAction(metadatavo.getAction());
			setScriptlinedata.setTestRunParamDesc(metadatavo.getStep_desc());
			setScriptlinedata.setMetadataId(metaDataId);

			setScriptlinedata.setLastUpdatedBy(null);
			setScriptlinedata.setLineExecutionStatus("New");
			setScriptlinedata.setLineErrorMessage(null);
			setScriptdata.addTestScriptParam(setScriptlinedata);

		}
		String scriptnumber = master.getScript_number();
		dao.pluginData(master, scriptnumber);
		String testsetName = mastervo.getTestrunName();
		int testSetId = dao.getTestsetIde(testsetName);
		int seqNum = dao.getseqNum(testSetId);
		int newSeqNum = seqNum + 1;
		TestSet getTestrun = dao.getTestrunData(testSetId);

		int Testsetlineid = dao.getTest_set_line_id();
		setScriptdata.setTestRunScriptId(Testsetlineid);
		setScriptdata.setScriptId(master.getScript_id());

		setScriptdata.setEnabled("Y");
		setScriptdata.setScriptNumber(master.getScript_number());
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
