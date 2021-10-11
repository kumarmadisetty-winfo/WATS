package com.winfo.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.WatsPluginDao;
import com.winfo.model.PluginMaster;
import com.winfo.model.PluginMetadata;
import com.winfo.model.ScriptsData;
import com.winfo.model.ScritplinesData;
import com.winfo.model.Testrundata;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsPluginMasterVO;
import com.winfo.vo.WatsPluginMetaDataVO;

@Service
public class PluginTestrunService {
	
	@Autowired
	WatsPluginDao dao;

	@Transactional
	public DomGenericResponseBean updateTestrun(WatsPluginMasterVO mastervo) {
		ScriptsData setScriptdata=new ScriptsData();

		String module=mastervo.getModule();
		String processArea=mastervo.getProcess_area();
		List<String> scriptNumbers=dao.getScriptNumber(processArea,module);
		
		String newmodule = mastervo.getModule_srt();

		String newScriptNumber=null;
		ArrayList<Integer> slist = new ArrayList<Integer>();
		if(scriptNumbers!=null) {
			for(String snumber:scriptNumbers) {
				Integer i = Integer.parseInt(snumber.replaceAll("[\\D]", ""));
				
				slist.add(i);
			}
			 int max = Collections.max(slist);
			int snum=max+1;
			 newScriptNumber=processArea+"."+newmodule+"."+snum;
			System.out.println(newScriptNumber);

		}
		else {
			 newScriptNumber=processArea+"."+newmodule+"."+"1";
		}
		
		
		PluginMaster master = new PluginMaster();
		int masterScriptId=dao.getMasterScriptId();
		master.setScript_id(masterScriptId);
		master.setModule(mastervo.getModule());
		master.setScenario_name(mastervo.getScenario_name());
		master.setScenario_description(mastervo.getScenario_description());
		master.setProduct_version(mastervo.getProduct_version());
		master.setPriority(mastervo.getPriority());
		master.setProcess_area(mastervo.getProcess_area());
		master.setRole(mastervo.getRole());
		master.setScript_number(newScriptNumber);
		master.setSub_process_area(mastervo.getSub_process_area());
		master.setStandard_custom(mastervo.getStandard_custom());
		master.setTest_script_status(mastervo.getTest_script_status());
		master.setCreated_by(mastervo.getCreated_by());
		master.setCreation_date(java.sql.Date.valueOf(mastervo.getCreation_date()));
		
		for(WatsPluginMetaDataVO metadatavo:mastervo.getMetaDataList()) {
			PluginMetadata metadata = new PluginMetadata();
			int metaDataId=dao.getMetaDataId();
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
			metadata.setConditional_popup("NA");
			metadata.setCreated_by(mastervo.getCreated_by());
			metadata.setCreation_date(java.sql.Date.valueOf(mastervo.getCreation_date()));
			master.addMetadata(metadata);
			
			ScritplinesData setScriptlinedata=new ScritplinesData();
			int sectiptlineid=dao.getParam_id();
			 System.out.println("sectiptlineid"+sectiptlineid);
			
			setScriptlinedata.setTestscriptperamid(sectiptlineid);
			setScriptlinedata.setInput_parameter(metadatavo.getInput_parameter());
			setScriptlinedata.setScript_id(master.getScript_id());
			setScriptlinedata.setScript_number(master.getScript_number());
			setScriptlinedata.setLine_number(metadatavo.getLine_number());
			setScriptlinedata.setInput_value(metadatavo.getInput_value());
			setScriptlinedata.setAction(metadatavo.getAction());
			setScriptlinedata.setTest_run_param_desc(metadatavo.getStep_desc());
			setScriptlinedata.setMetadata_id(metaDataId);

			setScriptlinedata.setLastupdatedby(null);
			setScriptlinedata.setLineexecutionstatues("New");
			setScriptlinedata.setLineerrormessage(null);			
			setScriptdata.addScriptlines(setScriptlinedata);
			
			
			
		}
        String scriptnumber=master.getScript_number();
		 dao.pluginData(master,scriptnumber);	
		 String testsetName=mastervo.getTestrunName();
		 int testSetId=dao.getTestsetIde(testsetName);
		 int seqNum=dao.getseqNum(testSetId);
		 int newSeqNum=seqNum+1;
			Testrundata getTestrun=dao.getTestrunData(testSetId);
			
			int Testsetlineid=dao.getTest_set_line_id();
			setScriptdata.setTestsetlineid(Testsetlineid);
			setScriptdata.setScriptid(master.getScript_id());

			setScriptdata.setEnabled("Y");
			setScriptdata.setScriptnumber(master.getScript_number());
			setScriptdata.setSeqnum(newSeqNum);
			setScriptdata.setStatus("New");

			getTestrun.addScriptsdata(setScriptdata);	
			
			dao.updateTestrun(getTestrun);

			DomGenericResponseBean res= new DomGenericResponseBean();
			res.setStatus(200);
			res.setStatusMessage("New ScriptNumber::"+scriptnumber);

			return res;
	}

}
