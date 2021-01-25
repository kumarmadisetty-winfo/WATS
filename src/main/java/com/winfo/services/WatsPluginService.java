package com.winfo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.WatsPluginDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsMasterVO;
import com.winfo.vo.WatsMetaDataVO;


@Service
public class WatsPluginService {

	@Autowired
	WatsPluginDao dao;
//sonarlint
//	@Transactional
//	public void PluginData(List<WatsPluginEntity> entity) {
//
//		EntityMaster master = new EntityMaster();
//
//		WatsPluginEntity e = entity.get(0);
//		master.setATTRIBUTE1(e.getATTRIBUTE1());
//		master.setATTRIBUTE2(e.getATTRIBUTE2());
//		master.setATTRIBUTE3(e.getATTRIBUTE3());
//		master.setATTRIBUTE4(e.getATTRIBUTE4());
//		master.setATTRIBUTE5(e.getATTRIBUTE5());
//		master.setATTRIBUTE6(e.getATTRIBUTE6());
//		master.setATTRIBUTE7(e.getATTRIBUTE7());
//		master.setATTRIBUTE8(e.getATTRIBUTE8());
//		master.setATTRIBUTE9(e.getATTRIBUTE9());
//		master.setATTRIBUTE10(e.getATTRIBUTE10());
//		master.setCUSTOMER_ID(e.getCUSTOMER_ID());
//		master.setCUSTOMISATION_REFERENCE(e.getCUSTOMISATION_REFERENCE());
//		master.setDEPENDENCY(e.getDEPENDENCY());
//		master.setEXPECTED_RESULT(e.getEXPECTED_RESULT());
//		master.setMODULE(e.getMODULE());
//		master.setPRIORITY(e.getPRIORITY());
//		master.setPROCESS_AREA(e.getPROCESS_AREA());
//		master.setPRODUCT_VERSION(e.getPRODUCT_VERSION());
//		master.setROLE(e.getROLE());
//		master.setSCENARIO_DESCRIPTION(e.getSCENARIO_DESCRIPTION());
//		master.setSCENARIO_NAME(e.getSCENARIO_NAME());
////	master.setSCRIPT_ID(e.getSCRIPT_ID());
//		master.setSCRIPT_NUMBER(e.getSCRIPT_NUMBER());
//		master.setSELENIUM_TEST_SCRIPT_NAME(e.getSELENIUM_TEST_SCRIPT_NAME());
//		master.setSTANDARD_CUSTOM(e.getSTANDARD_CUSTOM());
//		master.setSUB_PROCESS_AREA(e.getPROCESS_AREA());
//		master.setTEST_SCRIPT_STATUS(e.getTEST_SCRIPT_STATUS());
//
//		for (WatsPluginEntity en : entity) {
//
//			EntityMetaData data = new EntityMetaData();
//
//			data.setACTION(en.getACTION());
//			data.setINPUT_PARAMETER(en.getINPUT_PARAMETER());
//			data.setLINE_NUMBER(en.getLINE_NUMBER());
////		data.setSCRIPT_META_DATA_ID(en.getSCRIPT_METADATA_ID());
//			data.setSCRIPT_NUMBER(en.getSCRIPT_NUMBER());
//			data.setSTEP_DESC(en.getSTEP_DESCRIPTION());
//
//			master.addMetadata(data);
//		}
//		dao.PluginData(master);
//
//	}
	
	@Transactional
	public DomGenericResponseBean pluginData(WatsMasterVO mastervo) {
		String module=mastervo.getModule();
		String processArea=mastervo.getProcess_area();
		String scriptNumber=dao.getScriptNumber(processArea,module);
		
		String newmodule = null;
		if(module.equals("Purchasing")) {
			newmodule="PO";
		}else if(module.equals("General Ledger")) {
			newmodule="GL";
		}else if(module.equals("Fixed Assets")) {
			newmodule="FA";
		}else if(module.equals("Accounts Payable")) {
			newmodule="AP";
		}else if(module.equals("Accounts Receivable")) {
			newmodule="AR";
		}
		String newScriptNumber=null;
		if(scriptNumber!=null) {
			Integer i = Integer.parseInt(scriptNumber.replaceAll("[\\D]", ""));
			Integer j=i+1;
			 newScriptNumber=processArea+"."+newmodule+"."+j;
			System.out.println(newScriptNumber);

		}
		else {
			 newScriptNumber=processArea+"."+newmodule+"."+"1";
		}
		
		
		ScriptMaster master = new ScriptMaster();
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
		
		for(WatsMetaDataVO metadatavo:mastervo.getMetaDataList()) {
			ScriptMetaData metadata = new ScriptMetaData();
			metadata.setAction(metadatavo.getAction());
			metadata.setLine_number(metadatavo.getLine_number());
			metadata.setInput_parameter(metadatavo.getInput_parameter());
			metadata.setScript_number(newScriptNumber);
			master.addMetadata(metadata);
			
		}
        String scriptnumber=master.getScript_number();
		return dao.pluginData(master,scriptnumber);
	}
	
	@Transactional
	public DomGenericResponseBean copyTestrun(String testScriptNo) {
		dao.getdata(testScriptNo);
		return null;
	}

}