package com.winfo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.WatsPluginDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsLoginVO;
import com.winfo.vo.WatsPluginMasterVO;
import com.winfo.vo.WatsPluginMetaDataVO;


@Service
public class WatsPluginService {

	@Autowired
	WatsPluginDao dao;

	@Transactional
	public DomGenericResponseBean pluginData(WatsPluginMasterVO mastervo) {
		String module=mastervo.getModule();
		String processArea=mastervo.getProcess_area();
		String scriptNumber=dao.getScriptNumber(processArea,module);
		
		String newmodule = mastervo.getModule_srt();
//		if(module.equals("Purchasing")) {
//			newmodule="PO";
//		}else if(module.equals("General Ledger")) {
//			newmodule="GL";
//		}else if(module.equals("Fixed Assets")) {
//			newmodule="FA";
//		}else if(module.equals("Accounts Payable")) {
//			newmodule="AP";
//		}else if(module.equals("Accounts Receivable")) {
//			newmodule="AR";
//		}else if(module.equals("Cash Management")) {
//			newmodule="CM";
//		}
//		else if(module.equals("Project Portfolio Management")) {
//			newmodule="PM";
//		}else if(module.equals("Project Financial Management")) {
//			newmodule="PF";
//		}else if(module.equals("Procurement Contracts")) {
//			newmodule="POC";
//		}else if(module.equals("Sourcing")) {
//			newmodule="SO";
//		}else if(module.equals("Tax")) {
//			newmodule="TX";
//		}else if(module.equals("Projects")) {
//			newmodule="PA";
//		}else if(module.equals("Expenses")) {
//			newmodule="Ex";
//		}else if(module.equals("ADM")) {
//			newmodule="ADM";
//		}else if(module.equals("CWB")) {
//			newmodule="CWB";
//		}else if(module.equals("ESS")) {
//			newmodule="ESS";
//		}else if(module.equals("Compensation Workbench")) {
//			newmodule="CWD";
//		}else if(module.equals("MSS")) {
//			newmodule="MSS";
//		}else if(module.equals("Goal")) {
//			newmodule="Goal";
//		}else if(module.equals("Intercompany")) {
//			newmodule="IC";
//		}
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
		
		for(WatsPluginMetaDataVO metadatavo:mastervo.getMetaDataList()) {
			ScriptMetaData metadata = new ScriptMetaData();
			metadata.setAction(metadatavo.getAction());
			metadata.setLine_number(metadatavo.getLine_number());
			metadata.setInput_parameter(metadatavo.getInput_parameter());
			metadata.setStep_desc(metadatavo.getStep_desc());
			metadata.setScript_number(newScriptNumber);
			master.addMetadata(metadata);
			
		}
        String scriptnumber=master.getScript_number();
		return dao.pluginData(master,scriptnumber);
	}
	
	@Transactional
	public DomGenericResponseBean watslogin(WatsLoginVO loginvo) {
		DomGenericResponseBean response = new DomGenericResponseBean();
		String username = loginvo.getUsername();
		String password = loginvo.getPassword();
		// TODO Auto-generated method stub
		String userId = dao.getUserIdValidation(username);
		if(userId!=null) {
		String userIdEnd = dao.verifyEndDate(username);
		String userIdPwdEx = dao.verifyPasswordExpire(username);
		String userIdActive= dao.verifyUserActive(username);
		String passwordEncript=dao.getEncriptPassword(username);
         
		if(userIdEnd!=null&&userIdPwdEx!=null&&userIdActive!=null) {
			if(password.equalsIgnoreCase(passwordEncript)) {
				response.setStatus(200);
				response.setStatusMessage("Login successfully");
			}
			else {
				response.setStatus(404);
				response.setStatusMessage("Password is incorrect");
			}
		}
		if(userIdEnd==null) {
			response.setStatus(404);
			response.setStatusMessage("User account expired");
		}
		if(userIdPwdEx==null) {
			response.setStatus(404);
			response.setStatusMessage("User password expired.Please concat your administrator!");
		}
		if(userIdActive==null) {
			response.setStatus(404);
			response.setStatusMessage("UserId is in-active!");
		}
		}else {
			response.setStatus(404);
			response.setStatusMessage("User name does not exists");
		}
		return response;
	}

}