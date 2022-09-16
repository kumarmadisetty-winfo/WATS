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
			master.setScenarioName(masterdata.getScenarioName());
			master.setScenarioDescription(masterdata.getScenarioDescription());
			master.setProductVersion(masterdata.getProductVersion());
			master.setPriority(masterdata.getPriority());
			master.setProcessArea(masterdata.getProcessArea());
			master.setRole(masterdata.getRole());
			master.setScriptNumber(masterdata.getScriptNumber());
			master.setSubProcessArea(masterdata.getSubProcessArea());
			master.setStandardCustom(masterdata.getStandardCustom());
			master.setTestScriptStatus(masterdata.getTestScriptStatus());
			master.setCustomerId(masterdata.getCustomerId());
			master.setDependency(masterdata.getDependency());
			master.setDependentScriptNum(masterdata.getDependentScriptNum());
			master.setEnd2endScenario(masterdata.getEnd2endScenario());
			master.setExpectedResult(masterdata.getExpectedResult());
			master.setSeleniumTestScriptName(masterdata.getSeleniumTestScriptName());
			master.setSeleniumTestMethod(masterdata.getSeleniumTestMethod());
			master.setAuthor(masterdata.getAuthor());
			master.setCreatedBy(masterdata.getCreatedBy());
			master.setCreationDate(masterdata.getCreationDate());
			master.setUpdatedBy(masterdata.getUpdatedBy());
			master.setUpdateDate(masterdata.getUpdateDate());
			master.setCustomisationReference(masterdata.getCustomisationReference());
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
				metadata.setLineNumber(metadatavo.getLineNumber());
				metadata.setInputParameter(metadatavo.getInputParameter());
				metadata.setScriptNumber(masterdata.getScriptNumber());
				metadata.setXpathLocation(metadatavo.getXpathLocation());
				metadata.setXpathLocation1(metadatavo.getXpathLocation1());
				metadata.setCreatedBy(metadatavo.getCreatedBy());
				metadata.setCreationDate(metadatavo.getCreationDate());
				metadata.setUpdatedBy(metadatavo.getUpdatedBy());
				metadata.setUpdateDate(metadatavo.getUpdateDate());
				metadata.setStepDesc(metadatavo.getStepDesc());
				metadata.setFieldType(metadatavo.getFieldType());
				metadata.setHint(metadatavo.getHint());
				metadata.setScriptNumber(metadatavo.getScriptNumber());
				metadata.setDatatypes(metadatavo.getDatatypes());
				metadata.setUniqueMandatory(metadatavo.getUniqueMandatory());
				metadata.setValidationType(metadatavo.getValidationType());
				metadata.setValidationName(metadatavo.getValidationName());
				metadata.setMetadataInputvalue(metadatavo.getMetadataInputValue());
				master.addMetadata(metadata);

			}
			bean.add(dao.centralRepoData(master, masterdata.getScriptNumber(), masterdata.getProductVersion()));
		}

		return bean;

	}
}