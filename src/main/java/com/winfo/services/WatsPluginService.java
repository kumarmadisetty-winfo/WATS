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
		master.setCreated_by(mastervo.getCreated_by());
		master.setCreation_date(java.sql.Date.valueOf(mastervo.getCreation_date()));
		master.setPlugin_flag(true);
		for(WatsPluginMetaDataVO metadatavo:mastervo.getMetaDataList()) {
			ScriptMetaData metadata = new ScriptMetaData();
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
			metadata.setMetadata_inputvalue(metadatavo.getInput_value());
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
	@Transactional
	public List<String> getTestrunData() {
		// TODO Auto-generated method stub
		return dao.getTestrunData();
	}

	public List<String> getTestrunDataPVerson(String productverson) {
		// TODO Auto-generated method stub
		return dao.getTestrunDataPVerson(productverson);
	}

}