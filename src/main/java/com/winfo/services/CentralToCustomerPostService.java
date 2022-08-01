package com.winfo.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.CentralToCustomerPostDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.WatsMasterDataVOList;
import com.winfo.vo.ScriptMasterDto;
import com.winfo.vo.ScriptMetaDataDto;

@Service
public class CentralToCustomerPostService {

	@Autowired
	CentralToCustomerPostDao dao;

	@Transactional
	public List<DomGenericResponseBean> saveScriptMasterDtls(WatsMasterDataVOList mastervolist) {

		List<DomGenericResponseBean> bean = new ArrayList<>();

		for (ScriptMasterDto masterdata : mastervolist.getData()) {
			ScriptMaster master = new ScriptMaster();
			master.setModule(masterdata.getModule());
			master.setScenario_name(masterdata.getScenarioName());
			master.setScenario_description(masterdata.getScenarioDescription());
			master.setProduct_version(masterdata.getProductVersion());
			master.setPriority(masterdata.getPriority());
			master.setProcess_area(masterdata.getProcessArea());
			master.setRole(masterdata.getRole());
			master.setScript_number(masterdata.getScriptNumber());
			master.setSub_process_area(masterdata.getSubProcessArea());
			master.setStandard_custom(masterdata.getStandardCustom());
			master.setTest_script_status(masterdata.getTestScriptStatus());
			master.setCustomer_id(masterdata.getCustomerId());
			master.setDependency(masterdata.getDependency());
			master.setDependent_script_num(masterdata.getDependentScriptNum());
			master.setEnd2end_scenario(masterdata.getEnd2endScenario());
			master.setExpected_result(masterdata.getExpectedResult());
			master.setSelenium_test_script_name(masterdata.getSeleniumTestScriptName());
			master.setSelenium_test_method(masterdata.getSeleniumTestMethod());
			master.setAuthor(masterdata.getAuthor());
			master.setCreated_by(masterdata.getCreatedBy());
			master.setCreation_date(masterdata.getCreationDate());
			master.setUpdated_by(masterdata.getUpdatedBy());
			master.setUpdate_date(masterdata.getUpdateDate());
			master.setCustomisation_reference(masterdata.getCustomisationReference());
			master.setAttribute2(masterdata.getAttribute2());
			master.setAttribute3(masterdata.getAttribute3());
			master.setAttribute4(masterdata.getAttribute4());
			master.setAttribute5(masterdata.getAttribute5());
			master.setAttribute6(masterdata.getAttribute6());
			master.setAttribute7(masterdata.getAttribute7());
			master.setAttribute8(masterdata.getAttribute8());
			master.setAttribute9(masterdata.getAttribute9());
			master.setAttribute10(masterdata.getAttribute10());
			master.setTargetApplication(masterdata.getTargetApplication());
			for (ScriptMetaDataDto metadatavo : masterdata.getMetaDataList()) {
				ScriptMetaData metadata = new ScriptMetaData();
				metadata.setAction(metadatavo.getAction());
				metadata.setLine_number(metadatavo.getLineNumber());
				metadata.setInput_parameter(metadatavo.getInputParameter());
				metadata.setScript_number(masterdata.getScriptNumber());
				metadata.setXpath_location(metadatavo.getXpathLocation());
				metadata.setXpath_location1(metadatavo.getXpathLocation1());
				metadata.setCreated_by(metadatavo.getCreatedBy());
				metadata.setCreation_date(metadatavo.getCreationDate());
				metadata.setUpdated_by(metadatavo.getUpdatedBy());
				metadata.setUpdate_date(metadatavo.getUpdateDate());
				metadata.setStep_desc(metadatavo.getStepDesc());
				metadata.setField_type(metadatavo.getFieldType());
				metadata.setHint(metadatavo.getHint());
				metadata.setScript_number(metadatavo.getScriptNumber());
				metadata.setDatatypes(metadatavo.getDatatypes());
				metadata.setUnique_mandatory(metadatavo.getUniqueMandatory());
				metadata.setValidation_type(metadatavo.getValidationType());
				metadata.setValidation_name(metadatavo.getValidationName());
				metadata.setMetadata_inputvalue(metadatavo.getMetadataInputValue());
				master.addMetadata(metadata);

			}
			bean.add(dao.centralRepoData(master, masterdata.getScriptNumber(), masterdata.getProductVersion()));
		}

		return bean;

	}
}