package com.winfo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winfo.dao.CentralToCustomerPostDao;
import com.winfo.dao.DataBaseEntryDao;
import com.winfo.model.Customer;
import com.winfo.model.LookUp;
import com.winfo.model.LookUpCode;
import com.winfo.model.ScriptMaster;
import com.winfo.model.ScriptMetaData;
import com.winfo.repository.CustomerRepository;
import com.winfo.vo.DomGenericResponseBean;
import com.winfo.vo.LookUpCodeVO;
import com.winfo.vo.LookUpVO;
import com.winfo.vo.ScriptMasterDto;
import com.winfo.vo.ScriptMetaDataDto;
import com.winfo.vo.WatsMasterDataVOList;

@Service
public class CentralToCustomerPostService {
	public static final Logger logger = Logger.getLogger(CentralToCustomerPostService.class);
	@Autowired
	CentralToCustomerPostDao dao;
	@Autowired
	DataBaseEntryDao dataBaseEntryDao;
	@Autowired
	CustomerRepository customerRepository;

	@Transactional
	public List<DomGenericResponseBean> saveScriptMasterDtls(WatsMasterDataVOList mastervolist, String customerName) {

		List<DomGenericResponseBean> bean = new ArrayList<>();
		Customer customer=customerRepository.findByCustomerName(customerName);
		int customerId=customer.getCustomerId();	
		for (ScriptMasterDto masterdata : mastervolist.getData()) {
			List<String> result = dao.getExistScriptDetailsByScriptNumberAndProductVersion(masterdata.getScriptNumber(), masterdata.getProductVersion());
			DomGenericResponseBean response = new DomGenericResponseBean();
			if(result.isEmpty()){
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
			master.setCustomerId(customerId);
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
			master.setScriptMetaDatalist(new ArrayList<>());
			for (ScriptMetaDataDto metadatavo : masterdata.getMetaDataList()) {
				ScriptMetaData metadata = new ScriptMetaData();
				metadata.setAction(metadatavo.getAction());
				metadata.setLineNumber(metadatavo.getLineNumber());
				metadata.setInputParameter(metadatavo.getInputParameter());
				metadata.setScriptNumber(masterdata.getScriptNumber());
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
			LookUpVO lookUpVoObj = masterdata.getLookUpVO();
			if (lookUpVoObj != null) {
				String lookUpName = lookUpVoObj.getLookupName() == null ? "" : lookUpVoObj.getLookupName();
				if (dao.doesLookUpExist(lookUpName)) {
					LookUp lookUpObj = new LookUp();
					lookUpObj.setLookUpName(lookUpVoObj.getLookupName());
					lookUpObj.setLookUpDesc(lookUpVoObj.getLookupDesc());
					lookUpObj.setCreatedBy(lookUpVoObj.getCreatedBy());
					lookUpObj.setLastUpdatedBy(lookUpVoObj.getLastUpdatedBy());
					lookUpObj.setCreationDate(lookUpVoObj.getCreationDate());
					lookUpObj.setUpdatedDate(lookUpVoObj.getUpdateDate());
					dao.insertLookUpObj(lookUpObj);
				}

				Map<String, LookUpCodeVO> mapOfLookUpCodeVO = lookUpVoObj.getMapOfData();

				if (mapOfLookUpCodeVO != null) {
					for (Entry<String, LookUpCodeVO> entry : mapOfLookUpCodeVO.entrySet()) {

						String lookUpNameKey = entry.getKey();
						LookUpCodeVO lookUpCodeValue = entry.getValue();
						if (lookUpNameKey != null
								&& dao.doesLookUpCodeExist(lookUpName, lookUpNameKey)) {
							LookUpCode lookUpCodeObj = new LookUpCode();
							lookUpCodeObj.setLookUpId(lookUpCodeValue.getLookUpId());
							lookUpCodeObj.setLookUpName(lookUpCodeValue.getLookUpName());
							lookUpCodeObj.setLookUpCode(lookUpCodeValue.getLookUpCode());
							lookUpCodeObj.setTargetCode(lookUpCodeValue.getTargetCode());
							lookUpCodeObj.setMeaning(lookUpCodeValue.getMeaning());
							lookUpCodeObj.setDescription(lookUpCodeValue.getDescription());
							lookUpCodeObj.setEffectiveFrom(lookUpCodeValue.getEffectiveFrom());
							lookUpCodeObj.setEffectiveTo(lookUpCodeValue.getEffectiveTo());
							lookUpCodeObj.setCreatedBy(lookUpCodeValue.getCreatedBy());
							lookUpCodeObj.setLastUpdatedBy(lookUpCodeValue.getLastUpdatedBy());
							lookUpCodeObj.setCreationDate(lookUpCodeValue.getCreationDate());
							lookUpCodeObj.setUpdateDate(lookUpCodeValue.getUpdateDate());
							lookUpCodeObj.setProcessCode(lookUpCodeValue.getProcessCode());
							lookUpCodeObj.setModuleCode(lookUpCodeValue.getModuleCode());
							lookUpCodeObj.setTargetApplication(lookUpCodeValue.getTargetApplication());

							dao.insertLookUpCodeObj(lookUpCodeObj);
						}

					}
				}

			}

			bean.add(dao.centralRepoData(master, masterdata.getScriptNumber(), masterdata.getProductVersion()));
		}
		else
		{
			response.setStatus(400);
			response.setStatusMessage("ERROR");
			response.setDescription("Script Number Already exists");
			response.setFailed_Script(masterdata.getScriptNumber());
			bean.add(response);	
			logger.error("Script Number Already exists " + masterdata.getScriptNumber());
		}


	}
		logger.info("Successfully Migrated script");
		return bean;
	}
}