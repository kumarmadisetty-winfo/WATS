package com.winfo.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.WatsPluginDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.model.ScriptsData;
import com.winfo.model.ScritplinesData;
import com.winfo.model.Testrundata;
import com.winfo.vo.CopytestrunVo;
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
	public DomGenericResponseBean copyTestrun(@Valid CopytestrunVo copyTestrunvo) throws InterruptedException {
		Testrundata getTestrun=dao.getdata(copyTestrunvo.getTestScriptNo());
		Testrundata setTestrundata=new Testrundata();
		setTestrundata.setTest_set_desc(getTestrun.getTest_set_desc());
		setTestrundata.setTest_set_comments(getTestrun.getTest_set_comments());
		setTestrundata.setEnabled(getTestrun.getEnabled());
		setTestrundata.setDescription(getTestrun.getDescription());
		setTestrundata.setEffective_from(getTestrun.getEffective_from());
		setTestrundata.setEffective_to(getTestrun.getEffective_to());
		setTestrundata.setTestsetname(copyTestrunvo.getNewtestrunname());
		setTestrundata.setConfigurationid(copyTestrunvo.getConfiguration());
		setTestrundata.setProjectid(copyTestrunvo.getProject());
		 List<ScriptsData> listsScriptdata=new ArrayList<>();
		for(ScriptsData getScriptdata:getTestrun.getScriptsdata()) {
			
			ScriptsData setScriptdata=new ScriptsData();
			setScriptdata.setScriptid(getScriptdata.getScriptid());
			setScriptdata.setCreatedby(getScriptdata.getCreatedby());
			setScriptdata.setCreationdate(getScriptdata.getCreationdate());
			setScriptdata.setEnabled(getScriptdata.getEnabled());
			setScriptdata.setScriptnumber(getScriptdata.getScriptnumber());
			setScriptdata.setSeqnum(getScriptdata.getSeqnum());
			setScriptdata.setStatus(getScriptdata.getStatus());
			setTestrundata.addScriptsdata(setScriptdata);
			for(ScritplinesData getScriptlinedata:getScriptdata.getScriptslinedata()) {
				String getInputvalues=getScriptlinedata.getInput_value();
				System.out.println(getScriptlinedata.getInput_parameter().substring(0, 3));
				if(getScriptlinedata.getInput_parameter().substring(0, 3).contains("(#")) {
					if(getScriptlinedata.getValidationtype().equalsIgnoreCase("alphanumeric")) {
						DateFormat dateformate = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
						Date dateobj = new Date();
						String covertDateobj=dateformate.format(dateobj);
						Thread.sleep(1);
						covertDateobj=covertDateobj.replaceAll("[^0-9]", "");
						int fistOff=Integer.parseInt(covertDateobj.substring(0, 8));
						int secondHalf=Integer.parseInt(covertDateobj.substring(8, 15));
						String a=Integer.toString(fistOff , 36)+Integer.toString(secondHalf , 36);
						a=a+getInputvalues.substring(0, 5);
					}
					else if(getScriptlinedata.getValidationtype().equalsIgnoreCase("numeric")) {
						DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
						Date dateobj = new Date();
						String str=df.format(dateobj);
						Thread.sleep(1);
						str=str.replaceAll("[^0-9]", "");
					}
					}else {
						
					}
				}
			}
		}
//		setTestrundata.setScriptsdata(listsScriptdata);
		dao.saveTestrun(setTestrundata);
//			for(ScritplinesData getScriptlinedata:getScriptdata.getScriptslinedata()) {
//				
//				String getInputvalues=getScriptlinedata.getInput_value();
//				
//				if(getScriptlinedata.getInput_parameter().contains("#")) {
//				if("".equalsIgnoreCase("alphanumeric")) {
//					DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
//					Date dateobj = new Date();
//					String str=df.format(dateobj);
//					Thread.sleep(1);
//					str=str.replaceAll("[^0-9]", "");
//					String str5="agaggagagaga1qqqq";
//					int one=Integer.parseInt(str.substring(0, 8));
//					int two=Integer.parseInt(str.substring(8, 15));
//					String a=Integer.toString(one , 36)+Integer.toString(two , 36);
//					a=a+getInputvalues.substring(0, 5);
//				}
//				else if("".equalsIgnoreCase("numeric")) {
//					DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS");
//					Date dateobj = new Date();
//					String str=df.format(dateobj);
//					Thread.sleep(1);
//					str=str.replaceAll("[^0-9]", "");
//				}
//				
//			}
//			}
//		}
		return null;
	
	}

}