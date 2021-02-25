package com.winfo.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsMasterDataVOList;
import com.winfo.dao.CentralToCustomerPostDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.WatsMasterVO;
import com.winfo.vo.WatsMetaDataVO;


@Service
public class CentralToCustomerPostService {

	@Autowired
	CentralToCustomerPostDao dao;
	
	@Transactional
	public List<DomGenericResponseBean> centralRepoData(WatsMasterDataVOList mastervolist) {
	
		List<DomGenericResponseBean> bean = new ArrayList<DomGenericResponseBean>();
		
		for(WatsMasterVO masterdata:mastervolist.getData()) {
		ScriptMaster master = new ScriptMaster();
		master.setModule(masterdata.getModule());
		master.setScenario_name(masterdata.getScenario_name());
		master.setScenario_description(masterdata.getScenario_description());
		master.setProduct_version(masterdata.getProduct_version());
		master.setPriority(masterdata.getPriority());
		master.setProcess_area(masterdata.getProcess_area());
		master.setRole(masterdata.getRole());
		master.setScript_number(masterdata.getScript_number());
		master.setSub_process_area(masterdata.getSub_process_area());
		master.setStandard_custom(masterdata.getStandard_custom());
		master.setTest_script_status(masterdata.getTest_script_status());
		master.setCustomer_id(masterdata.getCustomer_id());
		master.setDependency(masterdata.getDependency());
		master.setDependent_script_num(masterdata.getDependent_script_num());
		master.setEnd2end_scenario(masterdata.getEnd2end_scenario());
		master.setExpected_result(masterdata.getExpected_result());
		master.setSelenium_test_script_name(masterdata.getSelenium_test_script_name());
		master.setSelenium_test_method(masterdata.getSelenium_test_method());
		master.setAuthor(masterdata.getAuthor());
		master.setCreated_by(masterdata.getCreated_by());
		master.setCreation_date(masterdata.getCreation_date());
		master.setUpdated_by(masterdata.getUpdated_by());
		master.setUpdate_date(masterdata.getUpdate_date());
		master.setCustomisation_reference(masterdata.getCustomisation_reference());
		master.setAttribute1(masterdata.getAttribute1());
		master.setAttribute2(masterdata.getAttribute2());
		master.setAttribute3(masterdata.getAttribute3());
		master.setAttribute4(masterdata.getAttribute4());
		master.setAttribute5(masterdata.getAttribute5());
		master.setAttribute6(masterdata.getAttribute6());
		master.setAttribute7(masterdata.getAttribute7());
		master.setAttribute8(masterdata.getAttribute8());
		master.setAttribute9(masterdata.getAttribute9());
		master.setAttribute10(masterdata.getAttribute10());
		
		for(WatsMetaDataVO metadatavo:masterdata.getMetaDataList()) {
			ScriptMetaData metadata = new ScriptMetaData();
			metadata.setAction(metadatavo.getAction());
			metadata.setLine_number(metadatavo.getLine_number());
			metadata.setInput_parameter(metadatavo.getInput_parameter());
			metadata.setScript_number(masterdata.getScript_number());
			metadata.setXpath_location(metadatavo.getXpath_location());
			metadata.setXpath_location1(metadatavo.getXpath_location1());
			metadata.setCreated_by(metadatavo.getCreated_by());
			metadata.setCreation_date(metadatavo.getCreation_date());
			metadata.setUpdated_by(metadatavo.getUpdated_by());
			metadata.setUpdate_date(metadatavo.getUpdate_date());
			metadata.setStep_desc(metadatavo.getStep_desc());
			metadata.setField_type(metadatavo.getField_type());
			metadata.setHint(metadatavo.getHint());
			  metadata.setScript_number(metadatavo.getScript_number());
	            metadata.setDatatypes(metadatavo.getDatatypes());
	            metadata.setUnique_mandatory(metadatavo.getUnique_mandatory());
	            metadata.setValidation_type(metadatavo.getValidation_type());
	            metadata.setValidation_name(metadatavo.getValidation_name());
	          
			master.addMetadata(metadata);
			
		}
		bean.add(dao.centralRepoData(master,masterdata.getScript_number(),masterdata.getProduct_version()));
       	}
	
		return bean;

	}
}