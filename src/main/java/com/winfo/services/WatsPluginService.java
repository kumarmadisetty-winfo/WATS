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
		master.setPluginFlag("true");
		for (WatsPluginMetaDataVO metadatavo : mastervo.getMetaDataList()) {
			ScriptMetaData metadata = new ScriptMetaData();
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
			metadata.setMetadataInputvalue(metadatavo.getInputValue());
			master.addMetadata(metadata);

		}
		String scriptnumber = master.getScriptNumber();
		return dao.pluginData(master, scriptnumber);
	}

	@Transactional
	public DomGenericResponseBean watslogin(WatsLoginVO loginvo) {
		DomGenericResponseBean response = new DomGenericResponseBean();
		String username = loginvo.getUsername();
		String password = loginvo.getPassword();
		String userId = dao.getUserIdValidation(username);
		if (userId != null) {
			String userIdEnd = dao.verifyEndDate(username);
			String userIdPwdEx = dao.verifyPasswordExpire(username);
			String userIdActive = dao.verifyUserActive(username);
			String passwordEncript = dao.getEncriptPassword(username);

			if (userIdEnd != null && userIdPwdEx != null && userIdActive != null) {
				if (password.equalsIgnoreCase(passwordEncript)) {
					response.setStatus(200);
					response.setStatusMessage("Login successfully");
				} else {
					response.setStatus(404);
					response.setStatusMessage("Password is incorrect");
				}
			}
			if (userIdEnd == null) {
				response.setStatus(404);
				response.setStatusMessage("User account expired");
			}
			if (userIdPwdEx == null) {
				response.setStatus(404);
				response.setStatusMessage("User password expired.Please concat your administrator!");
			}
			if (userIdActive == null) {
				response.setStatus(404);
				response.setStatusMessage("UserId is in-active!");
			}
		} else {
			response.setStatus(404);
			response.setStatusMessage("User name does not exists");
		}
		return response;
	}

	@Transactional
	public List<String> getTestrunData() {
		return dao.getTestrunData();
	}

	public List<String> getTestrunDataPVerson(String productverson) {
		return dao.getTestrunDataPVerson(productverson);
	}

}