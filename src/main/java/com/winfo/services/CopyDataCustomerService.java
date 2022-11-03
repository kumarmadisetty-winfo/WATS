package com.winfo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.CopyDataCustomerDao;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.vo.CopyDataDetails;
import com.winfo.vo.DomGenericResponseBean;

@Service
public class CopyDataCustomerService {
	@Autowired
	CopyDataCustomerDao dao;

	@Transactional
	public DomGenericResponseBean copyData(CopyDataDetails copyDataDetails) {

		List<ScriptMaster> masterDtlsforOldProductVersion = dao
				.getScriptMasterDtlByProductVersion(copyDataDetails.getProductVersionOld());

		List<ScriptMaster> masterDtlsforNewProductVersion = dao
				.getScriptMasterDtlByProductVersion(copyDataDetails.getProductVersionNew());

		List<ScriptMaster> newScriptWithNewProductVersion = new ArrayList<>();

		Map<String, ScriptMaster> mapOfScriptMasterNew = new HashMap<>();

		for (ScriptMaster newScriptMasterDtl : masterDtlsforNewProductVersion) {

			mapOfScriptMasterNew.put(newScriptMasterDtl.getScriptNumber(), newScriptMasterDtl);

		}
		
		Map<String, Integer> dependentScriptMap = new HashMap<>();
		
		int count = 0;
		for (ScriptMaster oldScriptMasterDtl : masterDtlsforOldProductVersion) {

			if (mapOfScriptMasterNew.containsKey(oldScriptMasterDtl.getScriptNumber())) {
				return new DomGenericResponseBean(409, "error",
						"Source script already present in Target Location. Please check the Script in target product version.");
			} else {
				count++;
				ScriptMaster newScriptMasterDtl = new ScriptMaster();
				newScriptMasterDtl.setScriptNumber(oldScriptMasterDtl.getScriptNumber());
				newScriptMasterDtl.setProcessArea(oldScriptMasterDtl.getProcessArea());
				newScriptMasterDtl.setSubProcessArea(oldScriptMasterDtl.getSubProcessArea());
				newScriptMasterDtl.setModule(oldScriptMasterDtl.getModule());
				newScriptMasterDtl.setRole(oldScriptMasterDtl.getModule());
				newScriptMasterDtl.setEnd2endScenario(oldScriptMasterDtl.getEnd2endScenario());
				newScriptMasterDtl.setScenarioName(oldScriptMasterDtl.getScenarioName());
				newScriptMasterDtl.setScenarioDescription(oldScriptMasterDtl.getScenarioDescription());
				newScriptMasterDtl.setExpectedResult(oldScriptMasterDtl.getExpectedResult());
				newScriptMasterDtl.setSeleniumTestScriptName(oldScriptMasterDtl.getSeleniumTestScriptName());
				newScriptMasterDtl.setSeleniumTestMethod(oldScriptMasterDtl.getSeleniumTestMethod());
				newScriptMasterDtl.setProductVersion(copyDataDetails.getProductVersionNew());
	
				if(oldScriptMasterDtl.getDependency() != null) {
					dependentScriptMap.put(oldScriptMasterDtl.getScriptNumber(), oldScriptMasterDtl.getScriptId());
				}
				newScriptMasterDtl.setDependency(oldScriptMasterDtl.getDependency());			
				
				newScriptMasterDtl.setDependentScriptNum(oldScriptMasterDtl.getDependentScriptNum());
				newScriptMasterDtl.setStandardCustom(oldScriptMasterDtl.getStandardCustom());
				newScriptMasterDtl.setTestScriptStatus(oldScriptMasterDtl.getTestScriptStatus());
				newScriptMasterDtl.setAuthor(oldScriptMasterDtl.getAuthor());
				newScriptMasterDtl.setCreatedBy(oldScriptMasterDtl.getCreatedBy());
				newScriptMasterDtl.setCreationDate(oldScriptMasterDtl.getCreationDate());
				newScriptMasterDtl.setUpdatedBy(oldScriptMasterDtl.getUpdatedBy());
				newScriptMasterDtl.setUpdateDate(oldScriptMasterDtl.getUpdateDate());
				newScriptMasterDtl.setCustomerId(oldScriptMasterDtl.getCustomerId());
				newScriptMasterDtl.setCustomisationReference(oldScriptMasterDtl.getCustomisationReference());
				newScriptMasterDtl.setAttribute2(oldScriptMasterDtl.getAttribute2());
				newScriptMasterDtl.setAttribute3(oldScriptMasterDtl.getAttribute3());
				newScriptMasterDtl.setAttribute4(oldScriptMasterDtl.getAttribute4());
				newScriptMasterDtl.setAttribute5(oldScriptMasterDtl.getAttribute5());
				newScriptMasterDtl.setAttribute6(oldScriptMasterDtl.getAttribute6());
				newScriptMasterDtl.setAttribute7(oldScriptMasterDtl.getAttribute7());
				newScriptMasterDtl.setAttribute8(oldScriptMasterDtl.getAttribute8());
				newScriptMasterDtl.setAttribute9(oldScriptMasterDtl.getAttribute9());
				newScriptMasterDtl.setAttribute10(oldScriptMasterDtl.getAttribute10());
				newScriptMasterDtl.setPriority(oldScriptMasterDtl.getPriority());
				newScriptMasterDtl.setPluginFlag(oldScriptMasterDtl.getPluginFlag());
				newScriptMasterDtl.setTargetApplication(oldScriptMasterDtl.getTargetApplication());
				newScriptMasterDtl.setApprForMigration(oldScriptMasterDtl.getApprForMigration());
				for (ScriptMetaData scriptMetaData : oldScriptMasterDtl.getScriptMetaDatalist()) {
					ScriptMetaData newScriptMetaData = new ScriptMetaData();
					newScriptMetaData.setInputParameter(scriptMetaData.getInputParameter());
					newScriptMetaData.setLineNumber(scriptMetaData.getLineNumber());
					newScriptMetaData.setAction(scriptMetaData.getAction());
					newScriptMetaData.setXpathLocation(scriptMetaData.getXpathLocation());
					newScriptMetaData.setXpathLocation1(scriptMetaData.getXpathLocation1());
					newScriptMetaData.setCreatedBy(scriptMetaData.getCreatedBy());
					newScriptMetaData.setCreationDate(scriptMetaData.getCreationDate());
					newScriptMetaData.setUpdatedBy(scriptMetaData.getUpdatedBy());
					newScriptMetaData.setUpdateDate(scriptMetaData.getUpdateDate());
					newScriptMetaData.setStepDesc(scriptMetaData.getStepDesc());
					newScriptMetaData.setFieldType(scriptMetaData.getFieldType());
					newScriptMetaData.setHint(scriptMetaData.getHint());
					newScriptMetaData.setScriptNumber(scriptMetaData.getScriptNumber());
					newScriptMetaData.setDatatypes(scriptMetaData.getDatatypes());
					newScriptMetaData.setUniqueMandatory(scriptMetaData.getUniqueMandatory());
					newScriptMetaData.setValidationType(scriptMetaData.getValidationType());
					newScriptMetaData.setValidationName(scriptMetaData.getValidationName());
					newScriptMetaData.setMetadataInputvalue(scriptMetaData.getMetadataInputvalue());
					newScriptMasterDtl.addMetadata(newScriptMetaData);
				}
				newScriptWithNewProductVersion.add(newScriptMasterDtl);

			}
		}
	
		Map<String, Integer> newDependentScriptMap = new HashMap<>();
		Map<Integer, Integer> dependentScriptId = new HashMap<>();

		for (ScriptMaster masterObj : newScriptWithNewProductVersion) {
			dao.updateScriptDtlsInMasterTable(masterObj);
			newDependentScriptMap.put(masterObj.getScriptNumber(), masterObj.getScriptId());
		}
		
		for(Entry<String, Integer> entrySet : dependentScriptMap.entrySet()) {
			String key = entrySet.getKey();
			Integer value = entrySet.getValue();
			if(newDependentScriptMap.containsKey(key)) {
				dependentScriptId.put(value, newDependentScriptMap.get(key));
			}
		}
		
		for(Entry<Integer, Integer> entrySet : dependentScriptId.entrySet()) {
			Integer key = entrySet.getKey();
			Integer value = entrySet.getValue();
			List<ScriptMaster> dependentScripts = dao.findDependentScriptByDependencyAndProductVersion(key, copyDataDetails.getProductVersionNew());
			for(ScriptMaster dependentScript : dependentScripts) {
				dependentScript.setDependency(value);
				dao.updateScriptDtlsInMasterTable(dependentScript);
			}
		}

		return new DomGenericResponseBean(200, "success",
				count + " Script'(s) Copied" + " to " + copyDataDetails.getProductVersionNew() + " Successfully");

	}
}
